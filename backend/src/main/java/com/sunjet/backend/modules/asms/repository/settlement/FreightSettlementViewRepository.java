package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.view.FreightSettlementView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 运输费用结算单
 * Created by Administrator on 2016/10/26.
 */
public interface FreightSettlementViewRepository extends JpaRepository<FreightSettlementView, String>, JpaSpecificationExecutor<FreightSettlementView> {

}
