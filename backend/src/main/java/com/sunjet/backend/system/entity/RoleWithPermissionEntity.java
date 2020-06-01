package com.sunjet.backend.system.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by wfb on 17-8-4.
 * 角色 与 权限 的关联关系
 */
@Data
@Entity
@Table(name = "SysRolePermission")
public class RoleWithPermissionEntity {

    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name = "ObjId", length = 32, nullable = false)
    private String objId;//主键

    @Column(name = "roleId", length = 32, nullable = false)
    private String roleId;//角色id

    @Column(name = "permissionId", length = 32, nullable = false)
    private String permissionId;//用户id
}
