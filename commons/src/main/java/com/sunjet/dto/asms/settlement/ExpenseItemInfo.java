package com.sunjet.dto.asms.settlement;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 结算费用列表
 */
@Data
public class ExpenseItemInfo extends DocInfo implements Serializable {

    private Integer rowNum;     // 行号
    private String srcDocType;    // 单据类型：三包服务单、首保服务单、服务活动单、故障件运输单
    private String srcDocID;     // 对应单据ID
    private String srcDocNo;//单据编号


    private Date businessDate = new Date();   // 单据时间

    private Double workingExpense = 0.0;   // 工时费用

    private Double outExpense = 0.0;      // 外出费用

    private Double partExpense = 0.0;      // 配件费用

    private Double freightExpense = 0.0;    // 运输费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;    // 费用合计

    private String comment;     // 备注

    private String dealerSettlementId; //三包费用结算单id

    private String typeCode;             //车辆类型
    private String vin;                  // 车辆vin

    private DealerSettlementInfo dealerSettlementInfo;
}
