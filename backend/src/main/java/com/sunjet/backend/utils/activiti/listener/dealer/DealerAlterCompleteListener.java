package com.sunjet.backend.utils.activiti.listener.dealer;


import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerAlterRequestEntity;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAlterRepostitory;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.utils.common.BeanHelper;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务站变更申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("dealerAlterCompleteListener")
public class DealerAlterCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DealerAlterRepostitory dealerAlterRepostitory;
    @Autowired
    private DealerRepository dealerRepository;

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
        System.out.println("服务站变更申请审批完成监听器--关闭业务对象？？");
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        DealerAlterRequestEntity alterRequest = dealerAlterRepostitory.findOne(businessId);
        // 3. 获取业务对象的某个属性
        DealerEntity dealerEntity = dealerRepository.findOne(alterRequest.getDealer());
        String dealer_id = dealerEntity.getObjId();
        // 赋值
        BeanUtils.copyProperties(alterRequest, dealerEntity, BeanHelper.getNullPropertyNames(alterRequest));
        dealerEntity.setObjId(dealer_id);
        dealerEntity.setEnabled(true);
//        dealerEntity.setEnabled(true);
        dealerRepository.save(dealerEntity);


        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
    }

}
