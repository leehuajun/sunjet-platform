package com.sunjet.backend.quartz;


import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyEntity;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将关闭的调拨供货单的结算信息写入待结算列表
 */
@DisallowConcurrentExecution
@Slf4j
public class PartSettlementHandleJob implements Job {
    @Autowired
    private SupplyRepository supplyRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<SupplyEntity> supplys = supplyRepository.findAllbySettlement(DocStatus.CLOSED.getIndex());
            for (SupplyEntity model : supplys) {
                PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(model.getObjId());
                if (pendingSettlementDetail != null) {
                    continue;
                }
                PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
                entity.setAgencyName(model.getAgencyName());
                entity.setAgencyCode(model.getAgencyCode());
                entity.setDealerCode(model.getDealerCode());
                entity.setDealerName(model.getDealerName());
                entity.setOtherExpense(model.getOtherExpense());
                entity.setOutExpense(0.0);
                entity.setExpenseTotal(model.getExpenseTotal());
                entity.setPartExpense(model.getPartExpense());
                entity.setFreightExpense(model.getTransportExpense());
                entity.setBusinessDate(model.getCreatedTime());
                entity.setSrcDocID(model.getObjId());
                entity.setSrcDocNo(model.getDocNo());
                entity.setSrcDocType("调拨供货单");
                entity.setSettlementDocType("配件结算单");
                entity.setCreatedTime(new Date());
                entity.setCreaterName("job");
                entity.setEnabled(true);
//                entity.setSettlement(false);
                entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                pendingSettlementDetailsRepository.save(entity);
//                model.setSettlement(true);
                model.setModifiedTime(new Date());
                model.setModifierName("job");
                // 把数据取过来后，把单据状态修改为待结算状态
                model.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                supplyRepository.save(model);
            }
            log.info("PartSettlementHandleJob");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }


}
