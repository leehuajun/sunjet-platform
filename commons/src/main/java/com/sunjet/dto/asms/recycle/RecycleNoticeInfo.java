package com.sunjet.dto.asms.recycle;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * <p>
 * 故障件返回通知单表单VO
 **/
@Data
public class RecycleNoticeInfo extends FlowDocInfo implements Serializable {


    //private String docType;  //  单据类型
    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String srcDocID;        // 来源对应单据ID
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private String comment; //备注
    private Date requestDate = new Date();        //提交时间
    private Date returnDate = DateHelper.nextMonthTenthDate();         // 返回日期  要求默认下个月10号

    private List<RecycleNoticeItemInfo> recycleNoticeItemInfoList = new ArrayList<RecycleNoticeItemInfo>();  // 故障件返回通知单子行


}
