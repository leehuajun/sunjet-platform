package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.WorkHoursReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 工时视图
 */
public interface WorkHoursReportViewRepository extends JpaRepository<WorkHoursReportView, String>, JpaSpecificationExecutor<WorkHoursReportView> {

}
