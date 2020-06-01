package com.sunjet.backend.system.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by wfb on 17-8-4.
 * 用户与角色的关系表
 */
@Data
@Entity
@Table(name = "SysUserRole")
public class UserWithRoleEntity {

    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name = "ObjId", length = 32, nullable = false)
    private String objId;//主键

    @Column(name = "userId", length = 32, nullable = false)
    private String userId;//用户id

    @Column(name = "roleId", length = 32, nullable = false)
    private String roleId;//角色id
}
