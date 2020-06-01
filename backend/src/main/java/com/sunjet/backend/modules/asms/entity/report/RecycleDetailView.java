package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 故障件明细视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT DISTINCT ari.obj_id, arn.`status`, ard.`status` AS ard_status, arn.dealer_code, arn.dealer_name, arn.doc_no, arn.src_doc_no, ard.doc_no AS ard_doc_no, ard.service_manager, ari.part_code, ari.part_name, arni.pattern, arni.reason, arni.amount, arni.back_amount AS back_amount, ( arni.amount - arni.back_amount ) AS wait_amount, acp.part_supply_type, arni.warranty_time, arni.warranty_mileage, ari.return_date, ard.arrive_date, ard.logistics_num, ard.logistics, arn.created_time, ard.created_time AS ard_create_time FROM asm_recycle_items AS ari LEFT JOIN asm_recycle_docs AS ard ON ard.obj_id = ari.recycle LEFT JOIN asm_recycle_notice_items AS arni ON arni.obj_id = ari.notice_item_id LEFT JOIN asm_recycle_notices AS arn ON arn.obj_id = arni.recycle_notice_id LEFT JOIN asm_commission_parts AS acp ON arni.commission_part_id = acp.obj_id WHERE ard.`status` <> -3  ")
public class RecycleDetailView {

    @Id
    private String objId;   // 主键
    private String status;   // 返回通知单状态
    private String ardStatus;   // 返回故障件单状态
    private String dealerCode;   // 服务站编号
    private String dealerName;   // 服务站名称
    private String docNo;   // 返回通知单编号
    private String srcDocNo;   // 来源编号
    private String ardDocNo;   // 返回故障件单据编号
    private String serviceManager;   // 服务经理
    private String partCode;   // 配件号
    private String partName;   // 配件名
    private String pattern;   // 故障模式
    private String reason;   // 换件原因
    private String amount;   // 需返回数量
    private String backAmount;   // 已返回数量
    private String waitAmount;   // 实际返回数量
    private String partSupplyType;   // 供货方式
    private String warrantyTime;   // 三包里程
    private String warrantyMileage;   // 三包时间
    private Date returnDate;   // 应返时间
    private Date arriveDate;   // 实返时间
    private String logisticsNum;   // 物流单号
    private String logistics;   // 物流公司
    private Date createdTime;   // 通知单创建时间
    private Date ardCreateTime;   // 故障件返回单创建时间


}
