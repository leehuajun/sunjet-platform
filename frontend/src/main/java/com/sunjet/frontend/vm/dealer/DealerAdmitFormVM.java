package com.sunjet.frontend.vm.dealer;

import com.sunjet.dto.asms.basic.CityInfo;
import com.sunjet.dto.asms.basic.CountyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.dealer.DealerAdmitService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import java.util.*;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DealerAdmitFormVM extends FormVM {
    @WireVariable
    private DealerAdmitService dealerAdmitService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private DictionaryService dictionaryService;
    @WireVariable
    private UserService userService;


    @Getter
    @Setter
    private DealerAdmitRequestInfo dealerAdmitRequest = new DealerAdmitRequestInfo();

    @Getter
    @Setter
    private List<UserInfo> serviceManagers = new ArrayList<>();
    @Getter
    @Setter
    private List<String> strOtherCollaborations = new ArrayList<>();
    @Getter
    @Setter
    private List<String> strProductsOfMaintains = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> selectedOtherCollaborations = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> selectedProductsOfMaintains = new ArrayList<>();
    @Getter
    @Setter
    private List<ProvinceInfo> provinceEntities;
    @Getter
    @Setter
    private List<CityInfo> cityEntities = new ArrayList<>();
    @Getter
    @Setter
    private List<CountyInfo> countyEntities;
    @Getter
    @Setter
    private List<DealerInfo> parentDealers = new ArrayList<>();       // 列出所有父级服务站
    @Getter
    @Setter
    private Boolean canSelectParent = false;        // 是否可以选择父级服务站
    @Getter
    @Setter
    private List<DictionaryInfo> stars = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> qualifications = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> productsOfMaintains = new ArrayList<>();//拟维修我公司产品系列
    @Getter
    @Setter
    private List<DictionaryInfo> otherCollaborations = new ArrayList<>();//其他合作内容
    @Getter
    @Setter
    private Map<String, Object> variables = new HashMap<>();  //流程变量

    @Getter
    @Setter
    private ProvinceInfo selectedProvince;        // 选中的 省份/直辖市
    @Getter
    @Setter
    private CityInfo selectedCity;                // 选中的 城市

    @Getter
    @Setter
    private DealerInfo dealerInfo;

    @Getter
    @Setter
    private UserInfo selectedServiceManager;

    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            this.dealerAdmitRequest = dealerAdmitService.findOneById(objId);
            if (this.dealerAdmitRequest.getDealerInfo().getProvinceName() != null) {
                this.cityEntities = regionService.findCitiesByProvinceId(dealerAdmitRequest.getDealerInfo().getProvinceId());
            }

        } else {
            dealerAdmitRequest = new DealerAdmitRequestInfo();
            dealerAdmitRequest.setDealerInfo(new DealerInfo());
            if (getActiveUser().getDealer() != null) {   // 服务站用户
                this.dealerAdmitRequest.getDealerInfo().setParentId(getActiveUser().getDealer().getObjId());
                this.dealerAdmitRequest.getDealerInfo().setParentName(getActiveUser().getDealer().getName());
                this.dealerAdmitRequest.getDealerInfo().setLevel("二级");
                this.canSelectParent = false;
            } else {
                this.canSelectParent = true;
            }
        }
        this.setProvinceEntities(regionService.findAllProvince());
        this.setStars(dictionaryService.findDictionariesByParentCode("10010"));
        this.setQualifications(dictionaryService.findDictionariesByParentCode("10020"));
        this.setProductsOfMaintains(dictionaryService.findDictionariesByParentCode("10050"));
        this.setOtherCollaborations(dictionaryService.findDictionariesByParentCode("10060"));
        this.setActiveUserMsg(this.dealerAdmitRequest);
        if (this.dealerAdmitRequest.getCanEditServiceManager()) {
            List<UserInfo> userInfos = userService.findAllByRoleName("服务经理");
            this.serviceManagers = userInfos;
        }
        this.setActiveUserMsg(this.dealerAdmitRequest);


    }

    /**
     * 获取文件路径
     *
     * @param filename
     * @return
     */
    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_DEALER + filename;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }


    /**
     * 表单提交,保存用户信息
     */
    @Command
