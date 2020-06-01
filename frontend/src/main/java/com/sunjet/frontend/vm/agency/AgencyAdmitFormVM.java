package com.sunjet.frontend.vm.agency;

import com.sunjet.dto.asms.agency.AgencyAdmitRequestInfo;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.CityInfo;
import com.sunjet.dto.asms.basic.CountyInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.agency.AgencyAdmitService;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class AgencyAdmitFormVM extends FormVM {

    @WireVariable
    private AgencyAdmitService agencyAdmitService;  //合作商准入申请service
    @WireVariable
    private RegionService regionService;           // 地区
    @WireVariable
    private AgencyService agencyService;           //合作商

    @Getter
    @Setter
    private List<ProvinceInfo> provinceInfos = new ArrayList<>();  // 省份/直辖市 集合
    @Getter
    @Setter
    private List<CityInfo> cityInfos = new ArrayList<>();          // 选中的省份/直辖市的下属城市 集合
    @Getter
    @Setter
    private List<CountyInfo> countyInfos = new ArrayList<>();      // 选中的城市的下属县/区 集合

    @Getter
    @Setter
    private AgencyAdmitRequestInfo agencyAdmitRequestInfo = new AgencyAdmitRequestInfo();        // 合作商准入申请对象

//    @Getter
//    @Setter
//    private AgencyInfo agency = new AgencyInfo();        //合作商对象


    @Init(superclass = true)
    public void init() {
        // 初始化省份
        this.provinceInfos = regionService.findAllProvince();

        if (StringUtils.isNotBlank(objId)) {

            this.agencyAdmitRequestInfo = agencyAdmitService.findOne(objId);
            //AgencyInfo agency = agencyService.findOne(this.agencyAdmitRequestInfo.getAgency());
            //this.agencyAdmitRequestInfo.setAgencyInfo(agency);

            if (StringUtils.isNotBlank(agencyAdmitRequestInfo.getAgencyInfo().getProvinceId())) {
                this.cityInfos = regionService.findCitiesByProvinceId(agencyAdmitRequestInfo.getAgencyInfo().getProvinceId());
            }
            if (StringUtils.isNotBlank(agencyAdmitRequestInfo.getAgencyInfo().getCityId())) {
                this.countyInfos = regionService.findCountiesByCityId(agencyAdmitRequestInfo.getAgencyInfo().getCityId());
            }
            //if (StringUtils.isNotBlank(agencyAdmitRequestInfo.getProcessInstanceId())) {
            //    this.readonly = true;
            //}

        } else {
            //agencyAdmitRequestInfo = new AgencyAdmitRequestInfo();
            this.agencyAdmitRequestInfo.setAgencyInfo(new AgencyInfo());
        }
        this.setActiveUserMsg(this.agencyAdmitRequestInfo);
    }

    /**
     * 保存实体
     */
    @Command
    //@NotifyChange("*")
    public void submit() {
        this.agencyAdmitRequestInfo = agencyAdmitService.save(agencyAdmitRequestInfo);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_LIST, null);
        flowDocInfo = agencyAdmitRequestInfo;
        this.updateUIState();
        showDialog();
    }


    /**
     * 选择省份
     */
    @Command
    @NotifyChange({"cityInfos", "countyInfos", "agencyAdmitRequestInfo"})
    public void selectProvince(@BindingParam("model") ProvinceInfo provinceInfo) {
        if (provinceInfo != null) {
            agencyAdmitRequestInfo.getAgencyInfo().setProvinceId(provinceInfo.getObjId());
            agencyAdmitRequestInfo.getAgencyInfo().setProvinceName(provinceInfo.getName());
            agencyAdmitRequestInfo.getAgencyInfo().setCityId(null);
            agencyAdmitRequestInfo.getAgencyInfo().setCityName(null);
            agencyAdmitRequestInfo.getAgencyInfo().setCountyId(null);
            agencyAdmitRequestInfo.getAgencyInfo().setCountyName(null);

            this.cityInfos = regionService.findCitiesByProvinceId(provinceInfo.getObjId());
            this.countyInfos.clear();
        }
    }

    /**
     * 选择市区
     */
    @Command
    @NotifyChange({"countyInfos", "agencyAdmitRequestInfo"})
    public void selectCity(@BindingParam("model") CityInfo cityInfo) {
        if (cityInfo != null) {
            this.countyInfos = regionService.findCountiesByCityId(cityInfo.getObjId());
            agencyAdmitRequestInfo.getAgencyInfo().setCityId(cityInfo.getObjId());
            agencyAdmitRequestInfo.getAgencyInfo().setCountyId(null);
            agencyAdmitRequestInfo.getAgencyInfo().setCountyName(null);

            agencyAdmitRequestInfo.getAgencyInfo().setCityName(cityInfo.getName());
        }
    }


    @Command
    @NotifyChange({"agencyAdmitRequestInfo"})
    public void selectCounty(@BindingParam("model") CountyInfo countyInfo) {
        if (countyInfo != null) {
            agencyAdmitRequestInfo.getAgencyInfo().setCountyId(countyInfo.getObjId());
            agencyAdmitRequestInfo.getAgencyInfo().setCountyName(countyInfo.getName());
        }
    }

    /**
     * 文件路径
     *
     * @param filename
     * @return
     */
    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_AGENCY + filename;
    }

    /**
     * 上传文件
     *
     * @param event
     * @param type
     */
    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_AGENCY);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileQualification(fileName);
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileOrgChart(fileName);
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileBusinessLicense(fileName);
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileGlobal(fileName);
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileTaxCertificate(fileName);
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileOffice(fileName);
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileBankAccountOpeningPermit(fileName);
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileReceptionOffice(fileName);
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileInvoiceInfo(fileName);
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFilePartStoreage(fileName);
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAdmitRequestInfo.getAgencyInfo().setFilePersonnelCertificate(fileName);
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileMap(fileName);
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileShelf(fileName);
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileComputer(fileName);
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileContainer(fileName);
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileTelephone(fileName);
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileLadderTruck(fileName);
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileFax(fileName);
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileForkTruck(fileName);
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileCamera(fileName);
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileLittleContainer(fileName);
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAdmitRequestInfo.getAgencyInfo().setFilePacker(fileName);
        }

