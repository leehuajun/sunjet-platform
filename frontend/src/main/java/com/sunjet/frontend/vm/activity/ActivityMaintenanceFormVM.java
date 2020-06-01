package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.*;
import com.sunjet.dto.asms.asm.CommissionPartInfo;
import com.sunjet.dto.asms.asm.GoOutInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.activity.*;
import com.sunjet.frontend.service.asm.CommissionPartService;
import com.sunjet.frontend.service.asm.GoOutService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.service.report.ReportService;
import com.sunjet.frontend.service.system.ConfigService;
import com.sunjet.frontend.service.system.DictionaryService;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 服务活动通知单 表单 VM
 */
public class ActivityMaintenanceFormVM extends FormVM {

    @WireVariable
    private ActivityMaintenanceService activityMaintenanceService;    //活动服务service

    @WireVariable
    private ActivityDistributionService activityDistributionService;   //活动分配单service

    @WireVariable
    private ActivityNoticeService activityNoticeService;   //活动通知单service

    @WireVariable
    private ActivityVehicleService activityVehicleService; //活动车辆service

    @WireVariable
    CommissionPartService commissionPartService;   //维修配件

    @WireVariable
    ActivityPartService activityPartService; // 活动配件

    @WireVariable
    private ConfigService configService;    // 配置参数

    @WireVariable
    private GoOutService goOutService; //活动外出service

    @WireVariable
    ReportService reportService;

    @WireVariable
    private DictionaryService dictionaryService;   // 数字字典

    @WireVariable
    private VehicleService vehicleService;

    @Getter
    @Setter
    private ActivityMaintenanceInfo activityMaintenanceRequest = new ActivityMaintenanceInfo();    //活动服务单

    @Getter
    @Setter
    private ActivityDistributionInfo activityDistributionInfo = new ActivityDistributionInfo();  // 已选择的活动分配单

    @Getter
    @Setter
    private ActivityNoticeInfo activityNoticeInfo = new ActivityNoticeInfo(); //活动通知单

    @Getter
    @Setter
    private List<ActivityDistributionInfo> distributionsEntities = new ArrayList<>();   //已关闭的活动分配单

    @Getter
    @Setter
    private List<ActivityVehicleItem> activityVehicles = new ArrayList<>();   //活动分配的活动车辆列表

    @Getter
    @Setter
    private ActivityVehicleItem activityVehicleItem = new ActivityVehicleItem(); // 已选择车辆

    @Getter
    @Setter
    private Map<String, ConfigInfo> configInfoMap = new HashMap<>();  //配置参数列表

    @Getter
    private Map<String, DictionaryInfo> vms = new HashMap<>();

    @Getter
    @Setter
    private VehicleInfo vehicleInfo = new VehicleInfo();

    @Getter
    @Setter
    protected String maintenanceHistoryUrl;

    @Getter
    @Setter
    private String title = "维修历史";

    @Init(superclass = true)
    public void init() {

        dictionaryService.findDictionariesByParentCode("15000").forEach(dic -> vms.put(dic.getCode(), dic));

        if (StringUtils.isNotBlank(objId)) {
            this.activityMaintenanceRequest = activityMaintenanceService.findOneById(objId);
        } else {
            //新增
            this.activityMaintenanceRequest = new ActivityMaintenanceInfo();
            DealerInfo dealerInfo = getActiveUser().getDealer();
            if (dealerInfo != null) {
                this.activityMaintenanceRequest.setDealerCode(dealerInfo.getCode());
                this.activityMaintenanceRequest.setDealerName(dealerInfo.getName());
                this.activityMaintenanceRequest.setSubmitterPhone(dealerInfo.getPhone());
                this.activityMaintenanceRequest.setSubmitterName(getActiveUser().getUsername());
                this.activityMaintenanceRequest.setServiceManager(dealerInfo.getServiceManagerName());
                this.activityMaintenanceRequest.setProvinceName(dealerInfo.getProvinceName());
                this.activityMaintenanceRequest.setDealerStar(dealerInfo.getStar());
            }
        }

        this.setMaintenanceHistoryUrl("/views/asm/maintenance_history.zul");

        //参数配置
        List<ConfigInfo> configInfoList = configService.findAll();
        for (ConfigInfo configInfo : configInfoList) {
            configInfoMap.put(configInfo.getConfigKey(), configInfo);
        }
        this.getVehicleInfo().setObjId(activityVehicleItem.getVehicleId());
        this.getVehicleInfo().setVin(activityVehicleItem.getVin());
        this.getVehicleInfo().setFmDate(activityVehicleItem.getFmDate());
        this.setActiveUserMsg(this.activityMaintenanceRequest);
    }


