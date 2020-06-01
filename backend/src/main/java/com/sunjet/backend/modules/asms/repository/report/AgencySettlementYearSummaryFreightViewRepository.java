package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.AgencySettlementYearSummaryFreightView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 合作商结算年汇总运费视图
 */
public interface AgencySettlementYearSummaryFreightViewRepository extends JpaRepository<AgencySettlementYearSummaryFreightView, String>, JpaSpecificationExecutor<AgencySettlementYearSummaryFreightView> {

}
