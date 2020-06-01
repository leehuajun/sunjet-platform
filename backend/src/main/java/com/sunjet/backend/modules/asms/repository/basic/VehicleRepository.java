package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface VehicleRepository extends JpaRepository<VehicleEntity, String>, JpaSpecificationExecutor<VehicleEntity> {

    @Query("select v from VehicleEntity v  where (v.vin like ?1  or v.vsn like ?1) and v.enabled = true")
    List<VehicleEntity> findAllByKeyword(String keyword);

    @Query("select v from VehicleEntity v  where v.vin=?1 and v.enabled = true")
    VehicleEntity findOneByVin(String vin);

    @Query("select v from VehicleEntity v  where (v.vin like ?1  or v.vsn like ?1) and v.fmDate IS NULL and v.enabled = true")
    List<VehicleEntity> findAllByKeywordAndFmDateIsNull(String objId);

    @Query("select v from VehicleEntity v where v.objId in (?1) and v.enabled = true")
    List<VehicleEntity> findByVehicleId(ArrayList<String> objIds);

    @Query("select v from VehicleEntity v where v.vin in (?1) and v.enabled = true")
    List<VehicleEntity> findAllByVinIn(List<String> vins);
}
