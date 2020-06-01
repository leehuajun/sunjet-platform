package com.sunjet.dto.asms.supply;

import com.sunjet.dto.system.base.DocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 调拨分配单清单VO(对应SupplyNoticeItemInfo实体)
 */
@Data
public class SupplyAllocationItem extends DocInfo implements Serializable {

    private String objId;
    private String partId;      //配件id
    private String partCode; //零件件号
    private String partName; //零件名称
    private String unit; // 配件单位
    private Double price = 0.0;     // 单价
    private String docType;   // 单据类型
    private String docNo;    //单据编号
    private String warrantyTime; // 三包时间
    private String warrantyMileage; // 三包里程
    private double requestAmount; //需求数量
    private double surplusAmount; //可分配数量
    private double distributionAmount; //本次分配数量
    private double sentAmount;          // 已供货数量
    private Date arrivalTime; //要求到货时间
    private String srcDocNo;  //来源单据编号
    private String srcDocID;     // 来源对应单据ID
    private Boolean secondaryDistribution; //二次分配
    private String agencyCode;      // 经销商编号　
    private String agencyName; //经销商  合作商名称
    private String pattern;             // 故障模式
    private String comment; // 备注comment
    private String allocatedStatus;   //分配状态
    private String commissionPartId;   //三包维修配件id
    private String supplyNoticeId;      //调拨通知单Id

    private SupplyNoticeItem supplyNotice;//调拨通知单

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

}
