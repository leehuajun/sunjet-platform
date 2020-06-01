package com.sunjet.backend.modules.asms.entity.report;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 服务站信息视图
 */
@Data
@Entity
@Immutable
@Subselect("SELECT bd.obj_id, `bd`.`code`, `bd`.`name`, `bd`.`phone`, `bd`.`fax`, `bd`.`address`, bd.province_id, bd.province_name, bd.city_id, bd.city_name, ( CASE WHEN (`bd`.`sgmw_system` = 1) THEN '是' ELSE '否' END ) AS `sgmw_system`, `bd`.`star`, `bd`.`qualification`, `bd`.`level`, bd.parent_id, bd.parent_name, `bd`.`org_code`, `bd`.`taxpayer_code`, `bd`.`bank`, `bd`.`bank_account`, `bd`.`business_license_code`, `bd`.`service_manager_name`, `bd`.`other_collaboration`, `bd`.`legal_person`, `bd`.`legal_person_phone`, `bd`.`station_master`, `bd`.`station_master_phone`, `bd`.`technical_director`, `bd`.`technical_director_phone`, `bd`.`claim_director`, `bd`.`claim_director_phone`, `bd`.`part_director`, `bd`.`part_director_phone`, `bd`.`finance_director`, `bd`.`finance_director_phone`, `bd`.`employee_count`, `bd`.`receptionist_count`, `bd`.`part_keeyper_count`, `bd`.`maintainer_count`, `bd`.`qc_inspector_count`, `bd`.`clerk_count`, `bd`.`parking_area`, `bd`.`reception_area`, `bd`.`general_area`, `bd`.`assembly_area`, `bd`.`storage_area`, `bd`.`storage_wuling_area`, `bd`.`storage_userd_part_area`, `bd`.`storage_wuling_userd_part_area`, `bd`.`other_maintain_condition`, `bd`.`other_brand`, `bd`.`products_of_maintain`, `bd`.`other_products`,`bd`.`enabled` FROM `basic_dealers` `bd`")
public class DealerMessageView {

    @Id
    private String objId;   //    主键

    private String code;   //    服务站编号

    private String name;   //        服务站名称

    private String phone;   //      电话

    private String fax;   //    传真

    private String address;   //地址

    private String provinceId;     // 所在省份Id

    private String provinceName;   //所在省

    private String cityId;     //所在市Id

    private String cityName;   // 市

    private String sgmwSystem;   //SGMW体系

    private String star;   // 申请等级

    private String qualification;   //维修资质

    private String level;   //服务站级别

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
