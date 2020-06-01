package com.sunjet.dto.system.base;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by SUNJET_WS on 2017/7/13.
 * 主键info
 */
public class IdInfo {
    @Getter
    @Setter
    private String objId;   // objID

    @Getter
    @Setter
    private Boolean enabled = false;   //是否启用
}
