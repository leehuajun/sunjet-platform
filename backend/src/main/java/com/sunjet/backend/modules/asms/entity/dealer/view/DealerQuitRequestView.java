package com.sunjet.backend.modules.asms.entity.dealer.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 服务站退出申请View
 * Created by zyf on 2017/8/4.
 */
@Data
@Entity
@Immutable
@Subselect("select " +
        "dqr.obj_Id,dqr.doc_no," +
        "dqr.created_time," +
        "dqr.submitter," +
        "dqr.submitter_name," +
        "dqr.status,bd.name," +
        "bd.code,bd.province_id," +
        "dqr.process_instance_id," +
        "bd.star,bd.qualification, " +
        "bd.service_manager_id " +
        "from dealer_quit_requests dqr,basic_dealers bd " +
        "where bd.obj_id = dqr.dealer ")
public class DealerQuitRequestView {

    @Id
    private String objId;// 主键

    private String docNo;// 单据编号

    private String code;// 服务站编号

    private String name;// 服务站名称

    private Date createdTime;// 申请时间

    private String submitter;// 申请人

    private String submitterName;// 申请人

    private Integer status;// 表单状态

    private String provinceId;//省份Id

    private String star;// 星级

    private String qualification;// 维修资质

    private String processInstanceId;   // 流程实例Id

    private String serviceManagerId;  // 服务经理id
}
