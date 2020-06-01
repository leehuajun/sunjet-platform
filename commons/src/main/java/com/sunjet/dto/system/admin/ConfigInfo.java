package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 参数配置
 */
@Data
public class ConfigInfo extends DocInfo implements Serializable {

    private String configKey;  //参数名称
    private String configValue;   //当前值
    private String configDefaultValue;  //配置项默认Value
    private String comment;     //描述


}
