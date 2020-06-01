package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商结算明细视图
 */
@Data
public class AgencySettlementDetailItem {

    private String objId;   // 主键
    private String status;   // 单据状态
    private String agencyName;   // 合作商名称
    private String agencyCode;   // 合作商名称
    private String createdTime;   // 创建时间
    private String docNo;   // 单据编号
    private String partExpense;   // 配件费用
    private String freightExpense;   // 运费费用
    private String expenseTotal;   // 费用总计


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期


}
