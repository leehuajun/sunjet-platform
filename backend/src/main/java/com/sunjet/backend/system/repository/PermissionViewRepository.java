package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.view.PermissionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wfb
 * @create 2018-08-04
 */
public interface PermissionViewRepository extends JpaRepository<PermissionView, String>, JpaSpecificationExecutor<PermissionView> {


    @Query(value = "select a.role_id,b.resource_name,b.access_name from sys_role_permission a LEFT JOIN sys_permissions b ON a.permission_id = b.obj_id and a.role_id in(?1) ORDER BY a.role_id,b.resource_name ASC", nativeQuery = true)
    ArrayList<Object> findPermissionsByRoleIds(ArrayList<String> roleIds);

}
