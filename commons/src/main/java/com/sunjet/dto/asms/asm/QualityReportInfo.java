package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 质量速报VO
 */
@Data
public class QualityReportInfo extends FlowDocInfo implements Serializable {

    //private String docNo;       // 单据类型
    private String title;       // 速报标题
    private String dealerCode;  // 服务站编号
    private String dealerName;  // 服务站名称
    private String reportType;  // 速报类别  必输项，单选选项，选项内容：普通；严重
    private String linkman;     // 联系人
    private String linkmanPhone;    // 联系人电话
    private String serviceManager;  // 服务经理
    private String serviceManagerPhone;  // 服务经理电话
    private String vehicleType;  //车辆分类
    private String faultDesc;       // 故障描述
    private String faultStatus;     // 故障时行驶状态
    private String faultRoad;       // 故障时路面情况
    private String faultAddress;    // 故障发生地点
    private String initialReason;   // 初步原因分析
    private String decisions;       // 处置意见
    private String file;            // 上传附件
    private String originFile;      // 原始文件名
    private String comment;   //备注

    private Boolean canEditType = false;     // 是否可以编辑分类

    private List<ReportVehicleInfo> reportVehicleInfos = new ArrayList<ReportVehicleInfo>(); //报表车辆
    private List<ReportPartInfo> reportPartInfos = new ArrayList<ReportPartInfo>(); // 报表配件


}
