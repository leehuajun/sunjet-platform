package com.sunjet.backend.modules.asms.entity.basic;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 服务站 实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicBackupDealers")
public class DealerBackupEntity extends DocEntity {


    private static final long serialVersionUID = -5627406796773353059L;
    @Column(length = 50)
    private String code;                    // 编号
    @Column(length = 100)
    private String name;                    // 名称
    @Column(length = 100)
    private String businessLicenseCode;     // 营业执照号
    @Column(length = 100)
    private String orgCode;                 // 组织机构代码号
    @Column(length = 100)
    private String taxpayerCode;            // 纳税人识别号
    @Column(length = 100)
    private String bank;                    // 开户行
    @Column(length = 100)
    private String bankAccount;             // 银行帐号
    @Column(length = 200)
    private String otherMaintainCondition;  //其他车辆维修条件
    @Column(length = 200)
    private String otherBrand;              //兼做的品牌服务
    @Column(length = 200)
    private String productsOfMaintain;      //拟维修我公司产
    @Column(length = 200)
    private String otherProducts;           // 其它（手工填写  拟维修我公司产）
    @Column(length = 200)
    private String partReport;              //配件储备表
    @Column(length = 200)
    private String otherCollaboration;      //其他合作内容
    @Column(length = 50)
    private Double taxRate = 0.0;                 //税率

    @Column(length = 100)
    private String phone;                   // 联系电话
    @Column(length = 20)
    private String fax;                     // 传真
    @Column(length = 200)
    private String address;                 // 地址
    @Column(length = 32)
    private String provinceId;              // 省份id
    @Column(length = 50)
    private String provinceName;            // 省份名称
    //    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "Province")
//    private ProvinceEntity province;        // 省份
    @Column(length = 32)
    private String cityId;            // 市id
    @Column(length = 50)
    private String cityName;              // 市名称
    //    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "City")
//    private CityEntity city;                // 市
    @Column(length = 32)
    private String countyId;            // 县区id
    @Column(length = 50)
    private String countyName;              // 县区名称
    //    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "County")
//    private CountyEntity county;            // 县区

    private Boolean sgmwSystem = false;     // 是否SGMW体系
    @Column(length = 10)

    private String star = "三星级";         // 申请等级，星级
    @Column(length = 50)

    private String qualification = "一类";   // 维修资质
    @Column(length = 10)

    private String level = "一级";           // 服务站等级
    //    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "Parent")
//    private DealerEntity parent;            // 父级服务站
    @Column(length = 32)
    private String parentId;                // 父级服务站Id
    @Column(length = 50)
    private String parentName;              // 父级服务站名称
    @Column(length = 50)
    private String legalPerson;             // 法人
    @Column(length = 20)
    private String legalPersonPhone;        // 法人联系方式
    @Column(length = 50)
    private String stationMaster;           // 站长
    @Column(length = 20)
    private String stationMasterPhone;      // 站长联系方式
    @Column(length = 50)
    private String technicalDirector;       // 技术主管
    @Column(length = 20)
    private String technicalDirectorPhone;  // 技术主管联系方式
    @Column(length = 50)
    private String claimDirector;           // 索赔主管
    @Column(length = 20)
    private String claimDirectorPhone;      // 索赔主管联系方式
    @Column(length = 50)
    private String partDirector;            // 配件主管
    @Column(length = 20)
    private String partDirectorPhone;       // 配件主管联系方式
    @Column(length = 50)
    private String financeDirector;         // 财务主管
    @Column(length = 20)
    private String financeDirectorPhone;    // 财务主管联系方式

    @Column(length = 10)
    private String employeeCount;           // 员工总人数
    @Column(length = 10)
    private String receptionistCount;       // 接待员数量
    @Column(length = 10)
    private String partKeeyperCount;        // 配件员数量
    @Column(length = 10)
    private String maintainerCount;         // 维修工数量
    @Column(length = 10)
    private String qcInspectorCount;        // 质检员数量
    @Column(length = 10)
    private String clerkCount;              // 结算员数量

    @Column(length = 20)
    private String parkingArea;             // 停车区面积
    @Column(length = 20)
    private String receptionArea;           // 接待区面积
    @Column(length = 20)
    private String generalArea;             // 综合维修区面积
    @Column(length = 20)
    private String assemblyArea;            // 总成维修区面积
    @Column(length = 20)
    private String storageArea;             // 配件库总面积
    @Column(length = 20)
    private String storageWulingArea;               // 五菱配件库面积
    @Column(length = 20)
    private String storageUserdPartArea;            // 旧件库面积
    @Column(length = 20)
    private String storageWulingUserdPartArea;      // 五菱旧件库面积

    @Column(length = 200)
    private String fileBusinessLicense;             // 营业执照 照片文件
    @Column(length = 200)
    private String fileTaxCertificate;              // 税务登记证 照片文件
    @Column(length = 200)
    private String fileBankAccountOpeningPermit;    // 银行开户行许可证
    @Column(length = 200)
    private String filePersonnelCertificate;        // 人员登记证书
    @Column(length = 200)
    private String fileQualification;               // 维修资质表
    @Column(length = 200)
    private String fileInvoiceInfo;                 // 服务站开票信息
    @Column(length = 200)
    private String fileRoadTransportLicense;        // 道路运输营业许可证
    @Column(length = 200)
    private String fileOrgChart;                    // 企业组织架构及设置书
    @Column(length = 200)
    private String fileDevice;                      // 设备清单
    @Column(length = 200)
    private String fileReceptionOffice;             // 接待室图片
    @Column(length = 200)
    private String fileGlobal;                      // 服务商全貌图片
    @Column(length = 200)
    private String fileOffice;                      // 办公室图片
    @Column(length = 200)
    private String fileWorkshop;                    // 维修车间
    @Column(length = 200)
    private String filePartStoreage;                // 配件库房图片
    @Column(length = 200)
    private String fileMap;                         // 地理位置
    @Column(length = 200)
    private String fileTrain;                       // 培训资料

    @Column(length = 32)
    private String serviceManagerId;                // 服务经理Id
    @Column(length = 50)
    private String serviceManagerName;              // 服务经理名称

}
