package com.sunjet.backend.modules.asms.repository.supply;

import com.sunjet.backend.modules.asms.entity.supply.view.SupplyWaitingItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 待发货清单
 */
public interface SupplyWaitingItemViewRepository extends JpaRepository<SupplyWaitingItemView, String>, JpaSpecificationExecutor<SupplyWaitingItemView> {
}
