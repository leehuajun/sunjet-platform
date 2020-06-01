package com.sunjet.dto.asms.flow;

import lombok.Data;

import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/9/7.
 * 流程实例
 */
@Data
public class ProcessInstanceInfo {

    /**
     * 流程实例Id
     */
    String id;

    /**
     * 流程实例的流程定义的ID.
     */
    String processDefinitionId;

    /**
     * 流程实例的流程定义的名称.
     */
    String processDefinitionName;

    /**
     * 流程过程定义的key.
     */
    String processDefinitionKey;

    /**
     * 流程定义的版本.
     */
    Integer processDefinitionVersion;

    /**
     * 部署ID
     */
    String deploymentId;

    /**
     * 流程业务 key
     */
    String businessKey;

    /**
     * 流程是否挂起
     * 如果流程实例已挂起，则返回true。
     */
    Boolean isSuspended;

    /**
     * Returns the process variables if requested in the process instance query
     */
    Map<String, Object> processVariables;

    /**
     * The tenant identifier of this process instance
     */
    String tenantId;

    /**
     * Returns the name of this process instance.
     */
    String name;

    /**
     * Returns the description of this process instance.
     */
    String description;

    /**
     * Returns the localized name of this process instance.
     */
    String localizedName;

    /**
     * Returns the localized description of this process instance.
     */
    String localizedDescription;


    public static final class ProcessInstanceInfoBuilder {
        String id;
        String processDefinitionId;
        String processDefinitionName;
        String processDefinitionKey;
        Integer processDefinitionVersion;
        String deploymentId;
        String businessKey;
        Boolean isSuspended;
        Map<String, Object> processVariables;
        String tenantId;
        String name;
        String description;
        String localizedName;
        String localizedDescription;

        private ProcessInstanceInfoBuilder() {
        }

        public static ProcessInstanceInfoBuilder aProcessInstanceInfo() {
            return new ProcessInstanceInfoBuilder();
        }

        public ProcessInstanceInfoBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public ProcessInstanceInfoBuilder withProcessDefinitionId(String processDefinitionId) {
            this.processDefinitionId = processDefinitionId;
            return this;
        }

        public ProcessInstanceInfoBuilder withProcessDefinitionName(String processDefinitionName) {
            this.processDefinitionName = processDefinitionName;
            return this;
        }

        public ProcessInstanceInfoBuilder withProcessDefinitionKey(String processDefinitionKey) {
            this.processDefinitionKey = processDefinitionKey;
            return this;
        }

        public ProcessInstanceInfoBuilder withProcessDefinitionVersion(Integer processDefinitionVersion) {
            this.processDefinitionVersion = processDefinitionVersion;
            return this;
        }

        public ProcessInstanceInfoBuilder withDeploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
            return this;
        }

        public ProcessInstanceInfoBuilder withBusinessKey(String businessKey) {
            this.businessKey = businessKey;
            return this;
        }

        public ProcessInstanceInfoBuilder withIsSuspended(Boolean isSuspended) {
            this.isSuspended = isSuspended;
            return this;
        }

        public ProcessInstanceInfoBuilder withProcessVariables(Map<String, Object> processVariables) {
            this.processVariables = processVariables;
            return this;
        }

        public ProcessInstanceInfoBuilder withTenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public ProcessInstanceInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ProcessInstanceInfoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ProcessInstanceInfoBuilder withLocalizedName(String localizedName) {
            this.localizedName = localizedName;
            return this;
        }

        public ProcessInstanceInfoBuilder withLocalizedDescription(String localizedDescription) {
            this.localizedDescription = localizedDescription;
            return this;
        }

        public ProcessInstanceInfo build() {
            ProcessInstanceInfo processInstanceInfo = new ProcessInstanceInfo();
            processInstanceInfo.setId(id);
            processInstanceInfo.setProcessDefinitionId(processDefinitionId);
            processInstanceInfo.setProcessDefinitionName(processDefinitionName);
            processInstanceInfo.setProcessDefinitionKey(processDefinitionKey);
            processInstanceInfo.setProcessDefinitionVersion(processDefinitionVersion);
            processInstanceInfo.setDeploymentId(deploymentId);
            processInstanceInfo.setBusinessKey(businessKey);
            processInstanceInfo.setIsSuspended(isSuspended);
            processInstanceInfo.setProcessVariables(processVariables);
            processInstanceInfo.setTenantId(tenantId);
            processInstanceInfo.setName(name);
            processInstanceInfo.setDescription(description);
            processInstanceInfo.setLocalizedName(localizedName);
            processInstanceInfo.setLocalizedDescription(localizedDescription);
            return processInstanceInfo;
        }
    }
}
