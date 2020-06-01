package com.sunjet.backend.modules.asms.entity.settlement;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 配件费用结算单合作库
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmAgencySettlementDocs")
public class AgencySettlementEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714142924946835458L;
    @Column(length = 200)
    private String operator;        // 经办人
    @Column(length = 200)
    private String operatorPhone;   // 联系电话
    @Column(length = 200)
    private String serviceManager;  // 服务经理
    @Column(length = 200)
    private String agencyCode;      // 经销商编号　
    @Column(length = 200)
    private String agencyName;      // 经销商名称
    @Column(length = 200)
    private String provinceName;    // 省份
    private Date startDate;        // 开始日期
    private Date endDate;        // 截至时间
    private Date requestDate;       // 申请时间  日历控件，选择项，默认当前时间，可改
    private Double partExpense;      // 配件费用
    private Double freightExpense;      // 运费
    private Double rewardExpense;      // 奖励费用
    private Double punishmentExpense;      // 惩罚费用
    private Double otherExpense;      // 其他费用
    private Double expenseTotal;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用

    private Boolean canEditAssess = false;  // 是否允许编辑考核内容

    @Column(length = 50)
    private Double tax;   // 税额

    @Column(length = 50)
    private Double nonTaxAmount;   // 未税金额

    //@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    //@JoinColumn(name = "AgencySettlementId")
    //private List<PartExpenseListEntity> items= new ArrayList<>();     // 费用列表

}
