package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 图标
 */
@Data
public class IconInfo extends DocInfo implements Serializable {

    private String name;    //图标名称


    public static final class iconsInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String name;    //图标名称
        private String CreaterName;  // 创建人名字
        private String modifierId;   // 修改人ID
        private String ModifierName; // 修改人修改
        private Date createdTime = new Date();   //创建时间
        private Date modifiedTime = new Date();  //修改时间

        private iconsInfoBuilder() {
        }

        public static iconsInfoBuilder aniconsInfo() {
            return new iconsInfoBuilder();
        }

        public iconsInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public iconsInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public iconsInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public iconsInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public iconsInfoBuilder withCreaterName(String CreaterName) {
            this.CreaterName = CreaterName;
            return this;
        }

        public iconsInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public iconsInfoBuilder withModifierName(String ModifierName) {
            this.ModifierName = ModifierName;
            return this;
        }

        public iconsInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public iconsInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public IconInfo build() {
            IconInfo iconInfo = new IconInfo();
            iconInfo.setObjId(objId);
            iconInfo.setCreaterId(createrId);
            iconInfo.setEnabled(enabled);
            iconInfo.setName(name);
            iconInfo.setCreaterName(CreaterName);
            iconInfo.setModifierId(modifierId);
            iconInfo.setModifierName(ModifierName);
            iconInfo.setCreatedTime(createdTime);
            iconInfo.setModifiedTime(modifiedTime);
            return iconInfo;
        }
    }
}
