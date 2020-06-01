package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.view.RoleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

/**
 * @author: wfb
 * @create: 2018-08-04
 * @description: 说明
 */
public interface RoleViewRepository extends JpaRepository<RoleView, String>, JpaSpecificationExecutor<RoleView> {

    @Query(value = "select a.user_id,b.name from sys_user_role a LEFT JOIN sys_roles b ON a.role_id = b.obj_id and a.user_id in(?1) ORDER BY user_id ASC", nativeQuery = true)
    ArrayList<Object> findRolesByUserIds(ArrayList<String> userIds);
}
