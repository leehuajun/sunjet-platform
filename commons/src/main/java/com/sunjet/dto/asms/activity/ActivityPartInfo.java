package com.sunjet.dto.asms.activity;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 活动配件子行VO
 */
@Data
public class ActivityPartInfo extends DocInfo implements Serializable {


    private String activityNoticeId;  //活动通知id
    private String partId;    //配件id
    private Integer amount = 1;     // 数量
    private String comment;     // 备注
}
