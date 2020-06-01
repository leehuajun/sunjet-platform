package com.sunjet.backend.utils.activiti.listener.settlement;


import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lhj on 16/11/23.
 */
@Component("pendingSettlementStatusListener")
public class PendingSettlementStatusListener implements TaskListener {
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        List<PendingSettlementDetailsEntity> pendingSettlementDetailsEntities =
                pendingSettlementDetailsRepository.getPendingsBySettlementID(businessId);
        for (PendingSettlementDetailsEntity pendingSettlementDetailsEntity :
                pendingSettlementDetailsEntities) {
            pendingSettlementDetailsEntity.setStatus(DocStatus.SETTLED.getIndex());
//            pendingSettlementDetailsEntity.setSettlementStatus(DocStatus.SETTLED.getIndex());
//            pendingSettlementDetailsRepository.save(pendingSettlementDetailsEntity);
        }
        pendingSettlementDetailsRepository.save(pendingSettlementDetailsEntities);

    }
}
