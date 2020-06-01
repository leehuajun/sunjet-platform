package com.sunjet.dto.system.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wfb on 2018/8/4.
 * 角色
 */
@Data
public class RoleItem implements Serializable {

    private String objId;
    private String roleId;  // 角色Id
    private String name;    // 角色名称
    private Boolean enabled;//状态

    private String users;   //角色关联的用户
    private String permissions;  //角色对应的权限

    private Date createdTime;

}
