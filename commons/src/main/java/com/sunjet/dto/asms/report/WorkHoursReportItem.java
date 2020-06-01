package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 工时视图
 */
@Data
public class WorkHoursReportItem {

    private String objId;   // 主键
    private String code;   // 项目编号
    private String name;   // 项目名称
    private String workTime;   // 工时定额
    private String measure;   // 维修措施
    private String nightExpense;   // 夜间工时补贴费用
    private Boolean nightWork;   // 是否夜间作业
    private String provinceName;   // 省份
    private String dealerCode;   // 服务站编号
    private String dealerName;   // 服务站名称
    private String docNo;   // 服务单号
    private String vin;   // VIN
    private String vehicleModel;   // 车型
    private String engineModel;   // 发动机型号
    private String engineNo;   // 发动机号
    private Date createdTime;   // 创建时间


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期

}
