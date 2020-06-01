package com.sunjet.backend.modules.asms.service.dealer;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerAlterRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAlterRequestView;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAlterRepostitory;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAlterViewRepostitory;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.dealer.DealerAlterRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAlterRequestItem;
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
 * 服务站变更申请
 */
@Transactional
@Service("dealerAlterService")
public class DealerAlterServiceImpl implements DealerAlterService {
    @Autowired
    private DealerAlterRepostitory dealerAlterRepostitory;
    @Autowired
    private DealerAlterViewRepostitory dealerAlterViewRepostitory;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private ProcessService processService;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<DealerAlterRequestInfo> findAll() {
        try {
            List<DealerAlterRequestEntity> list = this.dealerAlterRepostitory.findAll();
            List<DealerAlterRequestInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (DealerAlterRequestEntity dealerAlterRequestEntity : list) {
                    DealerAlterRequestInfo info = new DealerAlterRequestInfo();
                    infos.add(BeanUtils.copyPropertys(dealerAlterRequestEntity, info));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            DealerAlterRequestInfo dealerAlterRequestInfo = JsonHelper.map2Bean(variables.get("entity"), DealerAlterRequestInfo.class);
            if (!dealerAlterRequestInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                DealerAlterRequestEntity entity = BeanUtils.copyPropertys(dealerAlterRequestInfo, new DealerAlterRequestEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, null, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    DealerAlterRequestInfo dealerAlterRequest = findOneById(dealerAlterRequestInfo.getObjId());
                    dealerAlterRequest.setProcessInstanceId(processInstanceInfo.getId());

                    //变更状态

                    dealerAlterRequest.setStatus(DocStatus.AUDITING.getIndex());
                    save(dealerAlterRequest);

                    message.put("result", "提示");
                    message.put("message", "提交成功");
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
        DealerAlterRequestEntity dealerAlterRequestEntity = dealerAlterRepostitory.findOne(objId);
        try {
            if (processService.deleteProcessInstance(dealerAlterRequestEntity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(dealerAlterRequestEntity.getProcessInstanceId())) {
                dealerAlterRequestEntity.setStatus(DocStatus.OBSOLETE.getIndex());
                dealerAlterRequestEntity.setProcessInstanceId(null);
                dealerAlterRepostitory.save(dealerAlterRequestEntity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<DealerAlterRequestView> getPageList(PageParam<DealerAlterRequestItem> pageParam) {

        //1.查询条件
        DealerAlterRequestItem dealerAlterRequestItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DealerAlterRequestView> specification = null;

        if (dealerAlterRequestItem != null) {

            specification = Specifications.<DealerAlterRequestView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .predicate(StringUtils.isNotBlank(dealerAlterRequestItem.getSubmitter()),
                            com.sunjet.backend.system.Jpa.Specifications.or()
                                    .eq(StringUtils.isNotBlank(dealerAlterRequestItem.getSubmitter()), "submitter", dealerAlterRequestItem.getSubmitter())
                                    .eq(StringUtils.isNotBlank(dealerAlterRequestItem.getServiceManagerId()), "serviceManagerId", dealerAlterRequestItem.getServiceManagerId())
                                    .build())
                    .like(StringUtils.isNotBlank(dealerAlterRequestItem.getCode()), "code", dealerAlterRequestItem.getCode())
                    .like(StringUtils.isNotBlank(dealerAlterRequestItem.getName()), "name", dealerAlterRequestItem.getName())
                    .eq(StringUtils.isNotBlank(dealerAlterRequestItem.getProvinceId()), "provinceId", dealerAlterRequestItem.getProvinceId())
                    .eq(!dealerAlterRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", dealerAlterRequestItem.getStatus())
                    .ge(dealerAlterRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .eq(StringUtils.isNotBlank(dealerAlterRequestItem.getStar()), "star", dealerAlterRequestItem.getStar())
                    .eq(StringUtils.isNotBlank(dealerAlterRequestItem.getQualification()), "qualification", dealerAlterRequestItem.getQualification())
                    //.eq(StringUtils.isNotBlank(dealerAlterRequestItem.getServiceManagerId()), "serviceManagerId", dealerAlterRequestItem.getServiceManagerId())
                    .between((dealerAlterRequestItem.getStartDate() != null && dealerAlterRequestItem.getEndDate() != null), "createdTime", new Range<Date>(dealerAlterRequestItem.getStartDate(), dealerAlterRequestItem.getEndDate()))
                    .build();
        }


        //3.执行查询
        Page<DealerAlterRequestView> pages = dealerAlterViewRepostitory.findAll(specification, PageUtil.getPageRequest(pageParam));

//        //4.数据转换
//        List<DealerAlterRequestInfo> rows = new ArrayList<>();
//        for(DealerAlterRequestEntity entity :pages.getContent()){
//            DealerAlterRequestInfo info = entityToInfo(entity);
//            rows.add(info);
//        }


        //5.组装分页信息及集合信息
        //PageResult<DealerAlterRequestInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    @Override
    public DealerAlterRequestInfo findOneById(String objId) {
        try {
            DealerAlterRequestEntity entity = dealerAlterRepostitory.findOne(objId);
            DealerAlterRequestInfo dealerAlterRequestInfo = BeanUtils.copyPropertys(entity, new DealerAlterRequestInfo());

            DealerInfo dealerInfo = new DealerInfo();
            if (StringUtils.isNotBlank(dealerAlterRequestInfo.getDealer())) {
                DealerEntity dealerEntity = dealerRepository.findOne(entity.getDealer());
                dealerInfo = BeanUtils.copyPropertys(dealerEntity, new DealerInfo());

            }
            dealerAlterRequestInfo.setDealerInfo(dealerInfo);

            return dealerAlterRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存 实体
     *
     * @param dealerAlterRequestInfo
     * @return
     */
    @Override
    public DealerAlterRequestInfo save(DealerAlterRequestInfo dealerAlterRequestInfo) {
        try {
            DealerAlterRequestEntity entity = new DealerAlterRequestEntity();
            DealerEntity dealerEntity = dealerRepository.save(BeanUtils.copyPropertys(dealerAlterRequestInfo.getDealerInfo(), new DealerEntity()));
            DealerInfo dealerInfo = BeanUtils.copyPropertys(dealerEntity, new DealerInfo());
            entity.setDealer(dealerAlterRequestInfo.getDealerInfo().getObjId());
            entity = dealerAlterRepostitory.save(BeanUtils.copyPropertys(dealerAlterRequestInfo, entity));
            dealerAlterRequestInfo = BeanUtils.copyPropertys(entity, dealerAlterRequestInfo);
            dealerAlterRequestInfo.setDealerInfo(dealerInfo);

            return dealerAlterRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 dealerAlterRequestInfo 对象
     *
     * @param dealerAlterRequestInfo
     * @return
     */
    @Override
    public boolean delete(DealerAlterRequestInfo dealerAlterRequestInfo) {
        try {
            DealerAlterRequestEntity entity = new DealerAlterRequestEntity();
            entity = BeanUtils.copyPropertys(dealerAlterRequestInfo, entity);
            dealerAlterRepostitory.delete(entity);
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
            dealerAlterRepostitory.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerAlterRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerAlterRepostitory;
    }
}
