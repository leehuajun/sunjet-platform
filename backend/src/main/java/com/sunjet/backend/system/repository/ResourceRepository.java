package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * ResourceRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface ResourceRepository extends JpaRepository<ResourceEntity, String>, JpaSpecificationExecutor<ResourceEntity> {

    //@Query("select rm from ResourceEntity rm left join fetch rm.operationEntityList where rm.objId=?1")
    //ResourceEntity findOneWithOperationsById(String objId);

    //@Query("select rm from ResourceEntity rm left join fetch rm.operationEntityList")
    //List<ResourceEntity> findAllWithOperations();
}
