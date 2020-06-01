package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商供货汇总
 */
@Data
@Entity
@Immutable
@Subselect("SELECT DISTINCT asd.obj_id, asd.doc_no, asd.`status`, asd.created_time, asd.agency_code, asd.agency_name, asd.part_expense, asd.transport_expense, asd.other_expense, asd.expense_total FROM asm_supply_docs AS asd WHERE asd.`status` <> -3 ")
public class AgencySupplySummaryView {

    @Id
    private String objId;   // 主键
    private String status;   // 状态
    private String agencyCode;       //合作商编号
    private String agencyName;   //合作商名字
    private String createdTime;     //提交时间
    private String docNo;     //单据编号
    //private String srcDocNo;     //来源单据编号
    //private String srcDocType;     //来源单据类型
    private String partExpense;     //配件费用
    private String transportExpense;     //运费费用
    private String otherExpense;     //其他费用
    private String expenseTotal;     //费用总计


}
