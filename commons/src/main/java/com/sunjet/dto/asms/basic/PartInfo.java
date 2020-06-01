package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 配件VO
 */
@Data
public class PartInfo extends DocInfo implements Serializable {

    private String code;//物料编号
    private String name;//物料名称
    private Double price = 0.0;//价格
    private String unit;//计量单位
    private String partType = "配件";    // 物料类型  配件/辅料，默认是：配件，
    private String warrantyTime;        // 三包时间
    private String warrantyMileage;     // 三包里程
    private String partClassify;      //配件分类
    private String comment;    // 备注

}
