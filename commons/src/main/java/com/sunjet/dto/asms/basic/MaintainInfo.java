package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 项目工时定额VO
 */
@Data
public class MaintainInfo extends DocInfo implements Serializable {

    private String code = "";      // 维修项目编号
    private String name = "";      // 维修项目名称
    private Double workTime;  // 工时定额
    private Boolean claim = true;   // 是否索赔
    private String vehicleModelId;
    private String vehicleModelName = "";  //车型
    private String vehicleSystemId;
    private String vehicleSystemName = "";  //车辆系统
    private String vehicleSubSystemId;
    private String vehicleSubSystemName = "";  //车辆子系统
    private String comment; // 备注
}
