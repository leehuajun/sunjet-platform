package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.view.SupplyView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 调拨供货单
 * Created by wfb on 17/8/18.
 */
public interface SupplyViewRepository extends JpaRepository<SupplyView, String>, JpaSpecificationExecutor<SupplyView> {

}
