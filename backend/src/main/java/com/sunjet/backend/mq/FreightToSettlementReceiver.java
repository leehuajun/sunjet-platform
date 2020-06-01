package com.sunjet.backend.mq;

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
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: lhj
 * @create: 2017-11-11 13:47
 * @description: 说明
 */
@Slf4j
@Component
@RabbitListener(queues = "freight_to_settlement")
public class FreightToSettlementReceiver {
    @Autowired
    private RecycleRepository recycleRepository;
    @Autowired
    private RecycleItemRepository recycleItemRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private RecycleNoticeItemRepository recycleNoticeItemRepository;


    @RabbitHandler
    public void receiveFirstToSettlement(String objId) {
        RecycleEntity recycle = recycleRepository.findOne(objId);
        try {
            PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(recycle.getObjId());
            if (pendingSettlementDetail != null) {
                return;
            }
            PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
            DealerEntity parentDealer = dealerRepository.findParentDealerByDealerCode(recycle.getDealerCode());
            if (parentDealer == null) {
                entity.setDealerCode(recycle.getDealerCode());
                entity.setDealerName(recycle.getDealerName());
            } else {
                entity.setDealerCode(parentDealer.getCode());
                entity.setDealerName(parentDealer.getName());
                entity.setSecondDealerCode(recycle.getDealerCode());
                entity.setSecondDealerName(recycle.getDealerName());
            }
            entity.setOtherExpense(recycle.getOtherExpense());
            entity.setOutExpense(0.0);
            entity.setExpenseTotal(recycle.getExpenseTotal());
            entity.setFreightExpense(recycle.getTransportExpense());
            entity.setBusinessDate(recycle.getCreatedTime());
            entity.setSrcDocID(recycle.getObjId());
            entity.setSrcDocNo(recycle.getDocNo());
            entity.setSrcDocType("故障件返回单");
            entity.setSettlementDocType("运费结算单");
            entity.setCreatedTime(new Date());
            entity.setCreaterName("job");
            entity.setEnabled(true);
//                entity.setSettlement(false);
            entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());
            pendingSettlementDetailsRepository.save(entity);
            recycle.setSettlement(true);
            recycle.setModifiedTime(new Date());
            recycle.setModifierName("job");
            // 把数据取过来后，把单据状态修改为待结算状态
            recycle.setStatus(DocStatus.WAITING_SETTLE.getIndex());
            RecycleEntity recycleEntity = recycleRepository.save(recycle);

            //检查三包单状态是否已经审核
            checkWarrantyStatus(recycleEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 检查三包单状态是否已经审核
     *
     * @param recycle
     */
    private void checkWarrantyStatus(RecycleEntity recycle) {
        //获得所有的返回单明细
        List<RecycleItemEntity> recycleItemEntities = recycleItemRepository.findByRecycle(recycle.getObjId());
        //遍历所有的返回单明细
        for (RecycleItemEntity recycleItemEntity : recycleItemEntities) {
            //通过来源单号获取三包单
            WarrantyMaintenanceEntity warrantyMaintenance = warrantyMaintenanceRepository.findOneByDocNo(recycleItemEntity.getSrcDocNo());
            //如果三包单已审核,检验故障件返回单是否已经完成
            if (warrantyMaintenance.getStatus().equals(DocStatus.AUDITED.getIndex())) {
                // 检查三包单对应的返回通知单（故障件返回单）是否已完成（已关闭、待结算、结算中或已结算）
                if (checkRecycleIsOk(warrantyMaintenance)) {
                    //如果已完成 生成待结算单
                    PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(warrantyMaintenance.getObjId());
                    //如果已经存在就不再生成
                    if (pendingSettlementDetail != null) {
                        return;
                    }
                    PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
                    DealerEntity parentDealer = dealerRepository.findParentDealerByDealerCode(warrantyMaintenance.getDealerCode());
                    if (parentDealer == null) {
                        entity.setDealerCode(warrantyMaintenance.getDealerCode());
                        entity.setDealerName(warrantyMaintenance.getDealerName());
                    } else {
                        entity.setDealerCode(parentDealer.getCode());
                        entity.setDealerName(parentDealer.getName());
                        entity.setSecondDealerCode(warrantyMaintenance.getDealerCode());
                        entity.setSecondDealerName(warrantyMaintenance.getDealerName());
                    }
                    VehicleEntity vehicleEntity = vehicleRepository.findOne(warrantyMaintenance.getVehicleId());

                    entity.setWorkingExpense(warrantyMaintenance.getMaintainWorkTimeExpense());
                    entity.setOtherExpense((warrantyMaintenance.getOtherExpense() == null ? 0.0 : warrantyMaintenance.getOtherExpense()) + (warrantyMaintenance.getNightExpense() == null ? 0.0 : warrantyMaintenance.getNightExpense()));
                    entity.setOutExpense(warrantyMaintenance.getOutExpense());
                    entity.setExpenseTotal(warrantyMaintenance.getSettlementTotleExpense());
                    entity.setPartExpense(warrantyMaintenance.getSettlementPartExpense() + warrantyMaintenance.getSettlementAccesoriesExpense());
                    entity.setFreightExpense(0.0);
                    entity.setBusinessDate(warrantyMaintenance.getCreatedTime());
                    entity.setSrcDocID(warrantyMaintenance.getObjId());
                    entity.setSrcDocNo(warrantyMaintenance.getDocNo());
                    entity.setSrcDocType("三包服务单");
                    entity.setSettlementDocType("服务结算单");
                    entity.setCreatedTime(new Date());
                    entity.setCreaterName("job");
                    entity.setEnabled(true);
//                entity.setSettlement(false);
                    entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                    entity.setTypeCode(warrantyMaintenance.getTypeCode());
                    entity.setVin(vehicleEntity.getVin());
                    pendingSettlementDetailsRepository.save(entity);
//                model.setSettlement(true);
                    warrantyMaintenance.setModifiedTime(new Date());
                    warrantyMaintenance.setModifierName("job");
                    // 把数据取过来后，把单据状态修改为待结算状态
                    warrantyMaintenance.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                    log.info("生成三包服务单");
                    warrantyMaintenanceRepository.save(warrantyMaintenance);

                }
            }

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
        return true;
    }
}
