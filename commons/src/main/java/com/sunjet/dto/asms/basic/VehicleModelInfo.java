package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

/**
 * @author: lhj
 * @create: 2017-10-24 12:04
 * @description: 说明
 */
@Data
public class VehicleModelInfo extends DocInfo {

    private static final long serialVersionUID = -6092133173560904203L;
    /**
     * 车型代码
     */
    private String modelCode;
    /**
     * 车型类别代码
     */
    private String typeCode;
    /**
     * 车型类别名称
     */
    private String typeName;
}