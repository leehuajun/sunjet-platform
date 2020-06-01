package com.sunjet.backend.modules.asms.entity.activity.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 活动通知
 * Created by zyf on 2017/8/10.
 */
@Data
@Entity
@Immutable
@Subselect("SELECT aan.obj_Id, aan.doc_no, aan.title, aan.publish_date, aan.submitter, aan.submitter_name, aan. STATUS, aan.created_time, aan.`repair`, aan.distribute FROM asm_activity_notices aan WHERE aan.`status` <>-3")
public class ActivityNoticeView implements Serializable {

    @Id
    private String objId;// 主键

    private String docNo;// 单据编号

    private String title;// 标题

    private Date publishDate;// 发布时间  日历控件，选择项，默认当前时间，可改

    private String submitter;// 提交人姓名

    private String submitterName;// 提交人姓名

    private Integer status;// 表单状态

    private Date createdTime; //创建时间

    private Boolean distribute = false; // 是否已分配，默认为false

    private Boolean repair = false;     // 是否已参加维修，默认为false
}
