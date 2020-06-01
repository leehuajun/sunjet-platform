package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.TreeNodeEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by lhj on 2015/9/6.
 * 菜单实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysMenus")
public class MenuEntity extends TreeNodeEntity<MenuEntity> {
    private static final long serialVersionUID = -929747504110218495L;
    /**
     * 菜单名称
     */
    @Column(name = "Name", length = 20)
    private String name;
    /**
     * 菜单URL
     */
    @Column(name = "Url", length = 200)
    private String url;   // menu 使用
    /**
     * 菜单图标
     */

    @Column(name = "Icon", length = 50)
    private String icon = "z-icon-credit-card";
    /**
     * 菜单排序
     */
    @Column(name = "Seq")
    private Integer seq; // 排序
    /**
     * 菜单是否打开
     */
    @Column(name = "IsOpen")
    private Boolean open = true;  // 状态,true:打开   false:关闭
    /**
     * 菜单背景
     */

    @Transient
    private String background = "rgb(230,230,230)";
    /**
     * 菜单绑定的权限编码
     */
    @Column(name = "PermissionCode", length = 50)
    private String permissionCode;
    /**
     * 菜单绑定的权限名称
     */
    @Column(name = "PermissionName", length = 50)
    private String permissionName;


    public static final class MenuEntityBuilder {
        private String createrId;
        private String createrName;
        private MenuEntity parent;
        private String modifierId;
        private String name;
        private String modifierName;
        private String url;   // menu 使用
        private String objId;
        private Boolean enabled = false;

        private String icon = "z-icon-credit-card";
        private Integer seq; // 排序
        private Date createdTime = new Date();
        private Boolean open = true;  // 状态,true:打开   false:关闭
        private Date modifiedTime = new Date();

        private String background = "rgb(230,230,230)";
        private String permissionCode;
        private String permissionName;

        private MenuEntityBuilder() {
        }

        public static MenuEntityBuilder aMenuEntity() {
            return new MenuEntityBuilder();
        }

        public MenuEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public MenuEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public MenuEntityBuilder withParent(MenuEntity parent) {
            this.parent = parent;
            return this;
        }

        public MenuEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public MenuEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MenuEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public MenuEntityBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public MenuEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public MenuEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public MenuEntityBuilder withIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public MenuEntityBuilder withSeq(Integer seq) {
            this.seq = seq;
            return this;
        }

        public MenuEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public MenuEntityBuilder withOpen(Boolean open) {
            this.open = open;
            return this;
        }

        public MenuEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public MenuEntityBuilder withBackground(String background) {
            this.background = background;
            return this;
        }

        public MenuEntityBuilder withPermissionCode(String permissionCode) {
            this.permissionCode = permissionCode;
            return this;
        }

        public MenuEntityBuilder withPermissionName(String permissionName) {
            this.permissionName = permissionName;
            return this;
        }

        public MenuEntity build() {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setCreaterId(createrId);
            menuEntity.setCreaterName(createrName);
            menuEntity.setParent(parent);
            menuEntity.setModifierId(modifierId);
            menuEntity.setName(name);
            menuEntity.setModifierName(modifierName);
            menuEntity.setUrl(url);
            menuEntity.setObjId(objId);
            menuEntity.setEnabled(enabled);
            menuEntity.setIcon(icon);
            menuEntity.setSeq(seq);
            menuEntity.setCreatedTime(createdTime);
            menuEntity.setOpen(open);
            menuEntity.setModifiedTime(modifiedTime);
            menuEntity.setBackground(background);
            menuEntity.setPermissionCode(permissionCode);
            menuEntity.setPermissionName(permissionName);
            return menuEntity;
        }
    }
}
