package com.sunjet.dto.system.base;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by SUNJET_WS on 2017/7/13.
 * 业务基础Info
 */
public class DocInfo extends IdInfo {

    @Getter
    @Setter
    private String createrId;   //创建人ID

    @Getter
    @Setter
    private String createrName;  // 创建人名字


    @Getter
    @Setter
    private String modifierId;   // 修改人ID
    @Getter
    @Setter
    private String modifierName; // 修改人修改

    @Getter
    @Setter
    private Date createdTime = new Date();   //创建时间

    @Getter
    @Setter
    private Date modifiedTime = new Date();  //修改时间

}
