package com.sunjet.dto.asms.activity;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 活动分配列表VO
 */
@Data
public class ActivityDistributionItem extends FlowDocInfo implements Serializable {

    private String objId;// 主键

    private String docNo;// 单据编号

    private String dealerCode;// 服务站编号

    private String dealerName;// 服务站名称

    private String serviceManager;  // 服务经理

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期

    private Date endDate = DateHelper.getEndDateTime();           // 结束日期

    private String activityNoticeId;  //活动通知单objId

    private String submitter;// 提交人logId

    private String submitterName;// 提交人姓名

    private Integer status;// 表单状态

    private String activityNoticeDocNo;// 活动通知单单据编号

    private Boolean repair = false;     // 是否已参加维修，默认为false
    private List<String> activityNoticeObjIds = new ArrayList<>();   //查询用
}
