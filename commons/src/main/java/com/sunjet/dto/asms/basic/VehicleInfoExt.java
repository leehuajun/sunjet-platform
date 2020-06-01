package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 车辆VO
 */
@Data
public class VehicleInfoExt extends DocInfo implements Serializable {

    private String objId;               //ID
    private String vin;                 // 车辆VIN码
    private String vsn;                 // 车辆VSN码
    private String vehicleModel;        // 车型型号
    private String engineModel;         // 发动机型号
    private String engineNo;            // 发动机号
    private String brand;               // 品牌
    private Double displacement;        // 排量
    private Date manufactureDate;       // 生产日期
    private Date productDate;           // 出厂日期
    private Date purchaseDate;          // 购买日期
    private Date fmDate;                // 首保日期
    private String plate;               // 车牌号
    private String licensePlate;        // 车辆牌照
    private String seller;              // 销售商
    private String mileage;             // 行驶里程
    private String vmt;                 // 里程
    private String ownerName;           // 车主姓名
    private String sex;                 // 性别
    private String address;             // 详细地址
    private String phone;               // 固定电话
    private String mobile;              // 手机
    private String email;               // 电子邮件
    private String postcode;            // 邮政编码
    private String dealerName;          // 服务站名称
    private String dealerAddress;       // 服务站地址
    private String serviceManager;      // 服务经理名称

    private String provinceId;    // 省份Id
    private String provinceName;    // 省份Name
    private String cityId;            // 城市Id
    private String cityName;            // 城市Name
    private String countyId;        // 县/区Id
    private String countyName;        // 县/区Name

    private String typeCode;    //车型类别代码
    private String typeName;    //车型类别名称

    private Integer err;
}
