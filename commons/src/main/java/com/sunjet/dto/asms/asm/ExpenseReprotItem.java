package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 费用速报清单VO
 */
@Data
public class ExpenseReprotItem extends FlowDocInfo implements Serializable {


    private String dealerCode;          // 服务站编号

    private String dealerName;          // 服务站名称

    private String serviceManager;      // 服务经理

    private String title;               // 速报名称

    private String vehicleType;         //车辆分类

    private Double estimatedCost;       // 预计费用

    private String costType;            // 费用类别  必输项，单选选项，选项内容：非质保；特殊维修；

    private String vin;
    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

}
