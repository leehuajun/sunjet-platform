package com.sunjet.backend.mq;

import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyEntity;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: lhj
 * @create: 2017-11-11 13:47
 * @description: 说明
 */
@Slf4j
@Component
@RabbitListener(queues = "part_to_settlement")
public class PartToSettlementReceiver {
    @Autowired
    SupplyRepository supplyRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;


    @RabbitHandler
    public void receiveFirstToSettlement(String objId) {
        SupplyEntity supply = supplyRepository.findOne(objId);
        try {
            PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(supply.getObjId());
            if (pendingSettlementDetail != null) {
                return;
            }
            PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
            entity.setAgencyName(supply.getAgencyName());
            entity.setAgencyCode(supply.getAgencyCode());
            entity.setDealerCode(supply.getDealerCode());
            entity.setDealerName(supply.getDealerName());
            entity.setOtherExpense(supply.getOtherExpense());
            entity.setOutExpense(0.0);
            entity.setExpenseTotal(supply.getExpenseTotal());
            entity.setPartExpense(supply.getPartExpense());
            entity.setFreightExpense(supply.getTransportExpense());
            entity.setBusinessDate(supply.getCreatedTime());
            entity.setSrcDocID(supply.getObjId());
            entity.setSrcDocNo(supply.getDocNo());
            entity.setSrcDocType("调拨供货单");
            entity.setSettlementDocType("配件结算单");
            entity.setCreatedTime(new Date());
            entity.setCreaterName("job");
            entity.setEnabled(true);
//                entity.setSettlement(false);
            entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());
            pendingSettlementDetailsRepository.save(entity);
//                supply.setSettlement(true);
            supply.setModifiedTime(new Date());
            supply.setModifierName("job");
            // 把数据取过来后，把单据状态修改为待结算状态
            supply.setStatus(DocStatus.WAITING_SETTLE.getIndex());
            SupplyEntity supplyEntity = supplyRepository.save(supply);
            log.info("生成配件结算单");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
