package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by hxf on 2015/11/05.
 * 资源实体
 */
@Data
@Entity
@Table(name = "SysResources")
public class ResourceEntity extends DocEntity {
    private static final long serialVersionUID = -5833217238641539363L;
    /**
     * 资源名称
     */
    @Column(name = "Name", length = 50)
    private String name;
    /**
     * 资源编码
     */
    @Column(name = "Code", length = 50, unique = true)
    private String code;

    ///**
    // * 资源允许的操作列表
    // */
    //@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    //@JoinTable(name = "SysResourceOperation",
    //        inverseJoinColumns = @JoinColumn(name = "opt_id"),
    //        joinColumns = @JoinColumn(name = "res_id"))
    //private List<OperationEntity> operationEntityList = new ArrayList<>();

}
