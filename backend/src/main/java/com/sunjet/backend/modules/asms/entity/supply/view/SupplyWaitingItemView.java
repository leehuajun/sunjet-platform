package com.sunjet.backend.modules.asms.entity.supply.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 待发发货清单
 */
@Data
@Entity
@Immutable
@Subselect("SELECT asmi. obj_id, asmi.part_code, asmi.part_name, asmi.request_amount, asmi.surplus_amount, asmi.arrival_time, asmi.agency_code, asmi.agency_name, asmi.dealer_code, asmi.dealer_name, asmi. `comment`, asmi.sent_amount, asmi.service_manager, asmi.supply_notice_item_id, asmi.supply_dis_item_id, asmi.created_time, asni.src_doc_no, asn.doc_no FROM asm_supply_waiting_items AS asmi LEFT JOIN asm_supply_notice_items AS asni on asni.obj_id = asmi.supply_notice_item_id LEFT JOIN asm_supply_notices AS asn ON asn.obj_id = asni.supply_notice_id")
public class SupplyWaitingItemView {

    @Id
    private String objId;
    private String partCode; //零件件号
    private String partName; //零件名称
    private double requestAmount; //需求数量
    private double surplusAmount; //可分配数量
    private Date arrivalTime; //要求到货时间
    private String agencyCode; //经销商  合作商名称
    private String agencyName; //经销商  合作商名称
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String serviceManager;  //服务经理
    private String comment; // 备注
    private String srcDocNo;  // 来源单据
    private String docNo;  // 调拨通知单

    private String supplyDisItemId;  //二次分配id
    private String supplyNoticeItemId;//调拨分配id

    private Date createdTime;
}
