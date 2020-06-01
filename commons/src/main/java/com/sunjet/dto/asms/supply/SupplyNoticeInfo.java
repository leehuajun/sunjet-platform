package com.sunjet.dto.asms.supply;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 调拨通知单VO
 */
@Data
public class SupplyNoticeInfo extends FlowDocInfo implements Serializable {
    private String docType;  //  单据类型
    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String srcDocID;        // 来源对应单据ID
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private String serviceManager;   // 服务经理
    private String receive;         //收货人
    private String operatorPhone;   //收货人联系电
    private String dealerAdderss;       // 服务站收货地址

    private String qualityReportDocNo;  // 质量速报单号
    private String expenseReportDocNo; // 费用速报单号

    private String comment;  //备注

    //车辆信息
    private String vin;   // vin
    private String vsn;   // vsn
    private String seller;  // 经销商
    private String vehicleModel;  //车辆型号
    private Date manufactureDate;  //生产日期
    private Date purchaseDate;  //购买日期
    private String engineModel;  //发动机型号
    private Date productDate;  //出厂日期
    private String mileage;  //行驶里程
    private String plate;  //车牌号
    private String engineNo;  //发动机/电动机号

    private List<SupplyNoticeItemInfo> supplyNoticeItemInfos = new ArrayList<SupplyNoticeItemInfo>();  // 调拨通知单号子行

    /**
     * 经销商名称
     */
    private String agencyName;
    /**
     * 经销商名称
     */
    private String agencyCode;
    /**
     * 经销商Id
     */
    private String agencyId;


}
