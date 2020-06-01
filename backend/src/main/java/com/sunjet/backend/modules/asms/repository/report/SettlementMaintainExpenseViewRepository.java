package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.SettlementMaintainExpenseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 服务站结算明细
 */
public interface SettlementMaintainExpenseViewRepository extends JpaRepository<SettlementMaintainExpenseView, String>, JpaSpecificationExecutor<SettlementMaintainExpenseView> {

}
