package com.sunjet.backend.quartz;


import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.flow.CommentEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeItemEntity;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleItemRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyItemRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.dto.asms.flow.CommentInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将关闭的三包服务单的结算信息写入待结算列表
 */
@DisallowConcurrentExecution
@Slf4j
public class WarrantyCheckStatusJob implements Job {
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;
    @Autowired
    private SupplyItemRepository supplyItemRepository;
    @Autowired
    private RecycleItemRepository recycleItemRepository;
    @Autowired
    private RecycleNoticeItemRepository recycleNoticeItemRepository;
    @Autowired
    RecycleNoticeRepository recycleNoticeRepository;
    @Autowired
    RecycleRepository recycleRepository;
    @Autowired
    SupplyRepository supplyRepository;
    @Autowired
    private ProcessService processService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            // 查找所有已审核的三包服务单
            List<WarrantyMaintenanceEntity> auditedWarranties = warrantyMaintenanceRepository.findAudited(DocStatus.AUDITED.getIndex());
            log.info("auditedWarranties.size():" + auditedWarranties.size());
            if (auditedWarranties.size() <= 0)
                return;

            // 存在已审核的三包单据
            for (WarrantyMaintenanceEntity maintenanceEntity : auditedWarranties) {
//                if (checkSupplyIsOk(maintenanceEntity)) {
                // 检查三包单对应的返回通知单（故障件返回单）是否已完成（已关闭、待结算、结算中或已结算）
                if (checkRecycleIsOk(maintenanceEntity)) {
                    maintenanceEntity.setStatus(DocStatus.CLOSED.getIndex());
                    warrantyMaintenanceRepository.save(maintenanceEntity);


                    String businessKey = maintenanceEntity.getClass().getSimpleName()
                            + "." + maintenanceEntity.getObjId()
                            + "." + maintenanceEntity.getDocNo()
                            + "." + maintenanceEntity.getSubmitterName();

                    List<CommentInfo> comments = processService.findCommentByBusinessKey(businessKey);
                    if (comments == null || comments.size() == 0) {
                        businessKey = maintenanceEntity.getClass().getSimpleName()
                                + "." + maintenanceEntity.getObjId()
                                + "." + maintenanceEntity.getDocNo()
                                + "." + maintenanceEntity.getSubmitterName()
                                + "." + maintenanceEntity.getSubmitter();

                        comments = processService.findCommentByBusinessKey(businessKey);
                    }

                    for (CommentInfo cmt : comments) {
                        CommentEntity commentEntity = new CommentEntity();
                        commentEntity.setFlowInstanceId(cmt.getProcessInstanceId());
                        commentEntity.setDoDate(cmt.getTime());
                        commentEntity.setUsername(maintenanceEntity.getSubmitterName());
                        commentEntity.setUserId(maintenanceEntity.getSubmitter());
                        commentEntity.setResult(cmt.getType());
                        commentEntity.setMessage(cmt.getFullMessage());
                        processService.saveComment(commentEntity);
                    }
                }
//                }
            }
        } catch (Exception ex) {

            log.info(ex.getMessage());
        }
    }


//    private CustomComment getBeanFromJson(String json) {
//        CustomComment comment;
//        try {//        return JsonHelper.json2Bean(json,CustomComment.class);
//            comment = JsonHelper.json2Bean(json, CustomComment.class);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            comment = new CustomComment("", json);
//        }
//        return comment;
//    }

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
                            || recycleEntity.getStatus() != DocStatus.WAITING_SETTLE.getIndex()
                            || recycleEntity.getStatus() != DocStatus.SETTLED.getIndex()
                            || recycleEntity.getStatus() != DocStatus.SETTLING.getIndex()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 根据三包维修单，判断其关联的供货单是否已经关闭
     *
     * @param warrantyEntity
     * @return
     */
//    private Boolean checkSupplyIsOk(WarrantyMaintenanceEntity warrantyEntity) {
//        Boolean supplyIsOk = true;     // 判断调拨供货单是否已关闭
//        if (StringUtils.isBlank(warrantyEntity.getSupplyNoticeId())) {
//            return true;
//        }
//        // 2. 获取该调拨通知单的所有子项。
//        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = supplyNoticeItemRepository.findByNoticeId(warrantyEntity.getSupplyNoticeId());
////                    SupplyNoticeEntity supplyNoticeEntity = supplyNoticeRepository.findOne(maintenanceEntity.getSupplyNoticeId());
//        // 3. 判断子项的需求数量和已发数量是否一致。
//        for (SupplyNoticeItemEntity noticeItem : supplyNoticeItemEntities) {
//            // 4. 需求数量大于已发送数量，表示还未全部发运回来，直接跳过
//            if (noticeItem.getRequestAmount() > noticeItem.getSentAmount()) {
//                supplyIsOk = false;
//                break;
//            }
//        }
//        // 5. 判断已发送的是否已经全部接收（流程是否关闭）
//        for (SupplyNoticeItemEntity noticeItem : supplyNoticeItemEntities) {
//            List<SupplyItemEntity> supplyItems = supplyItemRepository.findAllByNoticeItemId(noticeItem.getObjId());
//            //如果调拨供货单没有生成返回false
//            if (supplyItems.size() == 0) {
//                supplyIsOk = false;
//            }
//            for (SupplyItemEntity item : supplyItems) {
//                SupplyEntity supplyEntity = supplyRepository.findOne(item.getSupplyId());
//                if (supplyEntity.getStatus() != DocStatus.CLOSED.getIndex() && supplyEntity.getStatus() != DocStatus.WAITING_SETTLE.getIndex()
//                        && supplyEntity.getStatus() != DocStatus.SETTLED.getIndex() && supplyEntity.getStatus() != DocStatus.SETTLING.getIndex()) {
//                    supplyIsOk = false;
//                    break;
//                }
//            }
//            if (supplyIsOk == false) {
//                break;
//            }
//        }
//        return supplyIsOk;
//    }
}
