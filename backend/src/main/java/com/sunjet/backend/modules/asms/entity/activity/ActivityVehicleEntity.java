package com.sunjet.backend.modules.asms.entity.activity;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 活动通知车辆子行
 * Created by lhj on 16/9/15.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmActivityVehicles")
public class ActivityVehicleEntity extends DocEntity {
    private static final long serialVersionUID = -4210527209833371297L;
    private String mileage;            // 行驶里程
    private Date repairDate;            // 维修日期
    private Boolean distribute = false; // 是否已分配，默认为false
    private Boolean repair = false;     // 是否已参加维修，默认为false
    private String vehicleId;
    private String typeCode;    // 车辆型号
    private String activityNoticeId;
    private String activityDistributionId;
    private String activityMaintenanceId; //活动服务单

}
