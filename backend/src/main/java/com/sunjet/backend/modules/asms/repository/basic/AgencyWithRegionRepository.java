package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.AgencyWithRegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by lhj on 16/9/17.
 * 合作商对应省份
 */
public interface AgencyWithRegionRepository extends JpaRepository<AgencyWithRegionEntity, String>, JpaSpecificationExecutor<AgencyWithRegionEntity> {

    /**
     * 删除合作商与省份的关系
     *
     * @param provinceId
     * @return
     */
    @Modifying
    @Query("delete from AgencyWithRegionEntity awr where awr.agencyId=?1 and awr.provinceId=?2")
    void deleteByAgencyWithProvinces(String agencyId, String provinceId);

    @Query("select awr from AgencyWithRegionEntity awr where awr.agencyId=?1 and awr.provinceId=?2")
    AgencyWithRegionEntity findOneByAgencyIdAAndProvinceId(String agencyId, String provinceId);

    @Modifying
    @Query("delete from AgencyWithRegionEntity awr where awr.agencyId=?1")
    void deleteByAgencyId(String agencyId);
}
