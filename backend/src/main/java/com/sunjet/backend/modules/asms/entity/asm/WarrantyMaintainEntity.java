package com.sunjet.backend.modules.asms.entity.asm;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 维修项目
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmWarrantyMaintains")
public class WarrantyMaintainEntity extends DocEntity {
    private static final long serialVersionUID = -4637484257477614557L;
    @Column(length = 50)
    private String code;      // 维修项目编号
    @Column(length = 50)
    private String name;      // 维修项目名称
    @Column(length = 200)
    private String measure;   // 维修措施
    @Column(length = 50)
    private String vehicleModelName;   //车型平台
    @Column(length = 50)
    private String vehicleSystemName;   //车辆系统
    @Column(length = 50)
    private String vehicleSubSystemName;    //子系统
    @Column(length = 20)
    private String type;      // 项目类型

    private Double workTime = 0.0;    // 工时定额

    private Double hourPrice = 0.0;   // 工时单价

    private Double total = 0.0;       // 工时费用
    @Column(length = 500)
    private String comment;           // 备注

    private String warrantyMaintenance;

}
