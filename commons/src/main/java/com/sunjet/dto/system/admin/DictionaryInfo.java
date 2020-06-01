package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 数据字典
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DictionaryInfo extends DocInfo implements Serializable {

    private String name;//名称
    private String code;//编码
    private Integer seq; // 排序
    private String value;   // 值
    private String background = "rgb(230,230,230)";

    //private List<DictionaryInfo> dictionaryInfos = new ArrayList<DictionaryInfo>(); // 子行
    private DictionaryInfo parent;    //父类


    public static final class DictionaryInfoBuilder {
        private String objId;   // objID
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String createrName;  // 创建人名字
        private String modifierId;   // 修改人ID
        private String name;//名称
        private String code;//编码
        private String modifierName; // 修改人修改
        private Integer seq; // 排序
        private String value;   // 值
        private Date createdTime = new Date();   //创建时间
        private String background = "rgb(230,230,230)";
        private Date modifiedTime = new Date();  //修改时间
        //private List<DictionaryInfo> dictionaryInfos = new ArrayList<DictionaryInfo>(); // 子行
        private DictionaryInfo parent;    //父类

        private DictionaryInfoBuilder() {
        }

        public static DictionaryInfoBuilder aDictionaryInfo() {
            return new DictionaryInfoBuilder();
        }

        public DictionaryInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public DictionaryInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public DictionaryInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public DictionaryInfoBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public DictionaryInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public DictionaryInfoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DictionaryInfoBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public DictionaryInfoBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public DictionaryInfoBuilder withSeq(Integer seq) {
            this.seq = seq;
            return this;
        }

        public DictionaryInfoBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        public DictionaryInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public DictionaryInfoBuilder withBackground(String background) {
            this.background = background;
            return this;
        }

        public DictionaryInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public DictionaryInfoBuilder withParent(DictionaryInfo parent) {
            this.parent = parent;
            return this;
        }

        public DictionaryInfo build() {
            DictionaryInfo dictionaryInfo = new DictionaryInfo();
            dictionaryInfo.setObjId(objId);
            dictionaryInfo.setCreaterId(createrId);
            dictionaryInfo.setEnabled(enabled);
            dictionaryInfo.setCreaterName(createrName);
            dictionaryInfo.setModifierId(modifierId);
            dictionaryInfo.setName(name);
            dictionaryInfo.setCode(code);
            dictionaryInfo.setModifierName(modifierName);
            dictionaryInfo.setSeq(seq);
            dictionaryInfo.setValue(value);
            dictionaryInfo.setCreatedTime(createdTime);
            dictionaryInfo.setBackground(background);
            dictionaryInfo.setModifiedTime(modifiedTime);
            dictionaryInfo.setParent(parent);
            return dictionaryInfo;
        }
    }


    @Override
    public String toString() {
        return this.name;
    }

}
