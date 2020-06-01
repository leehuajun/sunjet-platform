package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 配件需求列表VO
 */
@Data
public class CommissionPartItem extends DocInfo implements Serializable {

    private String docNo;    //单据编号

    private String partCode;    // 配件图号

    private String partName;  // 配件名称

    private String vmt;      //行驶里程

    private String pattern;   //故障模式

    private Double price;     // 单价

    private Integer amount;     // 数量

    private String agencyName;   // 供货方

    private String warrantyMaintenance;

    private String activityMaintenanceId;


}


