package com.sunjet.backend.system.entity.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wfb on 17-8-3.
 * 资源列表
 */
@Data
@Entity
@Immutable
@Subselect("select obj_Id,name,code,enabled,created_time from sys_resources")
public class ResourceView implements Serializable {

    @Id
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
    private Boolean enabled;

    private Date createdTime;

}
