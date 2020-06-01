package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 质量费用速报明细视图
 */
@Data
public class QualityExpenseReportDetailItem {

    private String objId;   // 主键
    private String title;   // 标题
    private String type;   // 单据类型
    private String docNo;   // 单据编号
    private String status;   // 单据状态
    private String dealerCode;   // 服务站编号
    private String dealerName;   // 服务站名称
    private String typeName;   // 车辆类型
    private String costType;   // 费用类型
    private String reportType;   // 速报类型
    private String submitterName;   // 提交人
    private String submitterPhone;   // 提交人电话
    private String serviceManager;   // 服务经理
    private String serviceManagerPhone;   // 服务经理电话
    private String comment;   // 备注
    private String faultStatus;   // 故障时行驶状态
    private String faultRoad;   // 故障时路面情况
    private String faultAddress;   // 故障发生地点
    private String faultDesc;   // 故障描述
    private String initialReason;   // 初步原因分析
    private String decisions;   // 处理意见
    private String estimatedCost;   // 预计费用
    private String createdTime;   // 创建时间
    private String vin;   // vin
    private String vehicleModel;   // 车辆型号
    private String engineNo;   //发动机号
    private String ownerName;   //车主
    private String mobile;   //手机号
    private Date purchaseDate;   //车辆购买日期
    private String mileage;   //行驶里程


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期


}
