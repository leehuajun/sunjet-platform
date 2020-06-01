package com.sunjet.backend.modules.asms.entity.asm;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务委托单配件子行
 * Created by lhj on 16/9/15.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmCommissionParts")
public class CommissionPartEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    //    private PartEntity part = new PartEntity();    // 零件
    @Column(length = 20)
    private String warrantyTime;        // 三包时间 50
    @Column(length = 20)
    private String warrantyMileage;     // 三包里程 50
    @Column(length = 50)
    private String partCode;
    @Column(length = 200)
    private String partName;
    @Column(length = 50)
    private String partClassify;      //配件分类
    @Column(length = 20)
    private String unit;            // 计量单位
    @Column(length = 20)
    private String partSupplyType;  // 供货方式，单选项：调拨、储备、自购
    @Column(length = 20)
    private String partType;        //零件类型  单选项：配件、辅料
    @Column(length = 200)
    private String pattern;         // 故障模式
    @Column(length = 200)
    private String reason;          // 换件原因

    private Double price = 0.0;     // 单价

    private Integer amount = 0;     // 数量

    private Double total = 0.0;     // 合计

    private Double settlementTotal = 0.0;   // 结算合计
    @Column(length = 200)
    private String chargeMode;    // 收费方式
    @Column(length = 200)
    private String comment;     // 备注

    private Boolean recycle = false;    // 是否回收

    private String warrantyMaintenance;  // 三包服务单id

    private String activityMaintenanceId;  //活动服务单id

    private String activityPartId; // 活动配件Id

}
