package com.sunjet.dto.asms.activity;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 活动车辆VO
 */
@Data
public class ActivityVehicleInfo extends DocInfo implements Serializable {

    private String mileage;            // 行驶里程
    private Date repairDate;            // 维修日期
    private Boolean distribute = false; // 是否已分配，默认为false
    private Boolean repair = false;     // 是否已参加维修，默认为false
    private String vehicleId;
    private String typeCode;    // 车辆型号
    private String activityNoticeId;   //活动通知Id
    private String activityDistributionId;  //活动分配id
    private String activityMaintenanceId; //活动服务单 id

}
