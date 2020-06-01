package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 2015/9/6.
 * 权限实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysPermissions")
public class PermissionEntity extends DocEntity {
    private static final long serialVersionUID = -2461553527985758890L;
    /**
     * 访问名称
     */
    @Column(name = "AccessName", length = 50)
    private String accessName;
    /**
     * 资源名称
     */
    @Column(name = "ResourceName", length = 50)
    private String resourceName;
    /**
     * 权限编码
     */
    @Column(name = "PermissionCode", length = 50, unique = true)
    private String permissionCode;
    /**
     * 序号
     */
    @Column(name = "Seq")
    private Integer seq;
    ///**
    // * 角色列表
    // */
    //
    //@ManyToMany(cascade = {CascadeType.REFRESH}, mappedBy = "permissions")
    //private List<RoleEntity> roles = new ArrayList<>();

}
