package com.sunjet.backend.modules.asms.entity.dealer.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 服务站变更申请View
 * Created by zyf on 2017/8/4.
 */
@Data
@Entity
@Immutable
@Subselect("select " +
        "dalrs.obj_Id," +
        "dalrs.doc_no," +
        "dalrs.created_time," +
        "dalrs.submitter," +
        "dalrs.submitter_name," +
        "dalrs.status," +
        "bd.name,bd.code," +
        "bd.province_id," +
        "dalrs.process_instance_id," +
        "bd.star,bd.qualification, " +
        "bd.service_manager_id " +
        "from dealer_alter_requests dalrs," +
        "basic_dealers bd " +
        "where bd.obj_id = dalrs.dealer ")
public class DealerAlterRequestView {

    @Id
    private String objId;// 主键

    private String docNo;// 单据编号

    private String code;// 服务站编号

    private String name;// 服务站名称

    private Date createdTime;// 申请时间

    private String submitter;// 申请人logId

    private String submitterName;// 申请人

    private Integer status;// 表单状态

    private String provinceId;//省份Id

    private String star;// 星级

    private String qualification;// 维修资质

    private String processInstanceId;   // 流程实例Id

    private String serviceManagerId;  // 服务经理id
}

