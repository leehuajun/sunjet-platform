package com.sunjet.backend.modules.asms.entity.activity.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 活动分配单
 * Created by zyf on 2017/8/28.
 */
@Data
@Entity
@Immutable
@Subselect("SELECT\n" +
        "\taad.obj_Id,\n" +
        "\taad.doc_no,\n" +
        "\taad.dealer_code,\n" +
        "\taad.dealer_name,\n" +
        "\taad.created_time,\n" +
        "\taad.submitter_name,\n" +
        "\taad. STATUS,\n" +
        "\taad.activity_notice_id,\n" +
        "\taad.service_manager,\n" +
        "\taan.doc_no as activity_notice_doc_no,\n" +
        "\taad.repair \n" +
        "FROM\n" +
        "\tasm_activity_distributions aad\n" +
        "LEFT JOIN asm_activity_notices AS aan ON aan.obj_id = aad.activity_notice_id\n")
public class ActivityDistributionView {

    @Id
    private String objId;// 主键

    private String docNo;// 单据编号

    private String dealerCode;// 服务站编号

    private String dealerName;// 服务站名称

    private Date createdTime;// 申请日期

    private String submitterName;// 提交人姓名

    private String activityNoticeId; //活动通知单id

    private Integer status;// 表单状态

    private String activityNoticeDocNo;// 活动通知单单据编号

    private String serviceManager;  // 服务经理

    private Boolean repair = false;     // 是否已参加维修，默认为false
}
