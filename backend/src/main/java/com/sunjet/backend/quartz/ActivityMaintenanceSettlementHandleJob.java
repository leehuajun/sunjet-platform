package com.sunjet.backend.quartz;


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
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by ws on 16/11/25.
 * 将关闭的活动服务单的结算信息写入待结算列表
 */
@DisallowConcurrentExecution
@Slf4j
public class ActivityMaintenanceSettlementHandleJob implements Job {
    @Autowired
    private ActivityMaintenanceRepository activityMaintenanceRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<ActivityMaintenanceEntity> activityMaintenanceEntities = activityMaintenanceRepository.findAllbySettlement(DocStatus.CLOSED.getIndex());
            for (ActivityMaintenanceEntity model : activityMaintenanceEntities) {
                PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(model.getObjId());
                if (pendingSettlementDetail != null) {
                    continue;
                }

                PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
                DealerEntity parentDealer = dealerRepository.findParentDealerByDealerCode(model.getDealerCode());
                if (parentDealer == null) {
                    entity.setDealerCode(model.getDealerCode());
                    entity.setDealerName(model.getDealerName());
                } else {
                    entity.setDealerCode(parentDealer.getCode());
                    entity.setDealerName(parentDealer.getName());
                    entity.setSecondDealerCode(model.getDealerCode());
                    entity.setSecondDealerName(model.getDealerName());
                }
                ActivityVehicleEntity activityVehicleEntity = activityVehicleRepository.findOne(model.getActivityVehicleId());
                VehicleEntity vehicleEntity = vehicleRepository.findOne(activityVehicleEntity.getVehicleId());

                entity.setWorkingExpense(model.getStandardExpense());
                entity.setOtherExpense((model.getOtherExpense() == null ? 0.0 : model.getOtherExpense()) + (model.getNightExpense() == null ? 0.0 : model.getNightExpense()));
                entity.setOutExpense(model.getOutExpense());
                entity.setExpenseTotal(model.getExpenseTotal());
                entity.setPartExpense(0.0);
                entity.setFreightExpense(0.0);
                entity.setBusinessDate(model.getCreatedTime());
                entity.setSrcDocID(model.getObjId());
                entity.setSrcDocNo(model.getDocNo());
                entity.setSrcDocType("活动服务单");
                entity.setSettlementDocType("服务结算单");
                entity.setCreatedTime(new Date());
                entity.setCreaterName("job");
                entity.setEnabled(true);
//                entity.setSettlement(false);
                entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                entity.setTypeCode(model.getTypeCode());
                entity.setVin(vehicleEntity.getVin());
                pendingSettlementDetailsRepository.save(entity);
//                model.setSettlement(true);
                model.setModifiedTime(new Date());

                // 把数据取过来后，把单据状态修改为待结算状态
                model.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                activityMaintenanceRepository.save(model);
            }
            log.info("ActivityMaintenanceSettlementHandleJob");
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }


//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//
//    }


}
