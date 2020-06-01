package com.sunjet.backend.modules.asms.entity.settlement;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 配件结算单子行
 * Created by lhj on 16/9/15.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmPartExpenseList")
public class PartExpenseListEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    @Column(length = 200)
    private String srcDocType;    // 单据类型：三包服务单、首保服务单、服务活动单、故障件运输单
    @Column(length = 200)
    private String srcDocID;     // 对应单据ID
    @Column(length = 200)
    private String srcDocNo;//单据编号

    private Double workingExpense = 0.0;   // 工时费用

    private Double partExpense = 0.0;      // 配件费用

    private Double freightExpense = 0.0;    // 运输费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;    // 费用合计
    @Column(length = 200)
    private String comment;     // 备注

    private String agencySettlementId;      //配件费用结算id
    //@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    //@JoinColumn(name = "AgencySettlementId")
    //private AgencySettlementEntity agencySettlement;

}

