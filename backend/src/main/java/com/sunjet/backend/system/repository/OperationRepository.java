package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface OperationRepository extends JpaRepository<OperationEntity, String>, JpaSpecificationExecutor<OperationEntity> {

    @Query(value = "select o from OperationEntity o where o.objId in(select distinct s.optId from ResourceWithOperationEntity s where s.resId = ?1)")
    public List<OperationEntity> findAllByResourceId(String resourceId);
}
