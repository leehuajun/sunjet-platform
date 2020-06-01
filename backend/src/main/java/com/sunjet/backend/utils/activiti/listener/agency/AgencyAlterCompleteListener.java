package com.sunjet.backend.utils.activiti.listener.agency;


import com.sunjet.backend.modules.asms.entity.agency.AgencyAlterRequestEntity;
import com.sunjet.backend.modules.asms.entity.basic.AgencyEntity;
import com.sunjet.backend.modules.asms.repository.agency.AgencyAlterRepostitory;
import com.sunjet.backend.modules.asms.repository.basic.AgencyRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.utils.common.BeanHelper;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 合作商变更申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("agencyAlterCompleteListener")
public class AgencyAlterCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private AgencyAlterRepostitory agencyAlterRepostitory;
    @Autowired
    private AgencyRepository agencyRepository;

//    @Override
//    public void notify(DelegateTask delegateTask) {
//        System.out.println("关闭业务对象！！");
//        // 1. 获取业务ID
//        String businessId = processService.findBusinessIdByProcessInstanceId(delegateTask.getProcessInstanceId());
//        // 2. 获取业务对象
//        DealerAdmitRequestEntity admitRequest = dealerAdmitService.findOneById(businessId);
//        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
//        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
////        delegateTask.getProcessInstanceId().
//    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println("合作商变更申请审批完成监听器--关闭业务对象？？");
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        AgencyAlterRequestEntity alterRequest = agencyAlterRepostitory.findOne(businessId);
        // 3. 获取业务对象的某个属性
        String agency_id = alterRequest.getAgency();
        AgencyEntity agencyEntity = agencyRepository.findOne(agency_id);
        // 赋值
        BeanUtils.copyProperties(alterRequest, agencyEntity, BeanHelper.getNullPropertyNames(alterRequest));
//        dealerEntity.setEnabled(true);
        agencyEntity.setObjId(agency_id);
        agencyEntity.setEnabled(true);
        agencyRepository.save(agencyEntity);
        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
    }

}
