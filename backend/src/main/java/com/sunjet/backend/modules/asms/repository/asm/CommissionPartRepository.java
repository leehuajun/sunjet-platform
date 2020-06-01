package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.CommissionPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务委托单配件子行
 * Created by lhj on 16/9/17.
 */
public interface CommissionPartRepository extends JpaRepository<CommissionPartEntity, String>, JpaSpecificationExecutor<CommissionPartEntity> {

    /**
     * 通过三包单objid查找配件列表
     *
     * @param objId
     * @return
     */
    @Query("select cp from CommissionPartEntity cp where cp.warrantyMaintenance=?1")
    List<CommissionPartEntity> findAllByWarrantyMaintenanceObjId(String objId);

    /**
     * 通过三包单objId删除配件实体
     *
     * @param objId
     */
    @Modifying
    @Query("delete from CommissionPartEntity cp where cp.warrantyMaintenance=?1")
    void deleteByWarrantyMaintenanceObjId(String objId);


    @Modifying
    @Query("delete from CommissionPartEntity cp where cp.activityMaintenanceId=?1")
    void deleteByActivityMaintenanceId(String activityMaintenanceId);

    /**
     * 通过活动服务单单objid查找配件列表
     *
     * @param objId
     * @return
     */
    @Query("select cp from CommissionPartEntity cp where cp.activityMaintenanceId=?1")
    List<CommissionPartEntity> findAllByActivityMaintenanceObjId(String objId);
}
