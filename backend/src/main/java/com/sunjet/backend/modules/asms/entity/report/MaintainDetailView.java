package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/16.
 * 服务明细报表
 */
@Data
@Entity
@Immutable
@Subselect("SELECT awm.obj_id, awm.created_time, awm.dealer_code AS dealer_code, awm.dealer_name AS dealer_name, awm.province_name AS province_name, awm.doc_no AS doc_no, awm.service_manager AS service_manager, awm.start_date AS start_date, awm.end_date AS end_date, awm.repair_type AS repair_type, awm.fault AS fault, bv.vin AS vin, bv.vehicle_model AS vehicle_model, bv.engine_model AS engine_Model, bv.engine_no AS engine_no, bv.owner_name AS owner_name, bv.mobile AS mobile, '' AS first_expense, convert(awm.out_expense,decimal(10,2)) AS out_expense, convert(awm.work_time_expense,decimal(10,2)) AS work_time_expense, convert(awm.part_expense,decimal(10,2))AS part_expense, convert(awm.other_expense,decimal(10,2)) AS other_expense, convert(awm.night_expense,decimal(10,2)) AS night_expense, convert(awm.expense_total,decimal(10,2)) AS expense_Total, ( CASE WHEN ( LEFT (`awm`.`doc_no`, 4) = 'WMFW' ) THEN '三包服务单' ELSE '无类型' END ) AS `doc_type`, ( CASE WHEN (awm. STATUS = 0) THEN '草稿' WHEN (awm. STATUS = 1) THEN '审核中' WHEN (awm. STATUS = 2) THEN '已审核' WHEN (awm. STATUS = 3) THEN '已关闭' WHEN (awm. STATUS = -(1)) THEN '已退回' WHEN (awm. STATUS = -(2)) THEN '已中止' WHEN (awm. STATUS = -(3)) THEN '已作废' WHEN (awm. STATUS = 1000) THEN '待结算' WHEN (awm. STATUS = 1001) THEN '结算中' WHEN (awm. STATUS = 1002) THEN '已结算' ELSE '已作废' END ) AS STATUS, awm.submitter_name AS submitter_name, awm.dealer_phone AS submitter_phone, awm.pull_in_date AS pull_in_date, awm.pull_out_date AS pull_out_date, awm.dealer_star AS dealer_star, awm.expense_report_doc_no AS expense_report_doc_no, awm.quality_report_doc_no AS quality_report_doc_no, '' AS activity_distribution_doc_no, awm.hour_price AS hour_price, awm.out_work_time_expense AS out_work_time_expense, awm.accessories_expense AS accessories_expense, awm.settlement_accesories_expense AS settlement_accesories_expense, awm.settlement_part_expense AS settlement_part_expense, awm.settlement_totle_expense AS settlement_totle_expense, awm.sender AS sender, awm.sender_phone AS sender_phone, awm.repairer AS repairer, bv.vsn AS vsn, bv.seller AS seller, bv.manufacture_date AS manufacture_date, bv.purchase_date AS purchase_date, bv.product_date AS product_date, bv.mileage AS mileage, bv.plate AS plate, bv.vmt AS vmt, bv.address AS address, ago.place AS place, ago.mileage AS ago_mileage, ago.tran_costs AS tran_costs, ago.trailer_mileage AS trailer_mileage, ago.trailer_cost AS trailer_cost, ago.out_go_num AS out_go_num, ago.out_go_day AS out_go_day, ago.personnel_subsidy AS personnel_subsidy, ago.night_subsidy AS night_subsidy, ago.amount_cost AS amount_cost, awm.maintain_work_time_expense AS maintain_work_time_expense FROM asm_warranty_maintenances AS awm LEFT JOIN asm_go_outs AS ago ON awm.obj_id = ago.warranty_maintenance LEFT JOIN basic_vehicles AS bv ON awm.vehicle_id = bv.obj_id WHERE awm.`status` > -3 UNION SELECT aam.obj_id, aam.created_time, aam.dealer_code, aam.dealer_name, aam.province_name, aam.doc_no, aam.service_manager, aam.start_date, aam.end_date, '' AS Name_exp_30, aam.fault, bv.vin, bv.vehicle_model, bv.engine_model, bv.engine_no, bv.owner_name, bv.mobile, '' AS Name_exp_37, convert(aam.out_expense,decimal(10,2)), convert(aam.hour_expense,decimal(10,2)), '' AS Name_exp_40, convert(aam.other_expense,decimal(10,2)), convert(aam.night_expense,decimal(10,2)), convert(aam.expense_total,decimal(10,2)), ( CASE WHEN ( LEFT (`aam`.`doc_no`, 4) = 'AMFW' ) THEN '活动服务单' ELSE '无类型' END ) AS `doc_type`, ( CASE WHEN (aam. STATUS = 0) THEN '草稿' WHEN (aam. STATUS = 1) THEN '审核中' WHEN (aam. STATUS = 2) THEN '已审核' WHEN (aam. STATUS = 3) THEN '已关闭' WHEN (aam. STATUS = -(1)) THEN '已退回' WHEN (aam. STATUS = -(2)) THEN '已中止' WHEN (aam. STATUS = -(3)) THEN '已作废' WHEN (aam. STATUS = 1000) THEN '待结算' WHEN (aam. STATUS = 1001) THEN '结算中' WHEN (aam. STATUS = 1002) THEN '已结算' ELSE '已作废' END ) AS STATUS, aam.submitter_name, aam.submitter_phone, aam.pull_in_date, aam.pull_out_date, aam.dealer_star, '', '', aad.doc_no, '', '', '', '', '', '', aam.sender, aam.sender_phone, aam.repairer, bv.vsn, bv.seller, bv.manufacture_date, bv.purchase_date, bv.product_date, bv.mileage, bv.plate, bv.vmt, bv.address, ago.place, ago.mileage, ago.tran_costs, ago.trailer_mileage, ago.trailer_cost, ago.out_go_num, ago.out_go_day, ago.personnel_subsidy, ago.night_subsidy, ago.amount_cost, '' AS maintainWorkTimeExpense FROM asm_activity_maintenances AS aam LEFT JOIN asm_activity_vehicles AS aav ON aam.activity_vehicle_id = aav.obj_id LEFT JOIN basic_vehicles AS bv ON aav.vehicle_id = bv.obj_id LEFT JOIN asm_activity_distributions AS aad ON aam.activity_distribution_id = aad.obj_id LEFT JOIN asm_go_outs AS ago ON ago.activity_maintenance_id = aam.obj_id WHERE aam.`status` > -3 UNION SELECT afmd.obj_id, afmd.created_time, afmd.dealer_code, afmd.dealer_name, afmd.province_name, afmd.doc_no, afmd.service_manager, afmd.start_date, afmd.end_date, '' AS Name_exp_51, afmd.fault, bv.vin, bv.vehicle_model, bv.engine_model, bv.engine_no, bv.owner_name, bv.mobile, afmd.standard_expense, convert(afmd.out_expense,decimal(10,2)), convert(afmd.hour_expense,decimal(10,2)), '' AS Name_exp_61, convert(afmd.other_expense,decimal(10,2)), convert(afmd.night_expense,decimal(10,2)), convert(afmd.expense_total,decimal(10,2)), ( CASE WHEN ( LEFT (`afmd`.`doc_no`, 4) = 'FMFW' ) THEN '首保服务单' ELSE '无类型' END ) AS `doc_type`, ( CASE WHEN (afmd. STATUS = 0) THEN '草稿' WHEN (afmd. STATUS = 1) THEN '审核中' WHEN (afmd. STATUS = 2) THEN '已审核' WHEN (afmd. STATUS = 3) THEN '已关闭' WHEN (afmd. STATUS = -(1)) THEN '已退回' WHEN (afmd. STATUS = -(2)) THEN '已中止' WHEN (afmd. STATUS = -(3)) THEN '已作废' WHEN (afmd. STATUS = 1000) THEN '待结算' WHEN (afmd. STATUS = 1001) THEN '结算中' WHEN (afmd. STATUS = 1002) THEN '已结算' ELSE '已作废' END ) AS STATUS, afmd.submitter_name, afmd.submitter_phone, afmd.pull_in_date, afmd.pull_out_date, afmd.dealer_star, '', '', '', afmd.hours, '', '', '', '', '', afmd.sender, afmd.sender_phone, afmd.repairer, bv.vsn, bv.seller, bv.manufacture_date, bv.purchase_date, bv.product_date, bv.mileage, bv.plate, bv.vmt, bv.address, ago.place, ago.mileage, ago.tran_costs, ago.trailer_mileage, ago.trailer_cost, ago.out_go_num, ago.out_go_day, ago.personnel_subsidy, ago.night_subsidy, ago.amount_cost, '' FROM asm_first_maintenances as afmd LEFT JOIN basic_vehicles AS bv ON bv.obj_id = afmd.vehicle_id LEFT JOIN asm_go_outs AS ago ON ago.first_maintenance_id = afmd.obj_id WHERE afmd.`status` > -3")
public class MaintainDetailView {
    @Id
    private String objId;   // 主键
    private String doc_no;       //单据名称
    private String dealer_code;   //服务站编号
    private String dealer_name;     //服务站名称
    private String submitter_name;  //提交人姓名
    private String submitter_phone;     //提交人电话
    private String province_name;       //省份
    private String service_manager;         //服务经理
    private Date createdTime;              //创建时间
    private Date pull_in_date;     //进站时间
    private Date pull_out_date;   //出站时间
    private String dealer_star;    //服务站星级
    private String doc_type;     //单据类型
    private String quality_report_doc_no;    //质量速报
    private String expense_report_doc_no;    //费用速报
    private String activity_distribution_doc_no;    //活动单
    private String night_expense;   //夜间补贴
    private String first_expense;   //首保费用标准
    private String hour_price;     //工时单价
    private String maintain_work_time_expense;   //项目工时费用
    private String out_expense;     //外出费用合计
    private String out_work_time_expense;    //外出工时补贴
    private String other_expense;    //其他费用
    private String accessories_expense;    //辅料费用合计
    private String part_expense;    //配件费用合计
    private String expense_Total;   //费用合计
    private String settlement_accesories_expense;   //应结算辅料费
    private String settlement_part_expense;   //应结算配件费用
    private String settlement_totle_expense;   //应结算费用
    private String sender;   //送修人
    private String sender_phone;   //送修人电话
    private Date start_date;  //开工日期
    private Date end_date;   //完工日期
    private String repairer;   //主修人
    private String repair_type;   //维修类别
    private String fault;   //故障描述
    private String vin;   //VIN
    private String vsn;   //VSN
    private String seller;    //经销商
    private String vehicle_model;   //车型型号
    private String manufacture_date;   // 生产日期
    private String purchase_date;   //购车日期
    private String engine_Model;   // 发动机型号
    private String engine_no;   //发动机号/电动机号
    private String mileage;   //行驶里程
    private String plate;   //车牌号
    private String owner_name;   //车主
    private String vmt;   // 服务里程
    private String mobile;   //电话
    private String address;   //详细地址
    private String place;  //车牌号
    private String ago_mileage;   //外出里程
    private String tran_costs;      //交通费用
    private String trailer_mileage;   //拖车里程
    private String trailer_cost;    //拖车费用
    private String out_go_num;    //  外出人数
    private String out_go_day;    //外出天数
    private String personnel_subsidy;   //人员补贴
    private String night_subsidy;   //住宿补贴
    private String amount_cost;   //外出费用
    private String status;   //当前状态


}
