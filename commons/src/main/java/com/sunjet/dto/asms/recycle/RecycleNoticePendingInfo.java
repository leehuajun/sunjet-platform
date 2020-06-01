package com.sunjet.dto.asms.recycle;

import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 待返回故障件清单VO
 */
@Data
public class RecycleNoticePendingInfo extends FlowDocInfo implements Serializable {

    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private Integer amount = 0;    //数量
    private Integer backAmount = 0;          // 本次返回数量  已返回数量
    private Integer currentAmount = 0;           // 待返数量  应发数量
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private Date returnDate;    // 规定返回时间

    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String pattern;         // 故障模式
    private String reason;          // 换件原因
    private String comment;

}
