package com.sunjet.backend.system.entity;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 16/6/27.
 * Web服务访问日志实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysWebserviceAccessLogs")
public class WebServiceAccessLogEntity extends DocEntity {
    private static final long serialVersionUID = -12032028016000298L;
    /**
     * 接口类型
     */
    @Column(name = "ObjType")
    private Integer type;
    /**
     * 操作类型
     */
    @Column(name = "OperateType")
    private Integer operateType;
    /**
     * 调用开始时间
     */
    @Column(name = "BeginTime")
    private Date beginTime;
    /**
     * 调用结束时间
     */
    @Column(name = "EndTime")
    private Date endTime;
    /**
     * 调用参数
     */
    @Column(name = "Param")
    private String param;
    /**
     * 失败原因
     */
    @Column(name = "FailResult")
    private String failResult;
    /**
     * 状态（成功/失败）
     */
    @Column(name = "Status")
    private Integer status;

    public static final class WebServiceAccessLogEntityBuilder {
        private String createrId;
        private String createrName;
        private String modifierId;
        private Integer type;
        private String modifierName;
        private Integer operateType;
        private String objId;
        private Date beginTime;
        private Boolean enabled = false;
        private Date endTime;
        private Date createdTime = new Date();
        private String param;
        private String failResult;
        private Date modifiedTime = new Date();
        private Integer status;

        private WebServiceAccessLogEntityBuilder() {
        }

        public static WebServiceAccessLogEntityBuilder aWebServiceAccessLogEntity() {
            return new WebServiceAccessLogEntityBuilder();
        }

        public WebServiceAccessLogEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withType(Integer type) {
            this.type = type;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withOperateType(Integer operateType) {
            this.operateType = operateType;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withBeginTime(Date beginTime) {
            this.beginTime = beginTime;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withParam(String param) {
            this.param = param;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withFailResult(String failResult) {
            this.failResult = failResult;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public WebServiceAccessLogEntityBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public WebServiceAccessLogEntity build() {
            WebServiceAccessLogEntity webServiceAccessLogEntity = new WebServiceAccessLogEntity();
            webServiceAccessLogEntity.setCreaterId(createrId);
            webServiceAccessLogEntity.setCreaterName(createrName);
            webServiceAccessLogEntity.setModifierId(modifierId);
            webServiceAccessLogEntity.setType(type);
            webServiceAccessLogEntity.setModifierName(modifierName);
            webServiceAccessLogEntity.setOperateType(operateType);
            webServiceAccessLogEntity.setObjId(objId);
            webServiceAccessLogEntity.setBeginTime(beginTime);
            webServiceAccessLogEntity.setEnabled(enabled);
            webServiceAccessLogEntity.setEndTime(endTime);
            webServiceAccessLogEntity.setCreatedTime(createdTime);
            webServiceAccessLogEntity.setParam(param);
            webServiceAccessLogEntity.setFailResult(failResult);
            webServiceAccessLogEntity.setModifiedTime(modifiedTime);
            webServiceAccessLogEntity.setStatus(status);
            return webServiceAccessLogEntity;
        }
    }
}