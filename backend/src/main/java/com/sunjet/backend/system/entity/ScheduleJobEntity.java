package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 16/6/20.
 * <p>
 * 计划任务实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysScheduleJobs")
public class ScheduleJobEntity extends DocEntity {
    private static final long serialVersionUID = 4498664073754373774L;
    /**
     * 任务名称
     */
    @Column(name = "JobName", length = 100)
    private String jobName;
    /**
     * 任务分组
     */
    @Column(name = "JobGroup", length = 100)
    private String jobGroup;
    /**
     * 任务运行时间表达式
     */
    @Column(name = "CronExpression", length = 100)
    private String cronExpression;
    /**
     * 任务描述
     */
    @Column(name = "JobDesc", length = 500)
    private String jobDesc;
    /**
     * 任务类
     */
    @Column(name = "JobClass", length = 500)
    private String jobClass;

    public static final class ScheduleJobEntityBuilder {
        private String createrId;
        private String createrName;
        private String jobName;
        private String modifierId;
        private String jobGroup;
        private String modifierName;
        private String objId;
        private Boolean enabled = false;
        private String cronExpression;
        private String jobDesc;
        private Date createdTime = new Date();
        private String jobClass;
        private Date modifiedTime = new Date();

        private ScheduleJobEntityBuilder() {
        }

        public static ScheduleJobEntityBuilder aScheduleJobEntity() {
            return new ScheduleJobEntityBuilder();
        }

        public ScheduleJobEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public ScheduleJobEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public ScheduleJobEntityBuilder withJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public ScheduleJobEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public ScheduleJobEntityBuilder withJobGroup(String jobGroup) {
            this.jobGroup = jobGroup;
            return this;
        }

        public ScheduleJobEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public ScheduleJobEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public ScheduleJobEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public ScheduleJobEntityBuilder withCronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
            return this;
        }

        public ScheduleJobEntityBuilder withJobDesc(String jobDesc) {
            this.jobDesc = jobDesc;
            return this;
        }

        public ScheduleJobEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public ScheduleJobEntityBuilder withJobClass(String jobClass) {
            this.jobClass = jobClass;
            return this;
        }

        public ScheduleJobEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public ScheduleJobEntity build() {
            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            scheduleJobEntity.setCreaterId(createrId);
            scheduleJobEntity.setCreaterName(createrName);
            scheduleJobEntity.setJobName(jobName);
            scheduleJobEntity.setModifierId(modifierId);
            scheduleJobEntity.setJobGroup(jobGroup);
            scheduleJobEntity.setModifierName(modifierName);
            scheduleJobEntity.setObjId(objId);
            scheduleJobEntity.setEnabled(enabled);
            scheduleJobEntity.setCronExpression(cronExpression);
            scheduleJobEntity.setJobDesc(jobDesc);
            scheduleJobEntity.setCreatedTime(createdTime);
            scheduleJobEntity.setJobClass(jobClass);
            scheduleJobEntity.setModifiedTime(modifiedTime);
            return scheduleJobEntity;
        }
    }
}
