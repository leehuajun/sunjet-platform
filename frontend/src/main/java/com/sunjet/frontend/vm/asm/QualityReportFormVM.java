package com.sunjet.frontend.vm.asm;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.sunjet.dto.asms.asm.QualityReportInfo;
import com.sunjet.dto.asms.asm.ReportPartInfo;
import com.sunjet.dto.asms.asm.ReportVehicleInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.QualityReportService;
import com.sunjet.frontend.service.asm.ReportPartService;
import com.sunjet.frontend.service.asm.ReportVehicleService;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.common.UUIDHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * 售后质量速报 表单 VM
 */
public class QualityReportFormVM extends FormVM {

    @WireVariable
    private QualityReportService qualityReportService;    //质量速报
    @WireVariable
    private DictionaryService dictionaryService;   //数据字典
    @WireVariable
    private ReportVehicleService reportVehicleService;    //速报车辆子行
    @WireVariable
    private ReportPartService reportPartService;    //速报配件子行
    @WireVariable
    private VehicleService vehicleService;          //车辆
    @WireVariable
    private PartService partService;   // 配件

    @WireVariable
    private UserService userService;

    @Getter
    @Setter
    private QualityReportInfo qualityReportInfo = new QualityReportInfo(); // 质量速报单
    @Getter
    @Setter
    private ReportVehicleInfo reportVehicleInfo = new ReportVehicleInfo();   // 速报车辆
    @Getter
    @Setter
    private ReportPartInfo reportPartInfo = new ReportPartInfo();
    @Getter
    @Setter
    private List<PartInfo> partList = new ArrayList<>(); //配件列表
    @Getter
    @Setter
    private List<PartInfo> selectedPartList = new ArrayList<>();
    @Getter
    @Setter
    private List<VehicleInfo> vehicleEntities = new ArrayList<>();
    @Getter
    @Setter
    private List selectedVehicleList = new ArrayList<>();
    @Getter
    @Setter
    private List<DealerInfo> dealerInfos = new ArrayList<DealerInfo>();  // 服务站列表
    @Getter
    @Setter
    private List<DictionaryInfo> fmvehicleTypes = new ArrayList<>();    // 车辆分类
    @Getter
    @Setter
    private DictionaryInfo selectedVehicleType;
    @Getter
    @Setter
    private Boolean manager = true;//服务经理
    @Getter
    @Setter
    private Window window;
    /**
     * 是否显示配件选择框
     */
    @Getter
    @Setter
    private Boolean showSelectWin = false;
    /**
     * 是否显示车辆选择框
     */
    @Getter
    @Setter
    private Boolean showSelectVehicleWin = false;


    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            this.qualityReportInfo = qualityReportService.findOneById(objId);
        } else {
            qualityReportInfo = new QualityReportInfo();
            DealerInfo dealer = getActiveUser().getDealer();
            if (dealer != null) {
                qualityReportInfo.setDealerCode(dealer.getCode());
                qualityReportInfo.setDealerName(dealer.getName());
                UserInfo serviceManager = userService.findOne(dealer.getServiceManagerId());
                qualityReportInfo.setServiceManagerPhone(serviceManager.getPhone());
                qualityReportInfo.setServiceManager(serviceManager.getName());
            }
            qualityReportInfo.setLinkman(getActiveUser().getUsername());
            qualityReportInfo.setLinkmanPhone(getActiveUser().getPhone());

        }


        fmvehicleTypes = dictionaryService.findDictionariesByParentCode("15000");
        if (StringUtils.isNotBlank(this.qualityReportInfo.getVehicleType())) {
            for (DictionaryInfo entity : this.fmvehicleTypes) {
                if (entity.getName().equals(this.qualityReportInfo.getVehicleType())) {
                    this.selectedVehicleType = entity;
                    break;
                }
            }
        }
        List<UserInfo> userInfoList = userService.findAllByRoleName("服务经理");
        for (UserInfo userInfo : userInfoList) {
            if (userInfo.getName().equals(getActiveUser().getUsername())) {
                manager = false;
                break;
            }
        }
        this.setActiveUserMsg(this.qualityReportInfo);
    }

    /**
     * 选择车辆类型
     */
    @Command
    @NotifyChange("*")
    public void changevehicleType() {
        qualityReportInfo.setVehicleType(this.selectedVehicleType.getName());
    }


    /**
     * 保存信息
     */
    @Command
    @NotifyChange("*")
    public void submit() {
        this.qualityReportInfo = qualityReportService.save(this.qualityReportInfo);
        this.updateUIState();
        showDialog();   //提示消息框
    }


    /**
     * 添加车辆
     *
     * @param selectedVehicleList 选择的车辆
     */
    @Command
    @NotifyChange("*")
    public void updateSelectedVehicleList(@BindingParam("vehicle") List<VehicleInfo> selectedVehicleList) {

        List<String> VehicleIdList = new ArrayList<>();
        //查询获取配件速报
        List<ReportVehicleInfo> reportVehicleInfoList = this.reportVehicleService.findByQrId(this.qualityReportInfo.getObjId());
        //给速报实体赋值
        this.qualityReportInfo.setReportVehicleInfos(reportVehicleInfoList);
        for (ReportVehicleInfo reportVehicleInfo : this.qualityReportInfo.getReportVehicleInfos()) {
            VehicleIdList.add(reportVehicleInfo.getVehicle_id());
        }
        for (VehicleInfo vehicleInfo : selectedVehicleList) {
            if (!VehicleIdList.contains(vehicleInfo.getObjId())) {
                ReportVehicleInfo reportVehicleInfo = new ReportVehicleInfo();
                reportVehicleInfo.setVehicle(vehicleInfo);
                reportVehicleInfo.setVehicle_id(vehicleInfo.getObjId());
                reportVehicleInfo.setQrId(this.qualityReportInfo.getObjId());
                reportVehicleInfo = this.reportVehicleService.save(reportVehicleInfo);
                this.qualityReportInfo.getReportVehicleInfos().add(reportVehicleInfo);
            }


        }
        this.showSelectVehicleWin = false;
        this.selectedVehicleList.clear();
        this.vehicleEntities.clear();
        this.keyword = "";

    }

    /**
     * 添加配件
     *
     * @param selectedParts 已经选择的配件列表
     */
    @Command
    @NotifyChange("*")
    public void updateCheckResultList(@BindingParam("vehicle") List<PartInfo> selectedParts) {
        List<String> partIdList = new ArrayList<>();
        //查询获取配件速报
        List<ReportPartInfo> reportPartInfoList = this.reportPartService.findByQrId(this.qualityReportInfo.getObjId());
        //给速报实体赋值
        this.qualityReportInfo.setReportPartInfos(reportPartInfoList);
        for (ReportPartInfo partInfo : this.qualityReportInfo.getReportPartInfos()) {
            partIdList.add(partInfo.getPart_id());
        }
        for (PartInfo part : selectedParts) {
            if (!partIdList.contains(part.getObjId())) {
                ReportPartInfo reportPart = new ReportPartInfo();
                reportPart.setPart(part);
                reportPart.setPart_id(part.getObjId());
                reportPart.setQrId(this.qualityReportInfo.getObjId());
                ReportPartInfo reportPartInfo = this.reportPartService.save(reportPart);
                this.qualityReportInfo.getReportPartInfos().add(reportPartInfo);
            }


        }
        this.showSelectWin = false;
        this.selectedPartList.clear();
        this.partList.clear();
        this.keyword = "";
    }


    /**
     * 根据状态索引，获取状态的名称
     *
     * @param index
     * @return
     */
    public String getStatusName(Integer index) {
        if (index != null) {
            return DocStatus.getName(index);
        }
        return null;
    }

    /**
     * 初始化后加载窗体
     *
     * @param view
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 添加车辆
     */
    @Command
    @NotifyChange({"showSelectVehicleWin"})
    public void selectVehicles() {
        if (StringUtils.isBlank(this.qualityReportInfo.getObjId())) {
            ZkUtils.showInformation("请先保存单据", "提示");
            return;
        }
        this.showSelectVehicleWin = true;

    }

    /**
     * 添加配件
     */
    @Command
    @NotifyChange({"showSelectWin"})
    public void selectParts() {
        if (StringUtils.isBlank(this.qualityReportInfo.getObjId())) {
            ZkUtils.showInformation("请先保存单据", "提示");
            return;
        }
        this.showSelectWin = true;

    }


    /**
     * 查询车辆
     */
    @Command
    @NotifyChange("*")
    public void searchVehicle() {
        if (this.keyword.trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
            this.vehicleEntities = vehicleService.findAllByKeyword(this.keyword.trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
        }
    }

    //查询配件
    @Command
    @NotifyChange("*")
    public void searchReportPart() {

        if (this.keyword.length() >= CommonHelper.FILTER_PARTS_LEN) {
            this.partList = partService.findAllByKeyword(this.keyword.trim());
            if (this.partList.size() < 1) {
                ZkUtils.showInformation("未查询到相关物料！", "提示");
            }
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_PARTS_ERROR, "提示");
        }
    }

    /**
     * 文件路径
     *
     * @param filename
     * @return
     */
    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_ASM + filename;
    }


    /**
     * 选择服务站信息
     *
     * @param dealer
     */
    @Command
    @NotifyChange("qualityReportInfo")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        qualityReportInfo.setDealerCode(dealer.getCode());
        qualityReportInfo.setDealerName(dealer.getName());
        if (dealer.getServiceManagerName() != null) {
            qualityReportInfo.setLinkman(dealer.getStationMaster());
            qualityReportInfo.setLinkmanPhone(dealer.getStationMasterPhone());
            qualityReportInfo.setServiceManager(dealer.getServiceManagerName());
        }
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
        qualityReportInfo.setOriginFile(event.getMedia().getName());
        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
        qualityReportInfo.setFile(fileName);
    }

    /**
     * 删除文件
     *
     * @param type
     */
    @Command
    @NotifyChange("*")
    public void delUploadFile(@BindingParam("event") String type) {
        qualityReportInfo.setFile("");
    }

    /**
     * 删除车辆列表
     *
     * @param model
     */
    @Command
    @NotifyChange("*")
    public void deleteVehicle(@BindingParam("model") ReportVehicleInfo model) {
        for (ReportVehicleInfo item : qualityReportInfo.getReportVehicleInfos()) {
            if (item == model) {
                qualityReportInfo.getReportVehicleInfos().remove(item);
                reportVehicleService.delete(model);
                return;
            }
        }
    }

    /**
     * 删除配件列表
     *
     * @param model
     */
    @Command
    @NotifyChange("*")
    public void deletePart(@BindingParam("model") ReportPartInfo model) {
        for (ReportPartInfo item : qualityReportInfo.getReportPartInfos()) {
            if (item == model) {
                //partsList.remove(model.getPart());
                qualityReportInfo.getReportPartInfos().remove(item);
                reportPartService.delete(model);
                return;
            }
        }
    }

    @Override
    protected Boolean checkValid() {
        //if (this.qualityReportEntity.getStatus() > 0 && this.qualityReportEntity.getProcessInstanceId()==null) {
        //    ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
        //    return false;
        //}
        if (StringUtils.isBlank(this.qualityReportInfo.getTitle())) {
            ZkUtils.showInformation("请填写速报标题！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getSubmitterPhone())) {
            ZkUtils.showInformation("请填写申请人电话号码", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getDealerCode())) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getFaultDesc())) {
            ZkUtils.showInformation("请填写故障描述！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getFaultStatus())) {
            ZkUtils.showInformation("请填写故障时行驶状态！", "提示");
            return false;
        }
        if (this.qualityReportInfo.getStatus().equals(DocStatus.AUDITED.getIndex())) {
            if (this.qualityReportInfo.getCanEditType() == true && StringUtils.isBlank(this.qualityReportInfo.getReportType())) {
                ZkUtils.showInformation("请选择速报级别", "提示");
            }
            return false;
        }
        if (this.qualityReportInfo.getCanEditType() == true && this.qualityReportInfo.getStatus().equals(DocStatus.AUDITED.getIndex())) {
            if (StringUtils.isBlank(this.qualityReportInfo.getVehicleType())) {
                ZkUtils.showInformation("请选择车辆类型", "提示");
            }
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getServiceManagerPhone())) {
            ZkUtils.showInformation("请填写服务经理电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getFaultRoad())) {
            ZkUtils.showInformation("请填写故障时路面情况！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getFaultAddress())) {
            ZkUtils.showInformation("请填写故障发生地点！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getInitialReason())) {
            ZkUtils.showInformation("请填写初步原因分析！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getDecisions())) {
            ZkUtils.showInformation("请填写处置意见！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportInfo.getFile())) {
            ZkUtils.showInformation("请上传附件！", "提示");
            return false;
        }

        //if (this.qualityReportInfo.getReportPartInfos().size() < 1) {
        //    ZkUtils.showInformation("请在检查结果选择配件！", "提示");
        //    return false;
        //}
        if (this.qualityReportInfo.getReportVehicleInfos().size() < 1) {
            ZkUtils.showInformation("请选择车辆！", "提示");
            return false;
        } else {
            for (ReportVehicleInfo vehicleInfo : this.qualityReportInfo.getReportVehicleInfos()) {
                if (StringUtils.isBlank(vehicleInfo.getMileage())) {
                    ZkUtils.showInformation("请输入行驶里程！", "提示");
                    return false;
                }
            }
        }


        return true;
    }


    /**
     * 提交,启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.qualityReportInfo)) {
                    this.qualityReportInfo = qualityReportService.save(this.qualityReportInfo);
                    flowDocInfo = this.qualityReportInfo;
                    Map<String, String> map = qualityReportService.startProcess(this.qualityReportInfo, getActiveUser());
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


    @Command
    @Override
    public void showHandleForm() {
        // 在同意的时候做下一步的检查

        if (this.qualityReportInfo.getCanEditType() == true && StringUtils.isBlank(this.qualityReportInfo.getReportType())) {
            ZkUtils.showExclamation("请选择速报级别", "系统提示");
            return;
        }
        //if (this.qualityReportInfo.getCanEditType() == true && StringUtils.isBlank(this.qualityReportInfo.getVehicleType())) {
        //    ZkUtils.showInformation("请选择车辆分类！", "提示");
        //    return;
        //}

        super.showHandleForm();
    }

    /**
     * 打印
     *
     * @throws IOException
     */
    @Command
    public void printReport() throws IOException {
        XWPFTemplate template = XWPFTemplate.compile(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/views/report/printPage/asm/qualityReport.docx").render(new HashMap<String, Object>() {
            {
                put("title", new TextRenderData(qualityReportInfo.getTitle() == null ? "" : qualityReportInfo.getTitle().toString()));
                put("docNo", new TextRenderData(qualityReportInfo.getDocNo() == null ? "" : qualityReportInfo.getDocNo().toString()));
                put("submitterName", new TextRenderData(qualityReportInfo.getSubmitterName() == null ? "" : qualityReportInfo.getSubmitterName().toString()));
                put("submitterPhone", new TextRenderData(qualityReportInfo.getSubmitterPhone() == null ? "" : qualityReportInfo.getSubmitterPhone().toString()));
                put("dealerCode", new TextRenderData(qualityReportInfo.getDealerCode() == null ? "" : qualityReportInfo.getDealerCode().toString()));
                put("dealerName", new TextRenderData(qualityReportInfo.getDealerName() == null ? "" : qualityReportInfo.getDealerName().toString()));
                put("serviceManager", new TextRenderData(qualityReportInfo.getServiceManager() == null ? "" : qualityReportInfo.getServiceManager().toString()));
                put("serviceManagerPhone", new TextRenderData(qualityReportInfo.getServiceManagerPhone() == null ? "" : qualityReportInfo.getServiceManagerPhone().toString()));
                put("vehicleList", new TableRenderData(new ArrayList<RenderData>() {{
                    add(new TextRenderData("d0d0d0", "序号"));
                    add(new TextRenderData("d0d0d0", "VIN"));
                    add(new TextRenderData("d0d0d0", "VSN"));
                    add(new TextRenderData("d0d0d0", "车辆型号"));
                    add(new TextRenderData("d0d0d0", "发动机号"));
                    add(new TextRenderData("d0d0d0", "车主"));
                    add(new TextRenderData("d0d0d0", "行驶里程"));
                    add(new TextRenderData("d0d0d0", "购买日期"));
                    add(new TextRenderData("d0d0d0", "报修日期"));
                }}, new ArrayList<Object>() {{
                    if (qualityReportInfo.getReportVehicleInfos() != null) {
                        Integer index = 0;
                        for (ReportVehicleInfo reportVehicleInfo : qualityReportInfo.getReportVehicleInfos()) {
                            if (reportVehicleInfo.getVehicle() != null) {
                                index++;
                                add(index.toString() + ";"
                                        + reportVehicleInfo.getVehicle().getVin() + ";"
                                        + reportVehicleInfo.getVehicle().getVsn() + ";"
                                        + reportVehicleInfo.getVehicle().getVehicleModel() + ";"
                                        + reportVehicleInfo.getVehicle().getEngineNo() + ";"
                                        + reportVehicleInfo.getVehicle().getOwnerName() + ";"
                                        + reportVehicleInfo.getMileage() + ";"
                                        + DateHelper.dateToString(reportVehicleInfo.getVehicle().getPurchaseDate()) + ";"
                                        + DateHelper.dateToString(reportVehicleInfo.getRepairDate()));
                            }
                        }
                    }

                }}, "no datas", 9860));
                put("partList", new TableRenderData(new ArrayList<RenderData>() {{
                    add(new TextRenderData("d0d0d0", "序号"));
                    add(new TextRenderData("d0d0d0", "零件名称"));
                    add(new TextRenderData("d0d0d0", "零件件号"));
                    add(new TextRenderData("d0d0d0", "数量"));
                    add(new TextRenderData("d0d0d0", "故障模式"));
                }}, new ArrayList<Object>() {{
                    if (qualityReportInfo.getReportPartInfos() != null) {
                        Integer index = 0;
                        for (ReportPartInfo reportPartInfo : qualityReportInfo.getReportPartInfos()) {
                            if (reportPartInfo.getPart() != null) {
                                index++;
                                add(index.toString() + ";"
                                        + reportPartInfo.getPart().getName() + ";"
                                        + reportPartInfo.getPart().getCode() + ";"
                                        + reportPartInfo.getAmount() + ";"
                                        + (reportPartInfo.getFault() == null ? "" : reportPartInfo.getFault()));

                            }
                        }
                    }

                }}, "no datas", 9860));
                put("faultDesc", new TextRenderData(qualityReportInfo.getFaultDesc() == null ? "" : qualityReportInfo.getFaultDesc().toString()));
                put("faultStatus", new TextRenderData(qualityReportInfo.getFaultStatus() == null ? "" : qualityReportInfo.getFaultStatus().toString()));
                put("faultRoad", new TextRenderData(qualityReportInfo.getFaultRoad() == null ? "" : qualityReportInfo.getFaultRoad().toString()));
                put("faultAddress", new TextRenderData(qualityReportInfo.getFaultAddress() == null ? "" : qualityReportInfo.getFaultAddress().toString()));
                put("initialReason", new TextRenderData(qualityReportInfo.getInitialReason() == null ? "" : qualityReportInfo.getInitialReason().toString()));
                put("decisions", new TextRenderData(qualityReportInfo.getDecisions() == null ? "" : qualityReportInfo.getDecisions().toString()));
                put("comment", new TextRenderData(qualityReportInfo.getComment() == null ? "" : qualityReportInfo.getComment().toString()));


            }


        });

        String fileName = "";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            template.write(out);
            template.close();
            fileName = UUIDHelper.newUuid() + ".docx";
            Filedownload.save(out.toByteArray(), null, fileName);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查是否能打印
     *
     * @return
     */
    @Override
    public Boolean getCheckCanPrint() {
        return true;
    }

    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                qualityReportService.desertTask(this.qualityReportInfo.getObjId());
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
        return qualityReportService.save((QualityReportInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return qualityReportService.findOneById(objId);
    }


    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("qualityReportInfo", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_QUALITY_REPORT_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    /**
     * 获取日期
     *
     * @param date
     * @return
     */
    public String getDate(Date date) {
        String strDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            strDate = dateFormat.format(date);
        }
        return strDate;
    }

}
