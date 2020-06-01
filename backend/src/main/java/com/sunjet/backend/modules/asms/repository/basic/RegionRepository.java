package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.ProvinceEntity;
import com.sunjet.backend.modules.asms.entity.basic.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 区域（Region） dao
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface RegionRepository extends JpaRepository<RegionEntity, String>, JpaSpecificationExecutor<RegionEntity> {


    /**
     * 根据合作商id查覆盖省份
     *
     * @param agencyId
     * @return
     */
    @Query(value = "SELECT a.* FROM basic_regions a, basic_agencies_region b WHERE a.obj_id = b.province_id AND b.agency_id= ?1", nativeQuery = true)
    List<ProvinceEntity> findAllProvincesByAgencyId(String agencyId);

}
