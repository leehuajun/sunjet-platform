package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 公告
 */
@Data
public class NoticeInfo extends DocInfo implements Serializable {
    private String title;       // 公告标题
    private Date publishDate = new Date(); // 发布时间
    private String publisher;   // 发布人
    private String content;     // 公告内容
    private String fileOriginName;    // 附件原始名称
    private String fileRealName;    // 附件真实路径
    private Boolean isTop = false;  // 是否置顶

}
