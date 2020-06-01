package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.AgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by lhj on 16/9/17.
 */
public interface AgencyRepository extends JpaRepository<AgencyEntity, String>, JpaSpecificationExecutor<AgencyEntity> {
    @Query("select ae from AgencyEntity ae where (ae.code like ?1 or ae.name like ?1)and ae.enabled=true")
    List<AgencyEntity> findAllByKeyword(String keyword);
    //
    //@Query("select ae from AgencyEntity ae where ae.code like ?1 or ae.name=?1")
    //AgencyEntity findOneByName(String name);

    @Query("select ae from AgencyEntity ae where ae.code=?1")
    AgencyEntity findOneByCode(String code);

    /**
     * 根据省id查合作商
     *
     * @param provinceId
     * @return
     */
    @Query(value = "SELECT a.* FROM basic_agencies a, basic_agencies_region b WHERE a.obj_id = b.agency_id AND b.province_id = ?1", nativeQuery = true)
    List<AgencyEntity> findAllAgencyByProvinceId(String provinceId);


    @Query("select ae from AgencyEntity ae where ae.enabled=1 order by ae.createdTime desc")
    List<AgencyEntity> findEnabled();
}
