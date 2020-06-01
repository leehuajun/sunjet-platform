package com.sunjet.backend.mq;


import com.sunjet.backend.base.FlowDocEntity;
import com.sunjet.backend.modules.asms.entity.settlement.DealerSettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.ExpenseListEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.asm.FirstMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.settlement.DealerSettlementRepository;
import com.sunjet.backend.modules.asms.repository.settlement.ExpenseListRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检查服务结算单状态
 */
@Slf4j
@Component
@RabbitListener(queues = "check_warranty_status")
public class CheckWarrantyBillStatusReceiver {
    @Autowired
    private DealerSettlementRepository dealerSettlementRepository;
    @Autowired
    private ExpenseListRepository expenseListRepository;
    @Autowired
    private FirstMaintenanceRepository firstMaintenanceRepository;
    @Autowired
    private ActivityMaintenanceRepository activityMaintenanceRepository;
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @RabbitHandler
    public void receiveCheckWarrantyBillStatus(String objId) {
        try {

            DealerSettlementEntity dealerSettlementEntity = dealerSettlementRepository.findOne(objId);

            List<ExpenseListEntity> expenseListEntityList = expenseListRepository.findByDealerSettlementId(dealerSettlementEntity.getObjId());

            for (ExpenseListEntity expenseListEntity : expenseListEntityList) {
                if ("三包服务单".equals(expenseListEntity.getSrcDocType())) {
                    chkEntity(warrantyMaintenanceRepository, warrantyMaintenanceRepository.findOne(expenseListEntity.getSrcDocID()));
                } else if ("首保服务单".equals(expenseListEntity.getSrcDocType())) {
                    chkEntity(firstMaintenanceRepository, firstMaintenanceRepository.findOne(expenseListEntity.getSrcDocID()));
                } else if ("活动服务单".equals(expenseListEntity.getSrcDocType())) {
                    chkEntity(activityMaintenanceRepository, activityMaintenanceRepository.findOne(expenseListEntity.getSrcDocID()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void chkEntity(JpaRepository repository, FlowDocEntity entity) {
        PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(entity.getObjId());
        if (!pendingSettlementDetailsEntity.getStatus().equals(entity.getStatus())) {
            entity.setStatus(pendingSettlementDetailsEntity.getStatus());
            repository.save(entity);
        }

    }


}
