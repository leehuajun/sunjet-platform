package com.sunjet.dto.system.base;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by SUNJET_WS on 2017/7/13.
 * 业务基础info
 */
public class FlowDocInfo extends DocInfo {
    @Getter
    @Setter
    private String processInstanceId;   // 流程实例Id

    @Getter
    @Setter
    private String submitter;   // 提交人LogId

    @Getter
    @Setter
    private String submitterName;       // 提交人姓名

    @Getter
    @Setter
    private String submitterPhone;      // 提交人电话

    @Getter
    @Setter
    private String handler;             // 当前处理人

    @Getter
    @Setter
    private String docNo;              //单据编号

    @Getter
    @Setter
    private Integer status = 0;         // 表单状态
}
