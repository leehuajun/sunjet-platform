package com.sunjet.dto.asms.supply;

import com.sunjet.dto.system.base.DocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 待发发货清单
 */
@Data
public class SupplyWaitingItemItem extends DocInfo implements Serializable {

    private String objId;
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称


    //调拨分配
    private SupplyAllocationItem supplyNoticeItem;//调拨分配
    private SupplyDisItemItem supplyDisItem;          // 二次分配对象

    private String supplyNoticeItemId;//调拨分配
    private String supplyDisItemId;//二次分配

    private String partCode; //零件件号
    private String partName; //零件名称
    private double requestAmount; //需求数量
    private double surplusAmount; //可分配数量
    private double sentAmount;          // 已供货数量
    private Date arrivalTime; //要求到货时间
    private String agencyName; //经销商  合作商名称
    private String agencyCode; //经销商  合作商编号
    private String comment; // 备注

    private double distributionAmount; //本次分配数量
    private String serviceManager;   // 服务经理
    private String srcDocNo; // 来源单据
    private String docNo;  // 调拨通知单

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

}
