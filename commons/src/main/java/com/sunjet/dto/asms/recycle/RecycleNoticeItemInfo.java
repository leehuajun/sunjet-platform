package com.sunjet.dto.asms.recycle;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 故障件返回通知单子行VO
 */
@Data
public class RecycleNoticeItemInfo extends DocInfo implements Serializable {

    private String docNo;
    private String partCode;    // 零件号
    private String partName;    // 零件编号
    private Integer amount = 0;    //数量
    private Integer backAmount = 0; // 已返回数量
    private Integer currentAmount = 0; // 未返回数量
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String pattern;         // 故障模式
    private String reason;          // 换件原因
    private String comment; //备注

    private String recycleNoticeId;     //故障件返回通知id

    private String commissionPartId;   //三包维修配件id

    //private RecycleNoticeInfo recycleNoticeInfo;

    private List<RecycleItemInfo> recycleItems = new ArrayList<>();

    private RecycleNoticeInfo recycleNotice;

}
