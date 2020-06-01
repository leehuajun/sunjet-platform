package com.sunjet.dto.system.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 树节点实体
 */
//@MappedSuperclass
public class TreeNodeInfo<T> extends DocInfo {

    /**
     * 父对象
     */
    //@ManyToOne(cascade = {CascadeType.REFRESH})
    //@JoinColumn(name = "parent_id")
    @Getter
    @Setter
    private T parent;


}
