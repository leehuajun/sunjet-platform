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
@Subselect("SELECT arl.obj_id, arl.part_code, arl.part_name, arl.src_doc_no, arl.src_doc_type, wm.dealer_code, wm.dealer_name, wm.request_date, v.vin, v.vehicle_model, v.engine_no, v.purchase_date, wm.vmt AS mileage, arl.pattern, arl.reason, arl.recycle, arl.wait_amount, arl.back_amount FROM asm_recycle_items arl LEFT JOIN `asm_warranty_maintenances` wm ON wm.obj_id = ( SELECT arn.src_docid FROM `asm_recycle_notices` arn WHERE arn.obj_id = ( SELECT arni.recycle_notice_id FROM `asm_recycle_notice_items` arni WHERE arni.`obj_id` = arl.notice_item_id ) ) LEFT JOIN `basic_vehicles` v ON v.obj_id = wm.`vehicle_id` ")
public class RecycleLabel {

    @Id
    private String objId;      // 主键id
    private String partCode;  //配件号
    private String partName;  //配件名
    private String srcDocNo;  // 来源单号
    private String srcDocType;  // 来源单号类型
    private String dealerCode;  //服务站编码
    private String dealerName;  // 服务站名
    private Date requestDate;  // 返回时间
    private String vin;         // 车辆vin
    private String vehicleModel;  //车辆型号
    private String engineNo;       // 发动机号
    private Date purchaseDate;   //购买时间
    private String mileage;     // 行车里程
    private String pattern;     //故障模式
    private String reason;     //故障原因
    private String recycle;    //故障件id
    private Integer waitAmount;  // 待返回数量
    private Integer backAmount;          // 本次返回数量


}
