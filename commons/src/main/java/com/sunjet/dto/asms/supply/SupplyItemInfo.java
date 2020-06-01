package com.sunjet.dto.asms.supply;

import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 调拨供货单子行VO
 */
@Data
public class SupplyItemInfo extends DocInfo implements Serializable {

    private String partCode; //配件号
    private String partName; //配件名称
    private Double price = 0.0;//价格
    private String unit; // 配件单位
    private String warrantyTime; // 三包时间
    private String warrantyMileage; // 三包里程
    private String logisticsNum;    //物流单号
    private Double amount = 0.0;        //发货数量
    private Double rcvamount = 0.0;     //收货数量
    private Double money = 0.0;         //金额
    private String comment;         //备注


    private PartInfo part;
    private String partId;      //配件id

    private String supplyWaitingItemId;   //待发id
    private SupplyWaitingItemItem supplyWaitingItem;  // 待发货对象

    private String supplyNoticeItemId;    //调拨通知单Id
    private SupplyAllocationItem supplyNoticeItem;   // 调拨通知单子行对象

    private String supplyId;      //供货单父表id
    private SupplyInfo supply;        // 供货单主体


}
