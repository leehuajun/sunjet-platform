package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.FirstMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 首保 dao
 * Created by lhj on 16/9/17.
 */
public interface FirstMaintenanceRepository extends JpaRepository<FirstMaintenanceEntity, String>, JpaSpecificationExecutor<FirstMaintenanceEntity> {

    @Query("SELECT fm from  FirstMaintenanceEntity fm where fm.vehicleId in (?1)")
    List<FirstMaintenanceEntity> findAllByVehicleObjIds(List<String> vehicleObjIds);

    @Query("SELECT fm from  FirstMaintenanceEntity fm where fm.vehicleId = ?1 order by fm.createdTime desc ")
    FirstMaintenanceEntity findOneByVehicleId(String vehicleId);

    //@Query("select fm from FirstMaintenanceEntity fm left join fetch fm.goOuts where fm.objId=?1")
    //FirstMaintenanceEntity findOneWithGoOutsById(String objId);
    //
    @Query("select fm from FirstMaintenanceEntity fm where  fm.status=?1")
    List<FirstMaintenanceEntity> findAllbySettlement(int status);

//    @Query("select fm from FirstMaintenanceEntity fm where fm.vin=?1")
//    FirstMaintenanceEntity findVin(String vin);
}

