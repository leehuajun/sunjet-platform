package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 项目工时定额VO
 */
@Data
public class MaintainInfoExt extends DocInfo implements Serializable {
    private String code;      // 维修项目编号
    private String name;      // 维修项目名称
    private Double workTime;  // 工时定额
    private Boolean claim = true;   // 是否索赔
    private String vehicleModelId;
    private String vehicleModelName;
    private String vehicleSystemId;
    private String vehicleSystemName;
    private String vehicleSubSystemId;
    private String vehicleSubSystemName;
    private String comment; // 备注
    private Integer err;   // 错误属性
}


