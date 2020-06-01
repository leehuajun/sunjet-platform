package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by lhj on 2015/9/6.
 * 系统日志实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysLogs")
public class LogEntity extends DocEntity {
    private static final long serialVersionUID = 1266364102549720670L;
    /**
     * 日志内容
     */
    @Column(name = "Message", length = 200)
    private String message;

    public static final class LogEntityBuilder {
        private String createrId;
        private String createrName;
        private String message;
        private String modifierId;
        private String modifierName;
        private String objId;
        private Boolean enabled = false;
        private Date createdTime = new Date();
        private Date modifiedTime = new Date();

        private LogEntityBuilder() {
        }

        public static LogEntityBuilder aLogEntity() {
            return new LogEntityBuilder();
        }

        public LogEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public LogEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public LogEntityBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public LogEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public LogEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public LogEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public LogEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public LogEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public LogEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public LogEntity build() {
            LogEntity logEntity = new LogEntity();
            logEntity.setCreaterId(createrId);
            logEntity.setCreaterName(createrName);
            logEntity.setMessage(message);
            logEntity.setModifierId(modifierId);
            logEntity.setModifierName(modifierName);
            logEntity.setObjId(objId);
            logEntity.setEnabled(enabled);
            logEntity.setCreatedTime(createdTime);
            logEntity.setModifiedTime(modifiedTime);
            return logEntity;
        }
    }
}
