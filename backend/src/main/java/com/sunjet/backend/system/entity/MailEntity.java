package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by lhj on 2015/9/6.
 * 邮件内容实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysMails")
public class MailEntity extends DocEntity {
    private static final long serialVersionUID = 263202896990504513L;

    /**
     * 执行人或组，角色
     */
    @Column(length = 200)
    private String executor;
    /**
     * 执行人类别
     */
    @Column(length = 200)
    private String executorType;
    /**
     * 流程Id
     */
    @Column(length = 200)
    private String processId;
    /**
     * 流程类型
     */
    @Column(length = 200)
    private String processType;
    /**
     * 流程节点名称
     */
    @Column(length = 200)
    private String taskName;
    /**
     * 流程对应的单据号
     */
    @Column(length = 200)
    private String docNo;
}
