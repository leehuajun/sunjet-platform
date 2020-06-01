package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 2015/9/6.
 * 操作实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysOperations")
public class OperationEntity extends DocEntity {
    private static final long serialVersionUID = 9150516330799632348L;
    /**
     * 操作编码
     */
    @Column(name = "OptCode", length = 20, unique = true)
    private String optCode; //  操作编码
    /**
     * 操作名称
     */
    @Column(name = "OptName", length = 20)
    private String optName; //  操作名称
    /**
     * 序号
     */
    @Column(name = "Seq")
    private Integer seq; // 序号

    /**
     * 资源列表
     */
    //@ManyToMany(cascade = {CascadeType.REFRESH}, mappedBy = "operationEntityList")
    //private List<ResourceEntity> resourceEntityList = new ArrayList<>();

}
