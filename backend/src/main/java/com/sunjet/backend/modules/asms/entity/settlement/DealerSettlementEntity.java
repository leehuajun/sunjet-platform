package com.sunjet.backend.modules.asms.entity.settlement;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 三包费用结算单
 * Created by lhj on 16/9/17.
 */
@Data
@Entity
@Table(name = "AsmDealerSettlementDocs")
public class DealerSettlementEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714142924946835458L;
    @Column(length = 200)
    private String settlementType;        // 结算类型  服务结算单，返回件结算单
    @Column(length = 200)
    private String operator;        // 经办人
    @Column(length = 200)
    private String operatorPhone;   // 联系电话
    @Column(length = 200)
    private String serviceManager;  // 服务经理
    @Column(length = 200)
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    @Column(length = 200)
    private String dealerName;      // 服务站名称
    @Column(length = 200)
    private String provinceName;    // 省份
    private Date startDate;         // 开始日期
    private Date endDate;           // 截至时间
    private Date requestDate;       // 申请时间  日历控件，选择项，默认当前时间，可改

    private Double partExpense = 0.0;

    private Double recycleExpense = 0.0;          // 故障件运费

    private Double workingExpense = 0.0;          // 工时费用

    private Double outExpense = 0.0;              // 外出费用

    private Double rewardExpense = 0.0;           // 奖励费用

    private Double punishmentExpense = 0.0;       // 惩罚费用

    private Double otherExpense = 0.0;            // 其他费用

    private Double busExpense = 0.0;           // 客车费用

    private Double nonRoadExpense = 0.0;          // 非道路车费用

    private Double refitExpense = 0.0;            // 改装车费用

    private Double expenseTotal = 0.0;            // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用

    private Boolean canEditAssess = false;  // 是否允许编辑考核内容

    @Column(length = 50)
    private Double taxRate = 0.0;                 //税率

    @Column(length = 50)
    private Double tax;   // 税额

    @Column(length = 50)
    private Double nonTaxAmount;   // 未税金额


    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "DealerSettlementId")
    //private List<ExpenseListEntity> items = new ArrayList<>();     // 费用列表


}
