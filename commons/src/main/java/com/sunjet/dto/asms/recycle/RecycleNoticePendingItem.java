package com.sunjet.dto.asms.recycle;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 待返回故障件清单列表VO
 */
@Data
public class RecycleNoticePendingItem extends FlowDocInfo implements Serializable {

    private String srcDocNo;      // 来源类型：三包服务单
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private Integer amount = 0;    //数量
    private Integer backAmount = 0;          // 本次返回数量  已返回数量
    private Integer currentAmount = 0;           // 待返数量  未返回数量
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date returnDate;    // 规定返回时间
    private Date createdTime;   //返回通知单创建时间
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String reason;   //换件原因
    private String pattern;  // 故障模式
    private String comment;            //备注


    private List<String> objIds = new ArrayList<>(); // 故障件返回通知单明细id集合 查询用
    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期


}
