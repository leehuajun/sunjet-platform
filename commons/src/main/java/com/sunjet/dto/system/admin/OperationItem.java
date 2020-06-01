package com.sunjet.dto.system.admin;

import lombok.Data;

/**
 * Created by wfb on 17-8-3.
 */
@Data
public class OperationItem {

    private String objId;    //主键

    private String optCode; //  操作编码

    private String optName; //  操作名称

    private Integer seq; // 序号

}
