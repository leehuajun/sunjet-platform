package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 服务站结算明细
 */
@Data
public class SettlementMaintainExpenseItem {

    private String objId;   // 主键

    private String dealerCode;  //服务站编号

    private String dealerName;  //服务站名称

    private String docNo;   //结算编号

    private String warrantyExpenseTotal;  //三包费用总计

    private String firstExpenseTotal;  //首保费用总计

    private String activityExpenseTotal;  //活动费用总计

    private String freightExpenseTotal;  //故障件运费用总计


    private String rewardPunishmentExpense;  //奖惩费用总计

    private String expenseTotal;  //费用总计


    private Date createdTime;  //创建时间


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期


}
