package com.sunjet.backend.modules.asms.repository.supply;

import com.sunjet.backend.modules.asms.entity.supply.view.SupplyAllocationView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface SupplyAllocationViewRepository extends JpaRepository<SupplyAllocationView, String>, JpaSpecificationExecutor<SupplyAllocationView> {

    @Query("select p from SupplyAllocationView p where p.objId in (?1)")
    public List<SupplyAllocationView> getSupplyAllocationByIds(ArrayList<String> ids);
}
