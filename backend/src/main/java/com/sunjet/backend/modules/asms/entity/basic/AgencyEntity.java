package com.sunjet.backend.modules.asms.entity.basic;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 配件合作商、经销商实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicAgencies")
public class AgencyEntity extends DocEntity {
    private static final long serialVersionUID = -6704173665780561623L;
    /**
     * 编号
     */
    @Column(length = 50, unique = true)
    private String code;
    /**
     * 名称
     */
    @Column(length = 100)
    private String name;
    /**
     * 电话
     */
    @Column(length = 100)
    private String phone;
    /**
     * 传真
     */
    @Column(length = 20)
    private String fax;
    /**
     * 地址
     */
    @Column(length = 200)
    private String address;
    /**
     * 省份Id
     */
    @Column(length = 32)
    private String provinceId;
    /**
     * 省份名称
     */
    @Column(length = 50)
    private String provinceName;
    /**
     * 市Id
     */
    @Column(length = 32)
    private String cityId;
    /**
     * 市名称
     */
    @Column(length = 50)
    private String cityName;
    /**
     * 区/县Id
     */
    @Column(length = 32)
    private String countyId;
    /**
     * 区/县名称
     */
    @Column(length = 50)
    private String countyName;

//    private ProvinceEntity province;      // 省份
//    private CityEntity city;                // 市
//    private CountyEntity county;            // 区/县


    /**
     * 覆盖省份
     */
    @Column(length = 500)
    private String coverProvinces;

    /**
     * 营业执照号
     */
    @Column(length = 50)
    private String businessLicenseCode;
    /**
     * 组织机构代码号
     */
    @Column(length = 50)
    private String orgCode;
    /**
     * 纳税人识别号
     */
    @Column(length = 50)
    private String taxpayerCode;
    /**
     * 税率
     */
    @Column(length = 50)
    private Double taxRate;
    /**
     * 开户行
     */
    @Column(length = 50)
    private String bank;
    /**
     * 银行帐号
     */
    @Column(length = 50)
    private String bankAccount;

    /**
     * 法人
     */
    @Column(length = 50)
    private String legalPerson;
    /**
     * 法人联系方式
     */
    @Column(length = 50)
    private String legalPersonPhone;
    /**
     * 店长
     */
    @Column(length = 50)
    private String shopManager;
    /**
     * 店长联系方式
     */
    @Column(length = 20)
    private String shopManagerPhone;
    /**
     * 技术主管
     */
    @Column(length = 50)
    private String technicalDirector;
    /**
     * 技术主管联系方式
     */
    @Column(length = 20)
    private String technicalDirectorPhone;
    /**
     * 计划主管
     */
    @Column(length = 50)
    private String planDirector;
    /**
     * 计划主管联系方式
     */
    @Column(length = 20)
    private String planDirectorPhone;
    /**
     * 配件主管
     */
    @Column(length = 50)
    private String partDirector;
    /**
     * 配件主管联系方式
     */
    @Column(length = 20)
    private String partDirectorPhone;
    /**
     * 财务主管
     */
    @Column(length = 50)
    private String financeDirector;
    /**
     * 财务主管联系方式
     */
    @Column(length = 20)
    private String financeDirectorPhone;

    /**
     * 员工总人数
     */
    @Column(length = 10)
    private String employeeCount;
    /**
     * 接待员数量
     */
    @Column(length = 10)
    private String receptionistCount;
    /**
     * 物流员数量
     */
    @Column(length = 10)
    private String logisticsClerkCount;
    /**
     * 开票制单员数量
     */
    @Column(length = 10)
    private String invoiceClerkCount;
    /**
     * 库管员数量
     */
    @Column(length = 10)
    private String storeKeeperCount;
    /**
     * 结算员数量
     */
    @Column(length = 10)
    private String clerkCount;
    /**
     * 液压叉车数量
     */
    @Column(length = 10)
    private String forkliftCount;

