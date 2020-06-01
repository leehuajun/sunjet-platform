package com.sunjet.backend.modules.asms.entity.agency.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_QRY on 2017/9/22.
 * 合作商准入申请视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT " +
        " ar.obj_id, " +
        " ar.doc_no, " +
        " ba.`code`, " +
        " ba.`name`, " +
        " ar.created_time, " +
        " ar.submitter, " +
        " ar.submitter_name, " +
        " ar.`status` " +
        "FROM " +
        " agency_quit_requests ar, " +
        " basic_agencies ba " +
        "WHERE " +
        " ar.agency = ba.obj_id")
public class AgencyQuitRequestView {

    @Id
    private String objId;// 主键

    private String docNo;// 单据编号

    private String code;// 合作商编号

    private String name;// 合作商名称

    private Date createdTime;// 申请时间

    private String submitter;// 申请人logId

    private String submitterName;// 申请人

    private Integer status;// 表单状态
}
