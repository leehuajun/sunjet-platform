package com.sunjet.dto.asms.flow;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/9/20.
 * 历史流程实例
 */
@Data
public class HistoricProcessInstanceInfo {


    private String id;

    private String businessKey;

    private String processDefinitionId;

    private String processDefinitionName;

    private String processDefinitionKey;

    private Integer processDefinitionVersion;

    private String deploymentId;

    private Date startTime;

    private Date endTime;

    private Long durationInMillis;

    //private String endActivityId;

    private String startUserId;

    private String startActivityId;

    private String deleteReason;

    private String superProcessInstanceId;

    private String tenantId;

    private String name;

    private String description;

    private Map<String, Object> processVariables;


}
