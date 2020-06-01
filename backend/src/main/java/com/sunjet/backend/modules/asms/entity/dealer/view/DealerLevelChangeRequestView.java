package com.sunjet.backend.modules.asms.entity.dealer.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 服务站等级变更申请
 * Created by zyf on 2017/8/4.
 */
@Data
@Entity
@Immutable
@Subselect("select " +
        "dlcr.obj_Id,dlcr.doc_no," +
        "dlcr.created_time," +
        "dlcr.submitter," +
        "dlcr.submitter_name," +
        "dlcr.status," +
        "bd.name,bd.code," +
        "bd.province_id," +
        "bd.star,bd.qualification, " +
        "dlcr.process_instance_id, " +
        "bd.service_manager_id " +
        "from dealer_level_change_requests dlcr,basic_dealers bd " +
        "where bd.obj_id = dlcr.dealer ")
public class DealerLevelChangeRequestView {

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
