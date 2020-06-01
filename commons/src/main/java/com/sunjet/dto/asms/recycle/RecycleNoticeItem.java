package com.sunjet.dto.asms.recycle;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 故障件返回通知单列表VO
 */
@Data
public class RecycleNoticeItem extends FlowDocInfo implements Serializable {

    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、

    private String srcDocNo;        //单据编号

    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出

    private String dealerName;      // 服务站名称

    private String vin;   // 车辆vin
    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期


}
