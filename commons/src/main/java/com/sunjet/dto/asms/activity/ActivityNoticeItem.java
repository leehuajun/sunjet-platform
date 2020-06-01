package com.sunjet.dto.asms.activity;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 活动通知VO
 */
@Data
public class ActivityNoticeItem {

    private String objId;// 主键

    private String docNo;// 单据编号

    private String title;// 标题

    private Date publishDate = new Date();// 发布时间  日历控件，选择项，默认当前时间，可改

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期

    private Date endDate = DateHelper.getEndDateTime();           // 结束日期

    private String submitter;// 提交人姓名

    private String submitterName;// 提交人姓名

    private Boolean distribute = false; // 是否已分配，默认为false
    private Boolean repair = false;     // 是否已参加维修，默认为false

    private Integer status;// 表单状态
}
