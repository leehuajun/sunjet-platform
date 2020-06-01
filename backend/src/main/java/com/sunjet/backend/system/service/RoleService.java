package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.RoleEntity;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.RoleItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * 角色管理
 * Created by lhj on 16/6/17.
 */
public interface RoleService {
    List<RoleInfo> findAll();

    RoleInfo save(RoleInfo roleInfo);

    boolean delete(String objId);

    boolean delete(RoleInfo info);

    PageResult<RoleItem> getPageList(PageParam<RoleItem> pageParam);

    RoleInfo findOneById(String objId);

    RoleInfo findOne(String roleId);

    boolean removeUsersFromRole(String roleId);

    boolean addUsersToRole(RoleInfo roleInfo);

    List<RoleInfo> findAllByUserId(String userId);

    /**
     * 删除角色 与 权限 的关联关系
     *
     * @param roleId
     */
    public Boolean deleteUserWithPermission(String roleId);

    /**
     * 保存角色 与 权限 的关联关系
     *
     * @param roleInfo
     */
    public Boolean addUsersWithPermission(RoleInfo roleInfo);

    RoleEntity findOneByRoleId(String roleId);
}
