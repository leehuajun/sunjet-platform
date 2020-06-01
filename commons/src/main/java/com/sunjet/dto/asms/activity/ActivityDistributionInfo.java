package com.sunjet.dto.asms.activity;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 活动分配单表单VO
 */
@Data
public class ActivityDistributionInfo extends FlowDocInfo implements Serializable {


    private String docType;  //  单据类型
    private String activityNoticeId;  //活动通知单编号ID
    private String activityNoticeDocNo; //活动通知单编号
    private String dealer;    // 服务站
    private String provinceName;       // 省份
    private String dealerCode;      // 服务站编号
    private String dealerName;      // 服务站名称
    private String serviceManager;  // 服务经理
    private String comment;          //备注
    private Boolean canEditSupply = true;      // 是否允许编辑生成调拨通知单,流程提交后，变为false，不允许再生成调拨申请
    private String supplyNoticeId;          // 供货通知单Id(调拨单)
    private ActivityNoticeInfo activityNotice; //服务活动通知单
    private Boolean repair = false;     // 是否已参加维修，默认为false
    private List<ActivityVehicleItem> activityVehicleItems = new ArrayList<>(); // 活动车辆
    private List<ActivityPartItem> activityPartItems = new ArrayList<>(); // 活动配件子行


}
