package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.GoOutInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.FirstMaintenanceService;
import com.sunjet.frontend.service.asm.GoOutService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.service.system.ConfigService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 首保服务单 表单 VM
 */
public class FirstMaintenanceFormVM extends FormVM {
    @WireVariable
    private FirstMaintenanceService firstMaintenanceService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private DictionaryService dictionaryService;
    @WireVariable
    private ConfigService configService;
    @WireVariable
    private GoOutService goOutService;

    @Getter
    @Setter
    private FirstMaintenanceInfo firstMaintenanceRequest;
    @Getter
    @Setter
    private VehicleInfo vehicle;
    @Getter
    @Setter
    private List<VehicleInfo> vehicles = new ArrayList<>();
    @Getter
    @Setter
    private String keyword = "";
    @Getter
    @Setter
    private List<DictionaryInfo> fmExpenseStandards = new ArrayList<>();
    @Getter
    @Setter
    private DictionaryInfo selectedExpenseStandard;
    @Getter
    @Setter
    private Window window;
    @Getter
    @Setter
    private Map<String, Object> variables = new HashMap<>();

    @Getter
    private Map<String, DictionaryInfo> vms = new HashMap<>();
    @Getter
    @Setter
    protected String maintenanceHistoryUrl;
    @Getter
    @Setter
    private String title = "维修历史";

