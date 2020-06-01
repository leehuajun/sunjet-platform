package com.sunjet.dto.asms.supply;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 调拨供货单 VO
 */
@Data
public class SupplyInfo extends FlowDocInfo implements Serializable {

    private String docType;  //  单据类型
    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String srcDocID;        // 来源对应单据ID
    private String operator;            // 经办人
    private String operatorPhone;       // 联系电话
    private String agencyName;        // 经销商
    private String agencyCode;        // 经销商编码
    private String agencyAddress;       //经销商地址
    private String agencyPhone;         //经销商电话
    private String transportmodel;    // 运输方式
    private Date arrivalTime;     //到货时间
    private String logistics;    // 物流名称
    private String logisticsNum;    // 物流单号
    private String logisticsfile;    // 物流附件
    private String logisticsfilename;   //物流附件
    private Date supplyDate = new Date();  // 发货时间
    private Date rcvDate;  // 收货时间

    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private String serviceManager;   // 服务经理
    private String receive;         //收货人
    private String receivePhone;         //收货联系电话
    private String dealerAdderss;       // 服务站收货地址

    private Double partExpense = 0.0;           //配件费用
    private Double transportExpense = 0.0;        // 运输费用
    private Double otherExpense = 0.0;            // 其他费用
    private Double expenseTotal = 0.0;           // 费用合计
    private Boolean received;   //是否收货

    private String comment; //备注

    //private Boolean settlement = false;//是否结算

    private List<SupplyItemInfo> items = new ArrayList<>();     // 物料列表

}
