package com.sunjet.backend.utils.activiti.listener.dealer;


import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerLevelChangeRepostitory;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.utils.common.BeanHelper;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务站等级变更申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("dealerLevelChangeCompleteListener")
public class DealerLevelChangeCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;

    @Autowired
    private DealerLevelChangeRepostitory dealerLevelChangeRepostitory;
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
        System.out.println("服务站等级变更申请审批完成监听器--关闭业务对象？？");
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        DealerLevelChangeRequestEntity levelChangeRequest = dealerLevelChangeRepostitory.findOne(businessId);
        // 3. 获取业务对象的某个属性
        DealerEntity dealerEntity = dealerRepository.findOne(levelChangeRequest.getDealer());
        String dealer_id = dealerEntity.getObjId();
        // 赋值
        BeanUtils.copyProperties(levelChangeRequest, dealerEntity, BeanHelper.getNullPropertyNames(levelChangeRequest));
//        dealerEntity.setEnabled(true);
        dealerEntity.setObjId(dealer_id);
        dealerEntity.setEnabled(true);
        dealerRepository.save(dealerEntity);
        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
    }

}
