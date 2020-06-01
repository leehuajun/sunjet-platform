package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.ActivitiNoticeReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 活动通知单报表
 * Created by SUNJET_WS on 2017/10/13.
 */
public interface ActivitiNoticeReportViewRepository extends JpaRepository<ActivitiNoticeReportView, String>, JpaSpecificationExecutor<ActivitiNoticeReportView> {

}