    /**
     * 查找状态已关闭的相关服务站单据
     *
     * @return
     */
    @Command
    @NotifyChange("distributionsEntities")
    public void searchActivityDistributions() {
        if (getActiveUser().getDealer() == null) {
            ZkUtils.showExclamation("请以服务站用户登录", "系统提示");
            return;
        }

        this.distributionsEntities = activityDistributionService.searchCloseActivityDistributionByDealerCodeAndKeyWord(this.getKeyword().trim(), getActiveUser().getDealer().getCode());

    }

    /**
     * 搜索活动分配单车辆
     */
    @Command
    @NotifyChange("activityVehicles")
    public void searchVehicles() {
        if (this.activityMaintenanceRequest.getActivityDistribution() == null) {
            ZkUtils.showExclamation("请先选择活动分配单!", "系统提示");
            return;
        }
        //通过活动分配Id获取活动车辆
        activityVehicles = activityVehicleService.searchActivityVehicleByActivityDistributionId(this.activityMaintenanceRequest.getActivityDistribution().getObjId(), keyword);


    }

    /**
     * 选择活动车辆
     *
     * @param activityVehicleItem
     */
    @Command
    @NotifyChange({"activityMaintenanceRequest", "activityVehicleItem"})
    public void selectVehicle(@BindingParam("model") ActivityVehicleItem activityVehicleItem) {
//        if (activityVehicleEntity.getObjId()!=null) {
        if (StringUtils.isNotBlank(this.activityMaintenanceRequest.getActivityVehicleId()) && !this.activityMaintenanceRequest.getActivityVehicleId().equals(activityVehicleItem.getObjId())) {
            ActivityVehicleItem oldActivityVehicle = activityVehicleService.findOneById(this.getActivityMaintenanceRequest().getActivityVehicleId());
            oldActivityVehicle.setRepair(false);
            oldActivityVehicle.setActivityMaintenanceId(null);
            activityVehicleService.save(oldActivityVehicle);
        }
        activityVehicleItem.setRepair(true);
        activityMaintenanceRequest.setActivityVehicleItem(activityVehicleItem);
        activityMaintenanceRequest.setActivityVehicleId(activityVehicleItem.getObjId());
        activityMaintenanceRequest.setVmt(activityVehicleItem.getVmt() == null ? "0" : activityVehicleItem.getVmt());
        activityMaintenanceRequest.setTypeCode(activityVehicleItem.getTypeCode());
        activityMaintenanceRequest.setActivityVehicleItem(activityVehicleItem);
        activityVehicleService.save(activityVehicleItem);
        this.activityMaintenanceRequest = this.activityMaintenanceService.save(this.activityMaintenanceRequest);
        this.setKeyword("");
        this.activityVehicles.clear();
    }


    /**
     * 选择活动分配单
     *
     * @param distributionInfo
     */
    @Command
    @NotifyChange("*")
    public void selectActivityDistribution(@BindingParam("model") ActivityDistributionInfo distributionInfo) {
        if (StringUtils.isBlank(this.activityMaintenanceRequest.getObjId())) {
            ZkUtils.showExclamation("请先保存单据!", "系统提示");
            return;
        }
//        this.activityMaintenanceRequest.getActivityParts().clear();
        this.activityDistributionInfo = distributionInfo;
        this.activityMaintenanceRequest.setActivityDistribution(distributionInfo);
        this.activityMaintenanceRequest.setActivityDistributionId(distributionInfo.getObjId());
        if (StringUtils.isNotBlank(activityDistributionInfo.getActivityNoticeId())) {
            //获取活动通知单
            activityNoticeInfo = activityNoticeService.findOneById(activityDistributionInfo.getActivityNoticeId());
            this.activityDistributionInfo.setActivityNotice(activityNoticeInfo);
        }
        this.activityVehicles.clear();
        this.activityMaintenanceRequest.setActivityVehicleItem(null);
        activityMaintenanceRequest.setStandardExpense(distributionInfo.getActivityNotice().getPerLaberCost());
        this.activityMaintenanceRequest.setActivityDistribution(distributionInfo);
        this.activityMaintenanceRequest.getCommissionParts().clear();
        List<ActivityPartItem> activityParts = activityPartService.findPartByActivityNoticeId(distributionInfo.getActivityNotice().getObjId());
        for (ActivityPartItem part : activityParts) {
            CommissionPartInfo commissionPart = new CommissionPartInfo();
            if (part != null) {
                commissionPart.setActivityPartId(part.getObjId());
                commissionPart.setPartCode(part.getCode());
                commissionPart.setPartName(part.getName());
                commissionPart.setPartType(part.getPartType());
                commissionPart.setUnit(part.getUnit());
                commissionPart.setPrice(part.getPrice());
                commissionPart.setAmount(part.getAmount());
                commissionPart.setRecycle(true);
                this.activityMaintenanceRequest.getCommissionParts().add(commissionPart);
            }

        }
        this.setKeyword("");
        this.distributionsEntities.clear();
        this.computeCost();

    }


