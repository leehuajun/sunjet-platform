package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.AgencySupplySummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 合作商供货汇总视图
 */
public interface AgencySupplySummaryViewRepository extends JpaRepository<AgencySupplySummaryView, String>, JpaSpecificationExecutor<AgencySupplySummaryView> {

}
