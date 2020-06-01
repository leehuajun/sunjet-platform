package com.sunjet.dto.asms.flow;

import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/9/13.
 */
@Data
public class CommentInfo {

    /**
     * unique identifier for this comment
     */
    private String id;

    /**
     * reference to the user that made the comment
     */
    private String userId;

    /**
     * time and date when the user made the comment
     */
    private Date time;

    /**
     * reference to the task on which this comment was made
     */
    private String taskId;

    /**
     * reference to the process instance on which this comment was made
     */
    private String processInstanceId;

    /**
     * reference to the type given to the comment
     */
    private String type;

    /**
     * the full comment message the user had related to the task and/or process instance
     */
    private String fullMessage;


}
