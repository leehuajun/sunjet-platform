package com.sunjet.backend.utils.activiti.listener.settlement;

import com.sunjet.backend.modules.asms.entity.settlement.AgencySettlementEntity;
import com.sunjet.backend.modules.asms.repository.settlement.AgencySettlementRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("agencySettlementEnableAssessListener")
public class AgencySettlementEnableAssessListener implements TaskListener {
    @Autowired
    private AgencySettlementRepository agencySettlementRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        AgencySettlementEntity agencySettlementEntity = agencySettlementRepository.findOne(businessId);
        // 切换状态
        //if (agencySettlementEntity.getCanEditAssess() == null || agencySettlementEntity.getCanEditAssess() == false) {
        agencySettlementEntity.setCanEditAssess(true);
        //} else {
        //    agencySettlementEntity.setCanEditAssess(false);
        //}
        agencySettlementRepository.save(agencySettlementEntity);

    }
}