//        else if (type.equalsIgnoreCase("t23")) {     //手动打包机
//            this.agencyAdmitRequest.getAgency().setFilePacker(fileName);
//        }
        else if (type.equalsIgnoreCase("file23")) {   //培训资料
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileTrain(fileName);
        }
//        Clients.showNotification(event.getMedia().getContentType());
//        if (!event.getMedia().getContentType().startsWith("image/")) {
////            Clients.showNotification("Please upload an image");
//            return;
//        } else {
////            Clients.showNotification(photo.getName());
//        }
//        this.photo = photo;
    }


    /**
     * 删除上传文件
     *
     * @param type
     */
    @Command
    @NotifyChange("agencyAdmitRequestInfo")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileQualification("");
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileOrgChart("");
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileBusinessLicense("");
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileGlobal("");
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileTaxCertificate("");
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileOffice("");
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileBankAccountOpeningPermit("");
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileReceptionOffice("");
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileInvoiceInfo("");
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAdmitRequestInfo.getAgencyInfo().setFilePartStoreage("");
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAdmitRequestInfo.getAgencyInfo().setFilePersonnelCertificate("");
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileMap("");
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileShelf("");
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileComputer("");
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileContainer("");
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileTelephone("");
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileLadderTruck("");
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileFax("");
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileForkTruck("");
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileCamera("");
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileLittleContainer("");
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAdmitRequestInfo.getAgencyInfo().setFilePacker("");
        }
