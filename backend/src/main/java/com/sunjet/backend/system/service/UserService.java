package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.admin.UserItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * @author: lhj
 * @create: 2017-07-02 00:51
 * @description: 说明
 */
public interface UserService {

    UserInfo findOneByLogId(String logId);

    List<UserInfo> findAll();

    UserInfo findOne(String objId);

    UserInfo save(UserInfo info);

    boolean delete(String objId);

    boolean delete(UserInfo info);


    PageResult<UserItem> getPageList(PageParam<UserItem> pageParam);

    List<UserInfo> findAllByRoleId(String roleId);

    /**
     * 绑定 用户 与 角色 的关联关系
     *
     * @param info
     * @return
     */
    public boolean addUsersToRole(UserInfo info);

    /**
     * 删除用户与角色的关联关系
     *
     * @param userId
     * @return
     */
    public boolean removeUsersFromRole(String userId);

    UserInfo changePassword(UserInfo userInfo);

    String findOneWithUserId(String userId);

    List<UserInfo> findAllByRoleName(String roleName);

    UserInfo findOneByEnabledLogId(String logId);


}
