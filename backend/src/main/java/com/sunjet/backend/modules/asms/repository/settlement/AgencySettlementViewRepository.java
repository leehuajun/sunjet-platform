package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.view.AgencySettlementView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 费用结算单合作库
 * Created by Administrator on 2016/10/26.
 */
public interface AgencySettlementViewRepository extends JpaRepository<AgencySettlementView, String>, JpaSpecificationExecutor<AgencySettlementView> {

}
