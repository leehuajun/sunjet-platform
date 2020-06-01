package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 项目工时定额VO
 */
@Data
public class MaintainTypeInfo extends DocInfo implements Serializable {

    /**
     * 类型名称
     */
    private String name;
    /**
     * 父级类型的id
     */
    private String parentId;
    /**
     * 描述
     */
    private String comment;


    public static final class MaintainTypeInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String createrName;  // 创建人名字
        private String name;
        private String parentId;
        private String modifierId;   // 修改人ID
        private String comment;
        private String modifierName; // 修改人修改
        private Date createdTime = new Date();   //创建时间
        private Date modifiedTime = new Date();  //修改时间

        private MaintainTypeInfoBuilder() {
        }

        public static MaintainTypeInfoBuilder aMaintainTypeInfo() {
            return new MaintainTypeInfoBuilder();
        }

        public MaintainTypeInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public MaintainTypeInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public MaintainTypeInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public MaintainTypeInfoBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public MaintainTypeInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MaintainTypeInfoBuilder withParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public MaintainTypeInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public MaintainTypeInfoBuilder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public MaintainTypeInfoBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public MaintainTypeInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public MaintainTypeInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public MaintainTypeInfo build() {
            MaintainTypeInfo maintainTypeInfo = new MaintainTypeInfo();
            maintainTypeInfo.setObjId(objId);
            maintainTypeInfo.setCreaterId(createrId);
            maintainTypeInfo.setEnabled(enabled);
            maintainTypeInfo.setCreaterName(createrName);
            maintainTypeInfo.setName(name);
            maintainTypeInfo.setParentId(parentId);
            maintainTypeInfo.setModifierId(modifierId);
            maintainTypeInfo.setComment(comment);
            maintainTypeInfo.setModifierName(modifierName);
            maintainTypeInfo.setCreatedTime(createdTime);
            maintainTypeInfo.setModifiedTime(modifiedTime);
            return maintainTypeInfo;
        }
    }
}
