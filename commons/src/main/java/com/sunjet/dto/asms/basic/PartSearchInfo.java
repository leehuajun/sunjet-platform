package com.sunjet.dto.asms.basic;

import lombok.Data;

import java.util.Date;

/**
 * @Author: wushi
 * @description: 配件目录查询
 * @Date: Created in 16:00 2018/4/8
 * @Modify by: wushi
 * @ModifyDate by: 16:00 2018/4/8
 */
@Data
public class PartSearchInfo {
    /**
     * 对象id
     */
    private String objId;
    /**
     * 配件名称
     */
    private String partName = "";
    /**
     * 配件代号
     */
    private String partCode = "";
    /**
     * 车型
     */
    private String vehicleModel = "";
    /**
     * 生效日期
     */
    private Date effectiveDate;
    /**
     * 失效日期
     */
    private Date expirationDate;
    /**
     * 配件类型
     */
    private String partType;
    /**
     * 配件分类-平台名称
     */
    private String partCategoryPlatformName = "";
    /**
     * 配件分类-平台代号
     */
    private String partCategoryPlatformCode = "";
    /**
     * 配件分类-平台分类
     */
    private String partCategoryPlatformCategory;

    /**
     * 平台名称
     */
    private String platformName;
    /**
     * 平台代号
     */
    private String platformCode;
    /**
     * 三包时间
     */
    private String warrantyTime;
    /**
     * 三包里程
     */
    private String warrantyMileage;
    /**
     * vin码
     */
    private String vin;
    /**
     * vsn码
     */
    private String vsn;
    /**
     * 生产时间
     */
    private Date productionDate = new Date();


}
