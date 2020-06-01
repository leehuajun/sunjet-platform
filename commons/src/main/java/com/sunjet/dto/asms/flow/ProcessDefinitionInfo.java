package com.sunjet.dto.asms.flow;

import lombok.Data;

/**
 * Created by SUNJET_WS on 2017/9/5.
 * 流程定义
 */
@Data
public class ProcessDefinitionInfo {


    /**
     * 流程ID
     */
    private String id;

    /**
     * category name which is derived from the targetNamespace attribute in the definitions element
     */
    private String category;

    /**
     * 流程名称
     */
    private String name;

    /**
     * Key
     */
    private String key;

    /**
     * 表单
     **/
    private String description;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 资源名字
     */
    private String resourceName;

    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * The resource name in the deployment of the diagram image (if any).
     */
    private String diagramResourceName;


}
