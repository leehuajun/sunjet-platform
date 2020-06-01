package com.sunjet.backend.system.entity;

/**
 * Created by wfb on 17-8-4.
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by hxf on 2015/11/05.
 * 资源 与 操作 关系表
 */
@Data
@Entity
@Table(name = "SysResourceOperation")
public class ResourceWithOperationEntity {

    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name = "ObjId", length = 32, nullable = false)
    private String objId;//主键

    @Column(name = "resId", length = 32, nullable = false)
    private String resId;//资源id

    @Column(name = "optId", length = 32, nullable = false)
    private String optId;//操作id
}