    @Init(superclass = true)
    public void init() {

        //this.setBaseService(firstMaintenanceService);

        dictionaryService.findDictionariesByParentCode("15000").forEach(dic -> vms.put(dic.getCode(), dic));


        if (StringUtils.isNotBlank(this.getBusinessId())) {
            this.firstMaintenanceRequest = firstMaintenanceService.findOneWithGoOutsById(this.getBusinessId());
        } else {
            this.firstMaintenanceRequest = new FirstMaintenanceInfo();

            DealerInfo dealerInfo = getActiveUser().getDealer();
            if (dealerInfo != null) {
                this.firstMaintenanceRequest.setDealerCode(dealerInfo.getCode());
                this.firstMaintenanceRequest.setDealerName(dealerInfo.getName());
                this.firstMaintenanceRequest.setDealerStar(getActiveUser().getDealer().getStar());
                this.firstMaintenanceRequest.setServiceManager(dealerInfo.getServiceManagerName() == null ? "" : dealerInfo.getServiceManagerName());
                //this.firstMaintenanceRequest.setHourPrice(this.getHourPriceByDealer(dealerInfo));
            }
        }

        this.setMaintenanceHistoryUrl("/views/asm/maintenance_history.zul");

        //设置用户信息
        setActiveUserMsg(firstMaintenanceRequest);
        //this.setEntity(this.firstMaintenanceRequest);

        this.fmExpenseStandards = dictionaryService.findDictionariesByParentCode("13000");
        //CacheManager.findDictionariesByParentCode("");

        if (StringUtils.isNotBlank(this.firstMaintenanceRequest.getStandardExpenseId())) {
            for (DictionaryInfo entity : this.fmExpenseStandards) {
                if (entity.getObjId().equals(this.firstMaintenanceRequest.getStandardExpenseId())) {
                    this.selectedExpenseStandard = entity;
                    break;
                }
            }
        }
    }

    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_ASM + filename;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
//        win = (Window) view;
//        if (win != null) {
//            win.setTitle(win.getTitle() + titleMsg);
//        }
    }

    /**
     * 保存信息
     */
    @Command
    @NotifyChange("*")
    public void submit() {
        //车辆信息
        if (this.firstMaintenanceRequest.getVehicleInfo() != null) {
            VehicleInfo vehicleInfo = vehicleService.save(this.firstMaintenanceRequest.getVehicleInfo());
            this.firstMaintenanceRequest.setVehicleInfo(vehicleInfo);
        }
        computeCost();
        //首保服务单信息
        this.firstMaintenanceRequest = firstMaintenanceService.save(this.firstMaintenanceRequest);
        this.flowDocInfo = this.firstMaintenanceRequest;
        this.updateUIState();
        //FlowDocEntity entity = this.saveEntity(this.firstMaintenanceRequest);

        //this.firstMaintenanceRequest = firstMaintenanceService.findOneWithGoOutsById(entity.getObjId());

        //this.setEntity(this.firstMaintenanceRequest);
        //提示框
        showDialog();
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
                if (startProcessInstance(this.firstMaintenanceRequest)) {
                    this.firstMaintenanceRequest = firstMaintenanceService.save(this.firstMaintenanceRequest);
                    flowDocInfo = this.firstMaintenanceRequest;
                    Map<String, String> map = firstMaintenanceService.startProcess(this.firstMaintenanceRequest, getActiveUser());
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
//
//    @Override
//    @Command
//    @NotifyChange("*")
//    public void saveFlowDocEntity() {
//        if (this.firstMaintenanceRequest.getVehicle() != null) {
//            vehicleService.getRepository().save(this.firstMaintenanceRequest.getVehicle());
//        }
//        FlowDocEntity entity = this.saveEntity(this.firstMaintenanceRequest);
//        this.firstMaintenanceRequest = firstMaintenanceService.findOneWithGoOutsById(entity.getObjId());
//        this.setEntity(this.firstMaintenanceRequest);
////        this.setEntity(this.saveEntity(this.firstMaintenanceRequest));
//        showDialog();
//    }

    @Override
    protected Boolean checkValid() {
        if (this.firstMaintenanceRequest.getEndDate().getTime() <= this.firstMaintenanceRequest.getStartDate().getTime()) {
            ZkUtils.showExclamation("【完工日期】必须大于【开工日期】！", "系统提示");
            return false;
        }
        if (this.firstMaintenanceRequest.getPullOutDate().getTime() <= this.firstMaintenanceRequest.getPullInDate().getTime()) {
            ZkUtils.showExclamation("【出站时间】必须大于【进站时间】！", "系统提示");
            return false;
        }
        if (this.selectedExpenseStandard == null) {
            ZkUtils.showExclamation("请选择【首保费用标准】！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.firstMaintenanceRequest.getVehicleInfo().getVin())) {
            ZkUtils.showExclamation("请选择车辆！", "系统提示");
            return false;
        }
//        if (StringUtils.isBlank(this.firstMaintenanceRequest.getAmeplate())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getManual())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getOdometer())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getInvoice())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getFront45())) {
//            ZkUtils.showExclamation("请上传所有凭证文件！", "系统提示");
//            return false;
//        }
        if (StringUtils.isBlank(this.firstMaintenanceRequest.getAmeplate())) {
            ZkUtils.showExclamation("请上传车辆铭牌凭证文件！", "系统提示");
            return false;
        } else if (StringUtils.isBlank(this.firstMaintenanceRequest.getManual())) {
            ZkUtils.showExclamation("请上传保养手册首页凭证文件！", "系统提示");
            return false;
        } else if (StringUtils.isBlank(this.firstMaintenanceRequest.getOdometer())) {
            ZkUtils.showExclamation("请上传里程表凭证文件！", "系统提示");
            return false;
        } else if (StringUtils.isBlank(this.firstMaintenanceRequest.getFront45())) {
            ZkUtils.showExclamation("请上传前侧45度照片凭证文件！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.firstMaintenanceRequest.getSender())
                || StringUtils.isBlank(this.firstMaintenanceRequest.getSenderPhone())
                || StringUtils.isBlank(this.firstMaintenanceRequest.getRepairer())) {
            ZkUtils.showExclamation("请填写维修信息！", "系统提示");
            return false;
        }
        if (this.firstMaintenanceRequest.getGoOuts().size() > 0) {
            for (GoOutInfo goOutInfo : this.firstMaintenanceRequest.getGoOuts()) {
                if (StringUtils.isBlank(goOutInfo.getOutGoPicture())) {
                    ZkUtils.showInformation("请上传外出凭证", "提示");
                    return false;
                }
                if (StringUtils.isBlank(goOutInfo.getPlace())) {
                    ZkUtils.showInformation("请填写外出地点", "提示");
                    return false;
                }
                if (goOutInfo.getMileage() <= 0.0) {
                    ZkUtils.showInformation("请填写单向里程", "提示");
                    return false;
                }
                //if (goOutInfo.getTrailerMileage().equals(0.0)) {
                //    ZkUtils.showInformation("请填写拖车里程", "提示");
                //    return false;
                //}
                if (goOutInfo.getOutGoNum() <= 0) {
                    ZkUtils.showInformation("请填写外出人数", "提示");
                    return false;
                }
                if (goOutInfo.getOutGoDay() <= 0) {
                    ZkUtils.showInformation("请填写外出天数", "提示");
                    return false;
                }
            }

        }

        return true;
    }


    /**
     * 更新行驶里程
     *
     * @param vmt
     */
    @Command
    @NotifyChange("firstMaintenanceRequest")
    public void updateVehicleMileage(@BindingParam("vmt") String vmt) {
        if (this.firstMaintenanceRequest.getVehicleInfo() != null) {
            this.firstMaintenanceRequest.getVehicleInfo().setMileage(vmt);
        }
    }

    @Command
    @NotifyChange("*")
    public void changeStandardExpense() {
        firstMaintenanceRequest.setStandardExpense(Double.parseDouble(this.selectedExpenseStandard.getValue()));

        this.firstMaintenanceRequest.setExpenseTotal(this.firstMaintenanceRequest.getOutExpense()
                + this.firstMaintenanceRequest.getStandardExpense()
                + this.firstMaintenanceRequest.getOtherExpense());

        firstMaintenanceRequest.setStandardExpenseId(this.selectedExpenseStandard.getObjId());

        this.computeCost();
    }

    @Command
    @NotifyChange("*")
    public void computeCost() {
        try {
            this.firstMaintenanceRequest.setOutExpense(0.0);
            this.firstMaintenanceRequest.setHours(0.0);
            if (this.firstMaintenanceRequest.getOtherExpense() == null) {
                this.firstMaintenanceRequest.setOtherExpense(0.0);
            }
            Double outKm = 0.0;
            Boolean isOut = false;
            Double outHourExpressTmp = 0.0;    // 外出工时补贴费用

            Map<String, ConfigInfo> configMap = configService.getAllConfig();

            this.firstMaintenanceRequest.setNightExpense(0.0);
            if (this.firstMaintenanceRequest.getNightWork()) {  // 夜间作业

                ConfigInfo configInfo = configMap.get("cost_night");

                this.firstMaintenanceRequest.setNightExpense(Double.parseDouble(configInfo.getConfigValue()));
            }

            for (GoOutInfo entity : this.firstMaintenanceRequest.getGoOuts()) {

                outKm = outKm + entity.getMileage();        // 外出里程累加
                entity.setTranCosts(entity.getMileage()
                        * Double.parseDouble(configMap.get("cost_per_km").getConfigValue()));          // 交通费用 = 外出单向里程 * 3.0 元
                entity.setTrailerCost(entity.getTrailerMileage()
                        * Double.parseDouble(configMap.get("cost_per_km_trailer").getConfigValue()));  // 拖车费用 = 拖车里程 * 2.8元
                entity.setPersonnelSubsidy(entity.getOutGoDay()
                        * entity.getOutGoNum()
                        * Double.parseDouble(configMap.get("cost_person_day").getConfigValue()));      // 人员补贴 = 外出天数 * 外出人员 * 55
                entity.setNightSubsidy(entity.getOutGoNum()
                        * ((entity.getOutGoDay() - 1) >= 0 ? (entity.getOutGoDay() - 1) : 0)
                        * Double.parseDouble(configMap.get("cost_person_night").getConfigValue()));    // 住宿补贴 = 外出天数 * 外出人员 * 80

                // 单项外出费用统计
                entity.setAmountCost(entity.getTranCosts()   // 交通费用
                        + entity.getTrailerCost()            // 拖车费用
                        + entity.getPersonnelSubsidy()       // 人员补贴
                        + entity.getNightSubsidy());           // 住宿补贴

                if (StringUtils.isNotBlank(entity.getPlace())) {   // 外出地点不为空时，才进行计算
                    isOut = true;       // 目的地不为空，表示确实有外出服务

                    // 外出费用合计
                    this.firstMaintenanceRequest.setOutExpense(this.firstMaintenanceRequest.getOutExpense()
                            + entity.getAmountCost());
                }
            }
            this.firstMaintenanceRequest.setExpenseTotal(
                    this.firstMaintenanceRequest.getOutExpense()
                            + this.firstMaintenanceRequest.getNightExpense()
                            + this.firstMaintenanceRequest.getStandardExpense()
                            + this.firstMaintenanceRequest.getOtherExpense());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审批记录
     *
     * @param outcome
     * @param comment
     * @throws IOException
     */
    @Override
    protected void completeTask(String outcome, String comment) throws IOException {

        if ("提交".equals(outcome)) {
            if (checkValid()) {
                super.completeTask(outcome, comment);
            }

        } else {
            super.completeTask(outcome, comment);

        }

    }


    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

//    @Command
//    public void saveFirstMaintenance() {
//        firstMaintenanceService.save(firstMaintenanceRequest);
//
//    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("t01")) {  // 车辆名牌
            this.firstMaintenanceRequest.setAmeplate(fileName);
        } else if (type.equalsIgnoreCase("t02")) {   //保养手册首页
            this.firstMaintenanceRequest.setManual(fileName);
        } else if (type.equalsIgnoreCase("t03")) {   //里程表
            this.firstMaintenanceRequest.setOdometer(fileName);
        } else if (type.equalsIgnoreCase("t04")) {   //购买发票
            this.firstMaintenanceRequest.setInvoice(fileName);
        } else if (type.equalsIgnoreCase("t05")) {   //车位前侧45度照片
            this.firstMaintenanceRequest.setFront45(fileName);
        }
    }

    @Command
    @NotifyChange("firstMaintenanceRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t01")) {  // 车辆名牌
            this.firstMaintenanceRequest.setAmeplate("");
        } else if (type.equalsIgnoreCase("t02")) {   //保养手册首页
            this.firstMaintenanceRequest.setManual("");
        } else if (type.equalsIgnoreCase("t03")) {   //里程表
            this.firstMaintenanceRequest.setOdometer("");
        } else if (type.equalsIgnoreCase("t04")) {   //购买发票
            this.firstMaintenanceRequest.setInvoice("");
        } else if (type.equalsIgnoreCase("t05")) {   //车位前侧45度照片
            this.firstMaintenanceRequest.setFront45("");
        }
    }

    /**
     * 上传外出凭证
     *
     * @param event
     * @param goOutitem
     */
    @Command
    @NotifyChange("*")
    public void outGoUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("each") GoOutInfo goOutitem) {
        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
        for (GoOutInfo outEntity : this.firstMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture(fileName);
            }

        }
    }

    /**
     * 删除外出凭证
     *
     * @param goOutitem
     */
    @Command
    @NotifyChange("*")
    public void deleteOutGoFile(@BindingParam("each") GoOutInfo goOutitem) {
        for (GoOutInfo outEntity : this.firstMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture("");
            }

        }

    }

    /**
     * 维修历史页面
     *
     * @param vehicleInfo
     * @param url
     */
    @Command
    @NotifyChange("firstMaintenanceRequest")
    public void openMaintenanceHistory(@BindingParam("vehicleInfo") VehicleInfo vehicleInfo, @BindingParam("url") String url, @BindingParam("title") String title) {

        Map<String, Object> paramMap = new HashMap<>();
        if (vehicleInfo.getObjId() != null) {
            paramMap.put("vehicleInfo", vehicleInfo);
        }

        try {
            if (firstMaintenanceRequest.getVehicleInfo().getVin() == null) {
                ZkUtils.showExclamation("请先选择车辆！", "系统提示");
                return;
            }
            ZkTabboxUtil.newTab(vehicleInfo.getObjId() == null ? URLEncoder.encode(title, "UTF-8") : vehicleInfo.getObjId() + "mh", title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (TabDuplicateException ex) {
            ex.printStackTrace();
        }
    }


    @Command
    @NotifyChange("*")
    public void searchVehicles() {
        if (this.keyword.trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
            this.vehicles = vehicleService.findAllByKeywordAndFmDateIsNull(this.keyword.trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
        }
    }

    @Command
    @NotifyChange("firstMaintenanceRequest")
    public void selectVehicle(@BindingParam("model") VehicleInfo vehicleInfo) throws InvocationTargetException, IllegalAccessException {
        this.firstMaintenanceRequest.setVehicleInfo(vehicleInfo);
        this.firstMaintenanceRequest.setVmt(vehicleInfo.getMileage());
        this.firstMaintenanceRequest.setTypeCode(vehicleInfo.getTypeCode());
    }

    @Command
    @NotifyChange("*")
    public void addGoOut() {
        this.firstMaintenanceRequest.getGoOuts().add(new GoOutInfo());
    }

    @Command
    @NotifyChange("*")
    public void deleteGoOut(@BindingParam("model") GoOutInfo entity) {
        ZkUtils.showQuestion("您确定删除该对象？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                //从内存中删除维修项目
                Iterator<GoOutInfo> iterator = firstMaintenanceRequest.getGoOuts().iterator();
                while (iterator.hasNext()) {
                    GoOutInfo goOut = iterator.next();
                    if (entity == goOut) {
                        iterator.remove();
                    }
                }
                if (StringUtils.isNotBlank(entity.getObjId())) {
                    goOutService.deleteByObjId(entity.getObjId());
                }
                this.computeCost();
                this.firstMaintenanceRequest = firstMaintenanceService.save(this.firstMaintenanceRequest);
                BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
            BindUtils.postNotifyChange(null, null, this, "firstMaintenanceRequest");
        });

    }

    /**
     * 打印
     */
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();

        map.put("objId", this.firstMaintenanceRequest.getObjId() == null ? "" : this.firstMaintenanceRequest.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/first_maintenance_printPage.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }

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
                firstMaintenanceService.desertTask(this.firstMaintenanceRequest.getObjId());
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
        return firstMaintenanceService.save((FirstMaintenanceInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return firstMaintenanceService.findOneWithGoOutsById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("firstMaintenanceRequest", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FIRST_MAINTENANCE_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }
}
