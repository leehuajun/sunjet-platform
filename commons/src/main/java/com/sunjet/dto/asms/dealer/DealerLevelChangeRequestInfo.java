package com.sunjet.dto.asms.dealer;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 服务站等级变更申请
 */
@Data
public class DealerLevelChangeRequestInfo extends FlowDocInfo implements Serializable {
    private DealerInfo dealerInfo;  //服务站信息
    private String dealer;    //服务站 id
    private String dealerBackupId; //服务站变更前备份id

    private String fileAlteration; //  变更函

    private String otherCollaboration;//其他合作内容
    private String productsOfMaintain; //拟维修我公司产
    private String otherMaintainCondition; //其他车辆维修条件
    private String otherBrand;  //兼做的品牌服务

    //    =========================
    private String phone;           // 联系电话
    private String fax;             // 传真
    private String address;         // 地址

    private String province;      // 省份

    private String city;                // 市
    private String countyName;      // 县区名称
    private Boolean sgmwSystem;         // 是否SGMW体系
    private String star;                // 申请等级，星级
    private String qualification;       // 维修资质
    private String level;               // 服务站等级
    private String parent;        // 父级服务站

    private String legalPerson;             // 法人
    private String legalPersonPhone;        // 法人联系方式
    private String stationMaster;           // 站长
    private String stationMasterPhone;      // 站长联系方式
    private String technicalDirector;       // 技术主管
    private String technicalDirectorPhone;  // 技术主管联系方式
    private String claimDirector;           // 索赔主管
    private String claimDirectorPhone;      // 索赔主管联系方式
    private String partDirector;            // 配件主管
    private String partDirectorPhone;       // 配件主管联系方式
    private String financeDirector;         // 财务主管
    private String financeDirectorPhone;    // 财务主管联系方式

    private String employeeCount;           // 员工总人数
    private String receptionistCount;       // 接待员数量
    private String partKeeyperCount;        // 配件员数量
    private String maintainerCount;         // 维修工数量
    private String qcInspectorCount;        // 质检员数量
    private String clerkCount;              // 结算员数量

    private String parkingArea;             // 停车区面积
    private String receptionArea;           // 接待区面积
    private String generalArea;             // 综合维修区面积
    private String assemblyArea;            // 总成维修区面积
    private String storageArea;             // 配件库总面积
    private String storageWulingArea;               // 五菱配件库面积
    private String storageUserdPartArea;            // 旧件库面积
    private String storageWulingUserdPartArea;      // 五菱旧件库面积


    private String fileBusinessLicense;             // 营业执照 照片文件
    private String fileTaxCertificate;              // 税务登记证 照片文件
    private String fileBankAccountOpeningPermit;    // 银行开户行许可证
    private String filePersonnelCertificate;        // 人员登记证书
    private String fileQualification;               // 维修资质表
    private String fileInvoiceInfo;                 // 服务站开票信息
    private String fileRoadTransportLicense;        // 道路运输营业许可证
    private String fileOrgChart;                    // 企业组织架构及设置书
    private String fileDevice;                      // 设备清单
    private String fileReceptionOffice;             // 接待室图片
    private String fileGlobal;                      // 服务商全貌图片
    private String fileOffice;                      // 办公室图片
    private String fileWorkshop;                    // 维修车间
    private String filePartStoreage;                // 配件库房图片
    private String fileMap;                         // 地理位置

    private String partReport;                     //配件清单表


}