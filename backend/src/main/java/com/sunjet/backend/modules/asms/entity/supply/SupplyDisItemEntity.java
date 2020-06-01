package com.sunjet.backend.modules.asms.entity.supply;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 二次分配列表
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmSupplyDisItems")
public class SupplyDisItemEntity extends DocEntity {
    private static final long serialVersionUID = 3390052374653452540L;
    @Column(length = 50)
    private String agencyCode;      // 合作商编号　
    @Column(length = 100)
    private String agencyName;      // 合作商
    // private PartEntity part;        // 零件
    @Column(length = 50)
    private String partCode;        // 零件号
    @Column(length = 200)
    private String partName;        // 零件名称
    private double requestAmount;       // 需求数量
    private double sentAmount;          // 已供货数量
    private double surplusAmount;    //未供货数量

    private double distributionAmount = 0;    //本次分配数量
    private Date arrivalTime;     // 到货时间
    @Column(length = 200)
    private String comment;         // 备注

    private String supplyNoticeItemId;   //调拨通知单子行
    //@ManyToOne
    //@JoinColumn(name="SupplyNoticeItemId")
    //private SupplyNoticeItemEntity supplyNoticeItem;                // 调拨需求单 子行对象
}

