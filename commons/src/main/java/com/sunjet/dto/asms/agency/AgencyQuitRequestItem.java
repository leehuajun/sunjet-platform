package com.sunjet.dto.asms.agency;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 合作商变更申请清单VO
 */
@Data
public class AgencyQuitRequestItem extends FlowDocInfo implements Serializable {

    private String code;// 合作商编号

    private String name;// 合作商名称

    private Date createdTime;// 申请时间

    private String submitterName;// 申请人

    private Integer status;// 表单状态

    private Date startDate = DateHelper.getFirstOfYear();
    ;// 开始日期，绑定页面搜索的开始日期

    private Date endDate = new Date();// 结束日期，绑定页面搜索的结束日期f
}
