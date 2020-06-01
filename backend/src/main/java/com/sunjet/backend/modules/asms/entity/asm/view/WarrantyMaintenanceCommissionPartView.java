package com.sunjet.backend.modules.asms.entity.asm.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/3.
 * 维修历史视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT acp.obj_id, awm.doc_no, acp.part_code, acp.part_name, awm.vmt, acp.pattern, acp.price, acp.amount, awm.created_time, acp.warranty_maintenance, acp.activity_maintenance_id, asd.agency_name, awm.`status` FROM asm_commission_parts AS acp LEFT JOIN asm_warranty_maintenances AS awm ON awm.obj_id = acp.warranty_maintenance LEFT JOIN asm_supply_notice_items AS asni ON acp.obj_id = asni.commission_part_id LEFT JOIN asm_supply_items AS asi ON asi.supply_notice_item_id = asni.obj_id LEFT JOIN asm_supply_docs AS asd ON asd.obj_id = asi.supply_id")
public class WarrantyMaintenanceCommissionPartView implements Serializable {
    @Id
    private String objId;     //主键

    private String docNo;    //单据编号

    private String status;   //单据状态

    private String partCode;    // 配件图号

    private String partName;  // 配件名称

    private String vmt;      //行驶里程

    private String pattern;   //故障模式

    private Double price;     // 单价

    private Integer amount;     // 数量

    private String agencyName;   // 供货方

    private Date createdTime;   // 申请时间

    private String warrantyMaintenance;

    private String activityMaintenanceId;

}
