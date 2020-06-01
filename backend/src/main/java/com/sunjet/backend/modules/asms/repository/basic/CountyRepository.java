package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.CountyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 县城/城区（Region） dao
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface CountyRepository extends JpaRepository<CountyEntity, String>, JpaSpecificationExecutor<CountyEntity> {

    @Query("Select c from CountyEntity c where c.cityId=?1")
    List<CountyEntity> findAllByCityId(String cityId);
}
