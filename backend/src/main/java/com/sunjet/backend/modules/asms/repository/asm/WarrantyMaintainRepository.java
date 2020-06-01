package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintainEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 维修项目
 * Created by lhj on 16/9/17.
 */
public interface WarrantyMaintainRepository extends JpaRepository<WarrantyMaintainEntity, String>, JpaSpecificationExecutor<WarrantyMaintenanceEntity> {

    /**
     * 通过三包单objid查找维修项目
     *
     * @param objId
     * @return
     */
    @Query("select wm from WarrantyMaintainEntity wm where wm.warrantyMaintenance=?1")
    List<WarrantyMaintainEntity> findAllByWarrantyMaintenanceObjId(String objId);

    /**
     * 通过三包单objId删除维修项目实体
     *
     * @param objId
     */
    @Modifying
    @Query("delete from WarrantyMaintainEntity vm where vm.warrantyMaintenance=?1")
    void deleteByWarrantyMaintenanceObjId(String objId);
}
