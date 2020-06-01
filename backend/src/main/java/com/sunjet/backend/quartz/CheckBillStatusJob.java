package com.sunjet.backend.quartz;


import com.sunjet.backend.base.FlowDocEntity;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.FirstMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.asm.FirstMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 检查服务单结算状态，并把状态反写到单据上
 */
@DisallowConcurrentExecution
@Slf4j
public class CheckBillStatusJob implements Job {
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private FirstMaintenanceRepository firstMaintenanceRepository;
    @Autowired
    private ActivityMaintenanceRepository activityMaintenanceRepository;
    @Autowired
    private RecycleRepository recycleRepository;
    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            // 检查首保单，服务结算单
            List<FirstMaintenanceEntity> firstMaintenanceEntities = firstMaintenanceRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (FirstMaintenanceEntity firstMaintenanceEntity : firstMaintenanceEntities) {
                this.chkEntity(firstMaintenanceRepository, firstMaintenanceEntity);
            }
            List<FirstMaintenanceEntity> firstMaintenanceEntities2 = firstMaintenanceRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (FirstMaintenanceEntity firstMaintenanceEntity : firstMaintenanceEntities2) {
                this.chkEntity(firstMaintenanceRepository, firstMaintenanceEntity);
            }

            // 检查活动服务单，服务结算单
            List<ActivityMaintenanceEntity> activityMaintenanceEntities = activityMaintenanceRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (ActivityMaintenanceEntity activityMaintenanceEntity : activityMaintenanceEntities) {
                this.chkEntity(activityMaintenanceRepository, activityMaintenanceEntity);
            }
            List<ActivityMaintenanceEntity> activityMaintenanceEntities2 = activityMaintenanceRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (ActivityMaintenanceEntity activityMaintenanceEntity : activityMaintenanceEntities2) {
                this.chkEntity(activityMaintenanceRepository, activityMaintenanceEntity);
            }

            // 检查三包服务单，服务结算单
            List<WarrantyMaintenanceEntity> warrantyMaintainEntities = warrantyMaintenanceRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintainEntities) {
                this.chkEntity(warrantyMaintenanceRepository, warrantyMaintenanceEntity);
            }
            List<WarrantyMaintenanceEntity> warrantyMaintainEntities2 = warrantyMaintenanceRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintainEntities2) {
                this.chkEntity(warrantyMaintenanceRepository, warrantyMaintenanceEntity);
            }

            // 检查回收件返回单，运费结算单
            List<RecycleEntity> recycleEntities = recycleRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (RecycleEntity recycleEntity : recycleEntities) {
                this.chkEntity(recycleRepository, recycleEntity);
            }

            List<RecycleEntity> recycleEntities2 = recycleRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (RecycleEntity recycleEntity : recycleEntities2) {
                this.chkEntity(recycleRepository, recycleEntity);
            }

            // 检查发货单,配件结算单
            List<SupplyEntity> supplyEntities = supplyRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (SupplyEntity supplyEntity : supplyEntities) {
                this.chkEntity(supplyRepository, supplyEntity);
            }
            List<SupplyEntity> supplyEntities2 = supplyRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (SupplyEntity supplyEntity : supplyEntities2) {
                this.chkEntity(supplyRepository, supplyEntity);
            }


        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    private void chkEntity(JpaRepository repository, FlowDocEntity entity) {
        PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(entity.getObjId());
        if (!pendingSettlementDetailsEntity.getStatus().equals(entity.getStatus())) {
            entity.setStatus(pendingSettlementDetailsEntity.getStatus());
            repository.save(entity);
        }
//        if (pendingSettlementDetailsEntity != null) {
//            if (pendingSettlementDetailsEntity.getSettlementStatus() != null
//                    && !pendingSettlementDetailsEntity.getSettlementStatus().equals(entity.getStatus())) {
//                entity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                repository.save(entity);
//            }
//        }
    }


}
