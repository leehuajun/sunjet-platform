package com.sunjet.dto.asms.recycle;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 故障件返回单表单VO
 */
@Data
public class RecycleInfo extends FlowDocInfo implements Serializable {


    //private String docType;  //  单据类型
    //private String srcDocNo;        //单据编号
    //private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、

    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private String logistics;       // 物流名称
    private String logisticsNum;    // 物流单号
    private String recyclefile;     // 故障件附件
    private String logisticsfile;   // 物流附件
    private String recyclefileName;     // 故障件附件显示
    private String logisticsfileName;   // 物流附件显示

    private Double transportExpense = 0.0;  // 运输费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;      // 费用合计
    private Date arriveDate; //预计送达
    private boolean received;//是否收货
    private Date rCVDate; //收货时间

    private boolean settlement = false;//是否结算

    private String comment;   //备注

    private List<RecycleItemInfo> recycleItemInfoList = new ArrayList<>();   //清单


}
