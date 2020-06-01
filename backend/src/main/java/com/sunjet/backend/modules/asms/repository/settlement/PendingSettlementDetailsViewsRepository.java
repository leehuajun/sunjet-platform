package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.view.PendingSettlementDetailsViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 待结算清单(配件／服务／运费)
 * Created by zyh on 16/11/14.
 */
public interface PendingSettlementDetailsViewsRepository extends JpaRepository<PendingSettlementDetailsViews, String>, JpaSpecificationExecutor<PendingSettlementDetailsViews> {
}