    /**
     * 统计费用
     */
    @Command
    @NotifyChange("*")
    public void computeCost() {
        if (this.activityMaintenanceRequest.getOtherExpense() == null) {
            this.activityMaintenanceRequest.setOtherExpense(0.0);
        }
        this.activityMaintenanceRequest.setExpenseTotal(0.0);           // 总合计费用

        this.activityMaintenanceRequest.setOutExpense(0.0);             // 外出费用合计
        this.activityMaintenanceRequest.setOutHours(0.0);

        Double outKm = 0.0;
        Boolean isOut = false;

        this.activityMaintenanceRequest.setNightExpense(0.0);
        if (this.activityMaintenanceRequest.getNightWork()) {  // 夜间作业
            this.activityMaintenanceRequest.setNightExpense(Double.parseDouble(configInfoMap.get("cost_night").getConfigValue()));
        }

        for (GoOutInfo entity : this.activityMaintenanceRequest.getGoOuts()) {
            if (entity.getPlace() != null && org.apache.commons.lang3.StringUtils.isNotBlank(entity.getPlace().trim())) {   // 外出地点不为空时，才进行计算
                isOut = true;       // 目的地不为空，表示确实有外出服务
                outKm = outKm + entity.getMileage();        // 外出里程累加
                entity.setTranCosts(entity.getMileage()
                        * Double.parseDouble(configInfoMap.get("cost_per_km").getConfigValue()));          // 交通费用 = 外出单向里程 * 3.0 元
                entity.setTrailerCost(entity.getTrailerMileage()
                        * Double.parseDouble(configInfoMap.get("cost_per_km_trailer").getConfigValue()));  // 拖车费用 = 拖车里程 * 2.8元
                entity.setPersonnelSubsidy(entity.getOutGoDay()
                        * entity.getOutGoNum()
                        * Double.parseDouble(configInfoMap.get("cost_person_day").getConfigValue()));      // 人员补贴 = 外出天数 * 外出人员 * 55
                entity.setNightSubsidy(entity.getOutGoNum()
                        * ((entity.getOutGoDay() - 1) >= 0 ? (entity.getOutGoDay() - 1) : 0)
                        * Double.parseDouble(configInfoMap.get("cost_person_night").getConfigValue()));    // 住宿补贴 = 外出天数 * 外出人员 * 80
                entity.setGoOutSubsidy(entity.getTimeSubsidy()
                        * this.activityMaintenanceRequest.getHourPrice());     // 外出补贴费用
                // 单项外出费用统计
                entity.setAmountCost(entity.getTranCosts()  // 交通费用
                        + entity.getTrailerCost()           // 拖车费用
                        + entity.getPersonnelSubsidy()      // 人员补贴
                        + entity.getNightSubsidy());          // 住宿补贴


                // 外出费用合计
                this.activityMaintenanceRequest.setOutExpense(this.activityMaintenanceRequest.getOutExpense()
                        + entity.getAmountCost());
            }
        }

        // 计算出来的工时补贴费用合计
        Double oriHourExpense = this.activityMaintenanceRequest.getHourPrice() * this.activityMaintenanceRequest.getOutHours();
        this.activityMaintenanceRequest.setHourExpense(oriHourExpense);
        if (isOut) {    // 有外出记录


            // 工时补贴费用合计
            if (outKm <= Double.parseDouble(configInfoMap.get("km_interval").getConfigValue())) {   // 外出总里程 < 50 公里

                if (this.activityMaintenanceRequest.getStandardExpense() < Double.parseDouble(configInfoMap.get("cost_less_km_interval").getConfigValue())) {
                    this.activityMaintenanceRequest.setHourExpense(Double.parseDouble(configInfoMap.get("cost_less_km_interval").getConfigValue())
                            - this.activityMaintenanceRequest.getStandardExpense());
                }
            } else {   // 外出里程 > 50

                if (this.activityMaintenanceRequest.getStandardExpense() < Double.parseDouble(configInfoMap.get("cost_greater_km_interval").getConfigValue())) {
                    this.activityMaintenanceRequest.setHourExpense(Double.parseDouble(configInfoMap.get("cost_greater_km_interval").getConfigValue())
                            - this.activityMaintenanceRequest.getStandardExpense());
                }
            }
        }

        this.activityMaintenanceRequest.setOutExpense(this.activityMaintenanceRequest.getOutExpense()
                + this.activityMaintenanceRequest.getHourExpense());

        // 总费用
        this.activityMaintenanceRequest.setExpenseTotal(
                +this.activityMaintenanceRequest.getStandardExpense()  // 活动标准费用
                        + this.activityMaintenanceRequest.getNightExpense()
                        + this.activityMaintenanceRequest.getOutExpense()             // 外出费用
                        + this.activityMaintenanceRequest.getOtherExpense()           // 其它费用
        );


    }


