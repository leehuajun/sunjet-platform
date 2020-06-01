package com.sunjet.backend.mq;


import com.sunjet.backend.base.FlowDocEntity;
import com.sunjet.backend.modules.asms.entity.settlement.FreightExpenseEntity;
import com.sunjet.backend.modules.asms.entity.settlement.FreightSettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleRepository;
import com.sunjet.backend.modules.asms.repository.settlement.FreightExpenseRepository;
import com.sunjet.backend.modules.asms.repository.settlement.FreightSettlementRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检查运费结算单状态
 */
@Slf4j
@Component
@RabbitListener(queues = "check_freight_status")
public class CheckFreightBillStatusReceiver {
    @Autowired
    private FreightSettlementRepository freightSettlementRepository;
    @Autowired
    private FreightExpenseRepository freightExpenseRepository;

    @Autowired
    private RecycleRepository recycleRepository;


    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @RabbitHandler
    public void receiveCheckWarrantyBillStatus(String objId) {
        try {

            FreightSettlementEntity freightSettlementEntity = freightSettlementRepository.findOne(objId);

            List<FreightExpenseEntity> freightExpenseEntityList = freightExpenseRepository.findByFreightSettlementId(freightSettlementEntity.getObjId());

            for (FreightExpenseEntity freightExpenseEntity : freightExpenseEntityList) {
                chkEntity(recycleRepository, recycleRepository.findOne(freightExpenseEntity.getSrcDocID()));
                System.out.println("生成运费结算单");
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
