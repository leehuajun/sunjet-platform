package com.sunjet.dto.asms.settlement;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 配件费用列表
 * Created by SUNJET_WS on 2017/7/17.
 */
@Data
public class PartExpenseItemsInfo extends DocInfo implements Serializable {

    private Integer rowNum;     // 行号
    private String srcDocType;    // 单据类型：三包服务单、首保服务单、服务活动单、故障件运输单
    private String srcDocID;     // 对应单据ID
    private String srcDocNo;//单据编号

    private String agencyCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出

    private Date startDate;         // 开始日期
    private Date endDate;           // 截至时间

    private Double workingExpense = 0.0;   // 工时费用

    private Double partExpense = 0.0;      // 配件费用

    private Double freightExpense = 0.0;    // 运输费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;    // 费用合计

    private String comment;     // 备注

    private String agencySettlementId;      //配件费用结算id

    private AgencySettlementInfo agencySettlementInfo;


}
