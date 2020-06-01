package com.sunjet.dto.asms.dealer;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 服务站准入申请清单VO
 */
@Data
public class DealerAdmitRequestItem extends FlowDocInfo implements Serializable {

    //private String objId;// 主键

    //private String docNo;// 单据编号

    private String code;// 服务站编号

    private String name;// 服务站名称

    //private Date createdTime;// 申请时间

    //private String submitterName;// 申请人

    //private Integer status;// 表单状态

    private String provinceId;//省份Id

    private String star;// 星级

    private Date startDate;// 开始日期，绑定页面搜索的开始日期

    private Date endDate;// 结束日期，绑定页面搜索的结束日期

    private String qualification;// 维修资质

    private String serviceManagerId;  // 服务经理id

}
