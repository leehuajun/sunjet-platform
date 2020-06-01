package com.sunjet.dto.asms.basic;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 县区信息实体
 */
@Data
public class CountyInfo extends RegionInfo implements Serializable {
    /**
     * 所属城市
     */
    private String cityId;
}
