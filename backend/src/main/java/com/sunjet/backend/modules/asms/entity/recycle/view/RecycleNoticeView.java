package com.sunjet.backend.modules.asms.entity.recycle.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_QRY on 2017/8/10.
 * 返回通知单
 */
@Data
@Entity
@Immutable
@Subselect("SELECT arn.obj_id,\n" +
        " arn.doc_no,  " +
        " arn.src_doc_type,  " +
        " arn.src_doc_no,  " +
        " arn.dealer_code,  " +
        " arn.dealer_name,  " +
        " arn.created_time,  " +
        " arn.submitter_name,  " +
        " arn.process_instance_id,  " +
        " arn.`status`  " +
        " FROM asm_recycle_notices arn")
public class RecycleNoticeView {

    @Id
    private String objId;   //主键id

    private String docNo;   //单号

    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、

    private String srcDocNo;        //单据编号

    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出

    private String dealerName;      // 服务站名称

    private Date createdTime;   // 提交时间

    private String submitterName;       // 提交人姓名

    private Integer status;         // 表单状态

    private String processInstanceId;   // 流程实例Id
}
