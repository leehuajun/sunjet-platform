package com.sunjet.dto.asms.basic;

import com.sunjet.dto.system.base.DocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/9/12.
 * 合作商列表
 */
@Data
public class AgencyItem extends DocInfo {

    //private String objId;   //主键
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
    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期

    private Date endDate = new Date();           // 结束日期


}
