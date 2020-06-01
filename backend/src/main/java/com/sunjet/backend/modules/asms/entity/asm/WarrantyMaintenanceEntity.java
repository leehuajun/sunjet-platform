package com.sunjet.backend.modules.asms.entity.asm;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 三包服务单
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmWarrantyMaintenances")
public class WarrantyMaintenanceEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714142924946835458L;
    @Column(length = 200)
    private String docType;         // 单据类型
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
    @Column(length = 100)
    private String dealerPhone;     // 服务站电话
    @Column(length = 50)
    private String dealerStar;      //服务站星级
    @Column(length = 50)
    private String provinceName;    // 省份

    private Date repairDate = new Date();        // 报修日期

    private Date pullInDate = new Date();        // 进站时间

    private Date pullOutDate = new Date();       // 出站时间

    private Date requestDate = new Date();       // 申请时间  日历控件，选择项，默认当前时间，可改
    //    private ExpenseReportEntity expenseReport; //费用速报
//    private QualityReportEntity qualityReport; //质量速报
    @Column(length = 200)
    private String comment;          //备注


    // 维修信息:    必输项：送修人、送修人电话
    @Column(length = 20)
    private String vmt;                    // 行驶里程
    @Column(length = 50)
    private String sender;          // 送修人
    @Column(length = 20)
    private String senderPhone;     // 送修人电话
    @Column(length = 50)
    private String repairer;        // 主修工

    private Date startDate = new Date();         // 开工日期

    private Date endDate = new Date();           // 完工日期
    @Column(length = 200)
    private String fault;           // 故障描述
    private Boolean takeAwayOldPart; // 带走旧件  单选，选项内容：是、否；
    private Boolean washing;        // 洗车    单选，选项内容：是、否；
    @Column(length = 50)
    private String repairType;      // 修理类别
    //三包凭证文件
    @Column(length = 200)
    private String ameplate;           // 铭牌
    @Column(length = 200)
    private String manual;           // 保养手册
    @Column(length = 200)
    private String odometer;           // 里程表
    @Column(length = 200)
    private String invoice;           // 购买发票
    @Column(length = 200)
    private String faultlocation;           // 故障部位
    @Column(length = 200)
    private String front45;           // 前侧45度

    //费用信息

    private Boolean nightWork = false;  // 夜间作业

    private Double nightExpense = 0.0;  // 夜间补贴

    private Double hourPrice = 0.0;     // 工时单价

    private Double outHours = 0.0;      // 外出工时

    private Double maintainHours = 0.0; // 维修工时

    private Double outWorkTimeExpense = 0.0; // 外出工时补贴费用合计

    private Double maintainWorkTimeExpense = 0.0; // 维修工时费用合计

    private Double workTimeExpense = 0.0; // 工 时费用合计系统带出，不能更改，计算公式：∑工时费用

    private Double partExpense = 0.0;     // 配件费用合计 系统带出，不能更改，计算公式：∑配件费用

    private Double outExpense = 0.0;      // 外出费用合计   系统带出，不能更改，计算公式：∑外出费用

    private Double accessoriesExpense = 0.0;   // 辅料费用合计 计算公式：∑物料类型为辅料费用

    private Double otherExpense = 0.0;    // 其他费用合计

    private Double expenseTotal = 0.0;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用

    private Double settlementAccesoriesExpense = 0.0;   // 应结算辅料费用

    private Double settlementPartExpense = 0.0;  // 应结算配件费用  ∑供货方式为自购的配件费用

    private Double settlementTotleExpense = 0.0;    // 应结算总费用


    @Column(length = 32)
    private String expenseReportId;     // 费用速报Id
    @Column(length = 32)
    private String expenseReportDocNo;     // 费用速报单号
    @Column(length = 32)
    private String qualityReportId;     // 质量速报Id
    @Column(length = 32)
    private String qualityReportDocNo;     // 质量速报单号
    @Column(length = 32)
    private String supplyNoticeId;          // 供货通知单Id(调拨单)
    @Column(length = 32)
    private String recycleNoticeId;         // 故障件返回通知单Id


    private Boolean canEditSupply = true;      // 是否允许编辑生成调拨通知单,流程提交后，变为false，不允许再生成调拨申请

    private Boolean canEditRecycle = false;     // 是否允许编辑生成故障件返回通知单  默认为false 可以生产故障件返回
//    private Boolean settlement = false;//是否结算

    private String vehicleId;  // 车辆

    //private List<WarrantyMaintainEntity> warrantyMaintains = new ArrayList<>();  // 维修项目列表


    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "WarrantyMaintenance")
    //private List<CommissionPartEntity> commissionParts = new ArrayList<>();           // 检查结果，零件信息


    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "WarrantyMaintenance")
    //private List<GoOutEntity> goOuts = new ArrayList<>();                // 出外子行

    private String agencyId;    // 经销商Id
    private String agencyName;  // 经销商名称
    private String agencyCode;  // 经销商名称

    private String typeCode;    // 车辆类型

}
