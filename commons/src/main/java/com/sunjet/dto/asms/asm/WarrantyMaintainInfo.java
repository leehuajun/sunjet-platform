package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 维修项目列表VO
 */
@Data
public class WarrantyMaintainInfo extends DocInfo implements Serializable {

    private String code;      // 维修项目编号
    private String name;      // 维修项目名称
    private String measure;   // 维修措施
    private String vehicleModelName;
    private String vehicleSystemName;
    private String vehicleSubSystemName;
    private String type;      // 项目类型
    private Double workTime = 0.0;    // 工时定额
    private Double hourPrice = 0.0;   // 工时单价
    private Double total = 0.0;       // 工时费用
    private String comment;           // 备注

    private String warrantyMaintenance; // 三包单id


}
