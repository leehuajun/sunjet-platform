package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.view.DealerSettlementView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 三包费用结算单
 * Created by wfb on 2017/8/18.
 */
public interface DealerSettlementViewRepository extends JpaRepository<DealerSettlementView, String>, JpaSpecificationExecutor<DealerSettlementView> {

}
