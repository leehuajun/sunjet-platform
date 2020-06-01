package com.sunjet.dto.asms.activity;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 活动通知单表单VO
 */
@Data
public class ActivityNoticeInfo extends FlowDocInfo implements Serializable {
    //    private String docType;  //  单据类型
    private String title;               // 标题
    private Date publishDate = new Date();          // 发布时间  日历控件，选择项，默认当前时间，可改
    private String activityType;        // 活动类型
    private String noticeFile;          // 活动文件
    private Date startDate = new Date();            // 开始日期
    private Date endDate = new Date();              // 结束时间
    private String comment;                         // 备注/活动概述
    private Double perLaberCost = 0.0;        // 单台人工成本
    private Double amountLaberCost = 0.0;     // 人工成本合计
    private Double perPartCost = 0.0;         // 单台配件成本
    private Double amountPartCost = 0.0;      // 配件成本合计
    private Double amount = 0.0;              // 总成本合计
    private Boolean distribute = false; // 是否已分配，默认为false
    private Boolean repair = false;     // 是否已参加维修，默认为false
    private List<ActivityVehicleItem> activityVehicleItems = new ArrayList<ActivityVehicleItem>(); //活动车辆子行
    private List<ActivityPartItem> activityPartItems = new ArrayList<ActivityPartItem>(); // 活动配件子行

}
