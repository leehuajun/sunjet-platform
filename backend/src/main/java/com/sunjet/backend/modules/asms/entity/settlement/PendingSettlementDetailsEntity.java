package com.sunjet.backend.modules.asms.entity.settlement;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 待结算列表（配件／服务／运费／）
 * Created by zyh on 16/11/14.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmPendingSettlementDetails")
public class PendingSettlementDetailsEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714141824946835458L;
    private String srcDocType;    // 单据类型：三包服务单、首保服务单、服务活动单、故障件运输单，供货单
    private String srcDocID;       // 对应单据ID
    private String srcDocNo;      //单据编号
    private String dealerCode;      // 服务站编号　
    private String dealerName;      // 服务站名称
    private String secondDealerCode;      // 二级服务站编号　
    private String secondDealerName;      // 二级服务站名称
    private String agencyCode;      // 经销商编号　
    private String agencyName;      // 经销商名称
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话

    private Date businessDate = new Date();   // 单据时间

    private Double workingExpense = 0.0;   // 工时费用

    private Double partExpense = 0.0;      // 配件费用

    private Double freightExpense = 0.0;    // 运输费用

    private Double outExpense = 0.00;      // 外出费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;    // 费用合计


    private String settlementDocType;    // 结算单据类型： 服务站结算单、配件结算单
    private String settlementDocID;       // 结算单ID
    private String settlementDocNo;      //结算单编号

    private String typeCode;             //车辆类型
    private String vin;                  // 车辆vin

//    private boolean settlement = false;   //是否结算
//    private Integer settlementStatus = 1000;       // 服务单结算状态:1000：待结算    1001：正在结算   1002：已结算
}
