package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.RecycleSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 故障件汇总明细
 */
public interface RecycleSummaryViewRepository extends JpaRepository<RecycleSummaryView, String>, JpaSpecificationExecutor<RecycleSummaryView> {

}
