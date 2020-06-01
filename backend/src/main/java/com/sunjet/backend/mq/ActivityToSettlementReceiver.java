package com.sunjet.backend.mq;

import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
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
@RabbitListener(queues = "activity_to_settlement")
public class ActivityToSettlementReceiver {
    @Autowired
    ActivityMaintenanceRepository activityMaintenanceRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;

    @RabbitHandler
    public void receiveFirstToSettlement(String objId) {

        try {
            ActivityMaintenanceEntity activityMaintenance = activityMaintenanceRepository.findOne(objId);
            PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(activityMaintenance.getObjId());
            if (pendingSettlementDetail != null) {
                return;
            }

            PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
            DealerEntity parentDealer = dealerRepository.findParentDealerByDealerCode(activityMaintenance.getDealerCode());
            if (parentDealer == null) {
                entity.setDealerCode(activityMaintenance.getDealerCode());
                entity.setDealerName(activityMaintenance.getDealerName());
            } else {
                entity.setDealerCode(parentDealer.getCode());
                entity.setDealerName(parentDealer.getName());
                entity.setSecondDealerCode(activityMaintenance.getDealerCode());
                entity.setSecondDealerName(activityMaintenance.getDealerName());
            }
            ActivityVehicleEntity activityVehicleEntity = activityVehicleRepository.findOne(activityMaintenance.getActivityVehicleId());
            VehicleEntity vehicleEntity = vehicleRepository.findOne(activityVehicleEntity.getVehicleId());

            entity.setWorkingExpense(activityMaintenance.getStandardExpense());
            entity.setOtherExpense((activityMaintenance.getOtherExpense() == null ? 0.0 : activityMaintenance.getOtherExpense()) + (activityMaintenance.getNightExpense() == null ? 0.0 : activityMaintenance.getNightExpense()));
            entity.setOutExpense(activityMaintenance.getOutExpense());
            entity.setExpenseTotal(activityMaintenance.getExpenseTotal());
            entity.setPartExpense(0.0);
            entity.setFreightExpense(0.0);
            entity.setBusinessDate(activityMaintenance.getCreatedTime());
            entity.setSrcDocID(activityMaintenance.getObjId());
            entity.setSrcDocNo(activityMaintenance.getDocNo());
            entity.setSrcDocType("活动服务单");
            entity.setSettlementDocType("服务结算单");
            entity.setCreatedTime(new Date());
            entity.setCreaterName("job");
            entity.setEnabled(true);
//                entity.setSettlement(false);
            entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());
            entity.setTypeCode(activityMaintenance.getTypeCode());
            entity.setVin(vehicleEntity.getVin());
            pendingSettlementDetailsRepository.save(entity);
//                model.setSettlement(true);
            activityMaintenance.setModifiedTime(new Date());

            // 把数据取过来后，把单据状态修改为待结算状态
            activityMaintenance.setStatus(DocStatus.WAITING_SETTLE.getIndex());
            activityMaintenanceRepository.save(activityMaintenance);
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
