package com.sunjet.backend.modules.asms.entity.supply.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 调拨分配 vo
 */
@Data
@Entity
@Immutable
@Subselect("SELECT asni.obj_id, asni.part_code, asni.part_name, asni.request_amount, asni.surplus_amount, asni.arrival_time, asni.secondary_distribution, asni.agency_code, asni.agency_name, asni.distribution_amount, asni.`comment`, asni.supply_notice_id, asni.commission_part_id, asni.warranty_mileage, asni.warranty_time, asn.created_time, asn.`status`, asn.src_doc_type, asn.src_docid, asn.src_doc_no, asn.doc_no, asn.doc_type, ( CASE WHEN (asni.surplus_amount = 0) THEN 'finishAllocated' WHEN ( asni.request_amount = asni.surplus_amount ) THEN 'allocated' WHEN ( asni.request_amount - asni.surplus_amount > 0 ) THEN 'assigning' ELSE '无状态' END ) AS `allocated_status`, asni.pattern FROM asm_supply_notice_items asni LEFT JOIN asm_supply_notices asn ON asn.obj_id = asni.supply_notice_id WHERE asn.`status` = 3")
public class SupplyAllocationView {

    @Id
    private String objId;
    private String partCode; //零件件号
    private String partName; //零件名称
    private String docNo;  // 单据编号
    private String docType; //单据类型
    private String srcDocID;  // 来源单据
    private String srcDocNo;//来源单据编号
    private String srcDocType;//来源单据类型
    private String warrantyMileage; //三包里程
    private String warrantyTime; //三包时间
    private double requestAmount; //需求数量
    private double surplusAmount; //可分配数量
    private Date arrivalTime; //要求到货时间
    private Boolean secondaryDistribution; //二次分配
    private String agencyCode;      // 经销商编号　
    private String agencyName; //经销商  合作商名称
    private double distributionAmount; //本次分配数量
    private String pattern;             // 故障模式
    private String comment; // 备注
    private Date createdTime;

    private String allocatedStatus; //分配状态


    private String supplyNoticeId;//调拨通知单id
    private String commissionPartId;// 配件需求id

    //srcDocType,srcDocNo,dealerCode,dealerName,provinceName

}
