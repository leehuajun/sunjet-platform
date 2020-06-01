package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hxf on 2015/11/05.
 * 资源实体
 */
@Data
public class ResourceInfo extends DocInfo implements Serializable {
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源编码
     */
    private String code;
    /**
     * 资源允许的操作列表
     */
    private List<OperationInfo> operationInfoList = new ArrayList<OperationInfo>();

    public static final class ResourceInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String CreaterName;  // 创建人名字
        private String name;
        private String modifierId;   // 修改人ID
        private String code;
        private String ModifierName; // 修改人修改
        private List<OperationInfo> operationInfoList = new ArrayList<OperationInfo>();
        private Date createdTime = new Date();   //创建时间
        private Date modifiedTime = new Date();  //修改时间

        private ResourceInfoBuilder() {
        }

        public static ResourceInfoBuilder aResourceInfo() {
            return new ResourceInfoBuilder();
        }

        public ResourceInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public ResourceInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public ResourceInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public ResourceInfoBuilder withCreaterName(String CreaterName) {
            this.CreaterName = CreaterName;
            return this;
        }

        public ResourceInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ResourceInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public ResourceInfoBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public ResourceInfoBuilder withModifierName(String ModifierName) {
            this.ModifierName = ModifierName;
            return this;
        }

        public ResourceInfoBuilder withOperationInfoList(List<OperationInfo> operationInfoList) {
            this.operationInfoList = operationInfoList;
            return this;
        }

        public ResourceInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public ResourceInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public ResourceInfo build() {
            ResourceInfo resourceInfo = new ResourceInfo();
            resourceInfo.setObjId(objId);
            resourceInfo.setCreaterId(createrId);
            resourceInfo.setEnabled(enabled);
            resourceInfo.setCreaterName(CreaterName);
            resourceInfo.setName(name);
            resourceInfo.setModifierId(modifierId);
            resourceInfo.setCode(code);
            resourceInfo.setModifierName(ModifierName);
            resourceInfo.setOperationInfoList(operationInfoList);
            resourceInfo.setCreatedTime(createdTime);
            resourceInfo.setModifiedTime(modifiedTime);
            return resourceInfo;
        }
    }
}
