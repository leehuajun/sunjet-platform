package com.sunjet.dto.asms.agency;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/11.
 * 合作商变更申请
 */
@Data
public class AgencyAlterRequestInfo extends FlowDocInfo implements Serializable {

    private AgencyInfo agencyInfo;  //合作商信息
    private String agency;  // 合作商id

    private String agencyBackupId; //变更前合作商备份id

    private String fileAlteration; //  变更函


    private String legalPersonPhone;          // 变更后 法人联系方式
    private String phone;                     // 变更后 电话
    private String fax;                       // 变更后 传真
    private String shopManager;             // 店长
    private String shopManagerPhone;        // 店长联系方式
    private String technicalDirector;       // 技术主管
    private String technicalDirectorPhone;  // 技术主管联系方式
    private String planDirector;            // 计划主管
    private String planDirectorPhone;       // 计划主管联系方式
    private String partDirector;            // 配件主管
    private String partDirectorPhone;       // 配件主管联系方式
    private String financeDirector;         // 财务主管
    private String financeDirectorPhone;    // 财务主管联系方式

    private String employeeCount;           // 员工总人数
    private String receptionistCount;       // 接待员数量
    private String logisticsClerkCount;     // 物流员数量
    private String invoiceClerkCount;       // 开票制单员数量
    private String storeKeeperCount;        // 库管员数量
    private String clerkCount;              // 结算员数量
    private String forkliftCount;           // 液压叉车数量

    private String officeArea;              // 办公室面积
    private String storageArea;             // 配件库面积
    private String receptionArea;           // 接待区面积
    private String shelfArea;               // 料架数量
    private String buildingStructure;       // 房屋结构
    private String productsOfSupply;        // 拟供应我公司产品系列
    private String otherBrand;              // 还兼做哪些品牌的配件

    private String filePersonnelCertificate;        // 人员登记证书
    private String fileQualification;               // 维修资质表
    private String fileBusinessLicense;             // 营业执照 照片文件
    private String fileTaxCertificate;              // 税务登记证 照片文件
    private String fileBankAccountOpeningPermit;    // 银行开户行许可证
    private String fileOrgChart;                    // 企业组织架构及设置书
    private String fileInvoiceInfo;                 // 合作库开票信息
    private String fileGlobal;                      // 合作库全貌图片
    private String fileOffice;                      // 办公室图片
    private String fileReceptionOffice;             // 接待室图片
    private String filePartStoreage;                // 配件库房图片
    private String fileMap;                         // 地理位置
    //  private String bank;

    private String shelfCount;                      // 标准货架数量
    private String fileShelf;                       // 标准货架 照片
    private String containerCount;                  // 定制货柜数量
    private String fileContainer;                   // 定制货柜 照片
    private String ladderTruckCount;                // 登高车数量
    private String fileLadderTruck;                 // 登高车 照片
    private String forkTruckCount;                  // 堆高车数量
    private String fileForkTruck;                   // 堆高车 照片
    private String littleContainerCount;            // 小件容器数量
    private String fileLittleContainer;             // 小容器 照片
    private String partNameplateCount;              // 零件铭牌数量
    private String storeLampCount;                  // 库房灯光数量
    private String tagCardCount;                    // 货物标签卡数量
    private String computerCount;                   // 电脑数量
    private String fileComputer;                    // 电脑照片
    private String telephoneCount;                  // 电话数量
    private String fileTelephone;                   // 电话照片
    private String faxCount;                        // 传真数量
    private String fileFax;                         // 传真机照片
    private String cameraCount;                     // 相机数量
    private String fileCamera;                      // 相机 照片
    private String packerCount;                     // 手动打包机数量
    private String filePacker;                      // 打包机 照片

    /**
     * 税率
     */
    private Double taxRate;


}
