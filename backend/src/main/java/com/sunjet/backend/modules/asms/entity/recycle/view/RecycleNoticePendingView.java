package com.sunjet.backend.modules.asms.entity.recycle.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_QRY on 2017/8/14.
 * 待返回清单 视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT arni.obj_id, arn.doc_no, arn.src_doc_no, arn.src_doc_type, arni.part_code, arni.part_name, arni.amount, arni.back_amount, arni.amount - arni.back_amount AS current_amount, arn.dealer_code, arn.dealer_name, arn.return_date, arni.warranty_time, arni.warranty_mileage, arni.reason, arni.pattern, arni.`comment`, arn.created_time FROM asm_recycle_notice_items arni LEFT JOIN asm_recycle_notices arn ON arn.obj_id = arni.recycle_notice_id WHERE arn.`status` = 3 ")
public class RecycleNoticePendingView {

    @Id
    private String objId;       //主键
    private String docNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String srcDocNo;      // 来源类型：三包服务单
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private Integer amount = 0;    //数量
    private Integer backAmount = 0;          // 本次返回数量  已返回数量
    private Integer currentAmount = 0;           // 待返数量  未返回数量
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date returnDate;    // 规定返回时间
    private String reason;   //换件原因
    private String pattern;  // 故障模式
    private Date createdTime;   //返回通知单创建时间

    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String comment;

}
