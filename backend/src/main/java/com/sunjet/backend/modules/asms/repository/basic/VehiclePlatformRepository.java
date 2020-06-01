package com.sunjet.backend.modules.asms.repository.basic;

import com.sunjet.backend.modules.asms.entity.basic.VehiclePlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: lhj
 * @create: 2017-10-18 14:07
 * @description: 说明
 */
public interface VehiclePlatformRepository extends JpaRepository<VehiclePlatformEntity, String>, JpaSpecificationExecutor<VehiclePlatformEntity> {
}
