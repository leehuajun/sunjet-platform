package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.GoOutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务委托单出外子行
 * Created by lhj on 16/9/17.
 */
public interface GoOutRepository extends JpaRepository<GoOutEntity, String>, JpaSpecificationExecutor<GoOutEntity> {


    /**
     * 通过三包单objid查找配件列表
     *
     * @param objId
     * @return
     */
    @Query("select go from GoOutEntity go where go.warrantyMaintenance=?1")
    List<GoOutEntity> findAllByWarrantyMaintenanceObjId(String objId);

    /**
     * 通过首保服务单objid查找外出列表
     *
     * @param objId
     * @return
     */
    @Query("select go from GoOutEntity go where go.firstMaintenanceId=?1")
    List<GoOutEntity> findAllByFirstMaintenanceId(String objId);

    /**
     * 通过首保单Id 删除外出信息
     *
     * @param firstMaintenanceId
     */
    @Modifying
    @Query("delete from GoOutEntity go where go.firstMaintenanceId=?1")
    void deleteByFirstMaintenance(String firstMaintenanceId);

    /**
     * 通过活动服务单objId删除外出实体
     *
     * @param activityMaintenanceId
     */
    @Modifying
    @Query("delete from GoOutEntity go where go.activityMaintenanceId=?1")
    void deleteByActivityMaintenanceId(String activityMaintenanceId);

    /**
     * 通过三包单objId删除外出实体
     *
     * @param warrantyMaintenanceId
     */
    @Modifying
    @Query("delete from GoOutEntity go where go.warrantyMaintenance=?1")
    void deleteByWarrantyMaintenanceId(String warrantyMaintenanceId);

    @Query("select go from GoOutEntity go where go.activityMaintenanceId=?1")
    List<GoOutEntity> findAllByActivityMaintenanceObjId(String objId);
}
