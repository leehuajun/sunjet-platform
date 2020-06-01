package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商结算明细视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT obj_id, `status`, agency_name, agency_code, created_time, doc_no, part_expense, freight_expense, expense_total FROM asm_agency_settlement_docs WHERE `status`<>-3")
public class AgencySettlementDetailView {
    @Id
    private String objId;   // 主键
    private String status;   // 单据状态
    private String agencyName;   // 合作商名称
    private String agencyCode;   // 合作商名称
    private String createdTime;   // 创建时间
    private String docNo;   // 单据编号
    private String partExpense;   // 配件费用
    private String freightExpense;   // 运费费用
    private String expenseTotal;   // 费用总计


}
