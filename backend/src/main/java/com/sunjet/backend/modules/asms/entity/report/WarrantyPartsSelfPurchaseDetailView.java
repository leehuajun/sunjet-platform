package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 三包配件自购明细
 */
@Data
@Entity
@Immutable
@Subselect("SELECT acp.obj_id, ( CASE WHEN (`awm`.`status` = 0) THEN '草稿' WHEN (`awm`.`status` = 1) THEN '审核中' WHEN (`awm`.`status` = 2) THEN '已审核' WHEN (`awm`.`status` = 3) THEN '已关闭' WHEN (`awm`.`status` = -(1)) THEN '已退回' WHEN (`awm`.`status` = -(2)) THEN '已中止' WHEN (`awm`.`status` = -(3)) THEN '已作废' WHEN (`awm`.`status` = 1000) THEN '待结算' WHEN (`awm`.`status` = 1001) THEN '结算中' WHEN (`awm`.`status` = 1002) THEN '已结算' ELSE '已作废' END ) AS `status`, acp.part_code, acp.part_name, acp.part_type, acp.pattern, acp.reason, acp.amount, acp.price, acp.part_supply_type, acp.warranty_time, acp.warranty_mileage, awm.doc_no, bv.vin, bv.vsn, bv.seller, bv.vehicle_model, bv.purchase_date, bv.mileage, bv.engine_no, bv.plate, bv.owner_name, bv.mobile, acp.`comment`, acp.part_classify, ( CASE WHEN (`acp`.`recycle` = TRUE) THEN '是' ELSE '否' END ) AS recycle, awm.doc_type, awm.dealer_name, awm.dealer_code, awm.submitter_name, awm.dealer_phone, awm.service_manager, awm.quality_report_doc_no, awm.expense_report_doc_no, awm.province_name, awm.created_time, awm.pull_in_date, awm.dealer_star FROM asm_commission_parts AS acp LEFT JOIN asm_warranty_maintenances AS awm ON acp.warranty_maintenance = awm.obj_id LEFT JOIN basic_vehicles AS bv ON awm.vehicle_id = bv.obj_id WHERE part_supply_type = '自购' AND awm.`status` != - 3")
public class WarrantyPartsSelfPurchaseDetailView {

    @Id
    private String objId;   // 主键

    private String status;  //状态

    private String partCode;  //配件号

    private String partName;   //配件名称

    private String partType;   //配件类型

    private String pattern;   //故障模式

    private String reason;   //换件原因

    private String amount;   //数量

    private String price;   //价格

    private String partSupplyType;    //供货方式

    private String warrantyTime;    //三包时间

    private String warrantyMileage;   //三包里程

    private String docNo;   //服务单号

    private String vin;     //车辆vin

    private String vsn;     //车辆vsn

    private String seller;      //销售商

    private String vehicleModel;    //车辆型号

    private Date purchaseDate;    //购买日期

    private String mileage; //行驶里程

    private String engineNo;    //发动机号码

    private String plate;   //车牌号

    private String ownerName;   //车主姓名

    private String mobile;      //车主电话

    private String comment;     //备注

    private String partClassify;    //配件分类

    private String recycle;     //是否返回

    private String docType;     //单据类型

    private String dealerName;      //服务站名称

    private String dealerCode;      //服务站名称

    private String submitterName;       //服务站联系人

    private String dealerPhone;     //服务站电话

    private String serviceManager;      //服务经理

    private String qualityReportDocNo;  //质量速报

    private String expenseReportDocNo;  //费用速报

    private String provinceName;    //省份

    private Date createdTime;     //提交时间

    private Date pullInDate;      //进站时间

    private String dealerStar;      //服务站星级


}
