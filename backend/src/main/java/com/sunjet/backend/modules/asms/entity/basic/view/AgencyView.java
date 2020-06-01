package com.sunjet.backend.modules.asms.entity.basic.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/9/12.
 * 合作商视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT obj_id, `code`, `name`, province_id, province_name, city_id, city_name, county_id, county_name, modified_time, phone, legal_person, shop_manager, enabled, created_time FROM basic_agencies")
public class AgencyView {

    @Id
    private String objId;   //主键
    private String code;       //编码
    private String name;        //名字
    private String provinceId;      //省份id
    private String provinceName;    //省份名字
    private String cityId;          //市id
    private String cityName;        //市
    private String countyId;        //镇id
    private String countyName;      //镇
    private Date modifiedTime;      //准入时间
    private String phone;           //电话
    private String legalPerson;       //法人
    private String shopManager;     // 店长
    private Boolean enabled;         //启用状态
    private Date createdTime;

}
