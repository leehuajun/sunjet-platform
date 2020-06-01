package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.PermissionInfo;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 权限
 */
public interface PermissionService {

    boolean deleteAllByResourceCode(String code);

    PermissionInfo save(PermissionInfo permissionInfo);

    boolean delete(PermissionInfo permissionInfo);

    boolean delete(String objId);

    PermissionInfo findOne(String objId);

    List<PermissionInfo> findAll();

    List<PermissionInfo> findAllByRoleId(String roleId);

}
