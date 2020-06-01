package com.sunjet.backend.system.repository;

import com.sunjet.backend.system.entity.view.OperationView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wfb
 * @create: 2018-08-04
 * @description: 说明
 */
public interface OperationViewRepository extends JpaRepository<OperationView, String>, JpaSpecificationExecutor<OperationView> {

    @Query(value = "select a.res_id,b.opt_name from sys_resource_operation a LEFT JOIN sys_operations b ON a.opt_id = b.obj_id and a.res_id in(?1) ORDER BY res_id ASC", nativeQuery = true)
    ArrayList<Object> findOperationsByRoleIds(ArrayList<String> roleIds);
}
