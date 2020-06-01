package com.sunjet.backend.modules.asms.entity.activity.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 活动服务单
 * Created by zyf on 2017/8/28.
 */
@Data
@Entity
@Immutable
@Subselect("SELECT aam.obj_id, aam.doc_no, aam.dealer_code, aam.dealer_name, aad.doc_no AS aad_doc_no, aam.activity_vehicle_id, aam.created_time, aad.activity_notice_id, aan.doc_no AS aan_doc_no, aam.submitter_name, aam.`status`, aam.service_manager, bv.vin, bv.plate, aam.sender FROM asm_activity_maintenances AS aam LEFT JOIN asm_activity_vehicles AS aav ON aav.obj_id = aam.activity_vehicle_id LEFT JOIN basic_vehicles AS bv ON bv.obj_id = aav.vehicle_id LEFT JOIN asm_activity_distributions AS aad ON aam.activity_distribution_id = aad.obj_id LEFT JOIN asm_activity_notices AS aan ON aan.obj_id = aad.activity_notice_id ")
public class ActivityMaintenanceView {

    @Id
    private String objId;// 主键
    private String docNo;// 单据编号
    private String dealerCode;// 服务站编号
    private String dealerName;// 服务站名称
    private String aadDocNo;// 活动分配单号编号
    private String aanDocNo;// 活动通知单编号
    private String activityVehicleId;// 活动通知单编号
    private Date createdTime;// 申请日期
    private String submitterName;// 提交人姓名
    private Integer status;// 表单状态
    private String serviceManager;  // 服务经理
    private String vin;  //车辆vin
    private String sender;         // 送修人
    private String plate;          //车牌号
}
