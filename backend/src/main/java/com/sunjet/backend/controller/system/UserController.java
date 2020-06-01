package com.sunjet.backend.controller.system;

//import com.sunjet.backend.system.entity.UserEntity;

import com.sunjet.backend.modules.asms.service.basic.AgencyService;
import com.sunjet.backend.modules.asms.service.basic.DealerService;
import com.sunjet.backend.system.service.MenuService;
import com.sunjet.backend.system.service.RoleService;
import com.sunjet.backend.system.service.UserService;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.admin.UserItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lhj
 * @create: 2017-07-03 17:08
 * @description: 说明
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private DealerService dealerService;//服务站

    @Autowired
    private AgencyService agencyService;//合作商

    @ApiOperation(value = "获取用户列表", notes = "")
    @GetMapping("/users")
    public List<UserInfo> getUserList() {
        log.info("客户访问getUserList()");
        return userService.findAll();
    }

    @ApiOperation(value = "查询单个用户", notes = "根据用户登录ID获取用户信息")
    @ApiImplicitParam(name = "logId", value = "用户登录ID", required = true, dataType = "String")
    @GetMapping("/{logId}")
    public UserInfo getUserByLogId(@PathVariable("logId") String logId) {
        log.info("客户访问getUserByLogId()");

        //获取用户
        UserInfo userInfo = userService.findOneByEnabledLogId(logId);
        if (userInfo != null) {
            //用户对应的角色
            List<RoleInfo> roleInfoList = roleService.findAllByUserId(userInfo.getObjId());
            userInfo.setRoles(roleInfoList);

            //绑定用户菜单
            List<MenuInfo> infolist = menuService.findAllByRoles(userInfo.getRoles());
            userInfo.setMenuInfoList(infolist);

            //绑定服务站
            if (StringUtils.isNotBlank(userInfo.getDealerId())) {
                DealerInfo dealerInfo = dealerService.findOneById(userInfo.getDealerId());
                userInfo.setDealer(dealerInfo);
            }

            //绑定合作商
            if (StringUtils.isNotBlank(userInfo.getAgencyId())) {
                AgencyInfo agencyInfo = agencyService.findOne(userInfo.getAgencyId());
                userInfo.setAgency(agencyInfo);
            }
        }

        return userInfo;
    }

    /**
     * 获取所有用户
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户")
    @PostMapping("/findAll")
    public List<UserInfo> findAll() {
        return userService.findAll();
    }

    @PostMapping("/tosave")
    public UserInfo tosave(@RequestBody UserInfo info) {

        return userService.save(info);
    }

    @PutMapping("/toupdata")
    public UserInfo toupdata(@RequestBody UserInfo info) {
        UserInfo updataEntity = userService.findOne(info.getObjId());
        updataEntity.setName(info.getName());
        updataEntity.setLogId(info.getLogId());
        updataEntity.setPassword(info.getPassword());
        updataEntity.setEnabled(info.getEnabled());
        updataEntity.setSalt(info.getSalt());
        updataEntity.setPhone(info.getPhone());
        updataEntity.setUserType(info.getUserType());
        return userService.save(updataEntity);
    }

    @DeleteMapping("/todelete")
    public boolean todelete(@RequestBody UserInfo info) {
        UserInfo todelete = userService.findOne(info.getObjId());
        return userService.delete(todelete);
    }


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<UserItem> getPageList(@RequestBody PageParam<UserItem> pageParam) {
        return userService.getPageList(pageParam);
    }

    @PostMapping("/findOne")
    public UserInfo findOne(@RequestBody String objId) {
        UserInfo userInfo = userService.findOne(objId);
        if (userInfo != null) {
            //绑定用户关联的角色
            List<RoleInfo> roles = roleService.findAllByUserId(objId);
            userInfo.setRoles(roles);

            //绑定服务站
            if (StringUtils.isNotBlank(userInfo.getDealerId())) {
                DealerInfo dealerInfo = dealerService.findOneById(userInfo.getDealerId());
                userInfo.setDealer(dealerInfo);
            }
            //绑定合作商
            if (StringUtils.isNotBlank(userInfo.getAgencyId())) {
                AgencyInfo agencyInfo = agencyService.findOne(userInfo.getAgencyId());
                userInfo.setAgency(agencyInfo);
            }
        }
        return userInfo;
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return userService.delete(objId);
    }


    /**
     * 修改密码
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/changePassWord")
    public UserInfo changePassword(@RequestBody UserInfo userInfo) {
        return userService.changePassword(userInfo);
    }


    /**
     * 通过userid 获取用户名
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "通过userid 获取用户名")
    @PostMapping("findOneWithUserId")
    public String findOneWithUserId(@RequestBody String userId) {
        return userService.findOneWithUserId(userId);
    }

    /**
     * 通过角色名获取所有用户
     *
     * @param roleName
     * @return
     */
    @PostMapping("findAllByRoleName")
    public List<UserInfo> findAllByRoleName(@RequestBody String roleName) {
        return userService.findAllByRoleName(roleName);
    }


    @PostMapping("findOneByLogId")
    public UserInfo findOneByLogId(@RequestBody String LogId) {
        return userService.findOneByLogId(LogId);
    }

}
