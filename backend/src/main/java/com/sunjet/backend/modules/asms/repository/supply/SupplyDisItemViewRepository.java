package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.view.SupplyDisItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/10/26.
 * 二次分配
 */
public interface SupplyDisItemViewRepository extends JpaRepository<SupplyDisItemView, String>, JpaSpecificationExecutor<SupplyDisItemView> {
}
