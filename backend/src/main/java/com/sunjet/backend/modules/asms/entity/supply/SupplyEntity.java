package com.sunjet.backend.modules.asms.entity.supply;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 调拨供货单
 */
@Data
@Entity
@Table(name = "AsmSupplyDocs")
public class SupplyEntity extends FlowDocEntity {
    private static final long serialVersionUID = 3746737280166924832L;
    @Column(length = 50)
    private String operator;            // 经办人
    @Column(length = 50)
    private String operatorPhone;       // 联系电话
    @Column(length = 50)
    private String agencyCode;          // 经销商编号　
    @Column(length = 200)
    private String agencyName;          // 经销商名称
    @Column(length = 200)
    private String agencyAddress;       //经销商地址
    @Column(length = 100)
    private String agencyPhone;         //经销商电话

    private Date supplyDate = new Date();            // 供应时间
    @Column(length = 50)
    private String dealerCode;          // 服务站编号
    @Column(length = 200)
    private String dealerName;          // 服务站名称
    @Column(length = 50)
    private String receive;             //收货人
    @Column(length = 200)
    private String dealerAdderss;       // 服务站收货地址
    @Column(length = 100)
    private String transportmodel;      // 运输方式
    private Date arrivalTime;     //到货时间
    @Column(length = 200)
    private String logistics;       //物流名称
    @Column(length = 50)
    private String logisticsNum;    //物流单号
    @Column(length = 200)
    private String logisticsfile;       //物流附件
    @Column(length = 200)
    private String logisticsfilename;   //物流附件

    private Double partExpense = 0.0;  //配件费用

    private Double transportExpense = 0.0;        // 运输费用

    private Double otherExpense = 0.0;            // 其他费用

    private Double expenseTotal = 0.0;
    ;         // 费用合计
    private Boolean received;   //是否收货
    private Date rcvDate;       //收货时间
    @Column(length = 200)
    private String comment;     // 备注


    //@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    //@JoinColumn(name = "SupplyId")
    //private List<SupplyItemEntity> items= new ArrayList<>();     // 物料列表


    //private Boolean settlement=false;//是否结算
}
