package com.sunjet.backend.utils.activiti.listener.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAdmitRepostitory;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Created by lhj on 16/11/23.
 */
@Transactional
@Component("dealerAdmitDisableServiceManagerStatusListener")
public class DealerAdmitDisableServiceManagerStatusListener implements TaskListener {
    @Autowired
    private DealerAdmitRepostitory dealerAdmitRepostitory;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];
        DealerAdmitRequestEntity dealerAdmitRequest = dealerAdmitRepostitory.findOne(businessId);

        dealerAdmitRequest.setCanEditServiceManager(false);  // 切换状态
        dealerAdmitRepostitory.save(dealerAdmitRequest);
        System.out.println("切换服务经理可编辑状态为:" + dealerAdmitRequest.getCanEditServiceManager());
    }
}
