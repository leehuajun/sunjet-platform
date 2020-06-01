package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/13.
 * 服务站消息提醒内容info
 */
@Data
public class MessageInfo extends DocInfo implements Serializable {

    private String title;        //标题
    private String content;      // 内容
    private String orgId;        // 组织Id
    private String url;          // url
    private String refId;           // 对象Id
    private String logId;           // 用户Id
    private Boolean isRead = false; // 是否已读
}
