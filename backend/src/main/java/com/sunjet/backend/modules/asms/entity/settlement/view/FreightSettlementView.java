package com.sunjet.backend.modules.asms.entity.settlement.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 运费结算单
 * Created by SUNJET_WS on 2017/7/17.
 */
@Data
@Entity
@Immutable
@Subselect("select obj_id,doc_no,dealer_code,dealer_name,created_time,expense_total,operator,operator_phone,process_instance_id,status,service_manager from asm_freight_settlement_docs")
public class FreightSettlementView implements Serializable {

    @Id
    private String objId;//
    private String docNo;//单据编号
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date createdTime;//申请日期
    private Double expenseTotal = 0.0;            // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话

    private String processInstanceId;//流程id
    private Integer status = 0;//当前状态

    private String serviceManager;  // 服务经理

}