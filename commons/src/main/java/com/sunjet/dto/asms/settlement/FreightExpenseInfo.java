package com.sunjet.dto.asms.settlement;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 故障件运费结算明细
 * Created by SUNJET_WS on 2017/7/17.
 */
@Data
public class FreightExpenseInfo extends DocInfo implements Serializable {

    private String srcDocType;    // 单据类型：故障件运输单
    private String srcDocID;     // 对应单据ID
    private String srcDocNo;//单据编号
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出

    private Date startDate;         // 开始日期
    private Date endDate;           // 截至时间

    private Double freightExpense = 0.0;    // 运输费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;    // 费用合计
    private String comment;     // 备注


    private String freightSettlementId;     //运费结算单id
    private FreightSettlementInfo freightSettlementInfo;


}