//    @NotifyChange("*")
    public void submit() {
        try {
            dealerAdmitRequest = dealerAdmitService.save(dealerAdmitRequest);
//            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEALER_ADMIT_LIST, null);
            this.updateUIState();
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Command
    @NotifyChange("parentDealers")
    public void searchDealers(@BindingParam("keyword") String keyword) {
        this.parentDealers = dealerService.searchDealers(keyword, getActiveUser());
    }

    @Command
    @NotifyChange("dealerAdmitRequest")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        if (selectedCity != null) {
            this.dealerAdmitRequest.getDealerInfo().setCityId(selectedCity.getObjId());
            this.dealerAdmitRequest.getDealerInfo().setCityName(selectedCity.getName());
        }
        this.dealerAdmitRequest.getDealerInfo().setParentId(dealer.getObjId());
        this.dealerAdmitRequest.getDealerInfo().setParentName(dealer.getName());
        dealerAdmitRequest.getDealerInfo().setLevel("二级");
    }

    @Override
    @Command
    @NotifyChange({"dealerAdmitRequest"})
    public void clearSelectedDealer() {
        this.dealerAdmitRequest.getDealerInfo().setParentName(null);
        dealerAdmitRequest.getDealerInfo().setLevel("一级");
    }

    @Command
    @NotifyChange("dealerAdmitRequest")
    public void selectProductsOfMaintain() {
        StringBuffer sb = new StringBuffer();
        int count = this.selectedProductsOfMaintains.size();
        this.strProductsOfMaintains = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strProductsOfMaintains.add(this.selectedProductsOfMaintains.get(i).getName());
            if (i == 0) {
                sb.append(this.selectedProductsOfMaintains.get(i).getName());
            } else {
                sb.append("/" + this.selectedProductsOfMaintains.get(i).getName());
            }
        }
        this.dealerAdmitRequest.getDealerInfo().setProductsOfMaintain(sb.toString().trim());
    }

    /**
     * 提交,启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        //checkValid();
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.dealerAdmitRequest)) {
                    this.dealerAdmitRequest = dealerAdmitService.save(this.dealerAdmitRequest);
                    Map<String, String> map = dealerAdmitService.startProcess(this.dealerAdmitRequest, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.dealerAdmitRequest = (DealerAdmitRequestInfo) findInfoById(this.dealerAdmitRequest.getObjId());
                    this.flowDocInfo = this.dealerAdmitRequest;
                    updateUIState();
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消作废", "提示");
            }
        });
    }

    /**
     * 选择省份
     */
    @Command
    @NotifyChange({"cityEntities"})
    public void selectProvince(@BindingParam("event") Event event) {
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        if (selectedProvince != null) {
            dealerAdmitRequest.getDealerInfo().setProvinceId(selectedProvince.getObjId());
            dealerAdmitRequest.getDealerInfo().setProvinceName(selectedProvince.getName());
            this.cityEntities = regionService.findCitiesByProvinceId(this.selectedProvince.getObjId());
        }
    }

    /**
     * 选择市区
     */
    @Command
    @NotifyChange({"cityEntities", "selectedCity"})
    public void selectCity(@BindingParam("event") Event event) {
        this.selectedCity = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        if (selectedCity != null) {
            dealerAdmitRequest.getDealerInfo().setCityId(selectedCity.getObjId());
            dealerAdmitRequest.getDealerInfo().setCityName(selectedCity.getName());
        }
    }

    @Command
    @NotifyChange("dealerAdmitRequest")
    public void selectCollaboration() {
//        .info(this.selectedOtherCollaborations.size() + "");
        StringBuffer sb = new StringBuffer();
        int count = this.selectedOtherCollaborations.size();
        this.strOtherCollaborations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strOtherCollaborations.add(this.selectedOtherCollaborations.get(i).getName());
            if (i == 0) {
                sb.append(this.selectedOtherCollaborations.get(i).getName());
            } else {
                sb.append("/" + this.selectedOtherCollaborations.get(i).getName());
            }
        }
        this.dealerAdmitRequest.getDealerInfo().setOtherCollaboration(sb.toString().trim());
    }

    /**
     * 上传图片
     *
     * @param event
     * @param type
     */
    @Command
    @NotifyChange("dealerAdmitRequest")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        //String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_DEALER);
        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_DEALER);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.dealerAdmitRequest.getDealerInfo().setFileBusinessLicense(fileName);
        } else if (type.equalsIgnoreCase("t02")) {
            this.dealerAdmitRequest.getDealerInfo().setFileTaxCertificate(fileName);
        } else if (type.equalsIgnoreCase("t03")) {
            this.dealerAdmitRequest.getDealerInfo().setFileBankAccountOpeningPermit(fileName);
        } else if (type.equalsIgnoreCase("t04")) {
            this.dealerAdmitRequest.getDealerInfo().setFilePersonnelCertificate(fileName);
        } else if (type.equalsIgnoreCase("t05")) {
            this.dealerAdmitRequest.getDealerInfo().setFileQualification(fileName);
        } else if (type.equalsIgnoreCase("t06")) {
            this.dealerAdmitRequest.getDealerInfo().setFileInvoiceInfo(fileName);
        } else if (type.equalsIgnoreCase("t07")) {
            this.dealerAdmitRequest.getDealerInfo().setFileRoadTransportLicense(fileName);
        } else if (type.equalsIgnoreCase("t08")) {
            this.dealerAdmitRequest.getDealerInfo().setFileOrgChart(fileName);
        } else if (type.equalsIgnoreCase("t09")) {
            this.dealerAdmitRequest.getDealerInfo().setFileDevice(fileName);
        } else if (type.equalsIgnoreCase("t10")) {
            this.dealerAdmitRequest.getDealerInfo().setFileReceptionOffice(fileName);
        } else if (type.equalsIgnoreCase("t11")) {
            this.dealerAdmitRequest.getDealerInfo().setFileMap(fileName);
        } else if (type.equalsIgnoreCase("t12")) {
            this.dealerAdmitRequest.getDealerInfo().setFileGlobal(fileName);
        } else if (type.equalsIgnoreCase("t13")) {
            this.dealerAdmitRequest.getDealerInfo().setFileOffice(fileName);
        } else if (type.equalsIgnoreCase("t14")) {
            this.dealerAdmitRequest.getDealerInfo().setFilePartStoreage(fileName);
        } else if (type.equalsIgnoreCase("t15")) {
            this.dealerAdmitRequest.getDealerInfo().setFileWorkshop(fileName);
        } else if (type.equalsIgnoreCase("t16")) {
            this.dealerAdmitRequest.getDealerInfo().setFileTrain(fileName);
        } else if (type.equalsIgnoreCase("t17")) {
            this.dealerAdmitRequest.getDealerInfo().setPartReport(fileName);
        }
    }

    /**
     * 删除图片
     *
     * @param type
     */
    @Command
    @NotifyChange("dealerAdmitRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t01")) {
            //营业执照
            this.dealerAdmitRequest.getDealerInfo().setFileBusinessLicense("");
        } else if (type.equalsIgnoreCase("t02")) {
            this.dealerAdmitRequest.getDealerInfo().setFileTaxCertificate("");
        } else if (type.equalsIgnoreCase("t03")) {
            this.dealerAdmitRequest.getDealerInfo().setFileBankAccountOpeningPermit("");
        } else if (type.equalsIgnoreCase("t04")) {
            this.dealerAdmitRequest.getDealerInfo().setFilePersonnelCertificate("");
        } else if (type.equalsIgnoreCase("t05")) {
            this.dealerAdmitRequest.getDealerInfo().setFileQualification("");
        } else if (type.equalsIgnoreCase("t06")) {
            this.dealerAdmitRequest.getDealerInfo().setFileInvoiceInfo("");
        } else if (type.equalsIgnoreCase("t07")) {
            this.dealerAdmitRequest.getDealerInfo().setFileRoadTransportLicense("");
        } else if (type.equalsIgnoreCase("t08")) {
            this.dealerAdmitRequest.getDealerInfo().setFileOrgChart("");
        } else if (type.equalsIgnoreCase("t09")) {
            this.dealerAdmitRequest.getDealerInfo().setFileDevice("");
        } else if (type.equalsIgnoreCase("t10")) {
            this.dealerAdmitRequest.getDealerInfo().setFileReceptionOffice("");
        } else if (type.equalsIgnoreCase("t11")) {
            this.dealerAdmitRequest.getDealerInfo().setFileMap("");
        } else if (type.equalsIgnoreCase("t12")) {
            this.dealerAdmitRequest.getDealerInfo().setFileGlobal("");
        } else if (type.equalsIgnoreCase("t13")) {
            this.dealerAdmitRequest.getDealerInfo().setFileOffice("");
        } else if (type.equalsIgnoreCase("t14")) {
            this.dealerAdmitRequest.getDealerInfo().setFilePartStoreage("");
        } else if (type.equalsIgnoreCase("t15")) {
            this.dealerAdmitRequest.getDealerInfo().setFileWorkshop("");
        } else if (type.equalsIgnoreCase("t16")) {
            this.dealerAdmitRequest.getDealerInfo().setFileTrain("");
        } else if (type.equalsIgnoreCase("t17")) {
            this.dealerAdmitRequest.getDealerInfo().setPartReport("");
        }


    }

    @Override
    protected Boolean checkValid() {
        List<String> ignoreFields = Arrays.asList("serialVersionUID", "objId", "createrId", "createrName", "modifierId", "modifierName", "code", "county", "parent", "serviceManager", "fileTrain");
        if (ZkUtils.checkFieldValid(this.dealerAdmitRequest.getDealerInfo(), ignoreFields) == false) {
            return false;
        }

        //this.dealerAdmitRequest.setStatus(DocStatus.DRAFT.getIndex());

        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getObjId()) && StringUtils.isNotBlank(this.dealerAdmitRequest.getDealerInfo().getCode())) {
            if (dealerService.checkCodeExists(this.dealerAdmitRequest.getDealerInfo().getCode())) {
                ZkUtils.showError("服务站编号【" + this.dealerAdmitRequest.getDealerInfo().getCode() + "】已存在!", "系统提示");
                return false;
            }
        }
        //基本信息验证
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getName())) {
            ZkUtils.showInformation("请填写服务站名称", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getAddress())) {
            ZkUtils.showInformation("请填写服务站地址", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getPhone())) {
            ZkUtils.showInformation("请填写电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFax())) {
            ZkUtils.showInformation("请填写传真", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getProvinceId())) {
            ZkUtils.showInformation("请选择省份", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getProvinceName())) {
            ZkUtils.showInformation("请选择省份", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getCityId())) {
            ZkUtils.showInformation("请选择市", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getCityName())) {
            ZkUtils.showInformation("请选择市", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getOrgCode())) {
            ZkUtils.showInformation("请填写组织机构代码", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getOrgCode())) {
            ZkUtils.showInformation("请填写纳税人识别号", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getBank())) {
            ZkUtils.showInformation("请填写开户银行", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getBankAccount())) {
            ZkUtils.showInformation("请填写银行账号", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getBusinessLicenseCode())) {
            ZkUtils.showInformation("请填写营业执照号", "提示");
            return false;
        }
        //人员信息验证
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getLegalPerson())) {
            ZkUtils.showInformation("请填写法人代表", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getLegalPersonPhone())) {
            ZkUtils.showInformation("请填写法人联系电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getStationMaster())) {
            ZkUtils.showInformation("请填写站长", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getStationMasterPhone())) {
            ZkUtils.showInformation("请填写站长电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getTechnicalDirector())) {
            ZkUtils.showInformation("请填写技术主管", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getTechnicalDirectorPhone())) {
            ZkUtils.showInformation("请填写技术主管电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getClaimDirector())) {
            ZkUtils.showInformation("请填写索赔主管", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getClaimDirectorPhone())) {
            ZkUtils.showInformation("请填写索赔主管电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getPartDirector())) {
            ZkUtils.showInformation("请填写配件主管", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getPartDirectorPhone())) {
            ZkUtils.showInformation("请填写配件主管电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFinanceDirector())) {
            ZkUtils.showInformation("请填写财务经理", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFinanceDirectorPhone())) {
            ZkUtils.showInformation("请填写财务经理电话", "提示");
            return false;
        }

        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getEmployeeCount())) {
            ZkUtils.showInformation("请填写员工人数", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getReceptionistCount())) {
            ZkUtils.showInformation("请填接待员数量", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getPartKeeyperCount())) {
            ZkUtils.showInformation("请填写配件员数量", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getMaintainerCount())) {
            ZkUtils.showInformation("请填写维修工数量", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getQcInspectorCount())) {
            ZkUtils.showInformation("请填写质检员数量", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getClerkCount())) {
            ZkUtils.showInformation("请填写结算员数量", "提示");
            return false;
        }
        //验证场地信息
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getParkingArea())) {
            ZkUtils.showInformation("请填写停车区面积", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getReceptionArea())) {
            ZkUtils.showInformation("请填写接待室面积", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getGeneralArea())) {
            ZkUtils.showInformation("请填写综合维修区面积", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getAssemblyArea())) {
            ZkUtils.showInformation("请填写总成维修区面积", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getStorageArea())) {
            ZkUtils.showInformation("请填写配件库总面积", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getStorageWulingArea())) {
            ZkUtils.showInformation("请填写五菱配件库总面积", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getStorageUserdPartArea())) {
            ZkUtils.showInformation("请填写旧件库总面积", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getStorageWulingUserdPartArea())) {
            ZkUtils.showInformation("请填写五菱旧件库总面积", "提示");
            return false;
        }
        //资质信息
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getOtherMaintainCondition())) {
            ZkUtils.showInformation("请填写其他车辆维修条件", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getOtherBrand())) {
            ZkUtils.showInformation("请填写兼做的品牌服务", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getProductsOfMaintain())) {
            ZkUtils.showInformation("请选择拟维修我公司产品", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getPartReport())) {
            ZkUtils.showInformation("请上传配件储配表", "提示");
            return false;
        }
        //图片信息(附件)检查
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileBusinessLicense())) {
            ZkUtils.showInformation("请上传营业执照", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileTaxCertificate())) {
            ZkUtils.showInformation("请上传税务登记证", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileBankAccountOpeningPermit())) {
            ZkUtils.showInformation("请上传银行开户行许可证", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFilePersonnelCertificate())) {
            ZkUtils.showInformation("请上传人员登记证书", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileQualification())) {
            ZkUtils.showInformation("请上传维修资质表", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileInvoiceInfo())) {
            ZkUtils.showInformation("请上传服务站开票信息", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileRoadTransportLicense())) {
            ZkUtils.showInformation("请上传道路运输营业许可证", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileOrgChart())) {
            ZkUtils.showInformation("请上传企业组织架构及设置书", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileDevice())) {
            ZkUtils.showInformation("请上传设备清单", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileReceptionOffice())) {
            ZkUtils.showInformation("请上传接待室图片", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileGlobal())) {
            ZkUtils.showInformation("请上传服务商全貌图片", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileOffice())) {
            ZkUtils.showInformation("请上传办公室图片", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileWorkshop())) {
            ZkUtils.showInformation("请上传维修车间图片", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFilePartStoreage())) {
            ZkUtils.showInformation("请上传配件库房图片", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileMap())) {
            ZkUtils.showInformation("请上传地理位置", "提示");
            return false;
        }
