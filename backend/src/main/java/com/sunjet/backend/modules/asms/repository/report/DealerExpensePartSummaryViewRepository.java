package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.DealerExpensePartSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 服务站配件费用年度汇总视图
 */
public interface DealerExpensePartSummaryViewRepository extends JpaRepository<DealerExpensePartSummaryView, String>, JpaSpecificationExecutor<DealerExpensePartSummaryView> {

}
