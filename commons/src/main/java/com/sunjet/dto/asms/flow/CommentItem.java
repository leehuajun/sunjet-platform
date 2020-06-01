package com.sunjet.dto.asms.flow;

import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/9/20.
 * 审批记录
 */
@Data
public class CommentItem {

    private Long id;            // 自增长id
    private String flowInstanceId;  // 流程实例id
    private String userId;          // 审批人id
    private String username;        // 审批人姓名
    private Date doDate;            // 审批时间
    private String result;          // 审批结果
    private String message;         // 审批意见

}
