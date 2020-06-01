package com.sunjet.backend.modules.asms.entity.activity.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 活动零件
 * Created by zyf on 2017/8/10.
 */
@Data
@Entity
@Immutable
@Subselect("select " +
        "aap.obj_id,bp.code,bp.name," +
        "bp.part_type,bp.price," +
        "bp.unit,bp.warranty_mileage," +
        "bp.warranty_time,aap.amount," +
        "aap.part_id,aap.activity_notice_id " +
        "from asm_activity_parts aap,basic_parts bp " +
        "where bp.obj_id = aap.part_id ")
public class ActivityPartView implements Serializable {

    @Id
    private String objId;  // 主键
    private String code;  // 零件件号
    private String name;  // 零件名称
    private String partType;  // 零件类型
    private Double price;  // 价格
    private String unit;   // 单位
    private String warrantyMileage;  // 三包里程
    private String warrantyTime;  // 三包里程
    private String activityNoticeId;  //活动ID
    private String partId; // 零件ID
    private Integer amount;  // 需求数量
}
