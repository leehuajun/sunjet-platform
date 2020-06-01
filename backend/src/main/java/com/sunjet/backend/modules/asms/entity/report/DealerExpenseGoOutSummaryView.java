package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 服务站服务外出费用年度汇总
 */
@Data
@Entity
@Immutable
@Subselect("SELECT '' AS obj_id, settlement_total.created_time, settlement_total.dealer_code, `settlement_total`.`dealer_name`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '01' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `january`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '02' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `february`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '03' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `march`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '04' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `april`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '05' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `may`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '06' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `june`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '07' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `july`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '08' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `august`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '09' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `september`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '10' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `october`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '11' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `november`, sum( IF ( ( date_format( `settlement_total`.`created_time`, '%Y%m' ) = concat( date_format( settlement_total.created_time, '%Y' ), '12' ) ), `settlement_total`.`expense_total`, 0 ) ) AS `december` FROM ( SELECT `adsd`.`obj_id`, `adsd`.`created_time`, adsd.dealer_code, `adsd`.`dealer_name`, `adsd`.`doc_no`, adsd.out_expense AS expense_total FROM `asm_dealer_settlement_docs` `adsd` WHERE adsd.`status` <> -3 ) AS settlement_total GROUP BY settlement_total.dealer_name, settlement_total.dealer_code, settlement_total.created_time")
public class DealerExpenseGoOutSummaryView {
    @Id
    private String objId;   // 主键
    private String dealerCode;   //服务站编号
    private String dealerName;   //服务站名称
    private Double january;       //一月
    private Double february;       //二月
    private Double march;       //三月
    private Double april;       //四月
    private Double may;       //五月
    private Double june;       //六月
    private Double july;       //七月
    private Double august;       //八月
    private Double september;       //九月
    private Double october;       //十月
    private Double november;       //十一月
    private Double december;       //十二月
    private Date createdTime;     //创建时间

}
