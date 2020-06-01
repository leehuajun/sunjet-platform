package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;

import javax.persistence.Column;
import java.util.Date;


/**
 * Created by lhj on 2015/9/6.
 * 组织（服务商）实体
 */
//@Entity
//@Table(name = "SysOrgs")
public class OrgEntity extends DocEntity {
    private static final long serialVersionUID = -6056297580918896377L;
    /**
     * 组织编号
     */
    @Column(name = "Code", length = 20, unique = true)
    private String code;
    /**
     * 组织名称
     */
    @Column(name = "Name", length = 20)
    private String name;
//    private Integer orgType;   // 组织类型，1：服务站  2：合作商
    /**
     * 是否服务站，服务站点
     */
    @Column(name = "IsService")
    private Boolean isService;
    /**
     * 是否配件合作商，经销商
     */
    @Column(name = "IsAgency")
    private Boolean isAgency;

    public static final class OrgEntityBuilder {
        private String createrId;
        private String code;
        private String createrName;
        private String name;
        private String modifierId;
        private Boolean isService;
        private String modifierName;
        private String objId;
        private Boolean isAgency;
        private Boolean enabled = false;
        private Date createdTime = new Date();
        private Date modifiedTime = new Date();

        private OrgEntityBuilder() {
        }

        public static OrgEntityBuilder anOrgEntity() {
            return new OrgEntityBuilder();
        }

        public OrgEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public OrgEntityBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public OrgEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public OrgEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public OrgEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public OrgEntityBuilder withIsService(Boolean isService) {
            this.isService = isService;
            return this;
        }

        public OrgEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public OrgEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public OrgEntityBuilder withIsAgency(Boolean isAgency) {
            this.isAgency = isAgency;
            return this;
        }

        public OrgEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public OrgEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public OrgEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public OrgEntity build() {
            OrgEntity orgEntity = new OrgEntity();
            orgEntity.setCreaterId(createrId);
            orgEntity.setCreaterName(createrName);
            orgEntity.setModifierId(modifierId);
            orgEntity.setModifierName(modifierName);
            orgEntity.setObjId(objId);
            orgEntity.setEnabled(enabled);
            orgEntity.setCreatedTime(createdTime);
            orgEntity.setModifiedTime(modifiedTime);
            orgEntity.isAgency = this.isAgency;
            orgEntity.code = this.code;
            orgEntity.name = this.name;
            orgEntity.isService = this.isService;
            return orgEntity;
        }
    }
}
