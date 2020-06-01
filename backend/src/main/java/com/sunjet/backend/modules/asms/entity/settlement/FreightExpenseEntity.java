package com.sunjet.backend.modules.asms.entity.settlement;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 故障件运费结算单子行
 * Created by lhj on 16/9/15.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmFreightExpense")
public class FreightExpenseEntity extends DocEntity {
    private static final long serialVersionUID = 8634460924832022123L;
    @Column(length = 200)
    private String srcDocType;    // 单据类型：故障件运输单
    @Column(length = 200)
    private String srcDocID;     // 对应单据ID
    @Column(length = 200)
    private String srcDocNo;//单据编号

    private Double freightExpense = 0.0;    // 运输费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;    // 费用合计
    @Column(length = 200)
    private String comment;     // 备注

    private String freightSettlementId;     //运费结算单id
    //@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    //@JoinColumn(name = "FreightSettlementId")
    //private FreightSettlementEntity freightSettlement;

}

