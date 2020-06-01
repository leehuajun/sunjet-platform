package com.sunjet.backend.system.repository;

import com.sunjet.backend.system.entity.ResourceWithOperationEntity;
import com.sunjet.backend.system.entity.UserWithRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by wfb on 17-8-4.
 * 用户 与 角色 的关联关系
 */
public interface UserWithRoleRepository extends JpaRepository<UserWithRoleEntity, String>, JpaSpecificationExecutor<UserWithRoleEntity> {

    @Modifying
    @Query("delete from UserWithRoleEntity r where r.userId = ?1")
    public void deleteByUser(String userId);

    @Modifying
    @Query("delete from UserWithRoleEntity r where r.roleId = ?1")
    public void deleteByRole(String roleId);
}
