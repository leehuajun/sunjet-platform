package com.sunjet.dto.asms.settlement;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/17.
 * 待返回结算单
 */
@Data
public class PendingSettlementDetailItems implements Serializable {

    private String objId;
    private String srcDocType;    // 单据类型：三包服务单、首保服务单、服务活动单、故障件运输单，供货单
    private String srcDocNo;      //单据编号
    private String dealerCode;      // 服务站编号　
    private String dealerName;      // 服务站名称
    private String agencyCode;      // 经销商编号　
    private String agencyName;      // 经销商名称
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private Date businessDate = new Date();   // 单据时间
    private Double expenseTotal = 0.0;    // 费用合计
    private String settlementDocNo;      //结算单编号
    //private Integer settlementStatus = 1000;       // 服务单结算状态:1000：待结算    1001：正在结算   1002：已结算
    private Integer status = 1000;

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    //    private Date endDate = new Date();           // 结束日期
    private Date endDate = DateHelper.getEndDateTime();           // 结束日期


    private String settlementDocType;    // 结算单据类型： 服务站结算单、配件结算单
    private String typeCode;        // 车辆类型
    private String vin;         // 车辆vin
}
