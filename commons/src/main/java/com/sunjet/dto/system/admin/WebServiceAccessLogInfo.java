package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhj on 16/6/27.
 * Web服务访问日志实体
 */
@Data
public class WebServiceAccessLogInfo extends DocInfo implements Serializable {
    /**
     * 接口类型
     */
    private Integer type;
    /**
     * 操作类型
     */
    private Integer operateType;
    /**
     * 调用开始时间
     */
    private Date beginTime;
    /**
     * 调用结束时间
     */
    private Date endTime;
    /**
     * 调用参数
     */
    private String param;
    /**
     * 失败原因
     */
    private String failResult;
    /**
     * 状态（成功/失败）
     */
    private Integer status;


    public static final class WebServiceAccessLogInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String CreaterName;  // 创建人名字
        private Integer type;
        private String modifierId;   // 修改人ID
        private Integer operateType;
        private String ModifierName; // 修改人修改
        private Date beginTime;
        private Date createdTime = new Date();   //创建时间
        private Date endTime;
        private String param;
        private Date modifiedTime = new Date();  //修改时间
        private String failResult;
        private Integer status;

        private WebServiceAccessLogInfoBuilder() {
        }

        public static WebServiceAccessLogInfoBuilder aWebServiceAccessLogInfo() {
            return new WebServiceAccessLogInfoBuilder();
        }

        public WebServiceAccessLogInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withCreaterName(String CreaterName) {
            this.CreaterName = CreaterName;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withType(Integer type) {
            this.type = type;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withOperateType(Integer operateType) {
            this.operateType = operateType;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withModifierName(String ModifierName) {
            this.ModifierName = ModifierName;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withBeginTime(Date beginTime) {
            this.beginTime = beginTime;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withParam(String param) {
            this.param = param;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withFailResult(String failResult) {
            this.failResult = failResult;
            return this;
        }

        public WebServiceAccessLogInfoBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public WebServiceAccessLogInfo build() {
            WebServiceAccessLogInfo webServiceAccessLogInfo = new WebServiceAccessLogInfo();
            webServiceAccessLogInfo.setObjId(objId);
            webServiceAccessLogInfo.setCreaterId(createrId);
            webServiceAccessLogInfo.setEnabled(enabled);
            webServiceAccessLogInfo.setCreaterName(CreaterName);
            webServiceAccessLogInfo.setType(type);
            webServiceAccessLogInfo.setModifierId(modifierId);
            webServiceAccessLogInfo.setOperateType(operateType);
            webServiceAccessLogInfo.setModifierName(ModifierName);
            webServiceAccessLogInfo.setBeginTime(beginTime);
            webServiceAccessLogInfo.setCreatedTime(createdTime);
            webServiceAccessLogInfo.setEndTime(endTime);
            webServiceAccessLogInfo.setParam(param);
            webServiceAccessLogInfo.setModifiedTime(modifiedTime);
            webServiceAccessLogInfo.setFailResult(failResult);
            webServiceAccessLogInfo.setStatus(status);
            return webServiceAccessLogInfo;
        }
    }
}