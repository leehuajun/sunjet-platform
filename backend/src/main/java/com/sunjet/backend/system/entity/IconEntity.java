package com.sunjet.backend.system.entity;

import com.sunjet.backend.base.DocEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by lhj on 2015/9/6.
 * 图标实体
 */
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SysIcons")
public class IconEntity extends DocEntity {
    private static final long serialVersionUID = -6056297580918896377L;
    /**
     * 图标名称
     */
    @Column(name = "Name", length = 50)
    private String name;

    public static final class IconEntityBuilder {
        private String createrId;
        private String createrName;
        private String modifierId;
        private String name;
        private String modifierName;
        private String objId;
        private Boolean enabled = false;
        private Date createdTime = new Date();
        private Date modifiedTime = new Date();

        private IconEntityBuilder() {
        }

        public static IconEntityBuilder anIconEntity() {
            return new IconEntityBuilder();
        }

        public IconEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public IconEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public IconEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public IconEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public IconEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public IconEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public IconEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public IconEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public IconEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public IconEntity build() {
            IconEntity iconEntity = new IconEntity();
            iconEntity.setCreaterId(createrId);
            iconEntity.setCreaterName(createrName);
            iconEntity.setModifierId(modifierId);
            iconEntity.setName(name);
            iconEntity.setModifierName(modifierName);
            iconEntity.setObjId(objId);
            iconEntity.setEnabled(enabled);
            iconEntity.setCreatedTime(createdTime);
            iconEntity.setModifiedTime(modifiedTime);
            return iconEntity;
        }
    }
}
