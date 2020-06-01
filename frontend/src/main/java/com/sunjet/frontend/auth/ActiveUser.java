package com.sunjet.frontend.auth;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: lhj
 * @create: 2017-07-02 20:51
 * @description: 说明
 */
@Data
@Builder
public class ActiveUser implements Serializable {
    private static final long serialVersionUID = -5809095705388430051L;
    private String userId;//用户id
    private String logId;//登录id
    private String username;//用户姓名


    private List<MenuInfo> menus;//菜单
    private List<RoleInfo> roles;//角色
    private List<String> permissions;//权限
    private DealerInfo dealer;        // 服务站
    private AgencyInfo agency;        // 合作商
    private String userType;        // 用户类别  wuling /  agency  / dealer
    private String phone;           // 用户联系电话


}
