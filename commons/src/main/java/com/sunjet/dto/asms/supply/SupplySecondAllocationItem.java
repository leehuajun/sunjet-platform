package com.sunjet.dto.asms.supply;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 二次分配清单VO
 */
@Data
public class SupplySecondAllocationItem extends FlowDocInfo implements Serializable {

    private String docType;  //  单据类型
    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份

    private String partCode; //零件件号
    private String partName; //零件名称
    private double requestAmount; //需求数量
    private double surplusAmount; //可分配数量
    private Date arrivalTime; //要求到货时间
    private String agencyName; //经销商  合作商名称
    private double distributionAmount; //本次分配数量
    private String comment; // 备注


    public static final class SupplySecondAllocationItemBuilder {
        private String objId;   // objID
        private String processInstanceId;   // 流程实例Id
        private String createrId;   //创建人ID
        private Boolean enabled = false;   //是否启用
        private String submitter;   // 提交人LogId
        private String createrName;  // 创建人名字
        private String docType;  //  单据类型
        private String srcDocNo;        //单据编号
        private String submitterName;       // 提交人姓名
        private String modifierId;   // 修改人ID
        private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
        private String submitterPhone;      // 提交人电话
        private String modifierName; // 修改人修改
        private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
        private Date createdTime = new Date();   //创建时间
        private String handler;             // 当前处理人
        private String dealerName;      // 服务站名称
        private Date modifiedTime = new Date();  //修改时间
        private String docNo;              //单据编号
        private String provinceName;    // 省份
        private String partCode; //零件件号
        private Integer status = 0;         // 表单状态
        private String partName; //零件名称
        private double requestAmount; //需求数量
        private double surplusAmount; //可分配数量
        private Date arrivalTime; //要求到货时间
        private String agencyName; //经销商  合作商名称
        private double distributionAmount; //本次分配数量
        private String comment; // 备注

        private SupplySecondAllocationItemBuilder() {
        }

        public static SupplySecondAllocationItemBuilder aSupplySecondAllocationItem() {
            return new SupplySecondAllocationItemBuilder();
        }

        public SupplySecondAllocationItemBuilder withObjId(String objId) {
            this.objId = objId;
            return this;
        }

        public SupplySecondAllocationItemBuilder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public SupplySecondAllocationItemBuilder withCreaterId(String createrId) {
            this.createrId = createrId;
            return this;
        }

        public SupplySecondAllocationItemBuilder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public SupplySecondAllocationItemBuilder withSubmitter(String submitter) {
            this.submitter = submitter;
            return this;
        }

        public SupplySecondAllocationItemBuilder withCreaterName(String createrName) {
            this.createrName = createrName;
            return this;
        }

        public SupplySecondAllocationItemBuilder withDocType(String docType) {
            this.docType = docType;
            return this;
        }

        public SupplySecondAllocationItemBuilder withSrcDocNo(String srcDocNo) {
            this.srcDocNo = srcDocNo;
            return this;
        }

        public SupplySecondAllocationItemBuilder withSubmitterName(String submitterName) {
            this.submitterName = submitterName;
            return this;
        }

        public SupplySecondAllocationItemBuilder withModifierId(String modifierId) {
            this.modifierId = modifierId;
            return this;
        }

        public SupplySecondAllocationItemBuilder withSrcDocType(String srcDocType) {
            this.srcDocType = srcDocType;
            return this;
        }

        public SupplySecondAllocationItemBuilder withSubmitterPhone(String submitterPhone) {
            this.submitterPhone = submitterPhone;
            return this;
        }

        public SupplySecondAllocationItemBuilder withModifierName(String modifierName) {
            this.modifierName = modifierName;
            return this;
        }

        public SupplySecondAllocationItemBuilder withDealerCode(String dealerCode) {
            this.dealerCode = dealerCode;
            return this;
        }

        public SupplySecondAllocationItemBuilder withCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public SupplySecondAllocationItemBuilder withHandler(String handler) {
            this.handler = handler;
            return this;
        }

        public SupplySecondAllocationItemBuilder withDealerName(String dealerName) {
            this.dealerName = dealerName;
            return this;
        }

        public SupplySecondAllocationItemBuilder withModifiedTime(Date modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public SupplySecondAllocationItemBuilder withDocNo(String docNo) {
            this.docNo = docNo;
            return this;
        }

        public SupplySecondAllocationItemBuilder withProvinceName(String provinceName) {
            this.provinceName = provinceName;
            return this;
        }

        public SupplySecondAllocationItemBuilder withPartCode(String partCode) {
            this.partCode = partCode;
            return this;
        }

        public SupplySecondAllocationItemBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public SupplySecondAllocationItemBuilder withPartName(String partName) {
            this.partName = partName;
            return this;
        }

        public SupplySecondAllocationItemBuilder withRequestAmount(double requestAmount) {
            this.requestAmount = requestAmount;
            return this;
        }

        public SupplySecondAllocationItemBuilder withSurplusAmount(double surplusAmount) {
            this.surplusAmount = surplusAmount;
            return this;
        }

        public SupplySecondAllocationItemBuilder withArrivalTime(Date arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public SupplySecondAllocationItemBuilder withAgencyName(String agencyName) {
            this.agencyName = agencyName;
            return this;
        }

        public SupplySecondAllocationItemBuilder withDistributionAmount(double distributionAmount) {
            this.distributionAmount = distributionAmount;
            return this;
        }

        public SupplySecondAllocationItemBuilder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public SupplySecondAllocationItem build() {
            SupplySecondAllocationItem supplySecondAllocationItem = new SupplySecondAllocationItem();
            supplySecondAllocationItem.setObjId(objId);
            supplySecondAllocationItem.setProcessInstanceId(processInstanceId);
            supplySecondAllocationItem.setCreaterId(createrId);
            supplySecondAllocationItem.setEnabled(enabled);
            supplySecondAllocationItem.setSubmitter(submitter);
            supplySecondAllocationItem.setCreaterName(createrName);
            supplySecondAllocationItem.setDocType(docType);
            supplySecondAllocationItem.setSrcDocNo(srcDocNo);
            supplySecondAllocationItem.setSubmitterName(submitterName);
            supplySecondAllocationItem.setModifierId(modifierId);
            supplySecondAllocationItem.setSrcDocType(srcDocType);
            supplySecondAllocationItem.setSubmitterPhone(submitterPhone);
            supplySecondAllocationItem.setModifierName(modifierName);
            supplySecondAllocationItem.setDealerCode(dealerCode);
            supplySecondAllocationItem.setCreatedTime(createdTime);
            supplySecondAllocationItem.setHandler(handler);
            supplySecondAllocationItem.setDealerName(dealerName);
            supplySecondAllocationItem.setModifiedTime(modifiedTime);
            supplySecondAllocationItem.setDocNo(docNo);
            supplySecondAllocationItem.setProvinceName(provinceName);
            supplySecondAllocationItem.setPartCode(partCode);
            supplySecondAllocationItem.setStatus(status);
            supplySecondAllocationItem.setPartName(partName);
            supplySecondAllocationItem.setRequestAmount(requestAmount);
            supplySecondAllocationItem.setSurplusAmount(surplusAmount);
            supplySecondAllocationItem.setArrivalTime(arrivalTime);
            supplySecondAllocationItem.setAgencyName(agencyName);
            supplySecondAllocationItem.setDistributionAmount(distributionAmount);
            supplySecondAllocationItem.setComment(comment);
            return supplySecondAllocationItem;
        }
    }
}
