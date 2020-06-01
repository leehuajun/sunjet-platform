package com.sunjet.backend.modules.asms.entity.asm.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/3.
 * 三包单列表视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT awm.obj_id, awm.doc_no, awm.dealer_code, awm.dealer_name, awm.created_time, awm.submitter_name, awm.process_instance_id, awm.vehicle_id, awm.`status`, awm.service_manager, awm.sender, bv.plate, bv.vin FROM asm_warranty_maintenances AS awm LEFT JOIN basic_vehicles AS bv ON bv.obj_id = awm.vehicle_id")
public class WarrantyMaintenanceView implements Serializable {
    @Id
    private String objId;    //主键
    private String docNo;    // 单据编号
    private String dealerCode;  // 服务站编号
    private String dealerName;   // 服务站名称
    private String processInstanceId;  // 流程Id
    private Date createdTime;   // 提交时间
    private String submitterName;   //提交人姓名
    private Integer status;     //单据状态
    private String serviceManager;   //服务经理
    private String vehicleId;  // 车辆id
    private String sender;         // 送修人
    private String plate;          //车牌号
    private String vin;            //车辆vin


}
