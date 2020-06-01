package com.sunjet.dto.system.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hxf on 2015/11/05.
 * 资源 list
 */
@Data
public class ResourceItem implements Serializable {

    private String objId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源编码
     */
    private String code;

    /**
     * 状态
     */
    private Boolean enabled = false;

    /**
     * 资源允许的操作列表
     */
    private String operations;

    private Date createdTime;
}
