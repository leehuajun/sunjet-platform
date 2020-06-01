package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.ReportVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 车辆子行
 * Created by Administrator on 2016/10/26.
 */
public interface ReportVehicleRepository extends JpaRepository<ReportVehicleEntity, String>, JpaSpecificationExecutor<ReportVehicleEntity> {
    //    @Query("select u from ReportVehicleEntity u left join fetch u.vehicle where u.vin=?1 ")
    //@Query("select u from ReportVehicleEntity u where u.vehicle.vin like ?1 ")
    //List<ReportVehicleEntity> fitlter(String keyword);

    @Query("select rv from ReportVehicleEntity rv where rv.qrId=?1")
    List<ReportVehicleEntity> findByQrId(String objId);

    @Query("select rv from ReportVehicleEntity rv where rv.crId=?1")
    List<ReportVehicleEntity> findByCrId(String objId);

    @Modifying
    @Query("delete from ReportVehicleEntity rv where rv.crId=?1")
    void deleteByExpenseReprotObId(String objId);

    @Modifying
    @Query("delete from ReportVehicleEntity rv where rv.qrId=?1")
    void deleteByQualityReportObjId(String objId);


}
