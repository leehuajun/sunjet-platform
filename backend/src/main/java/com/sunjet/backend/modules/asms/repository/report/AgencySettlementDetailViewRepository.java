package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.AgencySettlementDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 合作商结算明细视图
 */
public interface AgencySettlementDetailViewRepository extends JpaRepository<AgencySettlementDetailView, String>, JpaSpecificationExecutor<AgencySettlementDetailView> {

}
