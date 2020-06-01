package com.sunjet.dto.asms.settlement;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 三包费用服务结算
 * Created by SUNJET_WS on 2017/7/17.
 */
@Data
public class DealerSettlementItem implements Serializable {


    private String objId;//
    private String docNo;//单据编号
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date createdTime;//申请日期
    private Double expenseTotal = 0.0;            // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private String srcDocNo; // 来源单号
    private String vin; // vin

    private String processInstanceId;//流程id
    private Integer status = 0;//当前状态

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

    private String serviceManager;  // 服务经理

}
