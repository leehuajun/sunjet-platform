package com.sunjet.dto.asms.supply;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 调拨供货单
 */
@Data
public class SupplyItem implements Serializable {

    private String objId;   // objID
    private String docNo;      //单据编号
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date supplyDate;  // 发货时间
    private String agencyCode;        // 经销商编码
    private String agencyName;        // 经销商
    private String transportmodel;    // 运输方式
    private String logisticsNum;  //物流单号
    private Boolean received;   //是否收货
    //private String srcDocNo;    //来源单据
    //private String activityNoticeDocNo;    //活动通知单编号
    private String processInstanceId;   // 流程实例Id
    private Integer status = 0;         // 表单状态

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

}