//        else if (type.equalsIgnoreCase("t23")) {     //手动打包机
//            this.agencyAdmitRequestInfo.getAgencyInfo().setFilePacker(fileName);
//        }
        else if (type.equalsIgnoreCase("file23")) {   //培训资料
            this.agencyAdmitRequestInfo.getAgencyInfo().setFileTrain("");
        }
    }

    @Override
    protected Boolean checkValid() {
        List<String> ignoreFields = Arrays.asList("serialVersionUID", "objId", "createrId", "createrName", "modifierId", "modifierName", "code", "county", "fileTrain");
        if (ZkUtils.checkFieldValid(this.agencyAdmitRequestInfo.getAgencyInfo(), ignoreFields) == false) {
            return false;
        }
//        agencyAdmitService.save(agencyAdmitRequestInfo);

        if (StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getObjId()) && StringUtils.isNotBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getCode())) {
            if (agencyService.checkCodeExists(this.agencyAdmitRequestInfo.getAgencyInfo().getCode())) {
                ZkUtils.showError("合作商编号【" + this.agencyAdmitRequestInfo.getAgencyInfo().getCode() + "】已存在!", "系统提示");
                return false;
            }
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getName() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getName())) {
            ZkUtils.showInformation("请填写合作商名称", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getPhone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPhone())) {
            ZkUtils.showInformation("请填写电话", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFax() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFax())) {
            ZkUtils.showInformation("请填写传真", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getAddress() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getAddress())) {
            ZkUtils.showInformation("请填写地址", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getProvinceName() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getProvinceName())) {
            ZkUtils.showInformation("请填写省份", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getCityName() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getCityName())) {
            ZkUtils.showInformation("请填写市", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getCountyName() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getCountyName())) {
            ZkUtils.showInformation("请填写区/县", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getBusinessLicenseCode() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getBusinessLicenseCode())) {
            ZkUtils.showInformation("请填写营业执照号", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getOrgCode() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getOrgCode())) {
            ZkUtils.showInformation("请填写组织机构代码号", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getTaxpayerCode() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getTaxpayerCode())) {
            ZkUtils.showInformation("请填写纳税人识别号", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getBank() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getBank())) {
            ZkUtils.showInformation("请填写开户行", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getBankAccount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getBankAccount())) {
            ZkUtils.showInformation("请填写银行帐号", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getLegalPerson() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getLegalPerson())) {
            ZkUtils.showInformation("请填写法人", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getLegalPersonPhone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getLegalPersonPhone())) {
            ZkUtils.showInformation("请填写法人联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getShopManager() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getShopManager())) {
            ZkUtils.showInformation("请填写店长", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getShopManagerPhone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getShopManagerPhone())) {
            ZkUtils.showInformation("请填写店长联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getTechnicalDirector() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getTechnicalDirector())) {
            ZkUtils.showInformation("请填写技术主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getTechnicalDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getTechnicalDirectorPhone())) {
            ZkUtils.showInformation("请填写技术主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getPlanDirector() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPlanDirector())) {
            ZkUtils.showInformation("请填写计划主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getPlanDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPlanDirectorPhone())) {
            ZkUtils.showInformation("请填写计划主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getPartDirector() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPartDirector())) {
            ZkUtils.showInformation("请填写配件主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getPartDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPartDirectorPhone())) {
            ZkUtils.showInformation("请填写配件主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFinanceDirector() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFinanceDirector())) {
            ZkUtils.showInformation("请填写财务主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFinanceDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFinanceDirectorPhone())) {
            ZkUtils.showInformation("请填写财务主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getEmployeeCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getEmployeeCount())) {
            ZkUtils.showInformation("请填写员工总人数", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getReceptionistCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getReceptionistCount())) {
            ZkUtils.showInformation("请填写接待员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getLogisticsClerkCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getLogisticsClerkCount())) {
            ZkUtils.showInformation("请填写物流员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getInvoiceClerkCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getInvoiceClerkCount())) {
            ZkUtils.showInformation("请填写开票制单员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getStoreKeeperCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getStoreKeeperCount())) {
            ZkUtils.showInformation("请填写库管员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getClerkCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getClerkCount())) {
            ZkUtils.showInformation("请填写结算员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getForkliftCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getForkliftCount())) {
            ZkUtils.showInformation("请填写液压叉车数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getOfficeArea() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getOfficeArea())) {
            ZkUtils.showInformation("请填写办公室面积", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getStorageArea() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getStorageArea())) {
            ZkUtils.showInformation("请填写配件库面积", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getReceptionArea() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getReceptionArea())) {
            ZkUtils.showInformation("请填写接待区面积", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getShelfArea() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getShelfArea())) {
            ZkUtils.showInformation("请填写料架数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getBuildingStructure() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getBuildingStructure())) {
            ZkUtils.showInformation("请填写房屋结构", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getProductsOfSupply() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getProductsOfSupply())) {
            ZkUtils.showInformation("请填写拟供应我公司产品系列", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getOtherBrand() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getOtherBrand())) {
            ZkUtils.showInformation("请填写还兼做哪些品牌的配件", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFilePersonnelCertificate() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFilePersonnelCertificate())) {
            ZkUtils.showInformation("请填写人员登记证书", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileQualification() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileQualification())) {
            ZkUtils.showInformation("请填写维修资质表", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileBusinessLicense() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileBusinessLicense())) {
            ZkUtils.showInformation("请填写营业执照", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileTaxCertificate() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileTaxCertificate())) {
            ZkUtils.showInformation("请填写税务登记证", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileBankAccountOpeningPermit() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileBankAccountOpeningPermit())) {
            ZkUtils.showInformation("请填写银行开户行许可证", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileOrgChart() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileOrgChart())) {
            ZkUtils.showInformation("请填写企业组织架构及设置书", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileInvoiceInfo() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileInvoiceInfo())) {
            ZkUtils.showInformation("请填写合作库开票信息", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileGlobal() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileGlobal())) {
            ZkUtils.showInformation("请填写合作库全貌图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileOffice() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileOffice())) {
            ZkUtils.showInformation("请填写办公室图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileReceptionOffice() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPhone())) {
            ZkUtils.showInformation("请填写接待室图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFilePartStoreage() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFilePartStoreage())) {
            ZkUtils.showInformation("请填写配件库房图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileMap() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileMap())) {
            ZkUtils.showInformation("请填写地理位置", "提示");
            return false;
        }
//        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileTrain() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileTrain())) {
//            ZkUtils.showInformation("请填写培训资料", "提示");
//            return false;
//        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getShelfCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getShelfCount())) {
            ZkUtils.showInformation("请填写标准货架数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileShelf() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileShelf())) {
            ZkUtils.showInformation("请填写标准货架图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getContainerCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getContainerCount())) {
            ZkUtils.showInformation("请填写定制货柜数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileContainer() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileContainer())) {
            ZkUtils.showInformation("请填写定制货柜照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getLadderTruckCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getLadderTruckCount())) {
            ZkUtils.showInformation("请填写登高车数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileLadderTruck() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileLadderTruck())) {
            ZkUtils.showInformation("请填写登高车照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getForkTruckCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getForkTruckCount())) {
            ZkUtils.showInformation("请填写堆高车数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileForkTruck() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileForkTruck())) {
            ZkUtils.showInformation("请填写堆高车照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getLittleContainerCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getLittleContainerCount())) {
            ZkUtils.showInformation("请填写小件容器数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileLittleContainer() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileLittleContainer())) {
            ZkUtils.showInformation("请填写小容器照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getPartNameplateCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPartNameplateCount())) {
            ZkUtils.showInformation("请填写零件铭牌数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getStoreLampCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getStoreLampCount())) {
            ZkUtils.showInformation("请填写库房灯光数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getTagCardCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getTagCardCount())) {
            ZkUtils.showInformation("请填写货物标签卡数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getComputerCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getComputerCount())) {
            ZkUtils.showInformation("请填写电脑数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileComputer() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileComputer())) {
            ZkUtils.showInformation("请填写电脑照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getTelephoneCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getTelephoneCount())) {
            ZkUtils.showInformation("请填写电话数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileTelephone() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileTelephone())) {
            ZkUtils.showInformation("请填写电话照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFaxCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFaxCount())) {
            ZkUtils.showInformation("请填写传真数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileFax() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileFax())) {
            ZkUtils.showInformation("请填写传真机照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getCameraCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getCameraCount())) {
            ZkUtils.showInformation("请填写相机数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFileCamera() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileCamera())) {
            ZkUtils.showInformation("请填写相机照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getPackerCount() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getPackerCount())) {
            ZkUtils.showInformation("请填写手动打包机数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getFilePacker() == null || StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFilePacker())) {
            ZkUtils.showInformation("请填写打包机照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequestInfo.getAgencyInfo().getTaxRate() < 0.0) {
            ZkUtils.showInformation("请填写税率", "提示");
            return false;
        }

        return true;
    }


    @Command
    @NotifyChange("*")
    public void startProcess() {
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.agencyAdmitRequestInfo)) {
                    this.agencyAdmitRequestInfo = this.agencyAdmitService.save(this.agencyAdmitRequestInfo);
                    flowDocInfo = this.agencyAdmitRequestInfo;
                    Map<String, String> map = this.agencyAdmitService.startProcess(this.agencyAdmitRequestInfo, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
//            BindUtils.postNotifyChange(null, null, this, "checkCanEdit");
//            BindUtils.postNotifyChange(null, null, this, "readonly");
//            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_LIST, null);
                    this.updateUIState();
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });

    }


    @Command
    @Override
    public void showHandleForm() {
        if (this.agencyAdmitRequestInfo.getCanEditCode() == true && StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getCode())) {
            ZkUtils.showExclamation("请输入合作商编号！", "系统提示");
            return;
        }
        //if (StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getCoverProvinces())) {
        //    ZkUtils.showExclamation("请输入覆盖省份", "系统提示");
        //    return;
        //}
        if (this.agencyAdmitRequestInfo.getCanUpload() == true && StringUtils.isBlank(this.agencyAdmitRequestInfo.getAgencyInfo().getFileTrain())) {
            ZkUtils.showExclamation("请上传培训资料！", "系统提示");
            return;
        }
        super.showHandleForm();
    }

    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                agencyAdmitService.desertTask(this.agencyAdmitRequestInfo.getObjId());
                canHandleTask = false;
                canDesertTask = false;
                canEdit = false;
                this.updateUIState();
                showDialog();
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消作废", "提示");
            }
        });
    }


    /**
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return agencyAdmitService.save((AgencyAdmitRequestInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return agencyAdmitService.findOne(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("agencyAdmitRequestInfo", "handle", "canDesertTask", "canHandleTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_ADMIT_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }
}
