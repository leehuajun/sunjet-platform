package com.sunjet.backend.modules.asms.entity.asm.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_QRY on 2017/8/3.
 * 售后费用速报  视图
 */
@Data
@Entity
@Immutable
@Subselect("select (select GROUP_CONCAT(vin) from basic_vehicles where obj_id in (\n" +
        "\tselect vehicle_id from asm_report_vehicles where cr_id=er.obj_id)) as vin,er.*\n" +
        "from `asm_expense_reports` er\n")
public class ExpenseReportView implements Serializable {

    @Id
    private String objId;  // 主键id

    private String docNo;  // 单据编号

    private String dealerCode;          // 服务站编号

    private String dealerName;          // 服务站名称

    private String title;               // 速报名称

    private String vehicleType;         //车辆分类

    private Double estimatedCost;       // 预计费用

    private String costType;            // 费用类别  必输项，单选选项，选项内容：非质保；特殊维修；

    private Date createdTime;     // 申请日期

    private String submitterName;   // 申请人

    private String handler;             // 当前处理人

    private Integer status;         // 表单状态

    private String vin;   //车辆vin

    private String serviceManager;      // 服务经理

}
