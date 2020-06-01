package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商供货汇总
 */
@Data
public class AgencySupplySummaryItem {


    private String objId;   // 主键
    private String status;   // 状态
    private String agencyCode;       //合作商编号
    private String agencyName;   //合作商名字
    private String createdTime;     //提交时间
    private String docNo;     //单据编号
    //private String srcDocNo;     //来源单据编号
    //private String srcDocType;     //来源单据类型
    private String partExpense;     //配件费用
    private String transportExpense;     //运费费用
    private String otherExpense;     //其他费用
    private String expenseTotal;     //费用总计


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期


}
