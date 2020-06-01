package com.sunjet.backend.quartz;


import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleItemRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将关闭的三包服务单的结算信息写入待结算列表
 */
@DisallowConcurrentExecution
@Slf4j
public class WarrantyMaintenanceSettlementHandleJob implements Job {
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private RecycleNoticeItemRepository recycleNoticeItemRepository;
    @Autowired
    private RecycleItemRepository recycleItemRepository;
    @Autowired
    private RecycleRepository recycleRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllbySettlement(DocStatus.AUDITED.getIndex());
            for (WarrantyMaintenanceEntity model : warrantyMaintenanceEntities) {
                if (checkRecycleIsOk(model)) {
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
                    VehicleEntity vehicleEntity = vehicleRepository.findOne(model.getVehicleId());

                    entity.setWorkingExpense(model.getMaintainWorkTimeExpense());
                    entity.setOtherExpense((model.getOtherExpense() == null ? 0.0 : model.getOtherExpense()) + (model.getNightExpense() == null ? 0.0 : model.getNightExpense()));
                    entity.setOutExpense(model.getOutExpense());
                    entity.setExpenseTotal(model.getSettlementTotleExpense());
                    entity.setPartExpense(model.getSettlementPartExpense() + model.getSettlementAccesoriesExpense());
                    entity.setFreightExpense(0.0);
                    entity.setBusinessDate(model.getCreatedTime());
                    entity.setSrcDocID(model.getObjId());
                    entity.setSrcDocNo(model.getDocNo());
                    entity.setSrcDocType("三包服务单");
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
                    model.setModifierName("job");
                    // 把数据取过来后，把单据状态修改为待结算状态
                    model.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                    warrantyMaintenanceRepository.save(model);
                }
            }
            log.info("WarrantyMaintenanceSettlementHandleJob");
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    /**
     * 根据三包维修单，判断其关联的故障件返回单是否已经关闭
     *
     * @param warrantyEntity
     * @return
     */
    private Boolean checkRecycleIsOk(WarrantyMaintenanceEntity warrantyEntity) {
        // 如果没有返回通知单（三包服务单没有配件需要返回）
        if (StringUtils.isBlank(warrantyEntity.getRecycleNoticeId())) {
            return true;
        }

        // 获取三包服务单对应的返回通知单的明细
        List<RecycleNoticeItemEntity> recycleNoticeItemEntities = recycleNoticeItemRepository.findByRecycleNoticeId(warrantyEntity.getRecycleNoticeId());
        for (RecycleNoticeItemEntity noticeItem : recycleNoticeItemEntities) {
            // 4. 如果存在某个需返回零件的需求数量大于已发送数量，表示还未全部发运回来，直接返回false，表示返回业务未完成
            if (noticeItem.getAmount() > noticeItem.getBackAmount()) {
                return false;
            } else {
                List<RecycleItemEntity> recycleItems = recycleItemRepository.findAllByNoticeItemId(noticeItem.getObjId());
                for (RecycleItemEntity item : recycleItems) {
                    RecycleEntity recycleEntity = recycleRepository.findOne(item.getRecycle());
                    //不是作废单据
                    if(!recycleEntity.getStatus().equals(DocStatus.OBSOLETE.getIndex())){
                        //如果故障件返回单的状态不等于关闭状态返回false
                        if (recycleEntity.getStatus() != DocStatus.CLOSED.getIndex()
                                & recycleEntity.getStatus() != DocStatus.WAITING_SETTLE.getIndex()
                                & recycleEntity.getStatus() != DocStatus.SETTLED.getIndex()
                                & recycleEntity.getStatus() != DocStatus.SETTLING.getIndex()) {
                            return false;
                        }
                    }

                }
            }
        }
        return true;
    }


}