    @Command
    @NotifyChange("activityMaintenanceRequest")
    public void submit() {
        this.activityMaintenanceRequest = activityMaintenanceService.save(this.activityMaintenanceRequest);
        this.flowDocInfo = this.activityMaintenanceRequest;
        activityVehicleService.save(this.activityMaintenanceRequest.getActivityVehicleItem());
        activityVehicleService.save(this.activityVehicleItem);
        if (StringUtils.isNotBlank(activityDistributionInfo.getActivityNoticeId())) {
            //获取活动通知单
            activityNoticeInfo = activityNoticeService.findOneById(activityDistributionInfo.getActivityNoticeId());
            this.activityDistributionInfo.setActivityNotice(activityNoticeInfo);
        }
        this.updateUIState();
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
                if (startProcessInstance(this.activityMaintenanceRequest)) {
                    this.activityMaintenanceRequest = activityMaintenanceService.save(this.activityMaintenanceRequest);
                    flowDocInfo = this.activityMaintenanceRequest;
                    Map<String, String> map = activityMaintenanceService.startProcess(this.activityMaintenanceRequest, getActiveUser());
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
     * 维修历史页面
     *
     * @param activityVehicleItem
     * @param url
     */
    @Command
    @NotifyChange("activityVehicleInfo")
    public void openMaintenanceHistory(@BindingParam("activityVehicleItem") ActivityVehicleItem activityVehicleItem, @BindingParam("url") String url, @BindingParam("title") String title) {

        Map<String, Object> paramMap = new HashMap<>();
        if (activityVehicleItem.getObjId() != null) {
            paramMap.put("vehicleInfo", vehicleService.findOne(activityVehicleItem.getVehicleId()));
        }

        try {
            if (activityVehicleItem == null) {
                ZkUtils.showExclamation("请先选择车辆！", "系统提示");
                return;
            }
            ZkTabboxUtil.newTab(activityVehicleItem.getVehicleId() == null ? URLEncoder.encode(title, "UTF-8") : activityVehicleItem.getVehicleId() + "mh", title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (TabDuplicateException ex) {
            ex.printStackTrace();
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
        for (GoOutInfo outEntity : this.activityMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture(fileName);
            }

        }
    }


    @Override
    protected Boolean checkValid() {
        if (this.activityMaintenanceRequest.getEndDate().getTime() <= this.activityMaintenanceRequest.getStartDate().getTime()) {
            ZkUtils.showExclamation("【完工日期】必须大于【开工日期】！", "系统提示");
            return false;
        }
        if (this.activityMaintenanceRequest.getPullOutDate().getTime() <= this.activityMaintenanceRequest.getPullInDate().getTime()) {
            ZkUtils.showExclamation("【出站时间】必须大于【进站时间】！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.activityMaintenanceRequest.getActivityVehicleId())) {
            ZkUtils.showExclamation("请选择车辆！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.activityMaintenanceRequest.getSender())
                || StringUtils.isBlank(this.activityMaintenanceRequest.getSenderPhone())
                || StringUtils.isBlank(this.activityMaintenanceRequest.getRepairer())) {
            ZkUtils.showExclamation("请填写维修信息！", "系统提示");
            return false;
        }
        return true;
    }

    /**
     * 删除外出凭证
     *
     * @param goOutitem
     */
    @Command
    @NotifyChange("*")
    public void deleteOutGoFile(@BindingParam("each") GoOutInfo goOutitem) {
        for (GoOutInfo outEntity : this.activityMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture("");
            }

        }

    }

    @Command
    @NotifyChange("*")
    public void addGoOut() {
        System.out.println("增加一行");
        this.activityMaintenanceRequest.getGoOuts().add(new GoOutInfo());
    }

    @Command
    @NotifyChange("*")
    public void deleteGoOut(@BindingParam("model") GoOutInfo entity) {
        try {
            ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    if (clickedButton == Messagebox.OK) {
                        //从内存中删除维修项目
                        Iterator<GoOutInfo> iterator = activityMaintenanceRequest.getGoOuts().iterator();
                        while (iterator.hasNext()) {
                            GoOutInfo goOut = iterator.next();
                            if (entity == goOut) {
                                iterator.remove();
                            }
                        }
                        if (StringUtils.isNotBlank(entity.getObjId())) {
                            goOutService.deleteByObjId(entity.getObjId());
                        }
                        computeCost();
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);
                    } else {
                        return;
                    }
                }
            });

        } catch (Exception e) {
            ZkUtils.showError("删除失败", "警告");
            e.printStackTrace();
        }
    }


    /**
     * 获取图片附件路径
     *
     * @param filename
     * @return
     */
    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_ASM + filename;
    }