    /**
     * 办公室面积
     */
    @Column(length = 20)
    private String officeArea;
    /**
     * 配件库面积
     */
    @Column(length = 20)
    private String storageArea;
    /**
     * 接待区面积
     */
    @Column(length = 20)
    private String receptionArea;
    /**
     * 料架数量
     */
    @Column(length = 20)
    private String shelfArea;
    /**
     * 房屋结构
     */
    @Column(length = 50)
    private String buildingStructure;
    /**
     * 拟供应我公司产品系列
     */
    @Column(length = 200)
    private String productsOfSupply;
    /**
     * 还兼做哪些品牌的配件
     */
    @Column(length = 200)
    private String otherBrand;

    /**
     * 人员登记证书
     */
    @Column(length = 200)
    private String filePersonnelCertificate;
    /**
     * 维修资质表
     */
    @Column(length = 200)
    private String fileQualification;
    /**
     * 营业执照 照片文件
     */
    @Column(length = 200)
    private String fileBusinessLicense;
    /**
     * 税务登记证 照片文件
     */
    @Column(length = 200)
    private String fileTaxCertificate;
    /**
     * 银行开户行许可证
     */
    @Column(length = 200)
    private String fileBankAccountOpeningPermit;
    /**
     * 企业组织架构及设置书
     */
    @Column(length = 200)
    private String fileOrgChart;
    /**
     * 合作库开票信息
     */
    @Column(length = 200)
    private String fileInvoiceInfo;
    /**
     * 合作库全貌图片
     */
    @Column(length = 200)
    private String fileGlobal;
    /**
     * 办公室图片
     */
    @Column(length = 200)
    private String fileOffice;
    /**
     * 接待室图片
     */
    @Column(length = 200)
    private String fileReceptionOffice;
    /**
     * 配件库房图片
     */
    @Column(length = 200)
    private String filePartStoreage;
    /**
     * 地理位置
     */
    @Column(length = 200)
    private String fileMap;
    /**
     * 培训资料
     */
    @Column(length = 200)
    private String fileTrain;
    //  private String bank;

    /**
     * 标准货架数量
     */
    @Column(length = 10)
    private String shelfCount;
    /**
     * 标准货架 照片
     */
    @Column(length = 200)
    private String fileShelf;
    /**
     * 定制货柜数量
     */
    @Column(length = 10)
    private String containerCount;
    /**
     * 定制货柜 照片
     */
    @Column(length = 200)
    private String fileContainer;
    /**
     * 登高车数量
     */
    @Column(length = 10)
    private String ladderTruckCount;
    /**
     * 登高车 照片
     */
    @Column(length = 200)
    private String fileLadderTruck;
    /**
     * 堆高车数量
     */
    @Column(length = 10)
    private String forkTruckCount;
    /**
     * 堆高车 照片
     */
    @Column(length = 200)
    private String fileForkTruck;
    /**
     * 小件容器数量
     */
    @Column(length = 10)
    private String littleContainerCount;
    /**
     * 小容器 照片
     */
    @Column(length = 200)
    private String fileLittleContainer;
    /**
     * 零件铭牌数量
     */
    @Column(length = 10)
    private String partNameplateCount;
    /**
     * 库房灯光数量
     */
    @Column(length = 10)
    private String storeLampCount;
    /**
     * 货物标签卡数量
     */
    @Column(length = 10)
    private String tagCardCount;
    /**
     * 电脑数量
     */
    @Column(length = 10)
    private String computerCount;
    /**
     * 电脑照片
     */
    @Column(length = 200)
    private String fileComputer;
    /**
     * 电话数量
     */
    @Column(length = 10)
    private String telephoneCount;
    /**
     * 电话照片
     */
    @Column(length = 200)
    private String fileTelephone;
    /**
     * 传真数量
     */
    @Column(length = 10)
    private String faxCount;
    /**
     * 传真机照片
     */
    @Column(length = 200)
    private String fileFax;
    /**
     * 相机数量
     */
    @Column(length = 10)
    private String cameraCount;
    /**
     * 相机 照片
     */
    @Column(length = 200)
    private String fileCamera;
    /**
     * 手动打包机数量
     */
    @Column(length = 10)
    private String packerCount;
    /**
     * 打包机 照片
     */
    @Column(length = 200)
    private String filePacker;

}
