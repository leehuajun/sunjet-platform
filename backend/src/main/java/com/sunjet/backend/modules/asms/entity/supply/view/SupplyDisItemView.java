package com.sunjet.backend.modules.asms.entity.supply.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 二次分配
 */
@Data
@Entity
@Immutable
@Subselect("SELECT asdi.obj_id, asdi.part_code, asdi.part_name, asdi.request_amount, asdi.surplus_amount, asdi.arrival_time, asdi.agency_name, asdi.distribution_amount, asdi.supply_notice_item_id, asdi.created_time, asn.doc_no, asni.src_doc_no FROM asm_supply_dis_items AS asdi LEFT JOIN asm_supply_notice_items AS asni ON asni.obj_id = asdi.supply_notice_item_id LEFT JOIN asm_supply_notices AS asn ON asn.obj_id = asni.supply_notice_id ")
public class SupplyDisItemView implements Serializable {

    @Id
    private String objId;
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private double requestAmount;       // 需求数量
    private double surplusAmount;    //未供货数量
    private Date arrivalTime;     // 到货时间
    private String agencyName;      // 合作商
    private double distributionAmount = 0;    //本次分配数量
    private Date createdTime;
    private String docNo;               // 调拨通知单
    private String srcDocNo;            //来源单据编号
    private String supplyNoticeItemId;//调拨分配id

    //private String agencyCode;      // 合作商编号　
    //private double sentAmount;          // 已供货数量
    //private String comment;         // 备注
}
