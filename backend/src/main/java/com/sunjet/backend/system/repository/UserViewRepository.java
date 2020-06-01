package com.sunjet.backend.system.repository;

import com.sunjet.backend.system.entity.view.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

/**
 * @author: wfb
 * @create: 2018-08-04
 * @description: 说明
 */
public interface UserViewRepository extends JpaRepository<UserView, String>, JpaSpecificationExecutor<UserView> {

    @Query(value = "select a.role_id,b.name from sys_user_role a LEFT JOIN sys_users b ON a.user_id = b.obj_id and a.role_id in(?1) ORDER BY role_id ASC", nativeQuery = true)
    ArrayList<Object> findUsersByRoleIds(ArrayList<String> roleIds);

}
