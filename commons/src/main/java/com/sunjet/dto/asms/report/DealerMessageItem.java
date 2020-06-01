package com.sunjet.dto.asms.report;

import lombok.Data;


/**
 * Created by SUNJET_WS on 2017/8/21.
 * 服务站信息视图
 */
@Data
public class DealerMessageItem {

    private String objId;   //    主键

    private String code;   //    服务站编号

    private String name;   //        服务站名称

    private String phone;   //      电话

    private String fax;   //    传真

    private String address;   //地址

    private String provinceId;// 省份id

    private String provinceName;   //所在省

    private String cityId;// 市id

    private String cityName;   // 市

    private String sgmwSystem;   //SGMW体系

    private String star;   // 申请等级

    private String qualification;   //维修资质

    private String level;   //服务站级别

    private String parentId;   //服级服务站id

    private String parentName;   //父级服务站

    private String orgCode;   //组织机构代码

    private String taxpayerCode;   // 纳税人识别号

    private String bank;   //开户银行

    private String bankAccount;   //  银行账号

    private String businessLicenseCode;   // 营业执照号

    private String serviceManagerName;   //服务经理

    private String otherCollaboration;   // 其他合作内容

    private String legalPerson;   //  法人代表

    private String legalPersonPhone;   //法人电话

    private String stationMaster;   //站长

    private String stationMasterPhone;   //站长电话

    private String technicalDirector;   //技术主管

    private String technicalDirectorPhone;   //技术主管电话

    private String claimDirector;   //索赔主管

    private String claimDirectorPhone;   //索赔主管电话

    private String partDirector;   //配件主管

    private String partDirectorPhone;   // 配件主管电话

    private String financeDirector;   //  财务经理

    private String financeDirectorPhone;   //  财务经理电话

    private String employeeCount;   //  员工总数

    private String receptionistCount;   //  接待员数量

    private String partKeeyperCount;   //  配件员数量

    private String maintainerCount;   // 维修工数量

    private String qcInspectorCount;   // 质检员数量

    private String clerkCount;   // 结算员数量

    private String parkingArea;   //停车面积

    private String receptionArea;   //接待室

    private String generalArea;   // 综合维修区

    private String assemblyArea;   // 总成维修区

    private String storageArea;   // 配件库总面积

    private String storageWulingArea;   // 五菱库总面积

    private String storageUserdPartArea;   // 旧件库面积

    private String storageWulingUserdPartArea;   //五菱旧件库面积

    private String otherMaintainCondition;   //  其他车辆维修条件

    private String otherBrand;   //  其他品牌

    private String productsOfMaintain;   //  维修产品

    private String otherProducts;   //  情况说明

    private Boolean enabled;   //  启用状态

}
