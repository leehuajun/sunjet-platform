package com.sunjet.dto.asms.flow;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/9/13.
 */
@Data
public class TaskInfo {

    /**
     * DB id of the task.
     */
    private String id;

    /**
     * Name or title of the task.
     */
    private String name;

    /**
     * Free text description of the task.
     */
    private String description;

    /**
     * Indication of how important/urgent this task is
     */
    private Integer priority;


    private String owner;


    private String assignee;


    private String processInstanceId;


    private String executionId;


    private String processDefinitionId;

    /**
     * The date/time when this task was created
     */
    private Date createTime;

    /**
     * The id of the activity in the process defining this task or null if this is
     * not related to a process
     */
    private String taskDefinitionKey;

    /**
     * Due date of the task.
     */
    private Date dueDate;

    /**
     * The category of the task. This is an optional field and allows to 'tag'
     * tasks as belonging to a certain category.
     */
    private String category;

    /**
     * The parent task for which this task is a subtask
     */
    private String parentTaskId;

    /**
     * The tenant identifier of this task
     */
    private String tenantId;

    /**
     * The form key for the user task
     */
    private String formKey;

    /**
     * Returns the local task variables if requested in the task query
     */
    private Map<String, Object> taskLocalVariables;

    /**
     * Returns the process variables if requested in the task query
     */
    private Map<String, Object> processVariables;

}
