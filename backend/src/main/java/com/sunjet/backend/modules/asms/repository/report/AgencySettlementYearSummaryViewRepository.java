package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.AgencySettlementYearSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 合作商结算年汇总视图
 */
public interface AgencySettlementYearSummaryViewRepository extends JpaRepository<AgencySettlementYearSummaryView, String>, JpaSpecificationExecutor<AgencySettlementYearSummaryView> {

}
