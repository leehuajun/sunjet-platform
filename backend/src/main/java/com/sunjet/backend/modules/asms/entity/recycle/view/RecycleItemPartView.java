package com.sunjet.backend.modules.asms.entity.recycle.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_QRY on 2017/8/16.
 */
@Data
@Entity
@Immutable
@Subselect("SELECT " +
        " arl.obj_id, " +
        " arl.part_code, " +
        " arl.part_name, " +
        " arl.src_doc_type, " +
        " arl.src_doc_no, " +
        " arl.logistics_num, " +
        " arl.warranty_time, " +
        " arl.warranty_mileage, " +
        " arl.wait_amount, " +
        " arl.back_amount, " +
        " arl.accept_amount, " +
        " arl.notice_item_id, " +
        " arn.return_date, " +
        " arn.doc_no, " +
        " arl.`comment`, " +
        " arl.recycle " +
        "FROM " +
        " asm_recycle_items AS arl, " +
        " asm_recycle_notices AS arn, " +
        " asm_recycle_notice_items AS arni " +
        "WHERE " +
        " arl.notice_item_id = arni.obj_id " +
        "AND arni.recycle_notice_id = arn.obj_id")
public class RecycleItemPartView {

    @Id
    private String objId;
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String srcDocNo;        //单据编号
    private String logisticsNum;    // 物流单号
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private Integer waitAmount = 0;           // 待返数量
    private Integer backAmount = 0;          // 本次返回数量
    private Integer acceptAmount = 0;        // 收货数量
    private String noticeItemId;
    private Date returnDate;         // 返回日期  要求默认下个月10号
    private String docNo;
    private String comment;     // 备注
    private String recycle;     // 故障件返回单
}
