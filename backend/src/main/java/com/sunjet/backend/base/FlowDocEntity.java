package com.sunjet.backend.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 业务基础实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class FlowDocEntity extends DocEntity {

    @Column(length = 100)
    private String processInstanceId;   // 流程实例Id

    private Integer status = 0;         // 表单状态


    @Column(name = "SubmitDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitDate;            // 流程提交时间

    @Column(length = 50)
    private String submitter;           // 提交人LogId

    @Column(length = 50)
    private String submitterName;       // 提交人姓名

    @Column(length = 50)
    private String submitterPhone;      // 提交人电话

    @Column(length = 50)
    private String handler;             // 当前处理人

    @Column(length = 32, unique = true)
    private String docNo;              // 单据编号
//    private Boolean autoClose = false;    // 是否自动关闭, 默认为不自动关闭
}
