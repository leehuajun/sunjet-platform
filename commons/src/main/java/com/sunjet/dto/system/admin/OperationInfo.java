package com.sunjet.dto.system.admin;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/12.
 * 操作列表
 */
@Data
public class OperationInfo extends DocInfo implements Serializable {

    private String optCode; //  操作编码
    private String optName; //  操作名称
    private Integer seq; // 序号
}
