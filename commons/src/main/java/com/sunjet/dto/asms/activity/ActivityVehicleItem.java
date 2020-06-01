package com.sunjet.dto.asms.activity;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.util.Date;

/**
 * 活动车辆
 * Created by zyf on 2017/8/15.
 */
@Data
public class ActivityVehicleItem extends DocInfo {

    private String objId;  // 主键
    private String vin;  // 车辆VIN码
    private String vsn;  // 车辆VSN码
    private String vehicleModel;  // 车型型号
    private String engineModel; //发动型号
    private String engineNo; // 发动机号
    private String plate; // 车牌号
    private Date manufactureDate; // 生产日期
    private Date fmDate; // 首保日期
    private String seller; //经销商
    private String ownerName;  // 车主姓名
    private String phone;   // 固定电话
    private String mobile;  // 手机
    private String address;  // 详细地址
    private Date purchaseDate;  // 购买日期
    private Date productDate; // 出厂日期
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
