package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 质量速报清单VO
 */
@Data
public class QualityReprotItem extends FlowDocInfo implements Serializable {


    private String dealerCode;          // 服务站编号

    private String dealerName;          // 服务站名称

    private String serviceManager;      // 服务经理

    private String title;               // 速报标题

    private String vehicleType;         //车辆分类

    private String reportType;          // 速报类别  必输项，单选选项，选项内容：普通；严重

    private String submitterName;       // 提交人姓名

    private String vin;

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期
}
