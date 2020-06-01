package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by SUNJET_WS on 2017/8/18.
 * 三包配件明细列表视图
 */
@Data
@Entity
@Immutable
@Subselect(" SELECT asni.obj_id, asni.part_code, asni.part_name, `acp`.`part_type`, `acp`.`pattern` AS `pattern`, `acp`.`reason` AS `reason`, `acp`.`amount` AS `acp_amount`, `acp`.`price` AS `price`, `acp`.`part_supply_type`, `acp`.`warranty_time`, `acp`.`warranty_mileage`, `asni`.`src_doc_no` AS `src_doc_no`, `bv`.`vin` AS `vin`, `bv`.`vsn` AS `vsn`, `bv`.`seller` AS `seller`, `bv`.`vehicle_model`, `bv`.`purchase_date`, `bv`.`mileage`, `bv`.`engine_no`, `bv`.`plate` AS `plate`, `bv`.`owner_name`, `bv`.`mobile` AS `mobile`, `awm`.`comment` AS `awm_comment`, `acp`.`part_classify`, ( CASE WHEN (`acp`.`recycle` = 1) THEN '是' ELSE '否' END ) AS `recycle`, `asn`.`src_doc_type`, `awm`.`dealer_code`, `awm`.`dealer_name`, `awm`.`submitter_name` AS `submitter_name`, `awm`.`dealer_phone` AS `submitter_phone`, `awm`.`service_manager`, `awm`.`quality_report_doc_no`, `awm`.`expense_report_doc_no`, `awm`.`province_name`, `awm`.`created_time` AS `awm_created_time`, `awm`.`pull_in_date`, `awm`.`dealer_star`, `asn`.`doc_no` AS `asn_doc_no`, `asn`.`created_time` AS `asn_created_time`, `asn`.`comment` AS `asncomment`, `asd`.`doc_no`, `asd`.`agency_name`, `asi`.`amount` AS `amount`, `asi`.`money` AS `money`, `asni`.`arrival_time`, `asd`.`rcv_date`, `asd`.`transportmodel` AS `transportmodel`, `asi`.`logistics_num` AS `logistics_num`, `asd`.`logistics` AS `logistics`, `asd`.`created_time` AS `asd_created_time`, `asd`.`dealer_adderss`, `asd`.`receive` AS `receive`, `asd`.`operator_phone`, ( CASE WHEN (`awm`.`status` = 0) THEN '草稿' WHEN (`awm`.`status` = 1) THEN '审核中' WHEN (`awm`.`status` = 2) THEN '已审核' WHEN (`awm`.`status` = 3) THEN '已关闭' WHEN (`awm`.`status` = -(1)) THEN '已退回' WHEN (`awm`.`status` = -(2)) THEN '已中止' WHEN (`awm`.`status` = -(3)) THEN '已作废' WHEN (`awm`.`status` = 1000) THEN '待结算' WHEN (`awm`.`status` = 1001) THEN '结算中' WHEN (`awm`.`status` = 1002) THEN '已结算' ELSE '无单据' END ) AS `awm_status`, ( CASE WHEN (`asn`.`status` = 0) THEN '草稿' WHEN (`asn`.`status` = 1) THEN '审核中' WHEN (`asn`.`status` = 2) THEN '已审核' WHEN (`asn`.`status` = 3) THEN '已关闭' WHEN (`asn`.`status` = -(1)) THEN '已退回' WHEN (`asn`.`status` = -(2)) THEN '已中止' WHEN (`asn`.`status` = -(3)) THEN '已作废' WHEN (`asn`.`status` = 1000) THEN '待结算' WHEN (`asn`.`status` = 1001) THEN '结算中' WHEN (`asn`.`status` = 1002) THEN '已结算' ELSE '无单据' END ) AS `asn_status`, ( CASE WHEN (`asd`.`status` = 0) THEN '草稿' WHEN (`asd`.`status` = 1) THEN '审核中' WHEN (`asd`.`status` = 2) THEN '已审核' WHEN (`asd`.`status` = 3) THEN '已关闭' WHEN (`asd`.`status` = -(1)) THEN '已退回' WHEN (`asd`.`status` = -(2)) THEN '已中止' WHEN (`asd`.`status` = -(3)) THEN '已作废' WHEN (`asd`.`status` = 1000) THEN '待结算' WHEN (`asd`.`status` = 1001) THEN '结算中' WHEN (`asd`.`status` = 1002) THEN '已结算' ELSE '无单据' END ) AS `asd_status` FROM asm_supply_notice_items AS asni LEFT JOIN asm_supply_notices AS asn ON asn.obj_id = asni.supply_notice_id LEFT JOIN asm_supply_items AS asi ON asi.supply_notice_item_id = asni.obj_id LEFT JOIN asm_supply_docs AS asd ON asd.obj_id = asi.supply_id LEFT JOIN asm_commission_parts AS acp ON acp.obj_id = asni.commission_part_id LEFT JOIN asm_warranty_maintenances AS awm ON awm.obj_id = asn.src_docid LEFT JOIN basic_vehicles AS bv ON bv.obj_id = awm.vehicle_id WHERE asn.`status` <> -3 AND asn.src_doc_type = '三包服务单' ")
public class WarrantyPartDetailView {

    @Id
    private String objId;   // 主键

    private String partCode;             //配件件号
    private String partName;             //配件名称
    private String partType;             // 配件类型
    private String pattern;               //故障模式
    private String reason;                //换件原因
    private String acpAmount;            //数量
    private String price;                 // 单价
    private String partSupplyType;      //供货方式
    private String warrantyTime;        //三包时间
    private String warrantyMileage;     //三包里程
    private String srcDocNo;           //服务单号
    private String vin;                  //VIN
    private String vsn;                   //VSN
    private String seller;                //经销商
    private String vehicleModel;         //车辆型号
    private String purchaseDate;         //购买日期
    private String mileage;               //行驶里程
    private String engineNo;               //发动机号
    private String plate;                   //车牌号
    private String ownerName;              //车主姓名
    private String mobile;                  //电话
    private String awmComment;               //备注
    private String partClassify;           //配件分类
    private String recycle;                 //是否返回旧件
    private String srcDocType;            //单据类型
    private String dealerCode;             //服务站编码
    private String dealerName;             //服务站名称
    private String submitterName;          //服务站联系人
    private String submitterPhone;         //服务站联系电话
    private String serviceManager;         //服务经理
    private String qualityReportDocNo;           //质量速报单号
    private String expenseReportDocNo;          //费用速报单号
    private String provinceName;           //省份
    private String awmCreatedTime;          //申请时间
    private String pullInDate;            //进站时间
    private String dealerStar;             //服务站星级
    private String asnDocNo;              //调拨单号

    private String asnCreatedTime;         //调拨单申请时间
    private String asncomment;              //备注
    private String agencyName;             //供货单号
    private String docNo;             //供货单号
    private String amount;              //供货数量
    private String money;               //配件费用
    private String arrivalTime;        //应到货时间
    private String rcvDate;            //到货时间
    private String transportmodel;      //发运方式
    private String logisticsNum;        //物流单号
    private String logistics;           //物流公司
    private String asdCreatedTime;       //提交时间
    private String dealerAdderss;          //收货地址
    private String receive;                 //收货人
    private String operatorPhone;            //收货电话
    private String awmStatus;                   //三包单状态
    private String asnStatus;          //调拨通知单状态
    private String asdStatus;                   //供货通知单状态


}
