package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.ReportPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 速报配件子行
 * Created by Administrator on 2016/10/26.
 */
public interface ReportPartRepository extends JpaRepository<ReportPartEntity, String>, JpaSpecificationExecutor<ReportPartEntity> {
    ////@Query("select u from QrVehicleEntity u where u.vehicle.vin like ?1 ")
    //@Query("select r from ReportPartEntity r  where r.part.code like ?1")
    //List<ReportPartEntity> filter(String keyword);

    @Query("select rp from ReportPartEntity rp where rp.qrId=?1")
    List<ReportPartEntity> findByQrId(String objId);


    @Query("select rp from ReportPartEntity rp where rp.crId=?1")
    List<ReportPartEntity> findByCrId(String objId);

    @Modifying
    @Query("delete from ReportPartEntity rp where rp.crId=?1")
    void deleteByExpenseReportObjId(String objId);

    @Modifying
    @Query("delete from ReportPartEntity rp where rp.qrId=?1")
    void deleteByQualityReportObjId(String objId);
}
