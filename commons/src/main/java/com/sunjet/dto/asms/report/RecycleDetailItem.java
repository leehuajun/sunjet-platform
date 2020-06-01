package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 故障件明细视图
 */
@Data
public class RecycleDetailItem {

    private String objId;   // 主键
    private String status;   // 返回通知单状态
    private String ardStatus;   // 返回故障件单状态
    private String dealerCode;   // 服务站编号
    private String dealerName;   // 服务站名称
    private String docNo;   // 返回通知单编号
    private String srcDocNo;   // 来源编号
    private String ardDocNo;   // 返回故障件单据编号
    private String serviceManager;   // 服务经理
    private String partCode;   // 配件号
    private String partName;   // 配件名
    private String pattern;   // 故障模式
    private String reason;   // 换件原因
    private String amount;   // 需返回数量
    private String backAmount;   // 已返回数量
    private String waitAmount;   // 实际返回数量
    private String partSupplyType;   // 供货方式
    private String warrantyTime;   // 三包里程
    private String warrantyMileage;   // 三包时间
    private Date returnDate;   // 应返时间
    private Date arriveDate;   // 实返时间
    private String logisticsNum;   // 物流单号
    private String logistics;   // 物流公司
    private Date createdTime;   // 通知单创建时间
    private Date ardCreateTime;   // 故障件返回单创建时间


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期


}
