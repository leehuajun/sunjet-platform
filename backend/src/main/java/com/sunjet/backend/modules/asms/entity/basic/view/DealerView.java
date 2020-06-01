package com.sunjet.backend.modules.asms.entity.basic.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 服务站管理View
 * Created by zyf on 2017/8/3.
 */
@Data
@Entity
@Immutable
@Subselect("select " +
        "bd.obj_Id,bd.`code`," +
        "bd.`name`,bd.star,bd.parent_id," +
        "bd.level,bd.parent_name,bd.service_manager_id," +
        "bd.service_manager_name,bd.province_id," +
        "bd.province_name,bd.city_id,bd.city_name," +
        "bd.created_time,bd.phone," +
        "bd.legal_person,bd.station_master, " +
        "bd.enabled " +
        "from basic_dealers bd")
public class DealerView {

    @Id
    private String objId;// 主键

    private String code;// 编号

    private String name;// 名称

    private String star = "三星级";// 申请等级，星级

    private String level = "一级";// 服务站等级

    private String parentId; // 父级服务站Id

    private String parentName;// 父级服务站

    private String serviceManagerId;// 服务经理Id

    private String serviceManagerName;// 服务经理

    private String provinceId;// 省份id

    private String provinceName;// 省份

    private String cityId;// 市id

    private String cityName;// 市

    private Date createdTime;// 准入时间

    private String phone;// 联系电话

    private String legalPerson;// 法人

    private String stationMaster;// 站长

    private Boolean enabled;  //启用状态

}
