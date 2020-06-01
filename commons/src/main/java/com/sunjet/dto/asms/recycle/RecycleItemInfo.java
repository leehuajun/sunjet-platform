package com.sunjet.dto.asms.recycle;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 故障件返回通知单子行VO
 */
@Data
public class RecycleItemInfo extends DocInfo implements Serializable {

    private String logisticsNum;    // 物流单号
    //private Date requestDate;       // 开单时间
    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private Integer waitAmount = 0;           // 待返数量
    private Integer backAmount = 0;          // 本次返回数量
    private Integer acceptAmount = 0;        // 收货数量
    private String pattern;         // 故障模式
    private String reason;          // 换件原因
    private String comment;     // 备注

    private String recycle;     //故障件返回单

    private String noticeItemId;    //返回通知单子行id

    private Date returnDate;         // 返回日期  要求默认下个月10号

    private RecycleNoticeItemInfo recycleNoticeItem;// 返回通知单的行对象

}
