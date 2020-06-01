package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 配件需求列表VO
 */
@Data
public class CommissionPartInfo extends DocInfo implements Serializable {

    private Integer rowNum;     // 行号
    //    private PartEntity part = new PartEntity();    // 零件
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String partCode;
    private String partName;
    private String srcvmt;                //行驶里程
    private String srcdocNo;                //对应三包服务单、活动服务单的单据编号
    private String partClassify;      //配件分类
    private String unit;            // 计量单位
    private String partSupplyType;  // 供货方式，单选项：调拨、储备、自购
    private String partType;        //零件类型  单选项：配件、辅料
    private String pattern;         // 故障模式
    private String reason;          // 换件原因
    private Double price = 0.0;     // 单价
    private Integer amount = 1;     // 数量
    private Double total = 0.0;     // 合计
    private Double settlementTotal = 0.0;   // 结算合计
    private String chargeMode;    // 收费方式
    private String comment;     // 备注
    private Boolean recycle = false;    // 是否回收

    private String warrantyMaintenance;   //  三包单id

    private String activityMaintenanceId;  //活动服务单id

    private String activityPartId;


}


