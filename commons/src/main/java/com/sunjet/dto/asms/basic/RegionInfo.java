package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by hhn on 2017/7/14.
 * 地区
 */
@Data
public class RegionInfo<T> extends DocInfo implements Serializable {

    private String code;
    /**
     * 名称
     */
    private String name;

}
