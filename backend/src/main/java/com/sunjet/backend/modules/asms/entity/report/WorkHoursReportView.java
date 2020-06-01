package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 工时视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT wm.obj_id, wm.`code`, wm.`name`, wm.work_time, wm.measure, awm.night_work, awm.night_expense, awm.province_name, awm.dealer_code, awm.dealer_name, awm.doc_no, bv.vin, bv.vehicle_model, bv.engine_model, bv.engine_no, awm.created_time FROM asm_warranty_maintains AS wm LEFT JOIN asm_warranty_maintenances AS awm ON wm.warranty_maintenance = awm.obj_id LEFT JOIN basic_vehicles AS bv ON bv.obj_id = awm.vehicle_id WHERE awm.`status` <> -3")
public class WorkHoursReportView {

    @Id
    private String objId;   // 主键
    private String code;   // 项目编号
    private String name;   // 项目名称
    private String workTime;   // 工时定额
    private String measure;   // 维修措施
    private String nightExpense;   // 夜间工时补贴费用
    private Boolean nightWork;   // 是否夜间作业
    private String provinceName;   // 省份
    private String dealerCode;   // 服务站编号
    private String dealerName;   // 服务站名称
    private String docNo;   // 服务单号
    private String vin;   // VIN
    private String vehicleModel;   // 车型
    private String engineModel;   // 发动机型号
    private String engineNo;   // 发动机号
    private Date createdTime;   // 创建时间


}
