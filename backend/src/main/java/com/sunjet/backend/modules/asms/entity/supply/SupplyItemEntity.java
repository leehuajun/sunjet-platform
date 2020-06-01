package com.sunjet.backend.modules.asms.entity.supply;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 供货单物料列表
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmSupplyItems")
public class SupplyItemEntity extends DocEntity {
    private static final long serialVersionUID = 6763932315816003206L;

    private Integer rowNum;         //行号

    @Column(length = 50)
    private String partCode;        //零件号
    @Column(length = 200)
    private String partName;        //零件编号
    private Double price;           //价格
    @Column(length = 50)
    private String logisticsNum;    //物流单号

    private Double amount = 0.0;        //发货数量

    private Double rcvamount = 0.0;     //收货数量

    private Double money = 0.0;         //金额
    @Column(length = 200)
    private String comment;         //备注

    @Column(length = 32)
    private String partId;      //配件id
    //@ManyToOne(cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "PartId")
    //private PartEntity part;        //零件

    @Column(length = 32)
    private String supplyNoticeItemId;
    //@ManyToOne(cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "SupplyNoticeItemId")
    //private SupplyNoticeItemEntity supplyNoticeItem;   // 调拨通知单子行对象

    @Column(length = 32)
    private String supplyId;        //供货单id
    //@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    //@JoinColumn(name = "SupplyId")
    //private SupplyEntity supply;        // 供货单主体


    @Column(length = 32)
    private String supplyWaitingItemId;
    //@ManyToOne(cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "SupplyWaitingItemId")
    //private SupplyWaitingItemEntity supplyWaitingItem;  // 待发货对象

}
