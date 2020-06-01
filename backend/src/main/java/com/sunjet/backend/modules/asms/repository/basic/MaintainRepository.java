package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.MaintainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 维修项目 主实体
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface MaintainRepository extends JpaRepository<MaintainEntity, String>, JpaSpecificationExecutor<MaintainEntity> {
    @Query("select me from MaintainEntity me where me.code like ?1 and me.name like ?2 and me.vehicleModelName like ?3 and me.vehicleSystemName like ?4 and me.vehicleSubSystemName like ?5 order by me.code asc")
    List<MaintainEntity> findAllByFilter(String code, String name, String vehicleModelName, String vehicleSystemName, String vehicleSubSystemName);
}
