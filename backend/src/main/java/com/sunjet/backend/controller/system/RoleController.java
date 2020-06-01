package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.service.PermissionService;
import com.sunjet.backend.system.service.RoleService;
import com.sunjet.backend.system.service.UserService;
import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.RoleItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 * Created by zyf on 2017/7/25.
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @GetMapping("/findAll")
    public List<RoleInfo> findAll() {
        return roleService.findAll();
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<RoleItem> getPageList(@RequestBody PageParam<RoleItem> pageParam) {
        return roleService.getPageList(pageParam);
    }

    @PostMapping("/findOneById")
    public RoleInfo findOneById(@RequestBody String ObjId) {
        return roleService.findOneById(ObjId);
    }


    /**
     * 通过id获取角色对应的用户权限
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneWithUsersAndPermissionsById")
    public RoleInfo findOneWithUsersAndPermissionsById(@RequestBody String objId) {

        RoleInfo roleInfo = roleService.findOne(objId);

        List<UserInfo> userInfoList = userService.findAllByRoleId(objId);
        List<PermissionInfo> permissionInfoList = permissionService.findAllByRoleId(objId);

        roleInfo.setUserInfos(userInfoList);
        roleInfo.setPermissions(permissionInfoList);

        return roleInfo;
    }

    /**
     * 删除角色与用户的关联关系
     *
     * @param objId
     * @return
     */
    @PutMapping("/removeUsersFromRole")
    public boolean removeUsersFromRole(@RequestBody String objId) {
        return roleService.removeUsersFromRole(objId);
    }

    /**
     * 添加角色与用户的关联关系
     *
     * @return
     */
    @PostMapping("/addUsersToRole")
    public boolean addUsersToRole(@RequestBody RoleInfo roleInfo) {
        return roleService.addUsersToRole(roleInfo);
    }

    @PostMapping("/save")
    public RoleInfo save(@RequestBody RoleInfo roleInfo) {
        return roleService.save(roleInfo);
    }

    /**
     * 删除角色 与 权限 的关联关系
     *
     * @param roleId
     */
    @PostMapping("/deleteUserWithPermission")
    public Boolean deleteUserWithPermission(@RequestBody String roleId) {
        return roleService.deleteUserWithPermission(roleId);
    }

    /**
     * 保存角色 与 权限 的关联关系
     *
     * @param roleInfo
     */
    @PostMapping("/addUsersWithPermission")
    public Boolean addUsersWithPermission(@RequestBody RoleInfo roleInfo) {
        return roleService.addUsersWithPermission(roleInfo);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return roleService.delete(objId);
    }
}
