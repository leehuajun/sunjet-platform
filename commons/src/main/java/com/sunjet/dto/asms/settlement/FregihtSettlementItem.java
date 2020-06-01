package com.sunjet.dto.asms.settlement;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 运费结算列表
 */
@Data
public class FregihtSettlementItem extends FlowDocInfo implements Serializable {

    private String dealerCode;   // 服务站编号
    private String dealerName;   //  服务站名称
    private Date submitTime;   //  提交时间
    private Date submitName;   //  提交人
    private String currentNode;    // 当前节点


    public static final class FregihtSettlementItemBuilder {
        private String objId;   // objID
        private String processInstanceId;   // 流程实例Id
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String submitter;   // 提交人LogId
        private String createrName;  // 创建人名字
        private String dealerCode;   // 服务站编号
        private String dealerName;   //  服务站名称
        private String submitterName;       // 提交人姓名
        private String modifierId;   // 修改人ID
        private Date submitTime;   //  提交时间
        private String submitterPhone;      // 提交人电话
        private String modifierName; // 修改人修改
        private Date submitName;   //  提交人
        private String currentNode;    // 当前节点
        private Date createdTime = new Date();   //创建时间
        private String handler;             // 当前处理人
        private Date modifiedTime = new Date();  //修改时间
        private String docNo;              //单据编号
        private Integer status = 0;         // 表单状态

        private FregihtSettlementItemBuilder() {
        }

        public static FregihtSettlementItemBuilder aFregihtSettlementItem() {
            return new FregihtSettlementItemBuilder();
        }

        public FregihtSettlementItemBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public FregihtSettlementItemBuilder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public FregihtSettlementItemBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public FregihtSettlementItemBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public FregihtSettlementItemBuilder withSubmitter(String submitter) {
            this.submitter = submitter;
            return this;
        }

        public FregihtSettlementItemBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public FregihtSettlementItemBuilder withDealerCode(String dealerCode) {
            this.dealerCode = dealerCode;
            return this;
        }

        public FregihtSettlementItemBuilder withDealerName(String dealerName) {
            this.dealerName = dealerName;
            return this;
        }

        public FregihtSettlementItemBuilder withSubmitterName(String submitterName) {
            this.submitterName = submitterName;
            return this;
        }

        public FregihtSettlementItemBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public FregihtSettlementItemBuilder withSubmitTime(Date submitTime) {
            this.submitTime = submitTime;
            return this;
        }

        public FregihtSettlementItemBuilder withSubmitterPhone(String submitterPhone) {
            this.submitterPhone = submitterPhone;
            return this;
        }

        public FregihtSettlementItemBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public FregihtSettlementItemBuilder withSubmitName(Date submitName) {
            this.submitName = submitName;
            return this;
        }

        public FregihtSettlementItemBuilder withCurrentNode(String currentNode) {
            this.currentNode = currentNode;
            return this;
        }

        public FregihtSettlementItemBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public FregihtSettlementItemBuilder withHandler(String handler) {
            this.handler = handler;
            return this;
        }

        public FregihtSettlementItemBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public FregihtSettlementItemBuilder withDocNo(String docNo) {
            this.docNo = docNo;
            return this;
        }

        public FregihtSettlementItemBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public FregihtSettlementItem build() {
            FregihtSettlementItem fregihtSettlementItem = new FregihtSettlementItem();
            fregihtSettlementItem.setObjId(objId);
            fregihtSettlementItem.setProcessInstanceId(processInstanceId);
            fregihtSettlementItem.setCreaterId(createrId);
            fregihtSettlementItem.setEnabled(enabled);
            fregihtSettlementItem.setSubmitter(submitter);
            fregihtSettlementItem.setCreaterName(createrName);
            fregihtSettlementItem.setDealerCode(dealerCode);
            fregihtSettlementItem.setDealerName(dealerName);
            fregihtSettlementItem.setSubmitterName(submitterName);
            fregihtSettlementItem.setModifierId(modifierId);
            fregihtSettlementItem.setSubmitTime(submitTime);
            fregihtSettlementItem.setSubmitterPhone(submitterPhone);
            fregihtSettlementItem.setModifierName(modifierName);
            fregihtSettlementItem.setSubmitName(submitName);
            fregihtSettlementItem.setCurrentNode(currentNode);
            fregihtSettlementItem.setCreatedTime(createdTime);
            fregihtSettlementItem.setHandler(handler);
            fregihtSettlementItem.setModifiedTime(modifiedTime);
            fregihtSettlementItem.setDocNo(docNo);
            fregihtSettlementItem.setStatus(status);
            return fregihtSettlementItem;
        }
    }
}