//        if (this.dealerAdmitRequest.getDealerInfo().getFileTrain() == null ) {
//            ZkUtils.showInformation("请上传培训资料", "提示");
//            return false;
//        }
        if (this.dealerAdmitRequest.getDealerInfo().getTaxRate() < 0.0) {
            ZkUtils.showInformation("请填写税率", "提示");
            return false;
        }


        return true;
    }

    @Command
    @NotifyChange("dealerAdmitRequest")
    public void selectServiceManager(@BindingParam("event") Event event) {
        this.selectedServiceManager = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        this.dealerAdmitRequest.getDealerInfo().setServiceManagerId(selectedServiceManager.getObjId());
        this.dealerAdmitRequest.getDealerInfo().setServiceManagerName(selectedServiceManager.getName());

    }


    @Command
    @Override
    public void showHandleForm() {
        if (this.dealerAdmitRequest.getCanEditServiceManager()) {
            if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getServiceManagerId())) {
                ZkUtils.showInformation("请选择服务经理", "提示");
                return;
            }
        }
        if (this.dealerAdmitRequest.getCanUpload()) {
            if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getFileTrain()) || this.dealerAdmitRequest.getDealerInfo().getFileTrain() == null) {
                ZkUtils.showInformation("请上传培训资料", "提示");
                return;
            }
        }
        if (this.dealerAdmitRequest.getCanEditCode()) {
            if (StringUtils.isBlank(this.dealerAdmitRequest.getDealerInfo().getCode())) {
                ZkUtils.showExclamation("请输入服务站编号！", "系统提示");
                return;
            } else if (this.dealerAdmitRequest.getDealerInfo().getCode().length() != 7) {
                ZkUtils.showExclamation("服务站编号长度必须等于7位", "系统提示");
                return;
            } else if (!this.dealerAdmitRequest.getDealerInfo().getCode().trim().substring(3, 4).equals("1") && !this.dealerAdmitRequest.getDealerInfo().getCode().trim().substring(3, 4).equals("2")) {
//                ZkUtils.showInformation(this.dealerAdmitRequest.getDealer().getCode().trim().substring(3,4),"title");
//                ZkUtils.showInformation(String.valueOf(this.dealerAdmitRequest.getDealer().getCode().trim().charAt(3)),"title");
                ZkUtils.showExclamation("服务站编号第4位必须是1或者2", "系统提示");
                return;
            } else if (StringUtils.isNotBlank(dealerService.findOneByCode(this.dealerAdmitRequest.getDealerInfo().getCode()).getCode())) {
                ZkUtils.showExclamation("此服务站编号已存在,请更换", "系统提示");
                return;
            }

        }
        super.showHandleForm();
    }

    /**
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return dealerAdmitService.save((DealerAdmitRequestInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return dealerAdmitService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("dealerAdmitRequest", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEALER_ADMIT_LIST, null);
    }

    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                dealerAdmitService.desertTask(this.dealerAdmitRequest.getObjId());
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

}
