package com.sunjet.backend.modules.asms.entity.basic;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * 车辆信息实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicVehicles")
public class VehicleEntity extends DocEntity {
    private static final long serialVersionUID = -7508708530323132518L;
    /**
     * 车辆VIN码
     */
    @Column(length = 50, unique = true, nullable = false)
    private String vin;
    /**
     * 车辆VSN码
     */
    @Column(length = 50)
    private String vsn;
    /**
     * 车型型号
     */
    @Column(length = 50, nullable = false)
    private String vehicleModel;
    /**
     * 发动机型号
     */
    @Column(length = 50)
    private String engineModel;
    /**
     * 发动机号
     */
    @Column(length = 50)
    private String engineNo;
    /**
     * 品牌
     */
    @Column(length = 50)
    private String brand;
    /**
     * 排量
     */
    private Double displacement;
    /**
     * 生产日期
     */
    private Date manufactureDate;
    /**
     * 出厂日期
     */
    private Date productDate;
    /**
     * 购买日期
     */
    private Date purchaseDate;
    /**
     * 首保日期
     */
    private Date fmDate;
    /**
     * 车牌号
     */
    @Column(length = 50)
    private String plate;
    /**
     * 车辆牌照
     */
    @Column(length = 50)
    private String licensePlate;
    /**
     * 销售商
     */
    @Column(length = 200)
    private String seller;
    /**
     * 行驶里程
     */
    @Column(length = 20)
    private String mileage;
    /**
     * 里程
     */
    @Column(length = 20)
    private String vmt;
    /**
     * 车主姓名
     */
    @Column(length = 100)
    private String ownerName;
    /**
     * 性别
     */
    @Column(length = 5)
    private String sex;
    /**
     * 详细地址
     */
    @Column(length = 200)
    private String address;
    /**
     * 固定电话
     */
    @Column(length = 50)
    private String phone;
    /**
     * 手机
     */
    @Column(length = 50)
    private String mobile;
    /**
     * 电子邮件
     */
    @Column(length = 50)
    private String email;
    /**
     * 邮政编码
     */
    @Column(length = 10)
    private String postcode;
    /**
     * 服务站名称
     */
    @Column(length = 50)
    private String dealerName;
    /**
     * 服务站地址
     */
    @Column(length = 200)
    private String dealerAddress;
    /**
     * 服务经理名称
     */
    @Column(length = 50)
    private String serviceManager;
    /**
     * 车型类别代码
     */
    @Column(length = 200, nullable = false)
    private String typeCode;
    /**
     * 车型类别名称
     */
    @Column(length = 200, nullable = false)
    private String typeName;

//    /**
//     * 省份
//     */
//    @ManyToOne(cascade = CascadeType.REFRESH)
//    @JoinColumn(name = "ProvinceId")
//    private ProvinceEntity province;
//    /**
//     * 城市
//     */
//    @ManyToOne(cascade = CascadeType.REFRESH)
//    @JoinColumn(name = "CityId")
//    private CityEntity city;
//    /**
//     * 县/区
//     */
//    @ManyToOne(cascade = CascadeType.REFRESH)
//    @JoinColumn(name = "CountyId")
//    private CountyEntity county;

    /**
     * 省份
     */
    @Column(length = 32)
    private String provinceId;
    @Column(length = 100)
    private String provinceName;
    /**
     * 城市
     */
    @Column(length = 32)
    private String cityId;
    @Column(length = 100)
    private String cityName;
    /**
     * 县/区
     */
    @Column(length = 32)
    private String countyId;
    @Column(length = 100)
    private String countyName;


}
