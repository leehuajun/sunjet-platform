package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.QualityExpenseReportDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 质量费用速报明细视图
 */
public interface QualityExpenseReportDetailViewRepository extends JpaRepository<QualityExpenseReportDetailView, String>, JpaSpecificationExecutor<QualityExpenseReportDetailView> {

}
