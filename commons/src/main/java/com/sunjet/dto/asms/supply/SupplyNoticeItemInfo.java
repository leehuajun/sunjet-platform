package com.sunjet.dto.asms.supply;

import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * <p>
 * 调拨通知单子行vo（物料行）
 */
@Data
public class SupplyNoticeItemInfo extends DocInfo implements Serializable {

    private String partId;      //配件id
    private String partCode; //配件号
    private String partName; //配件名称
    private String unit; // 配件单位
    private Double price = 0.0;     // 单价
    private String warrantyTime; // 三包时间
    private String warrantyMileage; // 三包里程
    private Double requestAmount = 1.0;       // 需求数量
    private double sentAmount = 0;        // 已供货数量
    private double surplusAmount = 0;    //未供货数量
    private double distributionAmount = 0;    //本次分配数量
    private Date arrivalTime;  // 要求到货时间
    private String srcDocNo;     // 单据编号
    private String srcDocID;     // 来源对应单据ID
    private Boolean secondaryDistribution = false; //是否二次分配
    private PartInfo part;      //配件对象vo
    private String agencyCode;      // 经销商编号　
    private String agencyName;      // 经销商名称
    private String pattern;             // 故障模式
    private String comment;  // 备注
    private String commissionPartId;   //三包维修配件id
    private String supplyNoticeId;    //调拨通知单id
    private SupplyNoticeInfo supplyNotice;      //调拨通知单VO


}
