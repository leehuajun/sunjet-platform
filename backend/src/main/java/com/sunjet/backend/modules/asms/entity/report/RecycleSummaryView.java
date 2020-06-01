package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 故障件汇总明细
 */
@Data
@Entity
@Immutable
@Subselect("SELECT DISTINCT ard.obj_id, ard.created_time, ard.dealer_code, ard.dealer_name, ard.doc_no, ard.transport_expense, ard.service_manager FROM asm_recycle_docs ard WHERE ard. STATUS <> '-3'")
public class RecycleSummaryView {

    @Id
    private String objId;   // 主键
    private Date createdTime;   // 创建时间
    private String dealerCode;    //服务站代码
    private String dealerName;    //服务站名称
    private String docNo;         // 单据编号
    private String transportExpense; //运费
    private String serviceManager; //服务经理


}
