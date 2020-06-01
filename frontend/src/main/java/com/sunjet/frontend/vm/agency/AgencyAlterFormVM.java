package com.sunjet.frontend.vm.agency;

import com.sunjet.dto.asms.agency.AgencyAlterRequestInfo;
import com.sunjet.dto.asms.basic.AgencyBackupInfo;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.agency.AgencyAlterService;
import com.sunjet.frontend.service.basic.AgencyBackupService;
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
import org.springframework.beans.BeanUtils;
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
public class AgencyAlterFormVM extends FormVM {


    @WireVariable
    private AgencyAlterService agencyAlterService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private AgencyBackupService agencyBackupService;

    @Getter
    @Setter
    private AgencyAlterRequestInfo agencyAlterRequest = new AgencyAlterRequestInfo(); //接收列表model的服务站变更对象

    //@Getter
    //@Setter
    //private AgencyBackupInfo agency = new AgencyBackupInfo();        //合作商对象
    @Getter
    @Setter
    private AgencyBackupInfo agencyBackupInfo = new AgencyBackupInfo();        //合作商备份对象

    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            this.agencyAlterRequest = agencyAlterService.findOne(this.objId);
            if (StringUtils.isNotBlank(this.agencyAlterRequest.getAgencyBackupId())) {
                this.agencyBackupInfo = agencyBackupService.findOneBackupInfoById(this.agencyAlterRequest.getAgencyBackupId());
            }


        } else {
            agencyAlterRequest = new AgencyAlterRequestInfo();
            agencyAlterRequest.setAgencyInfo(new AgencyInfo());
        }
        this.setActiveUserMsg(this.agencyAlterRequest);
        //if (StringUtils.isNotBlank(agencyAlterRequest.getProcessInstanceId())) {
        //    this.readonly = true;
        //}
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgencies(@BindingParam("model") String keyword) {
        if (getActiveUser().getAgency() != null) {   // 合作库用户
            this.agencies.clear();
            this.agencies.add(getActiveUser().getAgency());
        } else if (getActiveUser().getDealer() != null) {  // 服务站用户
            this.agencies.clear();
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
        } else {   // 五菱用户
            this.agencies = agencyService.findAllByKeyword(keyword.trim());
        }
    }

    //@Command
    //@NotifyChange({"agency", "keyword"})
    //public void clearSelectedAgency() {
    //    this.agency = getActiveUser().getAgency();
    //    this.setKeyword("");
    //}

    @Command
    @NotifyChange({"agencyAlterRequest", "agencyBackupInfo"})
    public void selectAgency(@BindingParam("model") AgencyInfo agencyInfo) {
        this.setKeyword("");
        this.agencies.clear();
        BeanUtils.copyProperties(agencyInfo, this.agencyBackupInfo);
        this.agencyBackupInfo.setObjId(null);

        //this.agencyAlterRequest.setAgencyInfo(agency);

        //BeanUtils.copyProperties(agency,this.agencyAlterRequest);
        //this.agencyBackupInfo.setCode(agencyInfo.getCode());
        //this.agency.setName(agencyInfo.getName());
        //this.agencyAlterRequest.setAgencyInfo(agencyInfo);

        this.agencyAlterRequest.setAgency(agencyInfo.getObjId());
    }

    /**
     * 保存实体
     */
    @Command
    public void submit() {
        if (StringUtils.isBlank(this.agencyAlterRequest.getAgency())) {
            ZkUtils.showInformation("请选合作商再保存", "提示");
            return;
        }
        this.agencyBackupInfo = agencyBackupService.saveBackupInfo(this.agencyBackupInfo);
        this.agencyAlterRequest.setAgencyBackupId(this.agencyBackupInfo.getObjId());
        //BeanUtils.copyProperties(this.agencyAlterRequest, this.getAgency(), BeanHelper.getNullPropertyNames(this.agencyAlterRequest));
        //this.agencyAlterRequest.setAgencyInfo(getAgency());
        this.agencyAlterRequest = agencyAlterService.save(agencyAlterRequest);
        flowDocInfo = this.agencyAlterRequest;
        this.updateUIState();
        showDialog();
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
//        this.agencyAlterRequest.setFileQualification(fileName);

        if (type.equalsIgnoreCase("t00")) {  // 维修资质
            this.agencyAlterRequest.setFileAlteration(fileName);  // 变更函
        } else if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAlterRequest.setFileQualification(fileName);
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAlterRequest.setFileOrgChart(fileName);
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAlterRequest.setFileBusinessLicense(fileName);
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAlterRequest.setFileGlobal(fileName);
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAlterRequest.setFileTaxCertificate(fileName);
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAlterRequest.setFileOffice(fileName);
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAlterRequest.setFileBankAccountOpeningPermit(fileName);
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAlterRequest.setFileReceptionOffice(fileName);
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAlterRequest.setFileInvoiceInfo(fileName);
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAlterRequest.setFilePartStoreage(fileName);
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAlterRequest.setFilePersonnelCertificate(fileName);
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAlterRequest.setFileMap(fileName);
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAlterRequest.setFileShelf(fileName);
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAlterRequest.setFileComputer(fileName);
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAlterRequest.setFileContainer(fileName);
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAlterRequest.setFileTelephone(fileName);
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAlterRequest.setFileLadderTruck(fileName);
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAlterRequest.setFileFax(fileName);
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAlterRequest.setFileForkTruck(fileName);
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAlterRequest.setFileCamera(fileName);
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAlterRequest.setFileLittleContainer(fileName);
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAlterRequest.setFilePacker(fileName);
        }
    }

    /**
     * 删除上传文件
     *
     * @param type
     */
    @Command
    @NotifyChange("agencyAlterRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t00")) {  // 维修资质
            this.agencyAlterRequest.setFileAlteration("");  // 变更函
        } else if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAlterRequest.setFileQualification("");
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAlterRequest.setFileOrgChart("");
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAlterRequest.setFileBusinessLicense("");
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAlterRequest.setFileGlobal("");
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAlterRequest.setFileTaxCertificate("");
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAlterRequest.setFileOffice("");
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAlterRequest.setFileBankAccountOpeningPermit("");
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAlterRequest.setFileReceptionOffice("");
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAlterRequest.setFileInvoiceInfo("");
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAlterRequest.setFilePartStoreage("");
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAlterRequest.setFilePersonnelCertificate("");
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAlterRequest.setFileMap("");
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAlterRequest.setFileShelf("");
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAlterRequest.setFileComputer("");
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAlterRequest.setFileContainer("");
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAlterRequest.setFileTelephone("");
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAlterRequest.setFileLadderTruck("");
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAlterRequest.setFileFax("");
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAlterRequest.setFileForkTruck("");
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAlterRequest.setFileCamera("");
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAlterRequest.setFileLittleContainer("");
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAlterRequest.setFilePacker("");
        }
    }


    @Override
    protected Boolean checkValid() {
        if (org.apache.commons.lang.StringUtils.isBlank(this.agencyAlterRequest.getFileAlteration())) {
            ZkUtils.showInformation("请上传变更函", "提示");
            return false;
        }
        return true;
    }

    @Command
    public void startProcess() {

        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.agencyAlterRequest)) {
                    this.agencyAlterRequest = this.agencyAlterService.save(this.agencyAlterRequest);
                    flowDocInfo = this.agencyAlterRequest;
                    Map<String, String> map = this.agencyAlterService.startProcess(this.agencyAlterRequest, getActiveUser());
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
                ZkUtils.showInformation("取消提交", "提示");
            }
        });
    }


    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                agencyAlterService.desertTask(this.agencyAlterRequest.getObjId());
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
     * F
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return agencyAlterService.save((AgencyAlterRequestInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return agencyAlterService.findOne(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("agencyAlterRequest", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_ALTER_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

}
