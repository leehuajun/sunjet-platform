package com.sunjet.dto.asms.report;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 服务明细报表
 */
@Data
public class MaintainDetailItem {
    private String objId;// 主键
    private String doc_no;   //单据编号
    private String dealer_code; //服务站编号
    private String dealer_name; ///服务站名称
    private String submitter_name;  //经办人
    private String submitter_phone;  //经办人电话
    private String province_name;  // 省份
    private String service_manager;  // 服务经理
    private Date createdTime;  //申请时间
    private Date pull_in_date;  // 进站时间
    private Date pull_out_date;  // 出站时间
    private String dealer_star;   //服务站星级
    private String doc_type;  // 单据类型
    private String quality_report_doc_no;  //质量速报
    private String expense_report_doc_no;  //费用速报
    private String activity_distribution_doc_no;  //活动单
    private String night_expense;  // 夜间补贴
    private String first_expense;  //首保费用标准
    private String hour_price;  //工时单价
    private String maintain_work_time_expense;  //项目工时费用
    private String out_work_time_expense;  //外出工时补贴
    private String out_expense;     //外出费用合计
    private String other_expense;  //其他费用合计
    private String accessories_expense;   //辅料费用合计
    private String part_expense;   // 配件费用合计
    private String expense_Total;   //费用合计
    private String settlement_accesories_expense;   // 应结算辅料费
    private String settlement_part_expense;  //应结算配件费用
    private String settlement_totle_expense;  // 应结算费用
    private String sender;  // 送修人
    private String sender_phone;   // 送修人电话
    private Date start_date = DateHelper.getFirstOfYear();   // 开工日期
    private Date end_date = DateHelper.getEndDateTime();   //完工日期
    private String repairer;  //主修人
    private String repair_type;  // 维修类别
    private String fault;   //故障描述

    private String vin;   // 车辆VIN
    private String vsn;   // 车辆VSN
    private String seller;   // 经销商
    private String vehicle_model;   //车型型号
    private String manufacture_date;   // 生产日期
    private String purchase_date;   //购车日期
    private String engine_Model;    // 发动机型号
    private String engine_no;    // 发动机号/电动机号
    private String mileage;    //行驶里程
    private String plate;     // 车牌号
    private String owner_name;  //车主
    private String vmt;    //服务里程
    private String mobile;    //电话
    private String address;   //详细地址
    private String place;   //外出地点
    private String ago_mileage;   //单向里程
    private String tran_costs;   // 交通费用
    private String trailer_mileage;   // 拖车里程
    private String trailer_cost;  // 拖车费用
    private String out_go_num;  //外出人数
    private String out_go_day;   //外出天数
    private String personnel_subsidy;   //人员补贴
    private String night_subsidy;   //住宿补贴
    private String amount_cost;   // 外出费用
    private String status;   // 当前状态


}
