package com.sunjet.dto.asms.basic;

import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.util.Date;

/**
 * 服务站管理Item
 * Created by zyf on 2017/8/3.
 */
@Data
public class DealerItem {

    private String objId;// 主键

    private String code;// 编号

    private String name;// 名称

    private String star;// 申请等级，星级

    private String level;// 服务站等级

    private String parentId;// 父级服务站Id

    private String parentName;// 父级服务站

    private String serviceManagerId;// 服务经理Id

    private String serviceManagerName;// 服务经理

    private String provinceId;// 省份id

    private String provinceName;// 省份

    private String cityId;// 市id

    private String cityName;// 市

    //private Date modifiedTime;// 准入时间
    private Date createdTime;// 准入时间

    private String phone;// 联系电话

    private String legalPerson;// 法人

    private String stationMaster;// 站长

    private Boolean enabled;  //启用状态

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期

    private Date endDate = DateHelper.getEndDateTime();           // 结束日期
}
