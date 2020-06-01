package com.sunjet.backend.system.repository;

import com.sunjet.backend.system.entity.ResourceWithOperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by wfb on 17-8-4.
 * 资源 与 操作 关联关系
 */
public interface ResourceWithOperationRepository extends JpaRepository<ResourceWithOperationEntity, String>, JpaSpecificationExecutor<ResourceWithOperationEntity> {

    @Modifying
    @Query("delete from ResourceWithOperationEntity r where r.resId = ?1")
    public void deleteByResource(String resourceId);

    @Modifying
    @Query("delete from ResourceWithOperationEntity r where r.optId = ?1")
    public void deleteByOperation(String operationId);
}
