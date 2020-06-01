package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 公告
 */
@Data
public class VehiclePlatformInfo extends DocInfo implements Serializable {
    private String name;
}
