package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by lhj on 2014-07-27 10:05
 * Description:
 */
public interface RoleRepository extends JpaRepository<RoleEntity, String>, JpaSpecificationExecutor<RoleEntity> {


    @Query("select r from RoleEntity r where r.objId in (select distinct ur.roleId from UserWithRoleEntity ur where ur.userId = ?1)")
    public List<RoleEntity> findAllByUserId(String userId);

    @Query("select r from RoleEntity r where r.name =?1")
    RoleEntity findOneByRoleName(String roleName);

    @Query("select r from RoleEntity r where r.roleId =?1")
    RoleEntity findOneByRoleId(String roleId);
}
