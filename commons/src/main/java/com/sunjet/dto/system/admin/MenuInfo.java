package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 菜单
 */
@Data
@NoArgsConstructor
public class MenuInfo extends DocInfo implements Serializable {
    private String name;   // 菜单名字
    private String url;   // menu url
    private Integer seq; // 排序
    private String icon = "z-icon-credit-card";
    private String background = "rgb(230,230,230)";
    private Boolean open = true;  // 状态,true:打开   false:关闭
    private String permissionCode;   //权限编码
    private String permissionName;  //权限名称

    private MenuInfo parent;


    public static final class MenuInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String createrName;  // 创建人名字
        private String name;   // 菜单名字
        private String url;   // menu url
        private String modifierId;   // 修改人ID
        private Integer seq; // 排序
        private String icon = "z-icon-credit-card";
        private String modifierName; // 修改人修改
        private String background = "rgb(230,230,230)";
        private Date createdTime = new Date();   //创建时间
        private Boolean open = true;  // 状态,true:打开   false:关闭
        private Date modifiedTime = new Date();  //修改时间
        private String permissionCode;   //权限编码
        private String permissionName;  //权限名称
        private MenuInfo parent;

        private MenuInfoBuilder() {
        }

        public static MenuInfoBuilder aMenuInfo() {
            return new MenuInfoBuilder();
        }

        public MenuInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public MenuInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public MenuInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public MenuInfoBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public MenuInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MenuInfoBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public MenuInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public MenuInfoBuilder withSeq(Integer seq) {
            this.seq = seq;
            return this;
        }

        public MenuInfoBuilder withIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public MenuInfoBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public MenuInfoBuilder withBackground(String background) {
            this.background = background;
            return this;
        }

        public MenuInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public MenuInfoBuilder withOpen(Boolean open) {
            this.open = open;
            return this;
        }

        public MenuInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public MenuInfoBuilder withPermissionCode(String permissionCode) {
            this.permissionCode = permissionCode;
            return this;
        }

        public MenuInfoBuilder withPermissionName(String permissionName) {
            this.permissionName = permissionName;
            return this;
        }

        public MenuInfoBuilder withParent(MenuInfo parent) {
            this.parent = parent;
            return this;
        }

        public MenuInfo build() {
            MenuInfo menuInfo = new MenuInfo();
            menuInfo.setObjId(objId);
            menuInfo.setCreaterId(createrId);
            menuInfo.setEnabled(enabled);
            menuInfo.setCreaterName(createrName);
            menuInfo.setName(name);
            menuInfo.setUrl(url);
            menuInfo.setModifierId(modifierId);
            menuInfo.setSeq(seq);
            menuInfo.setIcon(icon);
            menuInfo.setModifierName(modifierName);
            menuInfo.setBackground(background);
            menuInfo.setCreatedTime(createdTime);
            menuInfo.setOpen(open);
            menuInfo.setModifiedTime(modifiedTime);
            menuInfo.setPermissionCode(permissionCode);
            menuInfo.setPermissionName(permissionName);
            menuInfo.setParent(parent);
            return menuInfo;
        }
    }
}
