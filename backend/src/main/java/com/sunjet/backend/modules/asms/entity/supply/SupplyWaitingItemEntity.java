package com.sunjet.backend.modules.asms.entity.supply;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 待发货清单
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmSupplyWaitingItems")
public class SupplyWaitingItemEntity extends DocEntity {
    private static final long serialVersionUID = 3390052374653452540L;
    @Column(length = 50)
    private String agencyCode;      // 经销商编号　
    @Column(length = 100)
    private String agencyName;      // 经销商名称
    //    private PartEntity part;        // 零件
    @Column(length = 100)
    private String dealerCode;      // 服务站编号
    @Column(length = 100)
    private String dealerName;      // 服务站名称
    @Column(length = 50)
    private String serviceManager;  // 服务经理
    @Column(length = 50)
    private String partCode;        // 零件号
    @Column(length = 200)
    private String partName;        // 零件名称
    private double requestAmount;       // 需求数量
    private double sentAmount;          // 已供货数量
    private double surplusAmount;    //未供货数量
    private Date arrivalTime;     // 到货时间
    @Column(length = 200)
    private String comment;         // 备注
    @Column(length = 32)
    private String supplyNoticeItemId;//调拨分配
    @Column(length = 32)
    private String supplyDisItemId;//二次分配
    //@ManyToOne
    //@JoinColumn(name = "SupplyNoticeItemId")
    //private SupplyNoticeItemEntity supplyNoticeItem;    // 调拨需求单 子行对象
    //@ManyToOne
    //@JoinColumn(name = "SupplyDisItemId")
    //private SupplyDisItemEntity supplyDisItem;          // 二次分配对象

}

