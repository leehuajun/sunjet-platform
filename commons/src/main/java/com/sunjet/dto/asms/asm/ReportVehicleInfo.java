package com.sunjet.dto.asms.asm;

import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 报表车辆
 */
@Data
public class ReportVehicleInfo extends DocInfo implements Serializable {


    private VehicleInfo vehicle;   //车辆实体

    private String vehicle_id;     // 车辆Id

    private Date repairDate = new Date();            // 报修日期

    private String mileage;            // 行驶里程

    private String comment;             // 其它，备注


    private String crId;    //费用速报id

    private String qrId; //质量速报id


}
