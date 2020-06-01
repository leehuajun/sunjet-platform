package com.sunjet.backend.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 业务基础实体
 */
@MappedSuperclass
public class DocEntity extends IdEntity {

    @Getter
    @Setter
    @Column(name = "CreaterId", length = 32)
    private String createrId;

    @Getter
    @Setter
    @Column(name = "CreaterName", length = 50)
    private String createrName;

    @Getter
    @Setter
    @Column(name = "ModifierId", length = 32)
    private String modifierId;

    @Getter
    @Setter
    @Column(name = "ModifierName", length = 50)
    private String modifierName;
    /**
     * 日期属性设置
     * DATE: 日期，2001-04-08
     * TIME: 时间：11:54:23
     * TIMESTAMP: 时间戳：2001-04-08 11:54:23
     *
     * @return
     */
    @Getter
    @Setter
    @Column(name = "CreatedTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime = new Date();

    @Getter
    @Setter
    @Column(name = "ModifiedTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime = new Date();

    @PrePersist
    public void prePersist() {
        this.setCreatedTime(new Date());
        this.setModifiedTime(new Date());
        // createrId = CommonHelper.getActiveUser().getUserId();
        // createrName = CommonHelper.getActiveUser().getUsername();
    }

    @PreUpdate
    public void preUpdate() {
        this.setModifiedTime(new Date());
        // modifierId =  CommonHelper.getActiveUser().getUserId();
        // modifierName = CommonHelper.getActiveUser().getUsername();
    }
}
