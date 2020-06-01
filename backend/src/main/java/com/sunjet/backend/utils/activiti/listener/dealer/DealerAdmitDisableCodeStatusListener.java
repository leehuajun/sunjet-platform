package com.sunjet.backend.utils.activiti.listener.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAdmitRepostitory;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("dealerAdmitDisableCodeStatusListener")
public class DealerAdmitDisableCodeStatusListener implements TaskListener {
    @Autowired
    private DealerAdmitRepostitory dealerAdmitRepostitory;

    @Override
    public void notify(DelegateTask delegateTask) {
        DealerAdmitRequestEntity dealerAdmitRequest = dealerAdmitRepostitory.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        dealerAdmitRequest.setCanEditCode(false);
        dealerAdmitRepostitory.save(dealerAdmitRequest);
        System.out.println("切换服务站编号可编辑状态为:" + dealerAdmitRequest.getCanEditCode());

    }
}
