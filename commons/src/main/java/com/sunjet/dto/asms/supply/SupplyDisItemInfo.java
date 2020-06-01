package com.sunjet.dto.asms.supply;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wfb on 17-7-17.
 * 二次分配
 */
@Data
public class SupplyDisItemInfo extends DocInfo implements Serializable {
    private String objId;
    private String agencyCode;      // 合作商编号　
    private String agencyName;      // 合作商
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private double requestAmount;       // 需求数量
    private double sentAmount;          // 已供货数量
    private double surplusAmount;    //未供货数量
    private double distributionAmount = 0;    //本次分配数量
    private Date arrivalTime;     // 到货时间
    private String comment;         // 备注


    private String supplyNoticeItemId;  //调拨通知单子行objid
    private SupplyAllocationItem supplyNoticeItem;                // 调拨需求单 子行对象

}
