package com.sunjet.dto.asms.agency;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 合作商退出申请
 */
@Data
public class AgencyQuitRequestInfo extends FlowDocInfo implements Serializable {

    private AgencyInfo agencyInfo;     // 合作库信息
    private String agency;
    private String reason;                  // 退出原因


}
