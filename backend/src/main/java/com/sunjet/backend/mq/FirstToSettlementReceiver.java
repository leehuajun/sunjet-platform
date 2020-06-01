package com.sunjet.backend.mq;

import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.FirstMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.asm.FirstMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
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
@RabbitListener(queues = "first_to_settlement")
public class FirstToSettlementReceiver {
    @Autowired
    FirstMaintenanceRepository firstMaintenanceRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    //    @RabbitListener(queues = "first_to_settlement")
    @RabbitHandler
    public void receiveFirstToSettlement(String objId) {

        try {
            FirstMaintenanceEntity firstMaintenance = firstMaintenanceRepository.findOne(objId);
            PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(firstMaintenance.getObjId());
            //如果已经生成了结算单
            if (pendingSettlementDetail != null) {
                return;
            }
            PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
            DealerEntity parentDealer = dealerRepository.findParentDealerByDealerCode(firstMaintenance.getDealerCode());
            if (parentDealer == null) {
                entity.setDealerCode(firstMaintenance.getDealerCode());
                entity.setDealerName(firstMaintenance.getDealerName());
            } else {
                entity.setDealerCode(parentDealer.getCode());
                entity.setDealerName(parentDealer.getName());
                entity.setSecondDealerCode(firstMaintenance.getDealerCode());
                entity.setSecondDealerName(firstMaintenance.getDealerName());
            }
            VehicleEntity vehicleEntity = vehicleRepository.findOne(firstMaintenance.getVehicleId());
            entity.setWorkingExpense(firstMaintenance.getStandardExpense());
            entity.setOtherExpense((firstMaintenance.getOtherExpense() == null ? 0.0 : firstMaintenance.getOtherExpense()) + (firstMaintenance.getNightExpense() == null ? 0.0 : firstMaintenance.getNightExpense()));
            entity.setOutExpense(firstMaintenance.getOutExpense());
            entity.setExpenseTotal(firstMaintenance.getExpenseTotal());
            entity.setPartExpense(0.0);
            entity.setFreightExpense(0.0);
            entity.setBusinessDate(firstMaintenance.getCreatedTime());
            entity.setSrcDocID(firstMaintenance.getObjId());
            entity.setSrcDocNo(firstMaintenance.getDocNo());
            entity.setSrcDocType("首保服务单");
            entity.setSettlementDocType("服务结算单");
            entity.setCreatedTime(new Date());
            entity.setCreaterName("job");
            entity.setEnabled(true);
            entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());

            entity.setTypeCode(firstMaintenance.getTypeCode());
            entity.setVin(vehicleEntity.getVin());
            pendingSettlementDetailsRepository.save(entity);

            firstMaintenance.setModifiedTime(new Date());
            firstMaintenance.setModifierName("job");
            // 把数据取过来后，把单据状态修改为待结算状态
            firstMaintenance.setStatus(DocStatus.WAITING_SETTLE.getIndex());
            firstMaintenanceRepository.save(firstMaintenance);
            log.info("生成首保结算单");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

//    @RabbitListener(queues = "freight_to_settlement")
//    public void receiveFreightToSettlement(String message) {
//        // message可以理解为任何内容，这里存储单据id
//        System.out.println("Receive: " + message);
//    }
}
