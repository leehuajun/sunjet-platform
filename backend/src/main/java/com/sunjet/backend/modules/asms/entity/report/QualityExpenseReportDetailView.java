package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 质量费用速报明细视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT DISTINCT arv.obj_id, aer.title, ( CASE WHEN (LEFT(aer.doc_no, 4) = 'FYSB') THEN '费用速报' ELSE '无类型' END ) AS type, aer.doc_no, ( CASE WHEN (aer. STATUS = 0) THEN '草稿' WHEN (aer. STATUS = 1) THEN '审核中' WHEN (aer. STATUS = 2) THEN '已审核' WHEN (aer. STATUS = 3) THEN '已关闭' WHEN (aer. STATUS = -(1)) THEN '已退回' WHEN (aer. STATUS = -(2)) THEN '已中止' WHEN (aer. STATUS = -(3)) THEN '已作废' WHEN (aer. STATUS = 1000) THEN '待结算' WHEN (aer. STATUS = 1001) THEN '结算中' WHEN (aer. STATUS = 1002) THEN '已结算' ELSE '已作废' END ) AS STATUS, aer.dealer_code, aer.dealer_name, bv.type_name, aer.cost_type, '' AS report_type, aer.submitter_name, aer.submitter_phone, aer.service_manager, aer.service_manager_phone, aer. COMMENT, aer.fault_status, aer.fault_road, aer.fault_address, aer.fault_desc, aer.initial_reason, aer.decisions, aer.estimated_cost, arv.created_time, bv.vin, bv.vehicle_model, bv.engine_no, bv.owner_name, bv.mobile, bv.purchase_date, bv.mileage FROM asm_report_vehicles arv LEFT JOIN asm_expense_reports aer ON aer.obj_id = arv.cr_id LEFT JOIN basic_vehicles bv ON bv.obj_id = arv.vehicle_id WHERE aer. STATUS <> - (3) UNION SELECT DISTINCT arv.obj_id, aqr.title, ( CASE WHEN (LEFT(aqr.doc_no, 4) = 'ZLSB') THEN '质量速报' ELSE '无类型' END ) AS type, aqr.doc_no, ( CASE WHEN (aqr. STATUS = 0) THEN '草稿' WHEN (aqr. STATUS = 1) THEN '审核中' WHEN (aqr. STATUS = 2) THEN '已审核' WHEN (aqr. STATUS = 3) THEN '已关闭' WHEN (aqr. STATUS = -(1)) THEN '已退回' WHEN (aqr. STATUS = -(2)) THEN '已中止' WHEN (aqr. STATUS = -(3)) THEN '已作废' WHEN (aqr. STATUS = 1000) THEN '待结算' WHEN (aqr. STATUS = 1001) THEN '结算中' WHEN (aqr. STATUS = 1002) THEN '已结算' ELSE '已作废' END ) AS STATUS, aqr.dealer_code, aqr.dealer_name, bv.type_name, '' AS cost_type, aqr.report_type, aqr.submitter_name, aqr.submitter_phone, aqr.service_manager, aqr.service_manager_phone, aqr. COMMENT, aqr.fault_status, aqr.fault_road, aqr.fault_address, aqr.fault_desc, aqr.initial_reason, aqr.decisions, '' AS estimated_cost, aqr.created_time, bv.vin, bv.vehicle_model, bv.engine_no, bv.owner_name, bv.mobile, bv.purchase_date, bv.mileage FROM asm_report_vehicles arv LEFT JOIN asm_quality_reports aqr ON aqr.obj_id = arv.qr_id LEFT JOIN basic_vehicles bv ON bv.obj_id = arv.vehicle_id WHERE aqr. STATUS <> - (3)")
public class QualityExpenseReportDetailView {

    @Id
    private String objId;   // 主键
    private String title;   // 标题
    private String type;   // 单据类型
    private String docNo;   // 单据编号
    private String status;   // 单据状态
    private String dealerCode;   // 服务站编号
    private String dealerName;   // 服务站名称
    private String typeName;   // 车辆类型
    private String costType;   // 费用类型
    private String reportType;   // 速报类型
    private String submitterName;   // 提交人
    private String submitterPhone;   // 提交人电话
    private String serviceManager;   // 服务经理
    private String serviceManagerPhone;   // 服务经理电话
    private String comment;   // 备注
    private String faultStatus;   // 故障时行驶状态
    private String faultRoad;   // 故障时路面情况
    private String faultAddress;   // 故障发生地点
    private String faultDesc;   // 故障描述
    private String initialReason;   // 初步原因分析
    private String decisions;   // 处理意见
    private String estimatedCost;   // 预计费用
    private String createdTime;   // 创建时间
    private String vin;   // vin
    private String vehicleModel;   // 车辆型号
    private String engineNo;   //发动机号
    private String ownerName;   //车主
    private String mobile;   //手机号
    private Date purchaseDate;   //车辆购买日期
    private String mileage;   //行驶里程


}
