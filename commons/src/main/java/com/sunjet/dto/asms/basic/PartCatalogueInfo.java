package com.sunjet.dto.asms.basic;

import lombok.Data;

import java.util.Date;

/**
 * @Author: wushi
 * @description: 配件目录视图
 * @Date: Created in 10:26 2018/4/2
 * @Modify by: wushi
 * @ModifyDate by: 10:26 2018/4/2
 */
@Data
public class PartCatalogueInfo {
    private String c_id;
    private String c_symbol;   //代号
    private String part_symbol;  //零件代码
    private String part_assembly;   //零件名称
    private String module_name;  //模块名称
    private Date c_create_time;  //创建时间
    private String c_version;  //版本号
    private Date c_publish_time;  //发布时间
    private Date c_last_modify_time;  //最后修改时间
    private String partName = "";   //配件名称
    private String vehicleModels = "";  //车型代码
    private String moduleName = "";   //模块名称
    private Integer pageSize = 0;
    private Integer page = 0;
    private Integer rowNum;


}