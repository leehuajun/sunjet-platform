package com.sunjet.backend.utils.activiti.listener.settlement;

import com.sunjet.backend.modules.asms.entity.settlement.DealerSettlementEntity;
import com.sunjet.backend.modules.asms.repository.settlement.DealerSettlementRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("dealerSettlementEnableAssessListener")
public class DealerSettlementEnableAssessListener implements TaskListener {
    @Autowired
    private DealerSettlementRepository dealerSettlementRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        DealerSettlementEntity dealerSettlementEntity = dealerSettlementRepository.findOne(businessId);
        // 切换状态
        //if (dealerSettlementEntity.getCanEditAssess() == null || dealerSettlementEntity.getCanEditAssess() == false) {
        dealerSettlementEntity.setCanEditAssess(true);
        //} else {
        //    dealerSettlementEntity.setCanEditAssess(false);
        //}
        dealerSettlementRepository.save(dealerSettlementEntity);

    }
}
