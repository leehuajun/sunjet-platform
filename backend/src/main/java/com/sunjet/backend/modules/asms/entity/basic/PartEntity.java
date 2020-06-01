package com.sunjet.backend.modules.asms.entity.basic;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 配件信息实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicParts")
public class PartEntity extends DocEntity {
    private static final long serialVersionUID = -6704173665780561623L;
    /**
     * 物料编号
     */
    @Column(length = 50, unique = true)
    private String code;
    /**
     * 物料名称
     */
    @Column(length = 200)
    private String name;
    /**
     * 价格
     */

    private Double price = 0.0;

    /**
     * 计量单位
     */
    @Column(length = 20)
    private String unit;
    /**
     * 物料类型  配件/辅料，默认是：配件
     */

    @Column(length = 20)
    private String partType = "配件";
    /**
     * 三包时间
     */
    @Column(length = 50)
    private String warrantyTime;
    /**
     * 三包里程
     */
    @Column(length = 50)
    private String warrantyMileage;
    /**
     * 配件分类
     */
    @Column(length = 50)
    private String partClassify;
    /**
     * 备注
     */
    @Column(length = 500)
    private String comment;


    public static final class PartEntityBuilder {
        private String createrId;
        private String createrName;
        private String modifierId;
        private String code;
        private String name;
        private String modifierName;
        private String objId;
        private Double price = 0.0;
        private Boolean enabled = false;
        /**
         * 计量单位
         */
        private String unit;
        private String partType = "配件";
        private Date createdTime = new Date();
        private String warrantyTime;
        private Date modifiedTime = new Date();
        private String warrantyMileage;
        private String partClassify;
        private String comment;

        private PartEntityBuilder() {
        }

        public static PartEntityBuilder aPartEntity() {
            return new PartEntityBuilder();
        }

        public PartEntityBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public PartEntityBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public PartEntityBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public PartEntityBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public PartEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public PartEntityBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public PartEntityBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public PartEntityBuilder withPrice(Double price) {
            this.price = price;
            return this;
        }

        public PartEntityBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public PartEntityBuilder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public PartEntityBuilder withPartType(String partType) {
            this.partType = partType;
            return this;
        }

        public PartEntityBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public PartEntityBuilder withWarrantyTime(String warrantyTime) {
            this.warrantyTime = warrantyTime;
            return this;
        }

        public PartEntityBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public PartEntityBuilder withWarrantyMileage(String warrantyMileage) {
            this.warrantyMileage = warrantyMileage;
            return this;
        }

        public PartEntityBuilder withPartClassify(String partClassify) {
            this.partClassify = partClassify;
            return this;
        }

        public PartEntityBuilder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public PartEntity build() {
            PartEntity partEntity = new PartEntity();
            partEntity.setCreaterId(createrId);
            partEntity.setCreaterName(createrName);
            partEntity.setModifierId(modifierId);
            partEntity.setCode(code);
            partEntity.setName(name);
            partEntity.setModifierName(modifierName);
            partEntity.setObjId(objId);
            partEntity.setPrice(price);
            partEntity.setEnabled(enabled);
            partEntity.setUnit(unit);
            partEntity.setPartType(partType);
            partEntity.setCreatedTime(createdTime);
            partEntity.setWarrantyTime(warrantyTime);
            partEntity.setModifiedTime(modifiedTime);
            partEntity.setWarrantyMileage(warrantyMileage);
            partEntity.setPartClassify(partClassify);
            partEntity.setComment(comment);
            return partEntity;
        }
    }
}
