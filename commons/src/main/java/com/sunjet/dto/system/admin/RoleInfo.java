package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 角色
 */
@Data
public class RoleInfo extends DocInfo implements Serializable {

    private String roleId;  // 角色Id
    private String name;    // 角色名称

    private List<UserInfo> userInfos = new ArrayList<UserInfo>();
    private List<PermissionInfo> permissions = new ArrayList<>();

}
