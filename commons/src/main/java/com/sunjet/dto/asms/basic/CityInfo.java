package com.sunjet.dto.asms.basic;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 城市信息实体
 */
@Data
public class CityInfo extends RegionInfo implements Serializable {
    /**
     * 所属省份
     */
    private String provinceId;
    /**
     * 城市类别  1. 一类城市   2. 二类城市
     */
    private Integer category;
}
