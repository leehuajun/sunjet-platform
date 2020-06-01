package com.sunjet.dto.asms.settlement;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 配件费用结算
 */
@Data
public class AgencySettlementInfo extends FlowDocInfo implements Serializable {
    //基本信息
    private Date requestDate;//申请日期
    private String operator;  // 经办人
    private String agencyCode;  // 合作商编号
    private String agencyName;  //合作商名称
    private String provinceName;  //省份
    private String operatorPhone;  //电话


    //费用信息
    private Double partExpense = 0.0; //配件费用
    private Double freightExpense = 0.0; //运输费用
    private Double rewardExpense = 0.0; //奖励费用
    private Double punishmentExpense = 0.0; //惩罚费用
    private Double otherExpense = 0.0; //其他费用
    private Double expenseTotal = 0.0; //总金额

    private Double tax = 0.0;   // 税额
    private Double nonTaxAmount = 0.0;   // 未税金额

    private Boolean canEditAssess = false;  // 是否允许编辑考核内容


    private Date startDate; //单据开始时间
    private Date endDate; //单据结束时间

    List<PartExpenseItemsInfo> partExpenseItemsInfos = new ArrayList<PartExpenseItemsInfo>();  //结算费用列表
}
