package com.sunjet.dto.asms.dealer;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 服务站退出申请
 */
@Data
public class DealerQuitRequestInfo extends FlowDocInfo implements Serializable {

    private DealerInfo dealerInfo;   // 服务站信息
    private String dealer;       //服务站ID
    private String reason;                  // 退出原因


    public static final class DealerQuitRequestInfoBuilder {
        private String objId;   // objID
        private String processInstanceId;   // 流程实例Id
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String submitter;   // 提交人LogId
        private String createrName;  // 创建人名字
        private DealerInfo dealerInfo;   // 服务站信息
        private String submitterName;       // 提交人姓名
        private String modifierId;   // 修改人ID
        private String reason;                  // 退出原因
        private String submitterPhone;      // 提交人电话
        private String modifierName; // 修改人修改
        private Date createdTime = new Date();   //创建时间
        private String handler;             // 当前处理人
        private Date modifiedTime = new Date();  //修改时间
        private String docNo;              //单据编号
        private Integer status = 0;         // 表单状态

        private DealerQuitRequestInfoBuilder() {
        }

        public static DealerQuitRequestInfoBuilder aDealerQuitRequestInfo() {
            return new DealerQuitRequestInfoBuilder();
        }

        public DealerQuitRequestInfoBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public DealerQuitRequestInfoBuilder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public DealerQuitRequestInfoBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public DealerQuitRequestInfoBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public DealerQuitRequestInfoBuilder withSubmitter(String submitter) {
            this.submitter = submitter;
            return this;
        }

        public DealerQuitRequestInfoBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public DealerQuitRequestInfoBuilder withDealerInfo(DealerInfo dealerInfo) {
            this.dealerInfo = dealerInfo;
            return this;
        }

        public DealerQuitRequestInfoBuilder withSubmitterName(String submitterName) {
            this.submitterName = submitterName;
            return this;
        }

        public DealerQuitRequestInfoBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public DealerQuitRequestInfoBuilder withReason(String reason) {
            this.reason = reason;
            return this;
        }

        public DealerQuitRequestInfoBuilder withSubmitterPhone(String submitterPhone) {
            this.submitterPhone = submitterPhone;
            return this;
        }

        public DealerQuitRequestInfoBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public DealerQuitRequestInfoBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public DealerQuitRequestInfoBuilder withHandler(String handler) {
            this.handler = handler;
            return this;
        }

        public DealerQuitRequestInfoBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public DealerQuitRequestInfoBuilder withDocNo(String docNo) {
            this.docNo = docNo;
            return this;
        }

        public DealerQuitRequestInfoBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public DealerQuitRequestInfo build() {
            DealerQuitRequestInfo dealerQuitRequestInfo = new DealerQuitRequestInfo();
            dealerQuitRequestInfo.setObjId(objId);
            dealerQuitRequestInfo.setProcessInstanceId(processInstanceId);
            dealerQuitRequestInfo.setCreaterId(createrId);
            dealerQuitRequestInfo.setEnabled(enabled);
            dealerQuitRequestInfo.setSubmitter(submitter);
            dealerQuitRequestInfo.setCreaterName(createrName);
            dealerQuitRequestInfo.setDealerInfo(dealerInfo);
            dealerQuitRequestInfo.setSubmitterName(submitterName);
            dealerQuitRequestInfo.setModifierId(modifierId);
            dealerQuitRequestInfo.setReason(reason);
            dealerQuitRequestInfo.setSubmitterPhone(submitterPhone);
            dealerQuitRequestInfo.setModifierName(modifierName);
            dealerQuitRequestInfo.setCreatedTime(createdTime);
            dealerQuitRequestInfo.setHandler(handler);
            dealerQuitRequestInfo.setModifiedTime(modifiedTime);
            dealerQuitRequestInfo.setDocNo(docNo);
            dealerQuitRequestInfo.setStatus(status);
            return dealerQuitRequestInfo;
        }
    }
}
