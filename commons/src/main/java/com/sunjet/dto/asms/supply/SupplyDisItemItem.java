package com.sunjet.dto.asms.supply;

import com.sunjet.dto.system.base.DocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 二次分配
 */
@Data
public class SupplyDisItemItem extends DocInfo implements Serializable {

    private String objId;

    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private double requestAmount;       // 需求数量
    private double sentAmount;          // 已供货数量
    private double surplusAmount;    //未供货数量
    private Date arrivalTime;     // 到货时间
    private double distributionAmount = 0;    //本次分配数量
    private String agencyCode;      // 合作商编号　
    private String agencyName;      // 合作商
    private String comment;         // 备注
    private String supplyNoticeItemId;
    private String srcDocNo;        // 来源单据编号
    private String docNo;  // 调拨通知单单据编号
    //调拨分配
    private SupplyAllocationItem supplyNoticeItem;
    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

}
