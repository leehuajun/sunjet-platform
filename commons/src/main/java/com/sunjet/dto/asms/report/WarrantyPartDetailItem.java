package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/18.
 * 三包配件明细列表视图
 */
@Data
public class WarrantyPartDetailItem {

    private String objId;   // 主键

    private String partCode;             //配件件号
    private String partName;             //配件名称
    private String partType;             // 配件类型
    private String pattern;               //故障模式
    private String reason;                //换件原因
    private String acpAmount;            //数量
    private String price;                 // 单价
    private String partSupplyType;      //供货方式
    private String warrantyTime;        //三包时间
    private String warrantyMileage;     //三包里程
    private String srcDocNo;           //服务单号
    private String vin;                  //VIN
    private String vsn;                   //VSN
    private String seller;                //经销商
    private String vehicleModel;         //车辆型号
    private String purchaseDate;         //购买日期
    private String mileage;               //行驶里程
    private String engineNo;               //发动机号
    private String plate;                   //车牌号
    private String ownerName;              //车主姓名
    private String mobile;                  //电话
    private String awmComment;               //备注
    private String partClassify;           //配件分类
    private String recycle;                 //是否返回旧件
    private String srcDocType;            //单据类型
    private String dealerCode;             //服务站编码
    private String dealerName;             //服务站名称
    private String submitterName;          //服务站联系人
    private String submitterPhone;         //服务站联系电话
    private String serviceManager;         //服务经理
    private String qualityReportDocNo;           //质量速报单号
    private String expenseReportDocNo;          //费用速报单号
    private String provinceName;           //省份
    private String awmCreatedTime;          //申请时间
    private String pullInDate;            //进站时间
    private String dealerStar;             //服务站星级
    private String asnDocNo;              //调拨单号
    ;
    private String asnCreatedTime;         //调拨单申请时间
    private String asncomment;              //备注
    private String agencyName;             //供货单号
    private String docNo;             //供货单号
    private String amount;              //供货数量
    private String money;               //配件费用
    private String arrivalTime;        //应到货时间
    private String rcvDate;            //到货时间
    private String transportmodel;      //发运方式
    private String logisticsNum;        //物流单号
    private String logistics;           //物流公司
    private String asdCreatedTime;       //提交时间
    private String dealerAdderss;          //收货地址
    private String receive;                 //收货人
    private String operatorPhone;            //收货电话
    private String awmStatus;                   //三包单状态
    private String asnStatus;          //调拨通知单状态
    private String asdStatus;                   //供货通知单状态


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期


}
