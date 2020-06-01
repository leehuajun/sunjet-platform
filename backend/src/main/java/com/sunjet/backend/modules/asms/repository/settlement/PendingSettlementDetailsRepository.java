package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * 待结算清单(配件／服务／运费)
 * Created by zyh on 16/11/14.
 */
public interface PendingSettlementDetailsRepository extends JpaRepository<PendingSettlementDetailsEntity, String>, JpaSpecificationExecutor<PendingSettlementDetailsEntity> {


    @Query("select fm from PendingSettlementDetailsEntity fm  where  fm.enabled=1 and fm.status=1000 and fm.settlementDocType like '%配件结算单' and fm.agencyCode= ?1 and  fm.businessDate between ?2 and ?3")
    List<PendingSettlementDetailsEntity> getAgencySelttlements(String agencyCode, Date startDate, Date endDate);

    @Query("select fm from PendingSettlementDetailsEntity fm  where  fm.srcDocID= ?1 ")
    PendingSettlementDetailsEntity getOneBySrcDocID(String srcDocID);

    @Query("select fm from PendingSettlementDetailsEntity fm  where  fm.settlementDocID= ?1 ")
    List<PendingSettlementDetailsEntity> getPendingsBySettlementID(String objId);

    @Query("select fm from PendingSettlementDetailsEntity fm  where  fm.enabled=1 and fm.status=1000 and fm.settlementDocType like '%服务结算单' and fm.dealerCode= ?1 and  fm.businessDate between ?2 and ?3")
    List<PendingSettlementDetailsEntity> getDealerSelttlements(String dealerCode, Date startDate, Date endDate);

    @Query("select fm from PendingSettlementDetailsEntity fm  where  fm.enabled=1 and fm.status=1000 and fm.settlementDocType like '%运费结算单' and fm.dealerCode= ?1 and  fm.businessDate between ?2 and ?3")
    List<PendingSettlementDetailsEntity> getFreightSelttlements(String code, Date startDate, Date endDate);

//    @Query("select fm from PendingSettlementDetailsEntity fm  where  fm.srcDocID= ?1 " )
//    PendingSettlementDetailsEntity findOneBySrcDocNo(String docNo);
}
