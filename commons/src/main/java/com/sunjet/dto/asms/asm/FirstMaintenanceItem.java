package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wfb on 2017/8/11.
 * <p>
 * 首保服务单表单VO
 */
@Data
public class FirstMaintenanceItem extends FlowDocInfo implements Serializable {

    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String vin;
    private String serviceManager;//服务经理
    private String sender;         // 送修人
    private String plate;          //车牌号

    private Date startDate = DateHelper.getFirstOfYear();//开始时间
    private Date endDate = new Date();//结束时间

}
