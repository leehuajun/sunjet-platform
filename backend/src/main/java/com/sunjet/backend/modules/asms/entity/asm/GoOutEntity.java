package com.sunjet.backend.modules.asms.entity.asm;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务委托单出外子行
 * Created by lhj on 16/9/15.
 */
@Data
@Entity
@Table(name = "AsmGoOuts")
public class GoOutEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    @Column(length = 200)
    private String place;    // 外出地点

    private Double mileage = 0.0;     // 单向里程

    private Double tranCosts = 0.0;     // 交通费用

    private Double trailerMileage = 0.0;     // 拖车里程

    private Double trailerCost = 0.0;     // 拖车费用

    private int outGoNum = 0;    // 外出人数

    private Double outGoDay = 0.0;    // 外出天数

    private Double personnelSubsidy = 0.0;    // 人员补贴

    private Double nightSubsidy = 0.0;    // 住宿补贴

    private Double timeSubsidy = 0.0;    // 外出工时费用

    private Double goOutSubsidy = 0.0;    // 外出补贴费用

    private Double amountCost = 0.0;      // 行汇总金额
    @Column(length = 200)
    private String outGoPicture;    //外出凭证照片
    @Column(length = 200)
    private String comment;     // 备注

    private String warrantyMaintenance;   //三包单Id

    private String firstMaintenanceId;//首保服务单Id

    private String activityMaintenanceId; //活动服务单id
}
