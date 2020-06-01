package com.sunjet.dto.asms.asm;

import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 报表配件
 */
@Data
public class ReportPartInfo extends DocInfo implements Serializable {

    //private String partCode;        //配件编号
    //private String partName;        //配件名称
    //private String unit;            //计量单位
    //private Double price = 0.0;       //价格
    //private Double money;               //金额
    //private String warrantyTime;        // 三包时间
    //private String warrantyMileage;     // 三包里程

    private PartInfo part;      // 配件实体

    private String part_id;     // 配件id

    //private Integer rowNum;         // 行号
    private Integer amount = 1;     // 数量d
    private String fault;           // 故障模式
    private String comment;         // 备注

    private String qrId; //质量速报id

    private String crId;    //费用速报id

}
