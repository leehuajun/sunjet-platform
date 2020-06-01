package com.sunjet.backend.base;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 树节点实体
 */
@Data
@MappedSuperclass
public class TreeNodeEntity<T> extends DocEntity {

    /**
     * 父对象
     */
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "parent_id")
    private T parent;

}
