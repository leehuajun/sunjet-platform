package com.sunjet.dto.asms.dealer;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 服务站准入申请
 */
@Data
public class DealerAdmitRequestInfo extends FlowDocInfo implements Serializable {

    private String dealer;   //服务站id

    private DealerInfo dealerInfo;  //服务站信息

    /**
     * 是否可以编辑服务站编码，默认不允许
     */

    private Boolean canEditCode = false;
    /**
     * 是否可以编辑服务经理，默认不允许
     */

    private Boolean canEditServiceManager = false;
    /**
     * 是否可以上传文件，默认不允许
     */

    private Boolean canUpload = false;

}
