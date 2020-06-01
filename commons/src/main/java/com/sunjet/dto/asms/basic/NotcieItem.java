package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 公告列表
 */
@Data
public class NotcieItem extends DocInfo implements Serializable {

    private String title;       // 公告标题
    private Date publishDate = new Date(); // 发布时间
    private String publisher;   // 发布人


    public static final class NotcieItemBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String title;       // 公告标题
        private String CreaterName;  // 创建人名字
        private Date publishDate = new Date(); // 发布时间
        private String modifierId;   // 修改人ID
        private String publisher;   // 发布人
        private String ModifierName; // 修改人修改
        private Date createdTime = new Date();   //创建时间
        private Date modifiedTime = new Date();  //修改时间

        private NotcieItemBuilder() {
        }

        public static NotcieItemBuilder aNotcieItem() {
            return new NotcieItemBuilder();
        }

        public NotcieItemBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public NotcieItemBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public NotcieItemBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public NotcieItemBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public NotcieItemBuilder withCreaterName(String CreaterName) {
            this.CreaterName = CreaterName;
            return this;
        }

        public NotcieItemBuilder withPublishDate(Date publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public NotcieItemBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public NotcieItemBuilder withPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public NotcieItemBuilder withModifierName(String ModifierName) {
            this.ModifierName = ModifierName;
            return this;
        }

        public NotcieItemBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public NotcieItemBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public NotcieItem build() {
            NotcieItem notcieItem = new NotcieItem();
            notcieItem.setObjId(objId);
            notcieItem.setCreaterId(createrId);
            notcieItem.setEnabled(enabled);
            notcieItem.setTitle(title);
            notcieItem.setCreaterName(CreaterName);
            notcieItem.setPublishDate(publishDate);
            notcieItem.setModifierId(modifierId);
            notcieItem.setPublisher(publisher);
            notcieItem.setModifierName(ModifierName);
            notcieItem.setCreatedTime(createdTime);
            notcieItem.setModifiedTime(modifiedTime);
            return notcieItem;
        }
    }
}
