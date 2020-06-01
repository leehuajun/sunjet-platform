package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 三包服务单列表VO
 */
@Data
public class WarrantyMaintenanceItem extends FlowDocInfo implements Serializable {

    private String dealerCode;  // 服务站编号
    private String dealerName;   // 服务站名称
    private String currentNode;    // 当前节点
    private String serviceManager;   //服务经理
    private String vehicleId;      // 车辆id
    private String sender;         // 送修人
    private String plate;          //车牌号
    private String vin;             //vin

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期


}
