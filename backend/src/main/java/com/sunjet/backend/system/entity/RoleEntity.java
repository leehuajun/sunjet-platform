package com.sunjet.backend.system.entity;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 2015/9/6.
 * 角色实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysRoles")
public class RoleEntity extends DocEntity {
    private static final long serialVersionUID = 8512757228891325156L;
    /**
     * 角色Id
     */
    @Column(length = 64, unique = true, nullable = false)
    private String roleId;  //
    /**
     * 角色名称
     */
    @Column(name = "Name", length = 20, unique = true, nullable = false)
    private String name;    //

    ///**
    // * 角色对应的用户列表
    // */
    //
    //@ManyToMany(cascade = {CascadeType.REFRESH},
    //        mappedBy = "roles",
    //        fetch = FetchType.LAZY)
    //private List<UserEntity> users = new ArrayList<>();
    ///**
    // * 角色所拥有的权限列表
    // */
    //
    //@ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    //@JoinTable(name = "SysRolePermission",
    //        inverseJoinColumns = @JoinColumn(name = "permission_id"),
    //        joinColumns = @JoinColumn(name = "role_id"))
    //private List<PermissionEntity> permissions = new ArrayList<PermissionEntity>();

}
