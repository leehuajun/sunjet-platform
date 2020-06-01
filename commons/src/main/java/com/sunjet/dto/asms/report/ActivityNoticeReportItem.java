package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * 活动通知单报表
 * <p>
 * Created by SUNJET_WS on 2017/10/13.
 */
@Data
public class ActivityNoticeReportItem {

    private String objId;   // 主键
    private String vin;   // 车辆vin
    private String vsn;   // 车辆vsn
    private String vehicleModel;   // 车辆vsn
    private String ownerName;   // 用户
    private String mobile;       //手机
    private String address;     //地址
    private String purchaseDate;   //购买日期
    private String mileage;    //车辆行驶里程
    private Boolean distribute;   //分配状态
    private String docNo;      //单据编号
    private Date startDate = DateHelper.getFirstOfYear();   //开始日期
    private Date endDate = DateHelper.getEndDateTime();    // 结束日期
    private Integer status;  //状态
    private String title;    //标题
    private Date createdTime;   //发布时间


}
