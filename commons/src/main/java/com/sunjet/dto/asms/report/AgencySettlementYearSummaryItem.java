package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商结算年汇总视图
 */
@Data
public class AgencySettlementYearSummaryItem {

    private String objId;   // 主键
    private String agencyName;       //合作商名称
    private String agencyCode;       //合作商编号
    private Double january;       //一月
    private Double february;       //二月
    private Double march;       //三月
    private Double april;       //四月
    private Double may;       //五月
    private Double june;       //六月
    private Double july;       //七月
    private Double august;       //八月
    private Double september;       //九月
    private Double october;       //十月
    private Double november;       //十一月
    private Double december;       //十二月
    private Date createdTime;     //创建时间

    private Date startDate = DateHelper.getFirstOfYear();   // 开工日期
    private Date endDate = DateHelper.getEndDateOfYear();   //完工日期

}
