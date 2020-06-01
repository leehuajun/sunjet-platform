package com.sunjet.frontend.vm.dealer;

import com.sunjet.dto.asms.basic.*;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.basic.DealerBackupService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.dealer.DealerLevelChangeService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.*;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DealerLevelChangeFormVM extends FormVM {

    @WireVariable
    private DealerLevelChangeService dealerLevelChangeService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private DictionaryService dictionaryService;
    @WireVariable
    private DealerBackupService dealerBackupService;

    @Getter
    @Setter
    private DealerLevelChangeRequestInfo dealerLevelChangeRequest = new DealerLevelChangeRequestInfo();

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

    //@Getter @Setter
    //private DealerInfo dealerInfo = new DealerInfo();

    @Getter
    @Setter
    private DealerBackupInfo dealerBackupInfo = new DealerBackupInfo();

    @Getter
    @Setter
    private RegionInfo city;

    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            this.dealerLevelChangeRequest = dealerLevelChangeService.findOneById(objId);
            if (StringUtils.isNotBlank(this.dealerLevelChangeRequest.getDealerBackupId())) {
                this.dealerBackupInfo = dealerBackupService.findOneBackupById(this.dealerLevelChangeRequest.getDealerBackupId());
            }
            //dealerInfo = dealerService.findOneById(dealerLevelChangeRequest.getDealerInfo().getObjId());
            city = regionService.findCityById(this.dealerBackupInfo.getProvinceId());
        } else {
            dealerLevelChangeRequest = new DealerLevelChangeRequestInfo();
            dealerLevelChangeRequest.setDealerInfo(new DealerInfo());

        }
        this.setProvinceEntities(regionService.findAllProvince());
        this.setStars(dictionaryService.findDictionariesByParentCode("10010"));
        this.setQualifications(dictionaryService.findDictionariesByParentCode("10020"));
        this.setProductsOfMaintains(dictionaryService.findDictionariesByParentCode("10050"));
        this.setOtherCollaborations(dictionaryService.findDictionariesByParentCode("10060"));
        this.setActiveUserMsg(this.dealerLevelChangeRequest);


    }

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
//    @NotifyChange("dealerLevelChangeRequest")
    public void submit() {
        try {
            if (StringUtils.isBlank(this.dealerLevelChangeRequest.getDealer())) {
                ZkUtils.showExclamation("请先选择服务站！", "系统提示");
                return;
            }
            this.dealerBackupInfo = dealerBackupService.saveDealerBackup(this.dealerBackupInfo);
            this.dealerLevelChangeRequest.setDealerBackupId(this.dealerBackupInfo.getObjId());
            this.dealerLevelChangeRequest = dealerLevelChangeService.save(dealerLevelChangeRequest);
            flowDocInfo = this.dealerLevelChangeRequest;
            this.updateUIState();
//            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEALER_LEVEL_CHANGE_LIST, null);
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择服务站编号
     */
    @Command
    @NotifyChange({"dealerLevelChangeRequest", "dealerBackupInfo"})
    public void selectDealer(@BindingParam("model") DealerInfo dealerInfo) {
        //dealerInfo = model;
        //dealerInfo.setCode(model.getCode());
        //dealerInfo.setName(model.getName());
        BeanUtils.copyProperties(dealerInfo, this.dealerBackupInfo);
        this.dealerBackupInfo.setObjId(null);
        //dealerLevelChangeRequest.setDealerInfo(dealerInfo);
        dealerLevelChangeRequest.setDealer(dealerInfo.getObjId());

    }

    @Command
    @NotifyChange("*")
    public void selectCollaboration() {
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
        this.dealerLevelChangeRequest.setOtherCollaboration(sb.toString().trim());
    }

    @Command
    @NotifyChange("dealerLevelChangeRequest")
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
        this.dealerLevelChangeRequest.setProductsOfMaintain(sb.toString().trim());
    }

    @Override
    protected Boolean checkValid() {
        if (StringUtils.isBlank(this.dealerLevelChangeRequest.getPartReport())) {
            ZkUtils.showInformation("请上传配件储备清单", "提示");
            return false;
        }
        return true;
    }

    /**
     * 启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {

        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.dealerLevelChangeRequest)) {
                    this.dealerLevelChangeRequest = this.dealerLevelChangeService.save(this.dealerLevelChangeRequest);
                    flowDocInfo = this.dealerLevelChangeRequest;
                    Map<String, String> map = this.dealerLevelChangeService.startProcess(this.dealerLevelChangeRequest, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.updateUIState();
                }

            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消作废", "提示");
            }
        });

    }

    /**
     * 上传图片
     *
     * @param event
     * @param type
     */
    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_DEALER);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("t15")) {  // 维修资质
            this.dealerLevelChangeRequest.setFileBusinessLicense(fileName);            // 营业执照 照片文件
        } else if (type.equalsIgnoreCase("t01")) {
            this.dealerLevelChangeRequest.setFileTaxCertificate(fileName);     // 税务登记证 照片文件
        } else if (type.equalsIgnoreCase("t02")) {
            this.dealerLevelChangeRequest.setFileBankAccountOpeningPermit(fileName);  // 银行开户行许可证
        } else if (type.equalsIgnoreCase("t03")) {
            this.dealerLevelChangeRequest.setFilePersonnelCertificate(fileName);  // 人员登记证书
        } else if (type.equalsIgnoreCase("t04")) {
            this.dealerLevelChangeRequest.setFileQualification(fileName);       // 维修资质表
        } else if (type.equalsIgnoreCase("t05")) {
            this.dealerLevelChangeRequest.setFileInvoiceInfo(fileName);      // 服务站开票信息
        } else if (type.equalsIgnoreCase("t06")) {
            this.dealerLevelChangeRequest.setFileRoadTransportLicense(fileName);   // 道路运输营业许可证
        } else if (type.equalsIgnoreCase("t07")) {
            this.dealerLevelChangeRequest.setFileOrgChart(fileName);        // 企业组织架构及设置书
        } else if (type.equalsIgnoreCase("t08")) {
            this.dealerLevelChangeRequest.setFileDevice(fileName);      // 设备清单
        } else if (type.equalsIgnoreCase("t09")) {
            this.dealerLevelChangeRequest.setFileReceptionOffice(fileName);  // 接待室图片
        } else if (type.equalsIgnoreCase("t10")) {
            this.dealerLevelChangeRequest.setFileMap(fileName);        // 地理位置
        } else if (type.equalsIgnoreCase("t11")) {
            this.dealerLevelChangeRequest.setFileGlobal(fileName);         // 服务商全貌图片
        } else if (type.equalsIgnoreCase("t12")) {
            this.dealerLevelChangeRequest.setFileOffice(fileName);          // 办公室图片
        } else if (type.equalsIgnoreCase("t13")) {
            this.dealerLevelChangeRequest.setFilePartStoreage(fileName);// 配件库房图片
        } else if (type.equalsIgnoreCase("t14")) {
            this.dealerLevelChangeRequest.setFileWorkshop(fileName);    // 维修车间
        } else if (type.equalsIgnoreCase("t16")) {
            this.dealerLevelChangeRequest.setPartReport(fileName);    // 配件储备清单
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
     * 删除图片
     *
     * @param type
     */
    @Command
    @NotifyChange("dealerLevelChangeRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t15")) {  // 维修资质
            this.dealerLevelChangeRequest.setFileBusinessLicense("");            // 营业执照 照片文件
        } else if (type.equalsIgnoreCase("t01")) {
            this.dealerLevelChangeRequest.setFileTaxCertificate("");     // 税务登记证 照片文件
        } else if (type.equalsIgnoreCase("t02")) {
            this.dealerLevelChangeRequest.setFileBankAccountOpeningPermit("");  // 银行开户行许可证
        } else if (type.equalsIgnoreCase("t03")) {
            this.dealerLevelChangeRequest.setFilePersonnelCertificate("");  // 人员登记证书
        } else if (type.equalsIgnoreCase("t04")) {
            this.dealerLevelChangeRequest.setFileQualification("");       // 维修资质表
        } else if (type.equalsIgnoreCase("t05")) {
            this.dealerLevelChangeRequest.setFileInvoiceInfo("");      // 服务站开票信息
        } else if (type.equalsIgnoreCase("t06")) {
            this.dealerLevelChangeRequest.setFileRoadTransportLicense("");   // 道路运输营业许可证
        } else if (type.equalsIgnoreCase("t07")) {
            this.dealerLevelChangeRequest.setFileOrgChart("");        // 企业组织架构及设置书
        } else if (type.equalsIgnoreCase("t08")) {
            this.dealerLevelChangeRequest.setFileDevice("");      // 设备清单
        } else if (type.equalsIgnoreCase("t09")) {
            this.dealerLevelChangeRequest.setFileReceptionOffice("");  // 接待室图片
        } else if (type.equalsIgnoreCase("t10")) {
            this.dealerLevelChangeRequest.setFileMap("");        // 地理位置
        } else if (type.equalsIgnoreCase("t11")) {
            this.dealerLevelChangeRequest.setFileGlobal("");         // 服务商全貌图片
        } else if (type.equalsIgnoreCase("t12")) {
            this.dealerLevelChangeRequest.setFileOffice("");          // 办公室图片
        } else if (type.equalsIgnoreCase("t13")) {
            this.dealerLevelChangeRequest.setFilePartStoreage("");// 配件库房图片
        } else if (type.equalsIgnoreCase("t14")) {
            this.dealerLevelChangeRequest.setFileWorkshop("");    // 维修车间
        } else if (type.equalsIgnoreCase("t16")) {
            this.dealerLevelChangeRequest.setPartReport("");    // 配件储备清单
        }
    }


    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                dealerLevelChangeService.desertTask(this.dealerLevelChangeRequest.getObjId());
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
        return dealerLevelChangeService.save((DealerLevelChangeRequestInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return dealerLevelChangeService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("dealerLevelChangeRequest", "handle", "canDesertTask", "canHandleTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null,
                GlobalCommandValues.REFRESH_DEALER_LEVEL_CHANGE_LIST, null);

    }

}
