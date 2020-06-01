package com.sunjet.dto.asms.activity;

import lombok.Data;

import java.io.Serializable;

/**
 * 活动零件
 * Created by zyf on 2017/8/10.
 */
@Data
public class ActivityPartItem implements Serializable {

    private String objId;  // 主键
    private String code;  // 零件件号
    private String name;  // 零件名称
    private String partType;  // 零件类型
    private Double price;  // 价格
    private String unit;   // 单位
    private String warrantyMileage;  // 三包里程
    private String warrantyTime;  // 三包里程
    private String activityNoticeId;  //活动ID
    private String partId; // 零件ID
    private Integer amount;  // 需求数量
}
