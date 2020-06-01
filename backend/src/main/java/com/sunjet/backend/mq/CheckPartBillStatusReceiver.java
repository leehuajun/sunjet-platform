package com.sunjet.backend.mq;


import com.sunjet.backend.base.FlowDocEntity;
import com.sunjet.backend.modules.asms.entity.settlement.AgencySettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PartExpenseListEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.settlement.AgencySettlementRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PartExpenseListRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检查配件结算单状态
 */
@Slf4j
@Component
@RabbitListener(queues = "check_part_status")
public class CheckPartBillStatusReceiver {
    @Autowired
    private AgencySettlementRepository agencySettlementRepository;
    @Autowired
    private PartExpenseListRepository partExpenseListRepository;

    @Autowired
    private SupplyRepository supplyRepository;


    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @RabbitHandler
    public void receiveCheckWarrantyBillStatus(String objId) {
        try {

            AgencySettlementEntity agencySettlementEntity = agencySettlementRepository.findOne(objId);

            List<PartExpenseListEntity> partExpenseListEntityList = partExpenseListRepository.findByAgencySettlementId(agencySettlementEntity.getObjId());

            for (PartExpenseListEntity partExpenseListEntity : partExpenseListEntityList) {
                chkEntity(supplyRepository, supplyRepository.findOne(partExpenseListEntity.getSrcDocID()));
                System.out.println("生成配件结算单");
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
