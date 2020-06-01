package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/13.
 * 权限
 */
@Data
public class PermissionInfo extends DocInfo implements Serializable {

    private String accessName;   //访问名称

    private String resourceName; //资源名称

    private String permissionCode;  //权限编码

    private Integer seq;   //序号

    private List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();  //角色列表

    @Override
    public String toString() {
        return resourceName + "->" + accessName;
    }

}
