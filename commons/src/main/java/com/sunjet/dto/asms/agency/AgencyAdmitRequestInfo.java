package com.sunjet.dto.asms.agency;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 合作商准入申请
 */
@Data
public class AgencyAdmitRequestInfo extends FlowDocInfo implements Serializable {

    private AgencyInfo agencyInfo;  //合作商信息
    private String agency; //合作商
    private Boolean canEditCode = false;  // 是否允许录入经销商编号
    private Boolean canUpload = false;      // 是否允许上传文件


}
