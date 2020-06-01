package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 活动通知单报表视图
 * <p>
 * Created by SUNJET_WS on 2017/10/13.
 */
@Data
@Entity
@Immutable
@Subselect("SELECT aav.obj_id, bv.vin, bv.vsn, bv.vehicle_model, bv.owner_name, bv.mobile, bv.address, bv.purchase_date, bv.mileage, aav.distribute, aan.doc_no, aan.start_date, aan.end_date, aan.`status`, aan.title, aan.created_time FROM asm_activity_vehicles AS aav LEFT JOIN basic_vehicles AS bv ON aav.vehicle_id = bv.obj_id LEFT JOIN asm_activity_notices AS aan ON aan.obj_id = aav.activity_notice_id WHERE aan.`status` <>- 3")
public class ActivitiNoticeReportView {
    @Id
    private String objId;   // 主键
    private String vin;   // 车辆vin
    private String vsn;   // 车辆vsn
    private String vehicleModel;   // 车辆vsn
    private String ownerName;   // 用户
    private String mobile;       //手机
    private String address;     //地址
    private String purchaseDate;   //购买日期
    private String mileage;    //车辆行驶里程
    private Boolean distribute;   //分配状态
    private String docNo;      //单据编号
    private Date startDate;   //开始日期
    private Date endDate;    // 结束日期
    private Integer status;  //状态
    private String title;    //标题
    private Date createdTime;   //发布时间


}
