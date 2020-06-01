package com.sunjet.backend.modules.asms.service.dealer;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerQuitRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerQuitRequestView;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerQuitRepostitory;
import com.sunjet.backend.modules.asms.repository.dealer.DealerQuitViewRepostitory;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.dealer.DealerQuitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerQuitRequestItem;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.JsonHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/9/5.
 * 服务站退出
 */
@Transactional
@Service("dealerQuitService")
public class DealerQuitServiceImpl implements DealerQuitService {
    @Autowired
    private DealerQuitRepostitory dealerQuitRepostitory;
    @Autowired
    private DealerQuitViewRepostitory dealerQuitViewRepostitory;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProcessService processService;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<DealerQuitRequestInfo> findAll() {
        try {
            List<DealerQuitRequestEntity> list = this.dealerQuitRepostitory.findAll();
            List<DealerQuitRequestInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (DealerQuitRequestEntity dealerQuitRequestEntity : list) {
                    DealerQuitRequestInfo info = new DealerQuitRequestInfo();
                    infos.add(BeanUtils.copyPropertys(dealerQuitRequestEntity, info));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<DealerQuitRequestView> getPageList(PageParam<DealerQuitRequestItem> pageParam) {

        //1.查询条件
        DealerQuitRequestItem dealerQuitRequestItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DealerQuitRequestView> specification = null;

        if (dealerQuitRequestItem != null) {

            specification = Specifications.<DealerQuitRequestView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .predicate(StringUtils.isNotBlank(dealerQuitRequestItem.getSubmitter()),
                            com.sunjet.backend.system.Jpa.Specifications.or()
                                    .eq(StringUtils.isNotBlank(dealerQuitRequestItem.getSubmitter()), "submitter", dealerQuitRequestItem.getSubmitter())
                                    .eq(StringUtils.isNotBlank(dealerQuitRequestItem.getServiceManagerId()), "serviceManagerId", dealerQuitRequestItem.getServiceManagerId())
                                    .build())
                    .like(StringUtils.isNotBlank(dealerQuitRequestItem.getCode()), "code", dealerQuitRequestItem.getCode())
                    .like(StringUtils.isNotBlank(dealerQuitRequestItem.getName()), "name", dealerQuitRequestItem.getName())
                    .eq(StringUtils.isNotBlank(dealerQuitRequestItem.getProvinceId()), "provinceId", dealerQuitRequestItem.getProvinceId())
                    .eq(!dealerQuitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", dealerQuitRequestItem.getStatus())
                    .ge(dealerQuitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .eq(StringUtils.isNotBlank(dealerQuitRequestItem.getStar()), "star", dealerQuitRequestItem.getStar())
                    .eq(StringUtils.isNotBlank(dealerQuitRequestItem.getQualification()), "qualification", dealerQuitRequestItem.getQualification())
                    //.eq(StringUtils.isNotBlank(dealerQuitRequestItem.getServiceManagerId()), "serviceManagerId", dealerQuitRequestItem.getServiceManagerId())
                    .between((dealerQuitRequestItem.getStartDate() != null && dealerQuitRequestItem.getEndDate() != null), "createdTime", new Range<Date>(dealerQuitRequestItem.getStartDate(), dealerQuitRequestItem.getEndDate()))
                    .build();
        }


        //3.执行查询
        Page<DealerQuitRequestView> pages = dealerQuitViewRepostitory.findAll(specification, PageUtil.getPageRequest(pageParam));

//        //4.数据转换
//        List<DealerQuitRequestInfo> rows = new ArrayList<>();
//        for(DealerQuitRequestEntity entity :pages.getContent()){
//            DealerQuitRequestInfo info = entityToInfo(entity);
//            rows.add(info);
//        }


        //5.组装分页信息及集合信息
        //PageResult<DealerQuitRequestInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 启动流程
     *
     * @param variables
     * @return
     */
    @Override
    public Map<String, String> startProcess(Map<String, Object> variables) {
        //提交流程返回信息
        Map<String, String> message = new HashMap<>();
        try {
            DealerQuitRequestInfo dealerQuitRequestInfo = JsonHelper.map2Bean(variables.get("entity"), DealerQuitRequestInfo.class);
            if (!dealerQuitRequestInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                DealerQuitRequestEntity entity = BeanUtils.copyPropertys(dealerQuitRequestInfo, new DealerQuitRequestEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                DealerEntity dealerEntity = dealerRepository.findOne(dealerQuitRequestInfo.getDealer());
                UserEntity userEntity = userRepository.findOne(dealerEntity.getServiceManagerId());
                if (userEntity == null) {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }
                //启动流程
                variables.put("serviceManager", userEntity.getLogId());
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    DealerQuitRequestInfo dealerQuitRequest = findOneById(dealerQuitRequestInfo.getObjId());
                    dealerQuitRequest.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态
                    dealerQuitRequest.setStatus(DocStatus.AUDITING.getIndex());
                    save(dealerQuitRequest);
                    message.put("result", "提示");
                    message.put("message", "提交成功");
                    return message;
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }
            } else {
                message.put("result", "提示");
                message.put("message", "提交失败");
            }

            return message;
        } catch (IOException e) {
            e.printStackTrace();
            message.put("result", "提示");
            message.put("message", "提交失败");
            return message;
        }
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean desertTask(String objId) {
        DealerQuitRequestEntity dealerQuitRequestEntity = dealerQuitRepostitory.findOne(objId);
        try {
            if (processService.deleteProcessInstance(dealerQuitRequestEntity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(dealerQuitRequestEntity.getProcessInstanceId())) {
                dealerQuitRequestEntity.setStatus(DocStatus.OBSOLETE.getIndex());
                dealerQuitRequestEntity.setProcessInstanceId(null);
                dealerQuitRepostitory.save(dealerQuitRequestEntity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DealerQuitRequestInfo findOneById(String objId) {
        try {
            DealerQuitRequestInfo info = new DealerQuitRequestInfo();
            DealerQuitRequestEntity entity = dealerQuitRepostitory.findOne(objId);

            info = BeanUtils.copyPropertys(entity, info);

            DealerEntity dealerEntity = dealerRepository.findOne(entity.getDealer());
            DealerInfo dealerInfo = BeanUtils.copyPropertys(dealerEntity, new DealerInfo());
            info.setDealerInfo(dealerInfo);

            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存 实体
     *
     * @param dealerQuitRequestInfo
     * @return
     */
    @Override
    public DealerQuitRequestInfo save(DealerQuitRequestInfo dealerQuitRequestInfo) {
        try {
            DealerQuitRequestEntity entity = new DealerQuitRequestEntity();
            DealerEntity dealerEntity = new DealerEntity();
            dealerEntity = dealerRepository.save(BeanUtils.copyPropertys(dealerQuitRequestInfo.getDealerInfo(), dealerEntity));
            entity.setDealer(dealerQuitRequestInfo.getDealerInfo().getObjId());
            entity = dealerQuitRepostitory.save(BeanUtils.copyPropertys(dealerQuitRequestInfo, entity));
            return BeanUtils.copyPropertys(entity, dealerQuitRequestInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 dealerQuitRequestInfo 对象
     *
     * @param dealerQuitRequestInfo
     * @return
     */
    @Override
    public boolean delete(DealerQuitRequestInfo dealerQuitRequestInfo) {
        try {
            DealerQuitRequestEntity entity = new DealerQuitRequestEntity();
            entity = BeanUtils.copyPropertys(dealerQuitRequestInfo, entity);
            dealerQuitRepostitory.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 --> 通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByObjId(String objId) {
        try {
            dealerQuitRepostitory.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerQuitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerQuitRepostitory;
    }
}
