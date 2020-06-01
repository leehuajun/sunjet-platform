package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.VehicleModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 配件
 * Created by zyh on 2016/10/24.
 */
public interface VehicleModelRepository extends JpaRepository<VehicleModelEntity, String>, JpaSpecificationExecutor<VehicleModelEntity> {


}
