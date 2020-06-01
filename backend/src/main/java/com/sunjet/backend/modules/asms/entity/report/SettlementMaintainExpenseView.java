package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 服务站结算明细
 */
@Data
@Entity
@Immutable
@Subselect("SELECT adsd.obj_id, `adsd`.`created_time`, `adsd`.`dealer_code`, `adsd`.`dealer_name`, `adsd`.`doc_no`, ael.warranty_expense_total, ael.first_expense_total, ael.activity_expense_total, '' AS `freight_expense_total`, ( `adsd`.`reward_expense` - `adsd`.`punishment_expense` ) AS `reward_punishment_expense`, adsd.expense_total FROM `asm_dealer_settlement_docs` `adsd`, ( SELECT ael.dealer_settlement_id, sum( IF ( ( `ael`.`src_doc_type` = '三包服务单' ), `ael`.`expense_total`, 0 ) ) AS `warranty_expense_total`, sum( IF ( ( `ael`.`src_doc_type` = '首保服务单' ), `ael`.`expense_total`, 0 ) ) AS `first_expense_total`, sum( IF ( ( `ael`.`src_doc_type` = '活动服务单' ), `ael`.`expense_total`, 0 ) ) AS `activity_expense_total` FROM asm_expense_list AS ael LEFT JOIN asm_dealer_settlement_docs AS adsd ON ael.dealer_settlement_id = adsd.obj_id WHERE adsd.`status` <> -3 GROUP BY ael.dealer_settlement_id ) AS ael WHERE ael.dealer_settlement_id = adsd.obj_id UNION SELECT afsd.obj_id, `afsd`.`created_time`, `afsd`.`dealer_code`, `afsd`.`dealer_name`, `afsd`.`doc_no`, '' AS `Name_exp_12`, '' AS `Name_exp_13`, '' AS `Name_exp_14`, afe.freight_expense_total, ( `afsd`.`reward_expense` - `afsd`.`punishment_expense` ) AS `reward_punishment_expense`, afsd.expense_total FROM `asm_freight_settlement_docs` `afsd`, ( SELECT afe.freight_settlement_id, sum( IF ( ( `afe`.`src_doc_type` = '故障件返回单' ), `afe`.`expense_total`, 0 ) ) AS `freight_expense_total` FROM asm_freight_expense AS afe LEFT JOIN asm_freight_settlement_docs AS afsd ON afe.freight_settlement_id = afsd.obj_id WHERE afsd.`status` <> - 3 GROUP BY afe.freight_settlement_id ) AS afe WHERE afsd.obj_id = afe.freight_settlement_id ")
public class SettlementMaintainExpenseView {

    @Id
    private String objId;   // 主键

    private String dealerCode;  //服务站编号

    private String dealerName;  //服务站名称

    private String docNo;   //结算编号

    private Double warrantyExpenseTotal;  //三包费用总计

    private Double firstExpenseTotal;  //首保费用总计

    private Double activityExpenseTotal;  //活动费用总计

    private Double freightExpenseTotal;  //故障件运费用总计


    private Double rewardPunishmentExpense;  //奖惩费用总计

    private Double expenseTotal;  //费用总计


    private Date createdTime;  //创建时间


}
