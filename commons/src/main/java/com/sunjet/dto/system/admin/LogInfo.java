package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhj on 2017/7/13.
 * 系统日志
 */
@Data
public class LogInfo extends DocInfo implements Serializable {

    private String message;     //日志内容

    public static final class LogInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String message;     //日志内容
        private String CreaterName;  // 创建人名字
        private String modifierId;   // 修改人ID
        private String ModifierName; // 修改人修改
        private Date createdTime = new Date();   //创建时间
        private Date modifiedTime = new Date();  //修改时间

        private LogInfoBuilder() {
        }

        public static LogInfoBuilder aLogInfo() {
            return new LogInfoBuilder();
        }

        public LogInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public LogInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public LogInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public LogInfoBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public LogInfoBuilder withCreaterName(String CreaterName) {
            this.CreaterName = CreaterName;
            return this;
        }

        public LogInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public LogInfoBuilder withModifierName(String ModifierName) {
            this.ModifierName = ModifierName;
            return this;
        }

        public LogInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public LogInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public LogInfo build() {
            LogInfo logInfo = new LogInfo();
            logInfo.setObjId(objId);
            logInfo.setCreaterId(createrId);
            logInfo.setEnabled(enabled);
            logInfo.setMessage(message);
            logInfo.setCreaterName(CreaterName);
            logInfo.setModifierId(modifierId);
            logInfo.setModifierName(ModifierName);
            logInfo.setCreatedTime(createdTime);
            logInfo.setModifiedTime(modifiedTime);
            return logInfo;
        }
    }
}
