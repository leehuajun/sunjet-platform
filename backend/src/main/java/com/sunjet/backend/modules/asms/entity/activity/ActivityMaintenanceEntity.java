package com.sunjet.backend.modules.asms.entity.activity;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 活动服务单
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmActivityMaintenances")
public class ActivityMaintenanceEntity extends FlowDocEntity {
    private static final long serialVersionUID = -157423414398173934L;
    @Column(length = 50)
    private String operator;        // 经办人
    @Column(length = 20)
    private String operatorPhone;   // 经办人电话
    @Column(length = 50)
    private String serviceManager;  // 服务经理
    @Column(length = 50)
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    @Column(length = 100)
    private String dealerName;      // 服务站名称
    @Column(length = 50)
    private String dealerStar;      //服务站星级
    @Column(length = 50)
    private String provinceName;    // 省份

    private Date repairDate = new Date();        // 报修日期

    private Date pullInDate = new Date();        // 进站时间

    private Date pullOutDate = new Date();       // 出站时间

    private Date requestDate = new Date();       // 申请时间  日历控件，选择项，默认当前时间，可改

    @Column(name = "Vmt", length = 20)
    private String vmt;             // 行驶里程
    // 维修信息:    必输项：送修人、送修人电话
    @Column(length = 50)
    private String sender;          // 送修人
    @Column(length = 20)
    private String senderPhone;     // 送修人电话
    @Column(length = 50)
    private String repairer;        // 主修工

    private Date startDate = new Date();         // 开工日期

    private Date endDate = new Date();           // 完工日期
    @Column(length = 500)
    private String fault;           // 故障描述

    private Boolean takeAwayOldPart = false; // 带走旧件  单选，选项内容：是、否；
    private Boolean washing;        // 洗车    单选，选项内容：是、否；
    @Column(length = 200)
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


    private String activityDistributionId;   //活动发布单


    private String activityVehicleId; //活动车辆ID


    private String typeCode;    // 车辆类型
}