package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface PermissionRepository extends JpaRepository<PermissionEntity, String>, JpaSpecificationExecutor<PermissionEntity> {

    @Query("select p from PermissionEntity p where p.permissionCode like concat(?1,':%')")
    public List<PermissionEntity> getPermissionsByResourceCode(String resourceCode);

    @Modifying
    @Query("delete from PermissionEntity pe where pe.permissionCode like ?1")
    public void deleteAllByCode(String code);

    @Query(value = "select p from PermissionEntity p,RoleWithPermissionEntity s where s.permissionId = p.objId and s.roleId = ?1")
    public List<PermissionEntity> findAllByRoleId(String roleId);

    @Query(value = "select p from PermissionEntity p,RoleWithPermissionEntity s where s.permissionId = p.objId and s.roleId in(?1)")
    public List<PermissionEntity> findAllByRoleIds(ArrayList<String> roleIds);

}
