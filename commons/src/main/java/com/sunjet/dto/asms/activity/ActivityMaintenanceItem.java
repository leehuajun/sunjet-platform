package com.sunjet.dto.asms.activity;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 活动服务单列表VO
 */
@Data
public class ActivityMaintenanceItem extends FlowDocInfo implements Serializable {


    private String dealerCode;// 服务站编号
    private String dealerName;// 服务站名称
    private String aadDocNo;// 活动分配单号编号
    private String aanDocNo;// 活动通知单号编号
    private String serviceManager;  // 服务经理
    private String vin;  // 车辆vin
    private String sender;         // 送修人
    private String plate;          //车牌号

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期


}
