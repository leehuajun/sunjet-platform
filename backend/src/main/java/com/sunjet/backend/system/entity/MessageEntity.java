package com.sunjet.backend.system.entity;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: lhj
 * @create: 2017-04-28 13:59
 * @description: 服务站提醒消息内容实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysMessages")
public class MessageEntity extends DocEntity {
    private static final long serialVersionUID = -8890946973346486567L;
    /**
     * 标题
     */
    @Column(name = "Title", length = 200)
    private String title;
    /**
     * 内容
     */
    @Column(name = "Content", length = 2000)
    private String content;
    /**
     * 组织Id
     */
    @Column(name = "OrgId", length = 100)
    private String orgId;
    /**
     * url
     */
    @Column(name = "Url", length = 200)
    private String url;
    /**
     * 引用的对象单据Id
     */
    @Column(name = "RefId", length = 32)
    private String refId;

    /**
     * 用户logId
     */
    @Column(name = "LogId", length = 100)
    private String logId;

    /**
     * 是否已读
     */
    private Boolean isRead = false;

}
