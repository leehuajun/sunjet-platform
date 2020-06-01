package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 首保服务单列表VO
 */
@Data
public class FirsMaintenanceItem extends FlowDocInfo implements Serializable {

    private String dealerCode;  // 服务站编号
    private String dealerName;   // 服务站名称
    private String currentNode;    // 当前节点


    public static final class FirsMaintenanceItemBuilder {
        private String objId;   // objID
        private String processInstanceId;   // 流程实例Id
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String dealerCode;  // 服务站编号
        private String submitter;   // 提交人LogId
        private String createrName;  // 创建人名字
        private String dealerName;   // 服务站名称
        private String submitterName;       // 提交人姓名
        private String currentNode;    // 当前节点
        private String modifierId;   // 修改人ID
        private String submitterPhone;      // 提交人电话
        private String modifierName; // 修改人修改
        private Date createdTime = new Date();   //创建时间
        private String handler;             // 当前处理人
        private Date modifiedTime = new Date();  //修改时间
        private String docNo;              //单据编号
        private Integer status = 0;         // 表单状态

        private FirsMaintenanceItemBuilder() {
        }

        public static FirsMaintenanceItemBuilder aFirsMaintenanceItem() {
            return new FirsMaintenanceItemBuilder();
        }

        public FirsMaintenanceItemBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public FirsMaintenanceItemBuilder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public FirsMaintenanceItemBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public FirsMaintenanceItemBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public FirsMaintenanceItemBuilder withDealerCode(String dealerCode) {
            this.dealerCode = dealerCode;
            return this;
        }

        public FirsMaintenanceItemBuilder withSubmitter(String submitter) {
            this.submitter = submitter;
            return this;
        }

        public FirsMaintenanceItemBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public FirsMaintenanceItemBuilder withDealerName(String dealerName) {
            this.dealerName = dealerName;
            return this;
        }

        public FirsMaintenanceItemBuilder withSubmitterName(String submitterName) {
            this.submitterName = submitterName;
            return this;
        }

        public FirsMaintenanceItemBuilder withCurrentNode(String currentNode) {
            this.currentNode = currentNode;
            return this;
        }

        public FirsMaintenanceItemBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public FirsMaintenanceItemBuilder withSubmitterPhone(String submitterPhone) {
            this.submitterPhone = submitterPhone;
            return this;
        }

        public FirsMaintenanceItemBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public FirsMaintenanceItemBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public FirsMaintenanceItemBuilder withHandler(String handler) {
            this.handler = handler;
            return this;
        }

        public FirsMaintenanceItemBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public FirsMaintenanceItemBuilder withDocNo(String docNo) {
            this.docNo = docNo;
            return this;
        }

        public FirsMaintenanceItemBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public FirsMaintenanceItem build() {
            FirsMaintenanceItem firsMaintenanceItem = new FirsMaintenanceItem();
            firsMaintenanceItem.setObjId(objId);
            firsMaintenanceItem.setProcessInstanceId(processInstanceId);
            firsMaintenanceItem.setCreaterId(createrId);
            firsMaintenanceItem.setEnabled(enabled);
            firsMaintenanceItem.setDealerCode(dealerCode);
            firsMaintenanceItem.setSubmitter(submitter);
            firsMaintenanceItem.setCreaterName(createrName);
            firsMaintenanceItem.setDealerName(dealerName);
            firsMaintenanceItem.setSubmitterName(submitterName);
            firsMaintenanceItem.setCurrentNode(currentNode);
            firsMaintenanceItem.setModifierId(modifierId);
            firsMaintenanceItem.setSubmitterPhone(submitterPhone);
            firsMaintenanceItem.setModifierName(modifierName);
            firsMaintenanceItem.setCreatedTime(createdTime);
            firsMaintenanceItem.setHandler(handler);
            firsMaintenanceItem.setModifiedTime(modifiedTime);
            firsMaintenanceItem.setDocNo(docNo);
            firsMaintenanceItem.setStatus(status);
            return firsMaintenanceItem;
        }
    }
}
