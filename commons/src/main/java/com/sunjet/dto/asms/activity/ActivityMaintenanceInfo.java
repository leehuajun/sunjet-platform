package com.sunjet.dto.asms.activity;

import com.sunjet.dto.asms.asm.CommissionPartInfo;
import com.sunjet.dto.asms.asm.GoOutInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * <p>
 * 活动服务单表单VO
 */
@Data
public class ActivityMaintenanceInfo extends FlowDocInfo implements Serializable {


    private String docType;  //  单据类型
    private String operator;        // 经办人
    private String operatorPhone;   // 经办人电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String dealerStar;      //服务站星级
    private String provinceName;    // 省份
    private Date repairDate = new Date();        // 报修日期
    private Date pullInDate = new Date();        // 进站时间
    private Date pullOutDate = new Date();       // 出站时间
    private Date requestDate = new Date();       // 申请时间  日历控件，选择项，默认当前时间，可改
    //private ActivityDistributionInfo activityDistributionInfo;   //活动发布单
    private String vmt;             // 行驶里程

    // 维修信息:    必输项：送修人、送修人电话
    private String sender;          // 送修人
    private String senderPhone;     // 送修人电话
    private String repairer;        // 主修工
    private Date startDate = new Date();         // 开工日期
    private Date endDate = new Date();           // 完工日期
    private String fault;           // 故障描述
    private Boolean takeAwayOldPart = false; // 带走旧件  单选，选项内容：是、否；
    private Boolean washing;        // 洗车    单选，选项内容：是、否；
    private String comment; //备注

    //费用信息
    private Boolean nightWork = false;  // 夜间作业
    private Double nightExpense = 0.0;  // 夜间补贴
    private Double hourPrice = 0.0;     // 工时单价
    private Double outHours = 0.0;      // 外出工时
    private Double hourExpense = 0.0;   // 工时补贴合计费用
    private Double standardExpense = 0.0; // 活动标准费用
    private Double outExpense = 0.0;      // 外出费用   系统带出，不能更改，计算公式：∑外出费用
    private Double otherExpense = 0.0;   //  其他费用
    private Double expenseTotal = 0.0;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private List<GoOutInfo> goOuts = new ArrayList<GoOutInfo>(); //出外子行
    private List<CommissionPartInfo> commissionParts = new ArrayList<CommissionPartInfo>();           // 配件需求列表

    private String activityDistributionId;   //活动发布单
    private ActivityDistributionInfo activityDistribution;
    private String activityVehicleId; //活动车辆ID
    private ActivityVehicleItem activityVehicleItem; // 车辆
    private String typeCode;    // 车辆类型


}
