package com.sunjet.backend.modules.asms.service.dealer;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerLevelChangeRequestView;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerLevelChangeRepostitory;
import com.sunjet.backend.modules.asms.repository.dealer.DealerLevelChangeViewRepostitory;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestInfo;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestItem;
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
 * 服务站等级变更
 */
@Transactional
@Service("dealerLevelChangeService")
public class DealerLevelChangeServiceImpl implements DealerLevelChangeService {
    @Autowired
    private DealerLevelChangeRepostitory dealerLevelChangeRepostitory;
    @Autowired
    private DealerLevelChangeViewRepostitory dealerLevelChangeViewRepostitory;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<DealerLevelChangeRequestInfo> findAll() {
        try {
            List<DealerLevelChangeRequestEntity> list = this.dealerLevelChangeRepostitory.findAll();
            List<DealerLevelChangeRequestInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (DealerLevelChangeRequestEntity dealerLevelChangeRequestEntity : list) {
                    DealerLevelChangeRequestInfo info = new DealerLevelChangeRequestInfo();
                    infos.add(BeanUtils.copyPropertys(dealerLevelChangeRequestEntity, info));
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
    public PageResult<DealerLevelChangeRequestView> getPageList(PageParam<DealerLevelChangeRequestItem> pageParam) {

        //1.查询条件
        DealerLevelChangeRequestItem dealerLevelChangeRequestItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DealerLevelChangeRequestView> specification = null;

        if (dealerLevelChangeRequestItem != null) {

            specification = Specifications.<DealerLevelChangeRequestView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .predicate(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getSubmitter()),
                            com.sunjet.backend.system.Jpa.Specifications.or()
                                    .eq(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getSubmitter()), "submitter", dealerLevelChangeRequestItem.getSubmitter())
                                    .eq(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getServiceManagerId()), "serviceManagerId", dealerLevelChangeRequestItem.getServiceManagerId())
                                    .build())
                    .like(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getCode()), "code", dealerLevelChangeRequestItem.getCode())
                    .like(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getName()), "name", dealerLevelChangeRequestItem.getName())
                    .eq(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getProvinceId()), "provinceId", dealerLevelChangeRequestItem.getProvinceId())
                    .eq(!dealerLevelChangeRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", dealerLevelChangeRequestItem.getStatus())
                    .ge(dealerLevelChangeRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .eq(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getStar()), "star", dealerLevelChangeRequestItem.getStar())
                    .eq(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getQualification()), "qualification", dealerLevelChangeRequestItem.getQualification())
                    //.eq(StringUtils.isNotBlank(dealerLevelChangeRequestItem.getServiceManagerId()), "serviceManagerId", dealerLevelChangeRequestItem.getServiceManagerId())
                    .between((dealerLevelChangeRequestItem.getStartDate() != null && dealerLevelChangeRequestItem.getEndDate() != null), "createdTime", new Range<Date>(dealerLevelChangeRequestItem.getStartDate(), dealerLevelChangeRequestItem.getEndDate()))
                    .build();
        }


        //3.执行查询
        Page<DealerLevelChangeRequestView> pages = dealerLevelChangeViewRepostitory.findAll(specification, PageUtil.getPageRequest(pageParam));

//        //4.数据转换
//        List<DealerLevelChangeRequestInfo> rows = new ArrayList<>();
//        for(DealerLevelChangeRequestEntity entity :pages.getContent()){
//            DealerLevelChangeRequestInfo info = entityToInfo(entity);
//            rows.add(info);
//        }


        //5.组装分页信息及集合信息
        //PageResult<DealerLevelChangeRequestInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

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
            DealerLevelChangeRequestInfo dealerLevelChangeRequestInfo = JsonHelper.map2Bean(variables.get("entity"), DealerLevelChangeRequestInfo.class);
            if (!dealerLevelChangeRequestInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                DealerLevelChangeRequestEntity entity = BeanUtils.copyPropertys(dealerLevelChangeRequestInfo, new DealerLevelChangeRequestEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                DealerEntity dealerEntity = dealerRepository.findOne(dealerLevelChangeRequestInfo.getDealer());
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
                    DealerLevelChangeRequestInfo dealerLevelChangeRequest = findOneById(dealerLevelChangeRequestInfo.getObjId());
                    dealerLevelChangeRequest.setProcessInstanceId(processInstanceInfo.getId());

                    //变更状态

                    dealerLevelChangeRequest.setStatus(DocStatus.AUDITING.getIndex());
                    save(dealerLevelChangeRequest);

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
        DealerLevelChangeRequestEntity dealerLevelChangeRequestEntity = dealerLevelChangeRepostitory.findOne(objId);
        try {
            if (processService.deleteProcessInstance(dealerLevelChangeRequestEntity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(dealerLevelChangeRequestEntity.getProcessInstanceId())) {
                dealerLevelChangeRequestEntity.setStatus(DocStatus.OBSOLETE.getIndex());
                dealerLevelChangeRequestEntity.setProcessInstanceId(null);
                dealerLevelChangeRepostitory.save(dealerLevelChangeRequestEntity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DealerLevelChangeRequestInfo findOneById(String objId) {
        try {
            DealerLevelChangeRequestInfo info = new DealerLevelChangeRequestInfo();
            DealerLevelChangeRequestEntity entity = dealerLevelChangeRepostitory.findOne(objId);

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
     * @param dealerLevelChangeRequestInfo
     * @return
     */
    @Override
    public DealerLevelChangeRequestInfo save(DealerLevelChangeRequestInfo dealerLevelChangeRequestInfo) {
        try {
            DealerLevelChangeRequestEntity entity = new DealerLevelChangeRequestEntity();
            DealerEntity dealerEntity = new DealerEntity();
            dealerEntity = dealerRepository.save(BeanUtils.copyPropertys(dealerLevelChangeRequestInfo.getDealerInfo(), dealerEntity));
            entity.setDealer(dealerLevelChangeRequestInfo.getDealerInfo().getObjId());
            entity = dealerLevelChangeRepostitory.save(BeanUtils.copyPropertys(dealerLevelChangeRequestInfo, entity));
            return BeanUtils.copyPropertys(entity, dealerLevelChangeRequestInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 dealerLevelChangeRequestInfo 对象
     *
     * @param dealerLevelChangeRequestInfo
     * @return
     */
    @Override
    public boolean delete(DealerLevelChangeRequestInfo dealerLevelChangeRequestInfo) {
        try {
            DealerLevelChangeRequestEntity entity = new DealerLevelChangeRequestEntity();
            entity = BeanUtils.copyPropertys(dealerLevelChangeRequestInfo, entity);
            dealerLevelChangeRepostitory.delete(entity);
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
            dealerLevelChangeRepostitory.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerLevelChangeRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerLevelChangeRepostitory;
    }
}
