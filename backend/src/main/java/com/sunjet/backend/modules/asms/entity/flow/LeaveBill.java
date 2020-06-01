package com.sunjet.backend.modules.asms.entity.flow;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by lhj on 16/10/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "flowLeaveBill")
public class LeaveBill extends FlowDocEntity {
    private static final long serialVersionUID = -6839029990447194465L;

    private Integer days;    // 请假天数

    @Temporal(TemporalType.DATE)
    private Date startDate = new Date();  // 开始日期

    private String reason = "";   // 请假原因

    private String comment = "";  // 备注

    public static final class LeaveBillBuilder {
        private String createrId;
        private String createrName;
        private String processInstanceId;   // 流程实例Id
        private Integer status = 0;         // 表单状态
        private String modifierId;
        private Integer days;    // 请假天数
        private Date submitDate;            // 流程提交时间
        private Date startDate = new Date();  // 开始日期
        private String modifierName;
        private String objId;
        private String reason = "";   // 请假原因
        private String submitter;           // 提交人LogId
        private Boolean enabled = false;
        private String comment = "";  // 备注
        private String submitterName;       // 提交人姓名
        private String submitterPhone;      // 提交人电话
        private Date createdTime = new Date();
        private String handler;             // 当前处理人
        private String docNo;
        private Date modifiedTime = new Date();

        private LeaveBillBuilder() {
        }

        public static LeaveBillBuilder aLeaveBill() {
            return new LeaveBillBuilder();
        }

        public LeaveBillBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public LeaveBillBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public LeaveBillBuilder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public LeaveBillBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public LeaveBillBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public LeaveBillBuilder withDays(Integer days) {
            this.days = days;
            return this;
        }

        public LeaveBillBuilder withSubmitDate(Date submitDate) {
            this.submitDate = submitDate;
            return this;
        }

        public LeaveBillBuilder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public LeaveBillBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public LeaveBillBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public LeaveBillBuilder withReason(String reason) {
            this.reason = reason;
            return this;
        }

        public LeaveBillBuilder withSubmitter(String submitter) {
            this.submitter = submitter;
            return this;
        }

        public LeaveBillBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public LeaveBillBuilder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public LeaveBillBuilder withSubmitterName(String submitterName) {
            this.submitterName = submitterName;
            return this;
        }

        public LeaveBillBuilder withSubmitterPhone(String submitterPhone) {
            this.submitterPhone = submitterPhone;
            return this;
        }

        public LeaveBillBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public LeaveBillBuilder withHandler(String handler) {
            this.handler = handler;
            return this;
        }

        public LeaveBillBuilder withDocNo(String docNo) {
            this.docNo = docNo;
            return this;
        }

        public LeaveBillBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public LeaveBill build() {
            LeaveBill leaveBill = new LeaveBill();
            leaveBill.setCreaterId(createrId);
            leaveBill.setCreaterName(createrName);
            leaveBill.setProcessInstanceId(processInstanceId);
            leaveBill.setStatus(status);
            leaveBill.setModifierId(modifierId);
            leaveBill.setDays(days);
            leaveBill.setSubmitDate(submitDate);
            leaveBill.setStartDate(startDate);
            leaveBill.setModifierName(modifierName);
            leaveBill.setObjId(objId);
            leaveBill.setReason(reason);
            leaveBill.setSubmitter(submitter);
            leaveBill.setEnabled(enabled);
            leaveBill.setComment(comment);
            leaveBill.setSubmitterName(submitterName);
            leaveBill.setSubmitterPhone(submitterPhone);
            leaveBill.setCreatedTime(createdTime);
            leaveBill.setHandler(handler);
            leaveBill.setDocNo(docNo);
            leaveBill.setModifiedTime(modifiedTime);
            return leaveBill;
        }
    }
}
