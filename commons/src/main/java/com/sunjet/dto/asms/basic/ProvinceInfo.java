package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zyf on 2017/7/14.
 * 省份
 */
@Data
public class ProvinceInfo extends RegionInfo implements Serializable {

    private Boolean cold = false;  // 是否严寒省份，默认为：非严寒省份

}
