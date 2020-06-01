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
@Subselect("SELECT DISTINCT acp.obj_id, aam.doc_no, acp.part_code, acp.part_name, aam.vmt, acp.pattern, acp.price, acp.amount, aam.created_time, aam.`status`, b.agency_name, acp.activity_maintenance_id FROM asm_commission_parts AS acp RIGHT JOIN asm_activity_maintenances AS aam ON aam.obj_id = acp.activity_maintenance_id LEFT JOIN asm_activity_distributions AS aad ON aad.obj_id = aam.activity_distribution_id LEFT JOIN (SELECT DISTINCT asni.obj_id, asd.agency_name, asni.part_code, asni.part_name, aad.obj_id AS aadObjId, asni.commission_part_id FROM asm_supply_notice_items AS asni RIGHT JOIN asm_activity_distributions AS aad ON aad.obj_id = asni.src_docid LEFT JOIN asm_supply_items AS asi ON asi.supply_notice_item_id = asni.obj_id LEFT JOIN asm_supply_docs AS asd ON asd.obj_id = asi.supply_id ) b ON aam.activity_distribution_id = b.aadObjId AND acp.activity_part_id = b.commission_part_id ")
public class ActivityMaintenanceCommissionPartView implements Serializable {
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

    private String activityMaintenanceId;

}
