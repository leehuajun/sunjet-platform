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
 * 故障件返回单清单VO
 */
@Data
public class RecycleItem extends FlowDocInfo implements Serializable {

    //private String currentNode;    // 当前节点

    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出

    private String dealerName;      // 服务站名称

    private String serviceManager;  // 服务经理

    private String logistics;       // 物流名称

    private String logisticsNum;    // 物流单号

    private String srcDocNo;   //来源单据

    private List<String> objIds = new ArrayList<>(); // 故障件单objids集合  查询用

    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    private Date endDate = new Date();           // 结束日期

}
