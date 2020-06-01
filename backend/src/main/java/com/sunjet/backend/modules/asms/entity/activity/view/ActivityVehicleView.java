package com.sunjet.backend.modules.asms.entity.activity.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 活动车辆
 * Created by zyf on 2017/8/10.
 */
@Data
@Entity
@Immutable
@Subselect("SELECT aav.obj_id,aav.activity_maintenance_id, aav.vehicle_id, aav.activity_notice_id, aav.activity_distribution_id, aav.distribute, aav.`repair`, aav.repair_date, bv.vin, bv.vsn, bv.vehicle_model, bv.engine_no ,bv.vmt,bv.engine_model, bv.plate, bv.fm_date, bv.manufacture_date, bv.seller, bv.owner_name, bv.phone, bv.mobile, bv.address, bv.purchase_date,bv.product_date, aav.mileage, bv.type_name, bv.type_code FROM asm_activity_vehicles aav LEFT JOIN basic_vehicles AS bv ON bv.obj_id = aav.vehicle_id")
public class ActivityVehicleView implements Serializable {

    @Id
    private String objId;  // 主键
    private String vin;  // 车辆VIN码
    private String vsn;  // 车辆VSN码
    private String vehicleModel;  // 车型型号
    private String engineModel; // 发动型号
    private String engineNo; // 发动机号
    private String plate; // 车牌号
    private Date manufactureDate; // 生产日期
    private Date fmDate; // 首保日期
    private String seller; //经销商
    private String ownerName;  // 车主姓名
    private String phone;   // 固定电话
    private String mobile;  // 手机
    private String address;  // 详细地址
    private Date productDate;
    private Date purchaseDate;  // 购买日期
    private String vmt;       //服务里程
    private String mileage;  // 行驶里程
    private Boolean distribute = false; // 是否已分配，默认为false
    private Boolean repair = false;     // 是否已参加维修，默认为false
    private Date repairDate;            // 维修日期
    private String activityNoticeId;  //车辆ID
    private String activityDistributionId;  // 活动分配单ID
    private String activityMaintenanceId;   //活动服务单ID
    private String vehicleId;  //车辆ID
    private String typeCode;        // 车辆型号
    private String typeName;        // 车辆类别

}
