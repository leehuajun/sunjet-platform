package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@Immutable
@Subselect("SELECT acp.obj_id, acp.part_code, acp.part_name, awm.doc_no AS src_doc_no, awm.doc_type AS src_doc_type, awm.dealer_code, awm.dealer_name, bv.vin, bv.vehicle_model, bv.engine_no, bv.purchase_date, awm.vmt AS mileage, acp.pattern, acp.reason, acp.amount AS wait_Amount, awm.obj_id AS awm_obj_id FROM asm_warranty_maintenances AS awm LEFT JOIN asm_commission_parts AS acp ON awm.obj_id = acp.warranty_maintenance LEFT JOIN basic_vehicles AS bv ON awm.vehicle_id = bv.obj_id WHERE acp.recycle = TRUE AND awm.`status` <>- 3  ")
public class WarrantyRecycleLabelView {

    @Id
    private String objId;      // 主键id
    private String partCode;  //配件号
    private String partName;  //配件名
    private String srcDocNo;  // 来源单号
    private String srcDocType;  // 来源单号类型
    private String dealerCode;  //服务站编码
    private String dealerName;  // 服务站名
    //private Date requestDate;  // 返回时间
    private String vin;         // 车辆vin
    private String vehicleModel;  //车辆型号
    private String engineNo;       // 发动机号
    private Date purchaseDate;   //购买时间
    private String mileage;     // 行车里程
    private String pattern;     //故障模式
    private String reason;     //故障原因
    //private String recycle;    //故障件id
    private Integer waitAmount;  // 待返回数量
    private String awmObjId;  // 三包单id


}
