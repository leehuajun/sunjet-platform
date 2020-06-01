package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 三包维修单
 * Created by lhj on 16/9/17.
 */
public interface WarrantyMaintenanceRepository extends JpaRepository<WarrantyMaintenanceEntity, String>, JpaSpecificationExecutor<WarrantyMaintenanceEntity> {
    //    @Query("select wm from WarrantyMaintenanceEntity wm left join fetch wm.warrantyMaintains left join fetch wm.commissionParts left join fetch wm.goOuts where wm.objId=?1")
//    WarrantyMaintenanceEntity findOneWithOthersById(String objId);
//
//    @Query("select wm from WarrantyMaintenanceEntity wm left join fetch wm.warrantyMaintains left join fetch wm.commissionParts left join fetch wm.goOuts where wm.vehicle.objId=?1 order by wm.requestDate desc")
//    WarrantyMaintenanceEntity findOneWithVehicleById(String objId);
//
    @Query("select wm from WarrantyMaintenanceEntity wm where wm.processInstanceId=?1")
    WarrantyMaintenanceEntity findOneByProcessInstanceId(String processInstanceId);
//
//    @Query("select wm from WarrantyMaintenanceEntity wm where  wm.status=?1")
//    List<WarrantyMaintenanceEntity> findAllbySettlement(int status);
//
//    @Query("select wm from WarrantyMaintenanceEntity wm where wm.status=?1")
//    List<WarrantyMaintenanceEntity> findAudited(Integer status);
//
//    //    @Query("select wm from WarrantyMaintenanceEntity wm left join fetch wm.maintainItems left join fetch wm.commissionParts left join fetch wm.goOutEntities where wm.vin=?1")
////    WarrantyMaintenanceEntity findVin(String vin);


    @Query("select wm from WarrantyMaintenanceEntity wm where wm.docNo=?1")
    WarrantyMaintenanceEntity findOneWithOthersBySrcDocNo(String srcDocNo);

    /**
     * 通过车辆id查三包服务单
     *
     * @param objId
     * @return
     */
    @Query("select wm from WarrantyMaintenanceEntity wm where wm.vehicleId =?1")
    List<WarrantyMaintenanceEntity> findAllByVehicleObjId(String objId);

    @Query("select wm from WarrantyMaintenanceEntity wm where wm.vehicleId in(?1)")
    List<WarrantyMaintenanceEntity> findAllByVehicleObjIds(List<String> vehicleObjIds);

    @Query("select wm from WarrantyMaintenanceEntity wm where wm.vehicleId =?1 and wm.status > 0 order by wm.createdTime desc ")
    List<WarrantyMaintenanceEntity> findAllByVehicleId(String vehicleId);

    @Query("select wm from WarrantyMaintenanceEntity wm where  wm.status=?1")
    List<WarrantyMaintenanceEntity> findAllbySettlement(int index);

    @Query("select wm from WarrantyMaintenanceEntity wm where wm.status=?1")
    List<WarrantyMaintenanceEntity> findAudited(int index);

    @Query("select wm from WarrantyMaintenanceEntity wm where wm.docNo=?1")
    WarrantyMaintenanceEntity findOneByDocNo(String docNo);
}
