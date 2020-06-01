package com.sunjet.backend.modules.asms.entity.supply.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 调拨供货单
 */
@Data
@Entity
@Immutable
@Subselect("select obj_id,doc_no,dealer_code,dealer_name,supply_date,agency_code,agency_name,logistics_num,transportmodel,received,process_instance_id,status,created_time from asm_supply_docs")
public class SupplyView {
    @Id
    private String objId;   // objID
    private String docNo;   //单据编号
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date supplyDate;  // 发货时间
    private String agencyCode;        // 经销商编码
    private String agencyName;        // 经销商
    private String logisticsNum;
    private String transportmodel;    // 运输方式
    private Boolean received;   //是否收货
    private Date createdTime;

    private String processInstanceId;   // 流程实例Id
    private Integer status = 0;         // 表单状态
}
