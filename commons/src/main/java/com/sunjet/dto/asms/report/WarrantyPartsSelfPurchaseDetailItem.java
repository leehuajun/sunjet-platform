package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 三包配件自购明细
 */
@Data
public class WarrantyPartsSelfPurchaseDetailItem {

    private String objId;   // 主键
    private String status;  //状态
    private String partCode;  //配件号
    private String partName;   //配件名称
    private String partType;   //配件类型
    private String pattern;   //故障模式
    private String reason;   //换件原因
    private String amount;   //数量
    private String price;   //价格
    private String partSupplyType;    //供货方式
    private String warrantyTime;    //三包时间
    private String warrantyMileage;   //三包里程
    private String docNo;   //服务单号
    private String vin;     //车辆vin
    private String vsn;     //车辆vsn
    private String seller;      //销售商
    private String vehicleModel;    //车辆型号
    private Date purchaseDate;    //购买日期
    private String mileage; //行驶里程
    private String engineNo;    //发动机号码
    private String plate;   //车牌号
    private String ownerName;   //车主姓名
    private String mobile;      //车主电话
    private String comment;     //备注
    private String partClassify;    //配件分类
    private String recycle;     //是否返回
    private String docType;     //单据类型
    private String dealerName;      //服务站名称
    private String dealerCode;      //服务站名称
    private String submitterName;       //服务站联系人
    private String dealerPhone;     //服务站电话
    private String serviceManager;      //服务经理
    private String qualityReportDocNo;  //质量速报
    private String expenseReportDocNo;  //费用速报
    private String provinceName;    //省份
    private Date createdTime;     //提交时间
    private Date pullInDate;      //进站时间
    private String dealerStar;      //服务站星级
    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期

}
