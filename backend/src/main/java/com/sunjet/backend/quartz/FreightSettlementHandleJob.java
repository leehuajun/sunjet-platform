package com.sunjet.backend.quartz;


import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleRepository;
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
 * Created by zyh on 16/11/25.
 * 将关闭的故障件返回单的运费结算信息写入待结算列表
 */
@DisallowConcurrentExecution
@Slf4j
public class FreightSettlementHandleJob implements Job {
    @Autowired
    private RecycleRepository recycleRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<RecycleEntity> recycles = recycleRepository.findAllbySettlement(DocStatus.CLOSED.getIndex());
            for (RecycleEntity model : recycles) {
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
                entity.setOtherExpense(model.getOtherExpense());
                entity.setOutExpense(0.0);
                entity.setExpenseTotal(model.getExpenseTotal());
                entity.setFreightExpense(model.getTransportExpense());
                entity.setBusinessDate(model.getCreatedTime());
                entity.setSrcDocID(model.getObjId());
                entity.setSrcDocNo(model.getDocNo());
                entity.setSrcDocType("故障件返回单");
                entity.setSettlementDocType("运费结算单");
                entity.setCreatedTime(new Date());
                entity.setCreaterName("job");
                entity.setEnabled(true);
//                entity.setSettlement(false);
                entity.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                pendingSettlementDetailsRepository.save(entity);
                model.setSettlement(true);
                model.setModifiedTime(new Date());
                model.setModifierName("job");
                // 把数据取过来后，把单据状态修改为待结算状态
                model.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                recycleRepository.save(model);

            }
            log.info("FreightSettlementHandleJob");
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }


}

