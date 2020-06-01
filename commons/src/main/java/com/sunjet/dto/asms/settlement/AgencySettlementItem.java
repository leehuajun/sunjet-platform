package com.sunjet.dto.asms.settlement;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 配件费用结算单
 */
@Data
public class AgencySettlementItem implements Serializable {
    private String objId;//
    private String docNo;//单据编号
    private String agencyCode;      // 经销商编号　
    private String agencyName;      // 经销商名称
    private Date createdTime;//申请日期
    private Double expenseTotal = 0.0;            // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private String srcDocNo;  // 来源单号
    private String vin; // 车辆vin

    private String processInstanceId;//流程id
    private Integer status = 0;//当前状态

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

    private String serviceManager;  // 服务经理
}
