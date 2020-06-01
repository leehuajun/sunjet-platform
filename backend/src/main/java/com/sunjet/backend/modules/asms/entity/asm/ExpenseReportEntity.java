package com.sunjet.backend.modules.asms.entity.asm;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/9/7.
 * <p>
 * 费用速报实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmExpenseReports")
public class ExpenseReportEntity extends FlowDocEntity {
    private static final long serialVersionUID = 3720803619098277432L;
    @Column(length = 200)
    private String title;               // 速报标题
    @Column(length = 32)
    private String dealerCode;          // 服务站编号
    @Column(length = 50)
    private String dealerName;          // 服务站名称
    @Column(length = 32)
    private String costType;            // 费用类别  必输项，单选选项，选项内容：非质保；特殊维修；
    @Column(length = 50)
    private String linkman;             // 联系人
    @Column(length = 20)
    private String linkmanPhone;        // 联系人电话
    @Column(length = 50)
    private String serviceManager;      // 服务经理
    @Column(length = 20)
    private String serviceManagerPhone;  // 服务经理电话
    @Column(length = 50)
    private String vehicleType;         // 车辆分类

    @Column(length = 500)
    private String faultDesc;           // 故障描述
    @Column(length = 500)
    private String faultStatus;         // 故障时行驶状态
    @Column(length = 500)
    private String faultRoad;           // 故障时路面情况
    @Column(length = 500)
    private String faultAddress;        // 故障发生地点
    @Column(length = 500)
    private String initialReason;       // 初步原因分析
    @Column(length = 500)
    private String decisions;           // 处置意见
    @Column(length = 200)
    private String file;                // 上传附件
    private Double estimatedCost;       // 预计费用
    @Column(length = 200)
    private String originFile;          // 原始文件名
    @Column(length = 200)
    private String comment;             //备注


    private Boolean canEditType = false;     // 是否可以编辑分类


    //@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    //@JoinColumn(name="CrId")
    //private List<ReportVehicleEntity> reportVehicles = new ArrayList<>();     // 车辆列表

    //@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    //@JoinColumn(name="CrId")
    //private List<ReportPartEntity> reportParts = new ArrayList<>();           // 检查结果，零件信息

}
