package com.sunjet.backend.modules.asms.entity.asm.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wfb on 17-8-11.
 */
@Data
@Entity
@Immutable
@Subselect("SELECT\n" +
        "\tafm.obj_id,\n" +
        "\tafm.doc_no,\n" +
        "\tafm.dealer_code,\n" +
        "\tafm.dealer_name,\n" +
        "\tafm.created_time,\n" +
        "\tafm.submitter_name,\n" +
        "\tafm.process_instance_id,\n" +
        "\tafm. `status`,\n" +
        "\tafm.service_manager,\n" +
        "\tafm.sender,\n" +
        "\tbv.vin,\n" +
        "\tbv.plate\n" +
        "FROM\n" +
        "\tasm_first_maintenances AS afm\n" +
        "LEFT JOIN basic_vehicles AS bv ON afm.vehicle_id = bv.obj_id")
public class FirstMaintenanceView implements Serializable {

    @Id
    private String objId;//主键
    private String docNo;//单据编号
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date createdTime;//申请时间
    private String submitterName;//申请时间
    private String processInstanceId;   // 流程实例Id
    private Integer status = 0;         // 表单状态
    private String vin;
    private String serviceManager;//服务经理
    private String sender;         // 送修人
    private String plate;          //车牌号
}
