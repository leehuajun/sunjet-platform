package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.RoleWithPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfb on 2017-08-04
 * 角色 与 权限 的关联关系
 */
public interface RoleWithPermissionRepository extends JpaRepository<RoleWithPermissionEntity, String>, JpaSpecificationExecutor<RoleWithPermissionEntity> {


    @Modifying
    @Query("delete from RoleWithPermissionEntity rp where rp.permissionId in(?1)")
    public void deleteRoleWithPermission(List<String> permissionIds);

    @Modifying
    @Query("delete from RoleWithPermissionEntity rp where rp.roleId = ?1")
    public void deleteRoleWithPermissionByRoleId(String roleId);

    @Query("select rp from RoleWithPermissionEntity rp where rp.roleId in(?1)")
    public List<RoleWithPermissionEntity> findAllByRoleIds(List<String> roleIds);
}