    /**
     * 初始化后加载窗体
     *
     * @param view
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        Selectors.wireComponents(view, this, false);
        //win = (org.zkoss.zul.Window) view;
    }

    @Override
    public Boolean getCheckCanPrint() {
        return true;
    }


    /**
     * 打印报表
     */
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("obj_id", this.activityMaintenanceRequest.getObjId() == null ? "" : this.activityMaintenanceRequest.getObjId());
        map.put("printType", "activityMaintenance.jasper");
        Window win = (Window) ZkUtils.createComponents("/views/report/printPage/asm/activity_maintenance_printPage.zul", null, map);
        win.setTitle("打印页面");
        win.doModal();
    }


    /**
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return activityMaintenanceService.save((ActivityMaintenanceInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return activityMaintenanceService.findOneById(objId);
    }


    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                this.activityMaintenanceService.desertTask(this.activityMaintenanceRequest.getObjId());
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
     * 刷新
     */
    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("activityMaintenanceRequest", "handle", "canDesertTask", "canHandleTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_MAINTENANCE_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    /**
     * 打开活动分配表单
     */
    @Command
    public void openActivityDistributionForm() {
        if (this.activityMaintenanceRequest.getActivityDistributionId() != null && StringUtils.isNotBlank(this.activityMaintenanceRequest.getActivityDistributionId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.activityMaintenanceRequest.getActivityDistributionId();
                if (StringUtils.isNotBlank(srcDocID)) {
                    paramMap.put("objId", srcDocID);
                    paramMap.put("businessId", srcDocID);
                }
                String title = "活动分配单";
                url = "/views/asm/activity_distribution_form.zul";
                ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 打开活动分配表单
     */
    @Command
    public void openActivityNoticeForm() {
        if (this.activityMaintenanceRequest.getActivityDistributionId() != null && StringUtils.isNotBlank(this.activityMaintenanceRequest.getActivityDistributionId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.activityMaintenanceRequest.getActivityDistribution().getActivityNoticeId();
                if (StringUtils.isNotBlank(srcDocID)) {
                    paramMap.put("objId", srcDocID);
                    paramMap.put("businessId", srcDocID);
                }
                String title = "活动通知单";
                url = "/views/asm/activity_notice_form.zul";
                ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }


}


