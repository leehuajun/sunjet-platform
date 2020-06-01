package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 故障件汇总明细
 */
@Data
public class RecycleSummaryItem {


    private String objId;   // 主键
    private Date createdTime;   // 创建时间
    //private String srcDocNo;    //来源单据
    private String dealerCode;    //服务站代码
    private String dealerName;    //服务站名称
    private String docNo;         // 单据编号
    private String transportExpense; //运费
    private String serviceManager; //服务经理


    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateTime();   //完工日期


}
