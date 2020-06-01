package com.sunjet.backend.modules.asms.entity.settlement.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 待结算列表（配件／服务／运费／）
 */
@Data
@Entity
@Immutable
@Subselect("select obj_id,src_doc_type,src_doc_no,dealer_code,dealer_name,agency_code,agency_name,operator,operator_phone,business_date,expense_total,settlement_doc_no,status,created_time,settlement_doc_type,type_code,vin from asm_pending_settlement_details")
public class PendingSettlementDetailsViews {
    @Id
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
    private Integer status = 1000;       // 服务单结算状态:1000：待结算    1001：正在结算   1002：已结算
    private Date createdTime;

    private String typeCode;    // 车辆类型
    private String vin;         // 车辆Vin

    private String settlementDocType;    // 结算单据类型： 服务站结算单、配件结算单

}
