package com.sunjet.dto.asms.flow;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/10/10.
 * 历史任务实例info
 */
@Data
public class HistoricTaskInstanceInfo extends TaskInfo {


    /**
     * 删除原因
     */
    private String deleteReason;

    /**
     * 任务实例开始时间
     */
    private Date startTime;

    /**
     * 任务实例结束时间
     */
    private Date endTime;

    /**
     * Difference between {@link #getEndTime()} and {@link #getStartTime()} in milliseconds.
     */
    private Long durationInMillis;

    /**
     * Difference between {@link #getEndTime()} and {@link #getClaimTime()} in milliseconds.
     */
    private Long workTimeInMillis;

    /**
     * Time when the task was claimed.
     */
    private Date claimTime;


    public static final class HistoricTaskInstanceInfoBuilder {
        private String id;
        private String deleteReason;
        private Date startTime;
        private String name;
        private Date endTime;
        private String description;
        private Long durationInMillis;
        private Integer priority;
        private String owner;
        private String assignee;
        private String processInstanceId;
        private Long workTimeInMillis;
        private String executionId;
        private String processDefinitionId;
        private Date claimTime;
        private Date createTime;
        private String taskDefinitionKey;
        private Date dueDate;
        private String category;
        private String parentTaskId;
        private String tenantId;
        private String formKey;
        private Map<String, Object> taskLocalVariables;
        private Map<String, Object> processVariables;

        private HistoricTaskInstanceInfoBuilder() {
        }

        public static HistoricTaskInstanceInfoBuilder aHistoricTaskInstanceInfo() {
            return new HistoricTaskInstanceInfoBuilder();
        }

        public HistoricTaskInstanceInfoBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withDeleteReason(String deleteReason) {
            this.deleteReason = deleteReason;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withStartTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withDurationInMillis(Long durationInMillis) {
            this.durationInMillis = durationInMillis;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withPriority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withAssignee(String assignee) {
            this.assignee = assignee;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withWorkTimeInMillis(Long workTimeInMillis) {
            this.workTimeInMillis = workTimeInMillis;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withExecutionId(String executionId) {
            this.executionId = executionId;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withProcessDefinitionId(String processDefinitionId) {
            this.processDefinitionId = processDefinitionId;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withClaimTime(Date claimTime) {
            this.claimTime = claimTime;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withTaskDefinitionKey(String taskDefinitionKey) {
            this.taskDefinitionKey = taskDefinitionKey;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withDueDate(Date dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withCategory(String category) {
            this.category = category;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withParentTaskId(String parentTaskId) {
            this.parentTaskId = parentTaskId;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withTenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withFormKey(String formKey) {
            this.formKey = formKey;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withTaskLocalVariables(Map<String, Object> taskLocalVariables) {
            this.taskLocalVariables = taskLocalVariables;
            return this;
        }

        public HistoricTaskInstanceInfoBuilder withProcessVariables(Map<String, Object> processVariables) {
            this.processVariables = processVariables;
            return this;
        }

        public HistoricTaskInstanceInfo build() {
            HistoricTaskInstanceInfo historicTaskInstanceInfo = new HistoricTaskInstanceInfo();
            historicTaskInstanceInfo.setId(id);
            historicTaskInstanceInfo.setDeleteReason(deleteReason);
            historicTaskInstanceInfo.setStartTime(startTime);
            historicTaskInstanceInfo.setName(name);
            historicTaskInstanceInfo.setEndTime(endTime);
            historicTaskInstanceInfo.setDescription(description);
            historicTaskInstanceInfo.setDurationInMillis(durationInMillis);
            historicTaskInstanceInfo.setPriority(priority);
            historicTaskInstanceInfo.setOwner(owner);
            historicTaskInstanceInfo.setAssignee(assignee);
            historicTaskInstanceInfo.setProcessInstanceId(processInstanceId);
            historicTaskInstanceInfo.setWorkTimeInMillis(workTimeInMillis);
            historicTaskInstanceInfo.setExecutionId(executionId);
            historicTaskInstanceInfo.setProcessDefinitionId(processDefinitionId);
            historicTaskInstanceInfo.setClaimTime(claimTime);
            historicTaskInstanceInfo.setCreateTime(createTime);
            historicTaskInstanceInfo.setTaskDefinitionKey(taskDefinitionKey);
            historicTaskInstanceInfo.setDueDate(dueDate);
            historicTaskInstanceInfo.setCategory(category);
            historicTaskInstanceInfo.setParentTaskId(parentTaskId);
            historicTaskInstanceInfo.setTenantId(tenantId);
            historicTaskInstanceInfo.setFormKey(formKey);
            historicTaskInstanceInfo.setTaskLocalVariables(taskLocalVariables);
            historicTaskInstanceInfo.setProcessVariables(processVariables);
            return historicTaskInstanceInfo;
        }
    }
}
