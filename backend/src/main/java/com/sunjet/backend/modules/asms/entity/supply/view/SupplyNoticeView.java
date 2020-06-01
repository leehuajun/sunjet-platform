package com.sunjet.backend.modules.asms.entity.supply.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 调拨通知单
 */
@Data
@Entity
@Immutable
@Subselect("SELECT asn.obj_id, asn.doc_no, asn.src_doc_no, asn.src_docid, asn.dealer_code, asn.dealer_name, asn.dealer_adderss, asn.receive, asn.operator_phone, asn.province_name, asn.src_doc_type, asn.created_time, asn.submitter_name, asn.process_instance_id, asn.`status`, asn.`comment`, asn.service_manager, asn.doc_type, aan.doc_no AS activity_notice_doc_no FROM asm_supply_notices AS asn LEFT JOIN asm_activity_distributions AS aad ON aad.obj_id = asn.src_docid LEFT JOIN asm_activity_notices AS aan ON aan.obj_id = aad.activity_notice_id ")
public class SupplyNoticeView implements Serializable {

    @Id
    private String objId;
    private String docNo;
    private String srcDocNo;     // 单据编号
    private String srcDocID;     // 来源对应单据ID
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String dealerAdderss;   //服务站地址
    private String receive;         //收货人
    private String operatorPhone;   //收货人电话
    private String provinceName;    // 省份
    private String srcDocType;

    private Date createdTime;//申请时间
    private String submitterName;//经办人

    private String processInstanceId;   // 流程实例Id
    private Integer status = 0;         // 表单状态
    private String comment;

    private String serviceManager;   // 服务经理

    /**
     * 单据类型
     */
    private String docType;

    private String activityNoticeDocNo;   //活动通知单
    /**
     * 经销商名称
     */
    //private String agencyName;
    /**
     * 经销商Id
     */
    //private String agencyId;
}
