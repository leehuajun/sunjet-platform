package com.sunjet.dto.asms.supply;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 调拨通知单清单VO
 */
@Data
public class SupplyNoticeItem implements Serializable {

    private String objId;
    private String docNo;
    private String srcDocNo;     // 来源单据编号
    private String srcDocID;     // 来源对应单据ID
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String dealerAdderss;   //服务站地址
    private String receive;         //收货人
    private String operatorPhone;    //收货人电话
    private String provinceName;    // 省份
    private String srcDocType;

    private Date createdTime;//申请时间
    private String submitterName;//经办人

    private String processInstanceId;   // 流程实例Id
    private Integer status = 0;         // 表单状态
    private String comment;//备注
    private String serviceManager;   // 服务经理

    /**
     * 单据类型
     */
    private String docType;
    /**
     * 经销商名称
     */
    private String agencyName;
    /**
     * 经销商Id
     */
    private String agencyId;

    private String activityNoticeDocNo;   //活动通知单
    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

}
