package com.sunjet.dto.asms.flow;

import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/9/6.
 * 部署对象
 */
@Data
public class DeploymentInfo {

    private String id;

    private String name;

    private Date deploymentTime;

    private String category;

    private String tenantId;

}
