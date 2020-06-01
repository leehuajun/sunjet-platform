package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.TreeNodeEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhaoyehai02 on 2016-06-08.
 * 字典
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysDictionaries")
public class DictionaryEntity extends TreeNodeEntity<DictionaryEntity> {
    private static final long serialVersionUID = 226806555109100262L;
    /**
     * 名称
     */
    @Column(name = "ItemName", length = 100)
    private String name;

    /**
     * 编码
     */
    @Column(name = "ItemCode", length = 10, unique = true)
    private String code;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 值
     */
    @Column(name = "ItemValue", length = 50)
    private String value;

    @Override
    public String toString() {
        return this.name;
    }


    public static final class DictionaryEntityBuilder {
        private String createrId;
        private String createrName;
        private DictionaryEntity parent;
        private String modifierId;
        private String name;
        private String modifierName;
        private String code;
        private String objId;
        private Integer seq;
        private Boolean enabled = false;
        private String value;
        private Date createdTime = new Date();
        private Date modifiedTime = new Date();

        private DictionaryEntityBuilder() {
        }

        public static DictionaryEntityBuilder aDictionaryEntity() {
            return new DictionaryEntityBuilder();
        }

        public DictionaryEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public DictionaryEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public DictionaryEntityBuilder withParent(DictionaryEntity parent) {
            this.parent = parent;
            return this;
        }

        public DictionaryEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public DictionaryEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DictionaryEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public DictionaryEntityBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public DictionaryEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public DictionaryEntityBuilder withSeq(Integer seq) {
            this.seq = seq;
            return this;
        }

        public DictionaryEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public DictionaryEntityBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        public DictionaryEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public DictionaryEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public DictionaryEntity build() {
            DictionaryEntity dictionaryEntity = new DictionaryEntity();
            dictionaryEntity.setCreaterId(createrId);
            dictionaryEntity.setCreaterName(createrName);
            dictionaryEntity.setParent(parent);
            dictionaryEntity.setModifierId(modifierId);
            dictionaryEntity.setName(name);
            dictionaryEntity.setModifierName(modifierName);
            dictionaryEntity.setCode(code);
            dictionaryEntity.setObjId(objId);
            dictionaryEntity.setSeq(seq);
            dictionaryEntity.setEnabled(enabled);
            dictionaryEntity.setValue(value);
            dictionaryEntity.setCreatedTime(createdTime);
            dictionaryEntity.setModifiedTime(modifiedTime);
            return dictionaryEntity;
        }
    }
}
