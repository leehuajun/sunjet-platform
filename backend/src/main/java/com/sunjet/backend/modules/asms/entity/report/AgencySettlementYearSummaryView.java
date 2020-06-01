package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商结算年汇总视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT '' AS obj_id, asm_agency_settlement_docs.created_time, asm_agency_settlement_docs.agency_code, `asm_agency_settlement_docs`.`agency_name`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '01' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `January`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '02' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `February`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '03' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `March`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '04' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `April`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '05' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `May`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '06' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `June`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '07' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `July`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '08' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `August`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '09' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `September`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '10' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `October`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '11' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `November`, sum( IF ( ( date_format( `asm_agency_settlement_docs`.`created_time`, '%Y%m' ) = concat( date_format( asm_agency_settlement_docs.created_time, '%Y' ), '12' ) ), `asm_agency_settlement_docs`.`part_expense`, 0 ) ) AS `December` FROM `asm_agency_settlement_docs` WHERE `status` <> -3 GROUP BY `asm_agency_settlement_docs`.created_time, `asm_agency_settlement_docs`.agency_code, `asm_agency_settlement_docs`.agency_name")
public class AgencySettlementYearSummaryView {
    @Id
    private String objId;   // 主键
    private String agencyName;       //合作商名称
    private String agencyCode;       //合作商编号
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
