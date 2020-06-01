package com.sunjet.backend.modules.asms.repository.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 活动服务维修 dao
 * Created by lhj on 16/9/17.
 */
public interface ActivityMaintenanceRepository extends JpaRepository<ActivityMaintenanceEntity, String>, JpaSpecificationExecutor<ActivityMaintenanceEntity> {
    @Query("select am from ActivityMaintenanceEntity am where am.activityVehicleId in(?1)")
    List<ActivityMaintenanceEntity> findAllByActivityVehicleids(List<String> activityVehicleids);

    @Query("select am from ActivityMaintenanceEntity am where am.activityVehicleId in (?1) and am.status > 0 ")
    List<ActivityMaintenanceEntity> findAllByVehicleId(List<String> vehicleId);

    @Query("select an from ActivityMaintenanceEntity an where  an.status=?1")
    List<ActivityMaintenanceEntity> findAllbySettlement(int index);

    @Query("select an from ActivityMaintenanceEntity an where  an.activityVehicleId=?1")
    List<ActivityMaintenanceEntity> findAllByActivityVehicleId(String objId);
    //@Query("select an from ActivityMaintenanceEntity an where an.objId=?1")
    //ActivityMaintenanceEntity findOneById(String objId);
    //
    //@Query("select an from ActivityMaintenanceEntity an left join fetch an.goOuts left join fetch an.commissionParts where an.objId=?1")
    //ActivityMaintenanceEntity findOneWithOthers(String objId);
    //
    //@Query("select an from ActivityMaintenanceEntity an left join fetch an.goOuts where an.activityVehicle.vehicle.objId=?1 order by an.repairDate desc")
    //ActivityMaintenanceEntity findOneWithVehicleById(String objId);
    //

}
