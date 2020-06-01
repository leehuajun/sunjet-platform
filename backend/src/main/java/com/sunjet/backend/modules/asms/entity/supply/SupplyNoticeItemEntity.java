package com.sunjet.backend.modules.asms.entity.supply;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 调拨通知单物料行
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmSupplyNoticeItems")
public class SupplyNoticeItemEntity extends DocEntity {
    private static final long serialVersionUID = 3390052374653452540L;
    @Column(length = 50)
    private String agencyCode;      // 经销商编号　
    @Column(length = 100)
    private String agencyName;      // 经销商名称
    @Column(length = 50)
    private String partCode;        // 零件号
    @Column(length = 200)
    private String partName;        // 零件编号
    @Column(length = 200)
    private String warrantyTime;        // 三包时间 50
    @Column(length = 200)
    private String warrantyMileage;     // 三包里程 50
    @Column(length = 200)
    private String pattern;             // 故障模式

    private Double requestAmount = 0.0;       // 需求数量

    private double sentAmount = 0;        // 已供货数量

    private double surplusAmount = 0;    //未供货数量

    private double distributionAmount = 0;    //本次分配数量

    private Boolean secondaryDistribution = false; //是否二次分配
    private Date arrivalTime;  //到货时间
    @Column(length = 200)
    private String comment;     // 备注

    @Column(length = 32)
    private String supplyNoticeId;


    @Column(length = 20)
    private String srcDocNo;     // 单据编号

    @Column(length = 32)
    private String srcDocID;     // 来源对应单据ID


    @Column(length = 32)
    private String commissionPartId;   //维修配件id
    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name="SupplyNoticeId")
    //private SupplyNoticeEntity supplyNotice;

    @Column(length = 32)
    private String partId;
    //@ManyToOne(cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "PartId")
    //private PartEntity part;        // 零件

}
