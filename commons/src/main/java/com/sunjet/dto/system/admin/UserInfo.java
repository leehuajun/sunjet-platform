package com.sunjet.dto.system.admin;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 用户
 */
@Data
public class UserInfo extends DocInfo implements Serializable {

    private String logId;                   // 登录名
    private String password;                // 加密后的密码
    private String salt;                    // 密码加密 盐
    private String name;                    // 姓名
    private String phone;                   // 电话
    private String email;                   //电子邮件地址

    private String userType;            // 用户类型   wuling:五菱工业用户  agency: 合作商  dealer: 服务站

    private String dealerId;//服务站id
    private String agencyId;//合作商id

    private DealerInfo dealer;        // 用户所属 服务站
    private AgencyInfo agency;        // 用户所属 合作商

    private List<MenuInfo> menuInfoList;//菜单

    private List<RoleInfo> roles = new ArrayList<RoleInfo>();


    public static final class UserInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String createrName;  // 创建人名字
        private String modifierId;   // 修改人ID
        private String logId;                   // 登录名
        private String modifierName; // 修改人修改
        private String password;                // 加密后的密码
        private Date createdTime = new Date();   //创建时间
        private String salt;                    // 密码加密 盐
        private String name;                    // 姓名
        private Date modifiedTime = new Date();  //修改时间
        private String phone;                   // 电话
        private String email;                   //电子邮件地址
        private String userType;            // 用户类型   wuling:五菱工业用户  agency: 合作商  dealer: 服务站
        private String dealerId;//服务站id
        private String agencyId;//合作商id
        private DealerInfo dealer;        // 用户所属 服务站
        private AgencyInfo agency;        // 用户所属 合作商
        private List<MenuInfo> menuInfoList;//菜单
        private List<RoleInfo> roles = new ArrayList<RoleInfo>();

        private UserInfoBuilder() {
        }

        public static UserInfoBuilder anUserInfo() {
            return new UserInfoBuilder();
        }

        public UserInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public UserInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public UserInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserInfoBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public UserInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public UserInfoBuilder withLogId(String logId) {
            this.logId = logId;
            return this;
        }

        public UserInfoBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public UserInfoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public UserInfoBuilder withSalt(String salt) {
            this.salt = salt;
            return this;
        }

        public UserInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UserInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public UserInfoBuilder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserInfoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserInfoBuilder withUserType(String userType) {
            this.userType = userType;
            return this;
        }

        public UserInfoBuilder withDealerId(String dealerId) {
            this.dealerId = dealerId;
            return this;
        }

        public UserInfoBuilder withAgencyId(String agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public UserInfoBuilder withDealer(DealerInfo dealer) {
            this.dealer = dealer;
            return this;
        }

        public UserInfoBuilder withAgency(AgencyInfo agency) {
            this.agency = agency;
            return this;
        }

        public UserInfoBuilder withMenuInfoList(List<MenuInfo> menuInfoList) {
            this.menuInfoList = menuInfoList;
            return this;
        }

        public UserInfoBuilder withRoles(List<RoleInfo> roles) {
            this.roles = roles;
            return this;
        }

        public UserInfo build() {
            UserInfo userInfo = new UserInfo();
            userInfo.setObjId(objId);
            userInfo.setCreaterId(createrId);
            userInfo.setEnabled(enabled);
            userInfo.setCreaterName(createrName);
            userInfo.setModifierId(modifierId);
            userInfo.setLogId(logId);
            userInfo.setModifierName(modifierName);
            userInfo.setPassword(password);
            userInfo.setCreatedTime(createdTime);
            userInfo.setSalt(salt);
            userInfo.setName(name);
            userInfo.setModifiedTime(modifiedTime);
            userInfo.setPhone(phone);
            userInfo.setEmail(email);
            userInfo.setUserType(userType);
            userInfo.setDealerId(dealerId);
            userInfo.setAgencyId(agencyId);
            userInfo.setDealer(dealer);
            userInfo.setAgency(agency);
            userInfo.setMenuInfoList(menuInfoList);
            userInfo.setRoles(roles);
            return userInfo;
        }
    }
}
