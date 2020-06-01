package com.sunjet.backend.modules.asms.entity.asm;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 车辆子行
 * Created by lhj on 16/9/15.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmReportVehicles")
public class ReportVehicleEntity extends DocEntity {
    private static final long serialVersionUID = -4210527209833371297L;

    //@ManyToOne(cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "VehicleId")
    private String vehicle_id;      // 车辆

    @Column(length = 20)
    private String mileage;            // 行驶里程

    private Date repairDate = new Date();            // 报修日期
    @Column(length = 200)
    private String comment;             // 其它，备注

    //@ManyToOne
    //@JoinColumn(name="CrId")
    //private ExpenseReportEntity expenseReportEntity; //费用速报
    private String crId;    //费用速报id
    //@ManyToOne
    //@JoinColumn(name="QrId")
    private String qrId; //质量速报id

}
