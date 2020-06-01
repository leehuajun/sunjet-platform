package com.sunjet.dto.system.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 用户
 */
@Data
public class UserItem implements Serializable {

    private String objId;                   //主键
    private String logId;                   // 登录名
    private String name;                    // 姓名
    private String phone;                   // 电话
    private String email;                   // 电子邮件
    private String userType;            // 用户类型   wuling:五菱工业用户  agency: 合作商  dealer: 服务站
    private Boolean enabled;           //是否启用

    private String dealerId;        // 用户所属 服务站
    private String dealerName;        // 用户所属 服务站
    private String agencyId;        // 用户所属 合作商
    private String agencyName;        // 用户所属 合作商
    private String roleNames;          //拥有角色

    private Date createdTime;
}
