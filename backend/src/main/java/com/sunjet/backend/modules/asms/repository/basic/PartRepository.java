package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 配件
 * Created by zyh on 2016/10/24.
 */
public interface PartRepository extends JpaRepository<PartEntity, String>, JpaSpecificationExecutor<PartEntity> {

    @Query("select p from PartEntity p where p.enabled = true and (p.code like ?1 or p.name like ?1)")
    List<PartEntity> findAllByKeyword(String productNo);
    //
    //@Query("select p from PartEntity p where p.enabled = true and p.code like ?1")
    //List<PartEntity> findAllByCode(String code);


    @Query("select p from PartEntity p where p.enabled = true and p.objId in (?1)")
    List<PartEntity> findByPartId(List<String> objIds);

    @Query("select p from PartEntity p where p.enabled = true and p.code in (?1)")
    List<PartEntity> findAllByCodeIn(List<String> codes);

    @Query("select p from PartEntity p where p.code = (?1)")
    PartEntity findOneByCode(String code);

    @Query("select p from PartEntity p where p.code like ?1 or p.name like ?1")
    List<PartEntity> findAllByCode(String code);
}
