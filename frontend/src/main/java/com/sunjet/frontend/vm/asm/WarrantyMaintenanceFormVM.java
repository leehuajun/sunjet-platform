package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.*;
import com.sunjet.dto.asms.basic.*;
import com.sunjet.dto.asms.recycle.RecycleNoticeInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.*;
import com.sunjet.frontend.service.basic.MaintainService;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.service.recycle.RecycleNoticeService;
import com.sunjet.frontend.service.supply.SupplyItemService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
import com.sunjet.frontend.service.supply.SupplyService;
import com.sunjet.frontend.service.system.ConfigService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 三包服务单  表单
 * Created by Administrator on 2017/7/13.
 */
public class WarrantyMaintenanceFormVM extends FormVM {

    @WireVariable
    private DictionaryService dictionaryService;   // 数字字典

    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;    //三包服务单service

    @WireVariable
    private WarrantyMaintainService warrantyMaintainService;   //三包维修项目

    @WireVariable
    private MaintainService maintainService;

    @WireVariable
    private CommissionPartService commissionPartService;   //配件维修列表service

    @WireVariable
    private PartService partService;

    @WireVariable
    private GoOutService goOutService; //外出列表

    @WireVariable
    private VehicleService vehicleService;

    @WireVariable
    private ConfigService configService;    // 配置参数

    @WireVariable
    private QualityReportService qualityReportService;      //质量速报

    @WireVariable
    private ExpenseReportService expenseReportService;    // 费用速报

    @WireVariable
    private ReportPartService reportPartService;   //速报配件

    @WireVariable
    private ReportVehicleService reportVehicleService;   //速报车辆

    @WireVariable
    private RecycleNoticeService recycleNoticeService; //故障件返回通知单

    @WireVariable
    private SupplyNoticeService supplyNoticeService; // 调拨通知单

    @WireVariable
    private SupplyService supplyService;//调拨供货
    @WireVariable
    private SupplyItemService supplyItemService;//调拨供货单子行


    @Setter
    @Getter
    private WarrantyMaintenanceInfo warrantyMaintenanceRequest = new WarrantyMaintenanceInfo();   //三包服务单


    @Getter
    @Setter
    private CommissionPartInfo commissionPartInfo; // 选中的配件

    private List<WarrantyMaintainInfo> warrantyMaintainInfoList = new ArrayList<>();

    //@Getter
    //@Setter
    //private WarrantyMaintainInfo warrantyMaintainInfo;   //维修项目实体

    @Getter
    @Setter
    private List<GoOutInfo> goOutInfoList = new ArrayList<>();

    @Getter
    @Setter
    private GoOutInfo goOutInfo;  //外出实体

    @Getter
    @Setter
    private List<DictionaryInfo> docTypes = new ArrayList<>();    //单据类型

    @Getter
    @Setter
    private DictionaryInfo dictionaryInfo = new DictionaryInfo();   // 数据字典

    @Getter
    @Setter
    private List<QualityReportInfo> qualityReports = new ArrayList<>();    //查询质量速报列表

    @Getter
    @Setter
    private QualityReportInfo qualityReport;   //质量速报
    @Getter
    @Setter
    private ExpenseReportInfo expenseReport;    //费用速报

    @Getter
    @Setter
    private List<ExpenseReportInfo> expenseReports = new ArrayList<>();    // 查询费用速报列表

    @Getter
    @Setter
    private List<DictionaryInfo> repairTypes = new ArrayList<>();//维修类别

    @Getter
    @Setter
    private List<PartInfo> parts = new ArrayList<>();   //配件
    @Getter
    @Setter
    private List<MaintainInfo> maintains = new ArrayList<>();   //工时定额

    @Getter
    @Setter
    private List<VehicleInfo> vehicles = new ArrayList<>();   // 查询车辆列表

    @Getter
    @Setter
    private Map<String, ConfigInfo> configInfoMap = new HashMap<>();  //配置参数列表

    @Getter
    @Setter
    private VehicleInfo vehicle;

    @Getter
    @Setter
    private SupplyNoticeInfo supplyNoticeInfo;  //调拨通知单

    @Getter
    @Setter
    protected String maintenanceHistoryUrl;
    @Getter
    @Setter
    private String title = "维修历史";

    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();  // 经销商列表

    @Getter
    private Map<String, DictionaryInfo> vms = new HashMap<>();

    @Getter
    @Setter
    private List<AgencyInfo> agencyInfoList = new ArrayList<>();
    @Getter
    @Setter
    private AgencyInfo selectAgencyInfo;   // 选中的合作商

    @Getter
    @Setter
    private Boolean isPartWarranty = true;

    @Getter
    @Setter
    private Window window;

    @Getter
    @Setter
    private Boolean isSelectWarranty = false;

    @Getter
    @Setter
    private MaintainInfo searchmaintainInfo = new MaintainInfo();

    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId) || StringUtils.isNotBlank(this.getBusinessId())) {
            this.warrantyMaintenanceRequest = warrantyMaintenanceService.findOneById(objId);

            if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getSupplyNoticeId())) {
                this.canShowOpenSupplyNoticeForm = true;
            }
            if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getRecycleNoticeId())) {
                this.canShowOpenRecycleNoticeForm = true;
            }
        } else {
            warrantyMaintenanceRequest = new WarrantyMaintenanceInfo();
            warrantyMaintenanceRequest.setVehicleInfo(new VehicleInfo());
            this.warrantyMaintenanceRequest.setDocType("三包维修");
            if (getActiveUser().getDealer() != null) {
                this.warrantyMaintenanceRequest.setDealerCode(getActiveUser().getDealer().getCode());
                this.warrantyMaintenanceRequest.setDealerName(getActiveUser().getDealer().getName());
                this.warrantyMaintenanceRequest.setDealerStar(getActiveUser().getDealer().getStar());
                this.warrantyMaintenanceRequest.setDealerPhone(getActiveUser().getDealer().getPhone());
                this.warrantyMaintenanceRequest.setProvinceName(getActiveUser().getDealer().getProvinceName());
                this.warrantyMaintenanceRequest.setServiceManager(getActiveUser().getDealer().getServiceManagerName());
                this.warrantyMaintenanceRequest.setHourPrice(this.getHourPriceByDealer(getActiveUser().getDealer()));
            }

        }
        this.setMaintenanceHistoryUrl("/views/asm/maintenance_history.zul");
        List<ConfigInfo> configInfoList = configService.findAll();
        for (ConfigInfo configInfo : configInfoList) {
            configInfoMap.put(configInfo.getConfigKey(), configInfo);
        }

        //获取维修类型
        repairTypes = dictionaryService.findDictionariesByParentCode("10070");
        //获取单据类型
        docTypes = dictionaryService.findDictionariesByParentCode("14000");

        //设置用户信息
        this.setActiveUserMsg(this.warrantyMaintenanceRequest);

        //获取配件销售质保
        dictionaryInfo = dictionaryService.findDictionaryByCode("14002");

        if (warrantyMaintenanceRequest.getDocType().equals(dictionaryInfo.getName())) {
            if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getSupplyNoticeId())) {
                isPartWarranty = true;
            } else {
                isPartWarranty = false;
            }
        }
        if (this.warrantyMaintenanceRequest.getDocType().equals(dictionaryInfo.getName())) {
            if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getDealerCode())) {
                DealerInfo dealerInfo = dealerService.findOneByCode(this.warrantyMaintenanceRequest.getDealerCode());
                if (dealerInfo != null) {
                    agencyInfoList = agencyService.findOneAgencnyById(dealerInfo.getProvinceId());
                }
            }
        }

        dictionaryService.findDictionariesByParentCode("15000").forEach(dic -> vms.put(dic.getCode(), dic));


    }


    /**
     * 增加维修项目行
     */
    @Command
    @NotifyChange({"warrantyMaintenanceRequest", "isSelectWarranty"})
    public void addMaintain() {
        if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getObjId())) {
            isSelectWarranty = true;
        } else {
            ZkUtils.showInformation("请先保存单据", "提示");
        }

        //this.warrantyMaintenanceRequest.getWarrantyMaintains().add(new WarrantyMaintainInfo());
    }

    /**
     * 删除维修项目行
     *
     * @param warrantyMaintainInfo
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void deleteMaintain(@BindingParam("model") WarrantyMaintainInfo warrantyMaintainInfo) {

        try {
            ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    // 用户点击的是确定按钮
                    if (clickedButton == Messagebox.OK) {
                        //从内存中删除维修项目
                        Iterator<WarrantyMaintainInfo> iterator = warrantyMaintenanceRequest.getWarrantyMaintains().iterator();
                        while (iterator.hasNext()) {
                            WarrantyMaintainInfo maintainInfo = iterator.next();
                            if (maintainInfo == warrantyMaintainInfo) {
                                iterator.remove();
                            }
                        }
                        //删除数据
                        if (StringUtils.isNotBlank(warrantyMaintainInfo.getObjId())) {
                            warrantyMaintainService.delete(warrantyMaintainInfo.getObjId());
                        }
                        computeCost();
                        warrantyMaintenanceRequest = warrantyMaintenanceService.save(warrantyMaintenanceRequest);
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
     * 增加外出行
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void addGoOut() {
        goOutInfo = new GoOutInfo();
        this.warrantyMaintenanceRequest.getGoOuts().add(goOutInfo);
    }

    /**
     * 删除外出行
     *
     * @param goOutInfo
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void deleteGoOut(@BindingParam("model") GoOutInfo goOutInfo) {
        try {
            ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    if (clickedButton == Messagebox.OK) {
                        //从内存中删除维修项目
                        Iterator<GoOutInfo> iterator = warrantyMaintenanceRequest.getGoOuts().iterator();
                        while (iterator.hasNext()) {
                            GoOutInfo goOut = iterator.next();
                            if (goOutInfo == goOut) {
                                iterator.remove();
                            }
                        }
                        if (StringUtils.isNotBlank(goOutInfo.getObjId())) {
                            goOutService.deleteByObjId(goOutInfo.getObjId());
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
     * 查询车辆列表
     */
    @Command
    @NotifyChange("vehicles")
    public void searchVehicles() {
        this.vehicles.clear();

        if (this.qualityReport == null && this.expenseReport == null) {
            if (this.getKeyword().trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
                this.vehicles = vehicleService.findAllByKeyword(this.getKeyword().trim());
            } else {
                ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
            }
        } else {
            if (this.qualityReport != null) {
                List<String> vehicleIdList = new ArrayList<>();
                List<ReportVehicleInfo> qrVehicles = this.reportVehicleService.findByQrId(this.qualityReport.getObjId());
                for (ReportVehicleInfo qrVehicle : qrVehicles) {
                    vehicleIdList.add(qrVehicle.getVehicle_id());
                }
                this.vehicles = vehicleService.findByVehicleId(vehicleIdList);

            } else {
                List<String> vehicleIdList = new ArrayList<>();
                List<ReportVehicleInfo> qrVehicles = this.reportVehicleService.findByCrId(this.expenseReport.getObjId());
                for (ReportVehicleInfo qrVehicle : qrVehicles) {
                    vehicleIdList.add(qrVehicle.getVehicle_id());
                }
                this.vehicles = vehicleService.findByVehicleId(vehicleIdList);
            }
        }
    }

    /**
     * 选中车辆
     *
     * @param vehicleInfo
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void selectVehicle(@BindingParam("model") VehicleInfo vehicleInfo) throws InvocationTargetException, IllegalAccessException {
        this.warrantyMaintenanceRequest.setVehicleId(vehicleInfo.getObjId());
        this.warrantyMaintenanceRequest.setVehicleInfo(vehicleInfo);
        this.warrantyMaintenanceRequest.setTypeCode(vehicleInfo.getTypeCode());
        this.setKeyword("");
        this.vehicles.clear();
    }

    /**
     * 更新行驶里程
     *
     * @param vmt
     */
    @Command
    @NotifyChange("vmt")
    public void updateVehiceleMileage(@BindingParam("vmt") String vmt) {
        if (this.warrantyMaintenanceRequest.getVehicleInfo() != null) {
            this.warrantyMaintenanceRequest.getVehicleInfo().setMileage(vmt);
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
     * 上传文件
     *
     * @param event
     * @param type
     */
    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
        System.out.println(getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
        if (type.equalsIgnoreCase("t01")) {
            this.warrantyMaintenanceRequest.setAmeplate(fileName);  // 车辆名牌
        } else if (type.equalsIgnoreCase("t02")) {
            this.warrantyMaintenanceRequest.setManual(fileName);    // 保养手册首页
        } else if (type.equalsIgnoreCase("t03")) {
            this.warrantyMaintenanceRequest.setOdometer(fileName);  // 里程表
        } else if (type.equalsIgnoreCase("t04")) {
            this.warrantyMaintenanceRequest.setInvoice(fileName);   // 购买发票
        } else if (type.equalsIgnoreCase("t05")) {
            this.warrantyMaintenanceRequest.setFront45(fileName);   // 车位前侧45度照片
        } else if (type.equalsIgnoreCase("t06")) {
            this.warrantyMaintenanceRequest.setFaultlocation(fileName); // 故障部位照片
        }
    }

    /**
     * 删除删除文件
     *
     * @param type
     */
    @Command
    @NotifyChange("*")
    public void dltUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t01")) {
            this.warrantyMaintenanceRequest.setAmeplate(null);  // 车辆名牌
        } else if (type.equalsIgnoreCase("t02")) {
            this.warrantyMaintenanceRequest.setManual(null);    // 保养手册首页
        } else if (type.equalsIgnoreCase("t03")) {
            this.warrantyMaintenanceRequest.setOdometer(null);  // 里程表
        } else if (type.equalsIgnoreCase("t04")) {
            this.warrantyMaintenanceRequest.setInvoice(null);   // 购买发票
        } else if (type.equalsIgnoreCase("t05")) {
            this.warrantyMaintenanceRequest.setFront45(null);   // 车位前侧45度照片
        } else if (type.equalsIgnoreCase("t06")) {
            this.warrantyMaintenanceRequest.setFaultlocation(null); // 故障部位照片
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
        for (GoOutInfo goOutInfo : this.warrantyMaintenanceRequest.getGoOuts()) {
            if (goOutInfo.equals(goOutitem)) {
                goOutInfo.setOutGoPicture(fileName);
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
        for (GoOutInfo goOutInfo : this.warrantyMaintenanceRequest.getGoOuts()) {
            if (goOutInfo.equals(goOutitem)) {
                goOutInfo.setOutGoPicture("");
            }

        }

    }


    /**
     * 统计费用
     */
    @Command
    @NotifyChange("*")
    public void computeCost() {
        this.warrantyMaintenanceRequest.setExpenseTotal(0.0);           // 总合计费用
        this.warrantyMaintenanceRequest.setPartExpense(0.0);            // 配件费用合计
        this.warrantyMaintenanceRequest.setAccessoriesExpense(0.0);     // 辅料费用合计
        this.warrantyMaintenanceRequest.setOutExpense(0.0);             // 外出费用合计

        this.warrantyMaintenanceRequest.setSettlementAccesoriesExpense(0.0);// 应结算辅料费用合计
        this.warrantyMaintenanceRequest.setSettlementPartExpense(0.0);      // 应结算配件费用
        this.warrantyMaintenanceRequest.setSettlementTotleExpense(0.0);     // 应结算配件费用
        this.warrantyMaintenanceRequest.setOutHours(0.0);                   // 外出工时合计
        this.warrantyMaintenanceRequest.setMaintainHours(0.0);              // 维修工时合计
        if (this.warrantyMaintenanceRequest.getOtherExpense() == null) {
            this.warrantyMaintenanceRequest.setOtherExpense(0.0);               //其他费用
        }
        Double outKm = 0.0;
        Boolean isOut = false;
        this.warrantyMaintenanceRequest.setNightExpense(0.0);
        if (this.warrantyMaintenanceRequest.getNightWork()) {  // 夜间作业
            this.warrantyMaintenanceRequest.setNightExpense(Double.parseDouble(configInfoMap.get("cost_night").getConfigValue()));
        }

        // 维修工时
        for (WarrantyMaintainInfo entity : this.warrantyMaintenanceRequest.getWarrantyMaintains()) {
            this.warrantyMaintenanceRequest.setMaintainHours(this.warrantyMaintenanceRequest.getMaintainHours()
                    + entity.getWorkTime());
        }
        // 维修工时费用
        this.warrantyMaintenanceRequest.setMaintainWorkTimeExpense(this.warrantyMaintenanceRequest.getMaintainHours()
                * this.warrantyMaintenanceRequest.getHourPrice());
        for (GoOutInfo entity : this.warrantyMaintenanceRequest.getGoOuts()) {
            if (entity.getPlace() != null && StringUtils.isNotBlank(entity.getPlace().trim())) {   // 外出地点不为空时，才进行计算
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
//                entity.setGoOutSubsidy(entity.getTimeSubsidy()
//                        * this.warrantyMaintenanceRequest.getHourPrice());     // 外出补贴费用
                // 单项外出费用统计
                entity.setAmountCost(entity.getTranCosts()  // 交通费用
                        + entity.getTrailerCost()           // 拖车费用
                        + entity.getPersonnelSubsidy()      // 人员补贴
                        + entity.getNightSubsidy());        // 住宿补贴
//                        + entity.getGoOutSubsidy());        // 外出补贴费用

                // 工时补贴合计
//                this.warrantyMaintenanceRequest.setOutHours(this.warrantyMaintenanceRequest.getOutHours()
//                        + entity.getTimeSubsidy());

                // 外出费用合计
                this.warrantyMaintenanceRequest.setOutExpense(this.warrantyMaintenanceRequest.getOutExpense()
                        + entity.getAmountCost());
            }
        }

        // 计算出来的工时补贴费用合计
        Double oriOutHourExpense = this.warrantyMaintenanceRequest.getHourPrice() * this.warrantyMaintenanceRequest.getOutHours();
        this.warrantyMaintenanceRequest.setOutWorkTimeExpense(oriOutHourExpense);
        if (isOut) {    // 有外出记录
            // 工时补贴费用合计
            if (outKm <= Double.parseDouble(configInfoMap.get("km_interval").getConfigValue())) {   // 外出总里程 < 50 公里
//                if (this.warrantyMaintenanceRequest.getOutWorkTimeExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))) {
//                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval")));
//                    this.warrantyMaintenanceRequest.setOutExpense(
//                            this.warrantyMaintenanceRequest.getOutExpense()
//                                    - oriOutHourExpense
//                                    + this.warrantyMaintenanceRequest.getOutWorkTimeExpense());
//                }

                if (this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense() < Double.parseDouble(configInfoMap.get("cost_less_km_interval").getConfigValue())) {
                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(configInfoMap.get("cost_less_km_interval").getConfigValue())
                            - this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense());
                }

            } else {   // 外出里程 > 50
//                if (this.warrantyMaintenanceRequest.getOutWorkTimeExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))) {
//                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval")));
//                    this.warrantyMaintenanceRequest.setOutExpense(
//                            this.warrantyMaintenanceRequest.getOutExpense()
//                                    - oriOutHourExpense
//                                    + this.warrantyMaintenanceRequest.getOutWorkTimeExpense());
//                }
                if (this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense() < Double.parseDouble(configInfoMap.get("cost_greater_km_interval").getConfigValue())) {
                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(configInfoMap.get("cost_greater_km_interval").getConfigValue())
                            - this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense());
                }
            }
        }

        this.warrantyMaintenanceRequest.setOutExpense(this.warrantyMaintenanceRequest.getOutExpense()
                + this.warrantyMaintenanceRequest.getOutWorkTimeExpense());


        // 配件费用
        for (CommissionPartInfo entity : this.warrantyMaintenanceRequest.getCommissionParts()) {
            entity.setTotal(entity.getPrice() * entity.getAmount());   // 单项费用
//      entity.setTotal(MathHelper.getDoubleAndTwoDecimalPlaces(entity.getTotal()));
            if (entity.getPartSupplyType().equals("调拨") || entity.getPartSupplyType().equals("寄销")) {
                entity.setSettlementTotal(0.0);
            } else {
                entity.setSettlementTotal(entity.getPrice() * entity.getAmount());  // 结算费用
//        entity.setSettlementTotal(MathHelper.getDoubleAndTwoDecimalPlaces(entity.getSettlementTotal()));
            }

            if (entity.getPartType().equals("配件")) {   // 配件
                // 叠加配件结算费用
                this.warrantyMaintenanceRequest.setSettlementPartExpense(this.warrantyMaintenanceRequest.getSettlementPartExpense()
                        + entity.getSettlementTotal());
                // 叠加配件费用
                this.warrantyMaintenanceRequest.setPartExpense(this.warrantyMaintenanceRequest.getPartExpense()
                        + entity.getTotal());
            } else {
                // 叠加辅料结算费用
                this.warrantyMaintenanceRequest.setSettlementAccesoriesExpense(this.warrantyMaintenanceRequest.getSettlementAccesoriesExpense()
                        + entity.getSettlementTotal());
                // 叠加辅料费用
                this.warrantyMaintenanceRequest.setAccessoriesExpense(this.warrantyMaintenanceRequest.getAccessoriesExpense()
                        + entity.getTotal());
            }
        }


        // 总费用
        this.warrantyMaintenanceRequest.setExpenseTotal(
                this.warrantyMaintenanceRequest.getAccessoriesExpense()      // 辅料费用
                        + this.warrantyMaintenanceRequest.getNightExpense()
//                        +this.warrantyMaintenanceRequest.getWorkTimeExpense()        // 外出工时费用
                        + this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense() // 维修工时费用
                        + this.warrantyMaintenanceRequest.getOutExpense()             // 外出费用
                        + this.warrantyMaintenanceRequest.getPartExpense()            // 配件费用
                        + this.warrantyMaintenanceRequest.getOtherExpense()           // 其它费用
        );
        // 应结算费用
        this.warrantyMaintenanceRequest.setSettlementTotleExpense(
                this.warrantyMaintenanceRequest.getSettlementAccesoriesExpense()        // 应结算辅料费用
                        + this.warrantyMaintenanceRequest.getNightExpense()
//          + this.warrantyMaintenanceRequest.getWorkTimeExpense()          // 应结算工时费用
                        + this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense()  // 维修工时费用
                        + this.warrantyMaintenanceRequest.getOutExpense()               // 应结算外出费用
                        + this.warrantyMaintenanceRequest.getSettlementPartExpense()    // 应结算配件费用
                        + this.warrantyMaintenanceRequest.getOtherExpense()             // 应结算其它费用

        );
    }


    /**
     * 选中配件行
     *
     * @param commissionPart
     */
    @Command
    @NotifyChange("commissionPartInfo")
    public void selectCommissionPart(@BindingParam("model") CommissionPartInfo commissionPart) {
        this.commissionPartInfo = commissionPart;
    }


    /**
     * 保存表单
     */
    @Command
    @NotifyChange("*")
    public void submit() {
        computeCost();
        this.warrantyMaintenanceRequest = warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);
        this.flowDocInfo = this.warrantyMaintenanceRequest;
        this.updateUIState();
        showDialog();
    }


    /**
     * 校验表单
     *
     * @return
     */
    @Override
    protected Boolean checkValid() {
        if (this.warrantyMaintenanceRequest.getEndDate().getTime() <= this.warrantyMaintenanceRequest.getStartDate().getTime()) {
            ZkUtils.showExclamation("【完工日期】必须大于【开工日期】！", "系统提示");
            return false;
        }
        if (this.warrantyMaintenanceRequest.getPullOutDate().getTime() <= this.warrantyMaintenanceRequest.getPullInDate().getTime()) {
            ZkUtils.showExclamation("【出站时间】必须大于【进站时间】！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getVehicleId())) {
            ZkUtils.showExclamation("请选择车辆！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getAmeplate())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getManual())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getOdometer())
//        || StringUtils.isBlank(this.warrantyMaintenanceRequest.getInvoice())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getFaultlocation())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getFront45())) {
            ZkUtils.showExclamation("请上传所有必填凭证文件！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getSender())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getSenderPhone())
//        || StringUtils.isBlank(this.warrantyMaintenanceRequest.getFault())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getRepairType())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getRepairer())) {
            ZkUtils.showExclamation("请填写维修信息！", "系统提示");
            return false;
        }
        for (CommissionPartInfo parts : this.warrantyMaintenanceRequest.getCommissionParts()) {
            if (parts.getAmount().equals(0)) {
                ZkUtils.showExclamation("配件需求数量小于0不能提交", "系统提示");
                return false;
            } else if (this.warrantyMaintenanceRequest.getSupplyNoticeId() == null && parts.getPartSupplyType().contains("调拨")) {
                ZkUtils.showExclamation("请生成调拨单再提交", "系统提示");
                return false;
            }

        }
        if (this.warrantyMaintenanceRequest.getGoOuts().size() > 0) {
            for (GoOutInfo goOutInfo : this.warrantyMaintenanceRequest.getGoOuts()) {
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

                if (goOutInfo.getTrailerCost() == null) {
                    goOutInfo.setTrailerCost(0.0);
                }

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
     * 提交,启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        //校验待返回故障件配件信息
        for (CommissionPartInfo part : warrantyMaintenanceRequest.getCommissionParts()) {
            if (StringUtils.isBlank(part.getPartCode())) {
                ZkUtils.showError("配件需求信息为0或者信息不全", "提示");
                return;
            }
            if (part.getAmount().equals(0)) {
                ZkUtils.showError("配件需求信息为0或者信息不全", "提示");
                return;
            }
        }

        if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getSupplyNoticeId())) {
            SupplyNoticeInfo supplyNotice = this.supplyNoticeService.findByOne(this.warrantyMaintenanceRequest.getSupplyNoticeId());
            if (!supplyNotice.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                ZkUtils.showInformation("调拨单没有审核通过不能提交", "提示");
                return;
            } else {
                //收货状态
                Boolean receiveState = supplyNoticeService.checkSupplyReceiveState(supplyNotice.getObjId());
                if (!receiveState) {
                    ZkUtils.showInformation("调拨供货单未确认收货", "提示");
                    return;
                }
            }
        }
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.warrantyMaintenanceRequest)) {
                    computeCost();
                    this.warrantyMaintenanceRequest = warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);

                    Map<String, String> map = warrantyMaintenanceService.startProcess(this.warrantyMaintenanceRequest, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.warrantyMaintenanceRequest = (WarrantyMaintenanceInfo) findInfoById(this.warrantyMaintenanceRequest.getObjId());
                    this.flowDocInfo = this.warrantyMaintenanceRequest;
                    this.updateUIState();
                }

            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });


    }

    /**
     * 检查生成故障件返回按钮
     *
     * @return
     */
    @Override
    public boolean checkCanEditRecycle() {
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getRecycleNoticeId()) && this.warrantyMaintenanceRequest.getCanEditRecycle()) {
            if (getActiveUser().getRoles() != null) {
                for (RoleInfo roleInfo : getActiveUser().getRoles()) {
                    if ("审单员".equals(roleInfo.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查生成调拨通知单按钮
     *
     * @return
     */
    @Override
    public Boolean checkCanEditSupply() {

        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getSupplyNoticeId()) && this.warrantyMaintenanceRequest.getStatus().equals(DocStatus.DRAFT.getIndex()) && this.warrantyMaintenanceRequest.getCanEditSupply()) {
            if ("admin".equals(getActiveUser().getLogId())) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 校验是否生成返回件
     */
    @Command
    @Override
    public void showHandleForm() {
        //校验退回后是否填写信息完整
        if (getActiveUser().getLogId().equals(this.warrantyMaintenanceRequest.getSubmitter())) {
            //等于false 信息不完整不能提交
            if (!this.checkValid()) {
                return;
            } else {
                super.showHandleForm();
            }

        } else {
            super.showHandleForm();
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

        if ("同意".equals(outcome) || "结算主管审批".equals(outcome)) {
            if (this.warrantyMaintenanceRequest.getCanEditRecycle() && StringUtils.isBlank(this.warrantyMaintenanceRequest.getRecycleNoticeId())) {
                Boolean canEditRecycle = false;
                //审单员角色id
                String roleId = "role014";
                for (RoleInfo roleInfo : getActiveUser().getRoles()) {
                    if (roleId.equals(roleInfo.getRoleId())) {
                        canEditRecycle = true;
                        break;
                    }
                }
                boolean haveRecycle = false;
                if (this.warrantyMaintenanceRequest.getCommissionParts().size() > 0) {
                    //检查是否有返回件
                    for (CommissionPartInfo part : this.warrantyMaintenanceRequest.getCommissionParts()) {
                        if (part.getRecycle()) {
                            haveRecycle = true;
                        }
                    }
                }

                if (canEditRecycle && haveRecycle) {
                    ZkUtils.showInformation("请生成故障件返回单", "提示");
                } else {
                    super.completeTask(outcome, comment);
                }
            } else {
                super.completeTask(outcome, comment);

            }
        } else {
            super.completeTask(outcome, comment);

        }

    }

    /**
     * 查询质量速报列表
     */
    @Command
    @NotifyChange("qualityReports")
    public void searchQualityReports() {
        // 查当前服务站的质量速报
        qualityReports = qualityReportService.findAllByKeywordAndDealerCode(this.getKeyword().trim(), this.warrantyMaintenanceRequest.getDealerCode());
        // 查当前服务站的质量速报
//        qualityReports = qualityReportService.findAllByStatusAndKeyword("%" + this.keyword + "%");
    }

    /**
     * 选中质量速报
     *
     * @param qualityReport
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Command
    @NotifyChange("*")
    public void selectQualityReport(@BindingParam("model") QualityReportInfo qualityReport) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getSupplyNoticeId())) {
            ZkUtils.showError("已经生成了调拨单,不能选择质量速报", "系统提示");
            return;
        }
        this.vehicles.clear();
        this.qualityReport = qualityReport;
        this.expenseReport = null;
        this.warrantyMaintenanceRequest.setVehicleInfo(null);
        this.warrantyMaintenanceRequest.setVehicleId(null);
        this.warrantyMaintenanceRequest.setQualityReportId(this.qualityReport.getObjId());
        this.warrantyMaintenanceRequest.setQualityReportDocNo(this.qualityReport.getDocNo());
        this.warrantyMaintenanceRequest.setExpenseReportId(null);
        this.warrantyMaintenanceRequest.setExpenseReportDocNo(null);
        this.setKeyword("");
        this.qualityReports.clear();

        //this.qualityReport = qualityReportService.findOneWithVehiclesAndParts(qualityReport.getObjId());
        //获取速报配件
        List<ReportPartInfo> reportPartInfoList = reportPartService.findByQrId(qualityReport.getObjId());
        List<String> partIdList = new ArrayList<>();
        if (reportPartInfoList != null && reportPartInfoList.size() > 0) {
            for (ReportPartInfo reportPartInfo : reportPartInfoList) {
                partIdList.add(reportPartInfo.getPart_id());
            }
            Map<String, PartInfo> map = new HashMap<>();

            List<PartInfo> partInfoList = partService.findByPartId(partIdList);
            if (partInfoList != null) {
                for (PartInfo partInfo : partInfoList) {
                    map.put(partInfo.getObjId(), partInfo);
                }
            }

            this.warrantyMaintenanceRequest.getCommissionParts().clear();
            commissionPartService.deleteByWarrantyMaintenanceObjId(this.warrantyMaintenanceRequest.getObjId());
            for (ReportPartInfo rpe : reportPartInfoList) {
                rpe.setPart(map.get(rpe.getPart_id()) == null ? new PartInfo() : map.get(rpe.getPart_id()));
                CommissionPartInfo part = new CommissionPartInfo();
                part.setPartCode(rpe.getPart().getCode());
                part.setPartName(rpe.getPart().getName());
                part.setPrice(rpe.getPart().getPrice());
                part.setPartType(rpe.getPart().getPartType());
                part.setAmount(rpe.getAmount());
                part.setWarrantyMileage(rpe.getPart().getWarrantyMileage());
                part.setWarrantyTime(rpe.getPart().getWarrantyTime());
                part.setPartSupplyType("自购");
                part.setPattern(rpe.getFault());
                part.setRecycle(true);
                this.warrantyMaintenanceRequest.getCommissionParts().add(part);
            }
            computeCost();
        }

    }

    /**
     * 查询费用速报列表
     */
    @Command
    @NotifyChange("expenseReports")
    public void searchExpenseReports() {
        // 查当前服务站的费用速报
        expenseReports = expenseReportService.findAllByKeywordAndDealerCode(this.getKeyword().trim(), this.warrantyMaintenanceRequest.getDealerCode());
        // 查所有服务站的费用速报
//        expenseReports = expenseReportService.findAllByStatusAndKeyword("%" + this.keyword + "%");
    }

    /**
     * 选中费用速报
     *
     * @param expenseReport
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Command
    @NotifyChange("*")
    public void selectExpenseReport(@BindingParam("model") ExpenseReportInfo expenseReport) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getSupplyNoticeId())) {
            ZkUtils.showError("已经生成了调拨单,不能选择费用速报", "系统提示");
            return;
        }
        this.vehicles.clear();
        this.expenseReport = expenseReport;
        this.qualityReport = null;
        this.warrantyMaintenanceRequest.setVehicleId(null);
        this.warrantyMaintenanceRequest.setVehicleInfo(null);
        this.warrantyMaintenanceRequest.setQualityReportId(null);
        this.warrantyMaintenanceRequest.setQualityReportDocNo(null);
        this.warrantyMaintenanceRequest.setExpenseReportId(this.expenseReport.getObjId());
        this.warrantyMaintenanceRequest.setExpenseReportDocNo(this.expenseReport.getDocNo());
        this.setKeyword("");
        this.expenseReports.clear();


        List<ReportPartInfo> reportPartInfos = reportPartService.findByCrId(expenseReport.getObjId());

        List<String> partIdList = new ArrayList<>();
        if (reportPartInfos != null && reportPartInfos.size() > 0) {
            for (ReportPartInfo reportPartInfo : reportPartInfos) {
                partIdList.add(reportPartInfo.getPart_id());
            }
            Map<String, PartInfo> map = new HashMap<>();

            List<PartInfo> partInfoList = partService.findByPartId(partIdList);
            for (PartInfo partInfo : partInfoList) {
                map.put(partInfo.getObjId(), partInfo);
            }

//        Set<CommissionPartEntity> partItems = this.warrantyMaintenanceRequest.getActivityParts();
            this.warrantyMaintenanceRequest.getCommissionParts().clear();
            commissionPartService.deleteByWarrantyMaintenanceObjId(this.warrantyMaintenanceRequest.getObjId());
            for (ReportPartInfo rpe : reportPartInfos) {
                rpe.setPart(map.get(rpe.getPart_id()) == null ? new PartInfo() : map.get(rpe.getPart_id()));
                CommissionPartInfo part = new CommissionPartInfo();
                part.setPartCode(rpe.getPart().getCode());
                part.setPartName(rpe.getPart().getName());
                part.setPrice(rpe.getPart().getPrice());
                part.setPartType(rpe.getPart().getPartType());
                part.setAmount(rpe.getAmount());
                part.setWarrantyMileage(rpe.getPart().getWarrantyMileage());
                part.setWarrantyTime(rpe.getPart().getWarrantyTime());
                part.setPartSupplyType("自购");
                part.setPattern(rpe.getFault());
                part.setRecycle(true);
                this.warrantyMaintenanceRequest.getCommissionParts().add(part);
            }
            computeCost();
        }

    }

    /**
     * 清除选中的质量速报对象
     */
    @Command
    @NotifyChange("*")
    public void clearQualityReport() {
        if (this.qualityReport != null) {
            this.qualityReport = null;
            this.vehicles.clear();
            this.warrantyMaintenanceRequest.setQualityReportDocNo("");
            this.warrantyMaintenanceRequest.setVehicleInfo(null);
            this.warrantyMaintenanceRequest.setVehicleId(null);
            this.warrantyMaintenanceRequest.getCommissionParts().clear();
            this.parts.clear();
            this.qualityReports.clear();
            computeCost();
        }
    }

    /**
     * 清除选中的费用速报
     */
    @Command
    @NotifyChange("*")
    public void clearExpenseReport() {
        if (this.expenseReport != null) {
            this.expenseReport = null;
            this.vehicles.clear();
            this.warrantyMaintenanceRequest.setExpenseReportDocNo("");
            this.warrantyMaintenanceRequest.setVehicleInfo(null);
            this.warrantyMaintenanceRequest.setVehicleId(null);
            this.warrantyMaintenanceRequest.getCommissionParts().clear();
            this.parts.clear();
            this.expenseReports.clear();
            computeCost();
        }
    }

    /**
     * 清除选中的车辆
     */
    @Command
    @NotifyChange("*")
    public void clearVehicle() {
        this.warrantyMaintenanceRequest.setVehicleId(null);
    }


    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("parts")
    public void searchParts() {
        if (this.getKeyword().trim().length() >= CommonHelper.FILTER_PARTS_LEN) {
            this.parts = partService.findAllByKeyword(this.getKeyword().trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_PARTS_ERROR, "提示");
        }

    }


    /**
     * 增加配件行
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void addPart() {
        commissionPartInfo = new CommissionPartInfo();
//        partEntity.setPartType("配件");
        if (commissionPartInfo.getPartType() == null) {
            commissionPartInfo.setPartType("配件");
        }
        if (commissionPartInfo.getPartType().equals("配件")) {
            commissionPartInfo.setPartSupplyType("调拨");
        } else {
            commissionPartInfo.setPartSupplyType("自购");
        }
        commissionPartInfo.setRecycle(true);
        this.warrantyMaintenanceRequest.getCommissionParts().add(commissionPartInfo);
    }

    /**
     * 选中配件
     *
     * @param partEntity
     */
    @Command
    @NotifyChange("*")
    public void selectPart(@BindingParam("model") PartInfo partEntity) {
        commissionPartInfo.setPartCode(partEntity.getCode());
        commissionPartInfo.setPartName(partEntity.getName());
        commissionPartInfo.setPrice(partEntity.getPrice());
        commissionPartInfo.setPartType(partEntity.getPartType());
        commissionPartInfo.setWarrantyTime(partEntity.getWarrantyTime());
        commissionPartInfo.setWarrantyMileage(partEntity.getWarrantyMileage());
        this.setKeyword("");
        this.parts.clear();
        computeCost();
        this.warrantyMaintenanceRequest = this.warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);
        this.flowDocInfo = this.warrantyMaintenanceRequest;
    }

    /**
     * 删除配件行
     *
     * @param commissionPartInfo
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void deletePart(@BindingParam("model") CommissionPartInfo commissionPartInfo) {

        try {
            ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    // 用户点击的是确定按钮
                    if (clickedButton == Messagebox.OK) {
                        //从内存中删除配件
                        Iterator<CommissionPartInfo> iterator = warrantyMaintenanceRequest.getCommissionParts().iterator();
                        while (iterator.hasNext()) {
                            CommissionPartInfo commissionPart = iterator.next();
                            if (commissionPart == commissionPartInfo) {
                                iterator.remove();
                            }
                        }
                        //删除记录
                        if (StringUtils.isNotBlank(commissionPartInfo.getObjId())) {
                            commissionPartService.delete(commissionPartInfo.getObjId());
                        }
                        computeCost();
                        warrantyMaintenanceRequest = warrantyMaintenanceService.save(warrantyMaintenanceRequest);
                        flowDocInfo = warrantyMaintenanceRequest;
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
     * 选中维修项目
     *
     * @param warrantyMaintain
     */
    @Command
    public void selectWarrantMaintain(@BindingParam("model") WarrantyMaintainInfo warrantyMaintain) {
        //this.warrantyMaintainInfo = warrantyMaintain;
    }

    /**
     * 查询维修项目列表
     */
    @Command
    @NotifyChange("maintains")
    public void searchMaintains() {
        //this.maintains = maintainService.findAllByFilter(this.getKeyword().trim());
        this.maintains = maintainService.findAllByFilter(searchmaintainInfo);

    }

    /**
     * 选中维修项目
     *
     * @param maintainInfo
     */
    @Command
    @NotifyChange("*")
    public void selectMaintain(@BindingParam("model") MaintainInfo maintainInfo) {
        WarrantyMaintainInfo warrantyMaintainInfo = new WarrantyMaintainInfo();
        warrantyMaintainInfo.setCode(maintainInfo.getCode());
        warrantyMaintainInfo.setName(maintainInfo.getName());
        warrantyMaintainInfo.setVehicleModelName(maintainInfo.getVehicleModelName());
        warrantyMaintainInfo.setVehicleSystemName(maintainInfo.getVehicleSystemName());
        warrantyMaintainInfo.setVehicleSubSystemName(maintainInfo.getVehicleSubSystemName());
        warrantyMaintainInfo.setWorkTime(maintainInfo.getWorkTime());
        warrantyMaintainInfo.setHourPrice(this.warrantyMaintenanceRequest.getHourPrice());
        warrantyMaintainInfo.setTotal(warrantyMaintainInfo.getHourPrice() * warrantyMaintainInfo.getWorkTime());
        warrantyMaintainInfo.setWarrantyMaintenance(this.warrantyMaintenanceRequest.getObjId());
        this.warrantyMaintainService.save(warrantyMaintainInfo);
        this.warrantyMaintenanceRequest = this.warrantyMaintenanceService.findOneById(this.warrantyMaintenanceRequest.getObjId());
        this.searchmaintainInfo = new MaintainInfo();
        this.maintains.clear();
        isSelectWarranty = false;
        computeCost();
        this.warrantyMaintenanceRequest = this.warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);
        this.flowDocInfo = this.warrantyMaintenanceRequest;

    }

    /**
     * 重置维修项目条件
     */
    @Command
    @NotifyChange("searchmaintainInfo")
    public void resetMaintains() {
        this.searchmaintainInfo = new MaintainInfo();
    }


    /**
     * 生成供货通知单/调拨单
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void generateSupplyNotice() {
        if (warrantyMaintenanceRequest.getDocType().equals(dictionaryInfo.getName()) && StringUtils.isBlank(warrantyMaintenanceRequest.getAgencyName())) {
            ZkUtils.showExclamation("不能生成调拨单,请先选择质保经销商", "系统提示");
            return;
        }
        if (StringUtils.isBlank(warrantyMaintenanceRequest.getObjId())) {
            ZkUtils.showError("三包服务单未保存，不能生成调拨单", "系统提示");
            return;
        }
        if (StringUtils.isBlank(warrantyMaintenanceRequest.getVehicleId())) {
            ZkUtils.showError("没有选择车辆,不能生成调拨单", "系统提示");
            return;
        }
        for (CommissionPartInfo commissionParts : warrantyMaintenanceRequest.getCommissionParts()) {
            if (commissionParts.getAmount() == 0) {
                ZkUtils.showError("配件需求数量为0或者没有选择配件信息,不能生成调拨单", "系统提示");
                return;
            }
            if (StringUtils.isBlank(commissionParts.getPartCode())) {
                ZkUtils.showError("配件需求数量为0或者没有选择配件信息,不能生成调拨单", "系统提示");
                return;
            }

        }
        if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getSupplyNoticeId())) {
            ZkUtils.showError("不允许重复生成调拨通知单/供货通知单", "系统提示");
            return;
        }
        //校验附件信息
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getAmeplate())) {
            ZkUtils.showError("请先上传车辆铭牌图片", "提示");
            return;
        }
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getManual())) {
            ZkUtils.showError("请先上传保养手册首页图片", "提示");
            return;
        }
        if (StringUtils.isBlank(this.getWarrantyMaintenanceRequest().getOdometer())) {
            ZkUtils.showError("请先上传里程表图片", "提示");
            return;
        }
        if (StringUtils.isBlank(this.getWarrantyMaintenanceRequest().getFront45())) {
            ZkUtils.showError("请先上传前侧45度图片", "提示");
            return;
        }
        if (StringUtils.isBlank(this.getWarrantyMaintenanceRequest().getFaultlocation())) {
            ZkUtils.showError("请先上传故障部位图片", "提示");
            return;
        }
        SupplyNoticeInfo supplyNotice = this.supplyNoticeService.findOneBySrcDocId(this.warrantyMaintenanceRequest.getObjId());
        if (supplyNotice != null) {
            this.warrantyMaintenanceRequest.setSupplyNoticeId(supplyNotice.getObjId());
            this.warrantyMaintenanceRequest = (WarrantyMaintenanceInfo) saveInfo(this.warrantyMaintenanceRequest);
            this.updateUIState();
            ZkUtils.showInformation("调拨通知单已经存在", "提示");
            return;
        }
        this.warrantyMaintenanceRequest = (WarrantyMaintenanceInfo) saveInfo(this.warrantyMaintenanceRequest);


        List<CommissionPartInfo> parts = this.warrantyMaintenanceRequest.getCommissionParts();

        SupplyNoticeInfo supplyNoticeEntity = new SupplyNoticeInfo();
        supplyNoticeEntity.setDocType(warrantyMaintenanceRequest.getDocType());
        supplyNoticeEntity.setSrcDocNo(warrantyMaintenanceRequest.getDocNo());
        supplyNoticeEntity.setSrcDocType("三包服务单");
        supplyNoticeEntity.setSrcDocID(warrantyMaintenanceRequest.getObjId());
        supplyNoticeEntity.setDealerCode(warrantyMaintenanceRequest.getDealerCode());
        supplyNoticeEntity.setDealerName(warrantyMaintenanceRequest.getDealerName());
        supplyNoticeEntity.setProvinceName(warrantyMaintenanceRequest.getProvinceName());
        supplyNoticeEntity.setServiceManager(warrantyMaintenanceRequest.getServiceManager());
        supplyNoticeEntity.setCreaterId(warrantyMaintenanceRequest.getCreaterId());
        supplyNoticeEntity.setCreaterName(warrantyMaintenanceRequest.getCreaterName());
        supplyNoticeEntity.setSubmitter(warrantyMaintenanceRequest.getSubmitter());
        supplyNoticeEntity.setSubmitterName(warrantyMaintenanceRequest.getSubmitterName());
        supplyNoticeEntity.setSubmitterPhone(warrantyMaintenanceRequest.getSubmitterPhone());
//        supplyNoticeEntity.setOperatorPhone(warrantyMaintenanceRequest.getOperatorPhone());
        supplyNoticeEntity.setAgencyName(warrantyMaintenanceRequest.getAgencyName());
        supplyNoticeEntity.setAgencyCode(warrantyMaintenanceRequest.getAgencyCode());
        supplyNoticeEntity.setAgencyId(warrantyMaintenanceRequest.getAgencyId());

        List<SupplyNoticeItemInfo> noticeParts = new ArrayList<>();

        for (CommissionPartInfo entity : parts) {
            if (entity.getPartSupplyType().equals("调拨")) {
                SupplyNoticeItemInfo item = new SupplyNoticeItemInfo();
                item.setPartCode(entity.getPartCode());
                item.setPartName(entity.getPartName());
                item.setPrice(entity.getPrice());
                item.setRequestAmount(Double.valueOf(entity.getAmount()));
                item.setSurplusAmount(entity.getAmount());
                item.setWarrantyTime(entity.getWarrantyTime());
                item.setWarrantyMileage(entity.getWarrantyMileage());
                item.setPattern(entity.getPattern());
                item.setSrcDocID(this.warrantyMaintenanceRequest.getObjId());
                item.setSrcDocNo(this.warrantyMaintenanceRequest.getDocNo());
                item.setCommissionPartId(entity.getObjId());
                noticeParts.add(item);
            }
        }
        if (noticeParts.size() <= 0) {
            ZkUtils.showExclamation("没有需调拨的物料！", "系统提示");
            return;
        }

        supplyNoticeEntity.setSupplyNoticeItemInfos(noticeParts);
        this.supplyNoticeInfo = supplyNoticeService.save(supplyNoticeEntity);

        this.warrantyMaintenanceRequest.setSupplyNoticeId(supplyNoticeInfo.getObjId());
        warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);
        ZkUtils.showInformation("已生成调拨单,请到调拨单列表中查看和提交。", "系统提示");
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_NOTICE_LIST, null);
        this.updateUIState();

    }

    /**
     * 生成故障件返回通知单
     */
    @Command
    public void generateRecycleNotice() {
        //判断当前登录用户的操作权限

        boolean Permissions = false;
        List<RoleInfo> roles = getActiveUser().getRoles();
        if (roles != null) {
            for (RoleInfo role : roles) {
                if (role.getName().equals("审单员")) {
                    Permissions = true;
                    break;
                }
            }
        }


        if (!Permissions) {
            ZkUtils.showError("你没有此操作权限", "提示");
            return;
        }

        if (StringUtils.isBlank(warrantyMaintenanceRequest.getObjId())) {
            ZkUtils.showError("三包服务单未保存，不能生成故障件返回通知单", "系统提示");
            return;
        }
        WarrantyMaintenanceInfo warrantyMaintenance = warrantyMaintenanceService.findOneById(warrantyMaintenanceRequest.getObjId());

        if (StringUtils.isNotBlank(warrantyMaintenance.getRecycleNoticeId())) {
            ZkUtils.showError("不允许重复生成故障件返回单", "系统提示");
            return;
        }

        List<CommissionPartInfo> parts = warrantyMaintenanceRequest.getCommissionParts();
        //校验待返回故障件配件信息
        for (CommissionPartInfo part : parts) {
            if (StringUtils.isBlank(part.getPartCode())) {
                ZkUtils.showError("配件需求信息为0或者信息不全", "提示");
                return;
            }

            if (part.getAmount().equals(0)) {
                ZkUtils.showError("配件需求信息为0或者信息不全", "提示");
                return;
            }
        }

        RecycleNoticeInfo recycleNoticeEntity = new RecycleNoticeInfo();

        recycleNoticeEntity.setSrcDocNo(warrantyMaintenanceRequest.getDocNo());
        recycleNoticeEntity.setSrcDocType("三包服务单");
        recycleNoticeEntity.setSrcDocID(warrantyMaintenanceRequest.getObjId());
        recycleNoticeEntity.setDealerCode(warrantyMaintenanceRequest.getDealerCode());
        recycleNoticeEntity.setDealerName(warrantyMaintenanceRequest.getDealerName());
        recycleNoticeEntity.setProvinceName(warrantyMaintenanceRequest.getProvinceName());
        recycleNoticeEntity.setSubmitter(getActiveUser().getLogId());
        recycleNoticeEntity.setSubmitterName(getActiveUser().getUsername());
//        supplyNoticeEntity.setServiceManager(warrantyMaintenanceRequest.getServiceManager());
//        supplyNoticeEntity.setOperatorPhone(warrantyMaintenanceRequest.getOperatorPhone());

        List<RecycleNoticeItemInfo> noticeParts = new ArrayList<>();
        for (CommissionPartInfo entity : parts) {
            if (entity.getRecycle()) {
                RecycleNoticeItemInfo part = new RecycleNoticeItemInfo();
                part.setPartCode(entity.getPartCode());
                part.setPartName(entity.getPartName());
                part.setAmount(entity.getAmount());
                part.setWarrantyMileage(entity.getWarrantyMileage());
                part.setWarrantyTime(entity.getWarrantyTime());
                part.setPattern(entity.getPattern());
                part.setReason(entity.getReason());
                part.setCommissionPartId(entity.getObjId());
                noticeParts.add(part);
            }
        }
        if (noticeParts.size() <= 0) {
            ZkUtils.showExclamation("没有需返回的故障件的物料！", "系统提示");
            return;
        }
        recycleNoticeEntity.setRecycleNoticeItemInfoList(noticeParts);
        RecycleNoticeInfo recycleNoticeInfo = this.recycleNoticeService.save(recycleNoticeEntity);
        this.warrantyMaintenanceRequest.setRecycleNoticeId(recycleNoticeInfo.getObjId());
        this.warrantyMaintenanceRequest = warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);
        ZkUtils.showInformation("已生成故障件返回通知单,请到障件返回通知单列表中查看和提交。", "系统提示");
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_NOTICE_LIST, null);
        this.updateUIState();

    }

    /**
     *
     */
    @Command
    public void desertTask() {
        if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getRecycleNoticeId())) {
            ZkUtils.showInformation("已经生成故障件返回单不能作废", "提示");
            return;
        } else if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getSupplyNoticeId())) {
            ZkUtils.showInformation("已经生成了调拨通知单不能作废", "提示");
            return;
        } else {
            ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    warrantyMaintenanceService.desertTask(this.warrantyMaintenanceRequest.getObjId());
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


    /**
     * 打印页面
     */
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.warrantyMaintenanceRequest.getObjId() == null ? "" : this.warrantyMaintenanceRequest.getObjId());
        map.put("printType", "warrantyMaintenance.jasper");
        Window window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/warranty_maintenance_printPage.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();

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
     * 初始化后加载窗体
     *
     * @param view
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        Selectors.wireComponents(view, this, false);
        //win = (org.zkoss.zul.Window) view;
    }

    /**
     * 维修历史页面
     *
     * @param vehicleInfo
     * @param url
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void openMaintenanceHistory(@BindingParam("vehicleInfo") VehicleInfo vehicleInfo, @BindingParam("url") String url, @BindingParam("title") String title) {

        Map<String, Object> paramMap = new HashMap<>();
        if (vehicleInfo.getObjId() != null) {
            paramMap.put("vehicleInfo", vehicleInfo);
        }

        try {
            if (warrantyMaintenanceRequest.getVehicleInfo().getVin() == null) {
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

    /**
     * 选择的单据为配件销售质保时，加载合作商列表
     */
    @Command
    @NotifyChange({"docTypes", "warrantyMaintenanceRequest", "selectAgencyInfo", "agencyInfoList", "isPartWarranty"})
    public void selectDocType() {
        if (StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getDealerCode())) {
            if (!warrantyMaintenanceRequest.getDocType().equals(dictionaryInfo.getName())) {
                isPartWarranty = false;
                agencyInfoList.clear();
                this.setSelectAgencyInfo(null);
                this.warrantyMaintenanceRequest.setAgencyCode("");
                this.warrantyMaintenanceRequest.setAgencyName("");
                this.warrantyMaintenanceRequest.setAgencyId("");
            } else {
                DealerInfo dealerInfo = dealerService.findOneByCode(this.warrantyMaintenanceRequest.getDealerCode());
                if (dealerInfo != null) {
                    agencyInfoList = agencyService.findOneAgencnyById(dealerInfo.getProvinceId());
                }
            }
        } else {
            ZkUtils.showInformation("请先选择服务站", "提示");
        }
    }

    /**
     * 选中的合作商
     *
     * @param event
     */
    @Command
    @NotifyChange({"warrantyMaintenanceRequest", "agencyInfoList"})
    public void selectedAgency(@BindingParam("event") Event event) {
        this.selectAgencyInfo = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        this.warrantyMaintenanceRequest.setAgencyCode(this.selectAgencyInfo.getCode());
        this.warrantyMaintenanceRequest.setAgencyName(this.selectAgencyInfo.getName());
        this.warrantyMaintenanceRequest.setAgencyId(this.selectAgencyInfo.getObjId());
    }

    /**
     * 检查配件编辑状态
     *
     * @return
     */
    public Boolean checkCanEditPart() {
        //草稿状态可以编辑   已经退回状态可以编辑    已经撤回状态可以编辑
        if (warrantyMaintenanceRequest.getStatus().equals(DocStatus.DRAFT.getIndex()) || warrantyMaintenanceRequest.getStatus().equals(DocStatus.REJECT.getIndex()) || warrantyMaintenanceRequest.getStatus().equals(DocStatus.WITHDRAW.getIndex())) {
            if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getSupplyNoticeId()) || !warrantyMaintenanceRequest.getCreaterId().equals(getActiveUser().getUserId())) {
                return false;
            }
            return true;
        }
        return false;
    }


    /**
     * 打印故障件标签
     */
    @Command
    public void printRecycleItemReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.warrantyMaintenanceRequest.getObjId() == null ? "" : this.warrantyMaintenanceRequest.getObjId());
        //map.put("printType", "recycleLabel.jasper");
        window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/warranty_recycle_label.zul", null, map);
        window.setTitle("打印故障件标签");
        window.doModal();
    }


    /**
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return warrantyMaintenanceService.save((WarrantyMaintenanceInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return warrantyMaintenanceService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        this.canShowOpenSupplyNoticeForm = this.warrantyMaintenanceRequest.getSupplyNoticeId() == null ? false : true;
        this.canShowOpenRecycleNoticeForm = this.warrantyMaintenanceRequest.getRecycleNoticeId() == null ? false : true;
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("warrantyMaintenanceRequest", "handle", "canDesertTask", "canHandleTask", "readonly", "canEdit", "canShowOpenRecycleNoticeForm", "canShowOpenSupplyNoticeForm", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_WARRANTY_MAINTENANCE_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    /**
     * 打开来源质量速报速报
     */
    @Command
    public void openSrcQualityReportForm() {
        if (this.warrantyMaintenanceRequest != null && StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getQualityReportId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.warrantyMaintenanceRequest.getQualityReportId();
                if (StringUtils.isNotBlank(srcDocID)) {
                    paramMap.put("objId", srcDocID);
                    paramMap.put("businessId", srcDocID);
                }
                String title = "质量速报";
                url = "/views/asm/quality_report_form.zul";
                ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 打开来源质量速报
     */
    @Command
    public void openSrcExpenseReportForm() {
        if (this.warrantyMaintenanceRequest != null && StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getExpenseReportId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.warrantyMaintenanceRequest.getExpenseReportId();
                if (StringUtils.isNotBlank(srcDocID)) {
                    paramMap.put("objId", srcDocID);
                    paramMap.put("businessId", srcDocID);
                }
                String title = "费用速报";
                url = "/views/asm/expense_report_form.zul";
                ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 打开调拨通知单
     */
    @Command
    public void openSupplyNoticeForm() {
        if (this.warrantyMaintenanceRequest != null && StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getSupplyNoticeId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.warrantyMaintenanceRequest.getSupplyNoticeId();
                if (StringUtils.isNotBlank(srcDocID)) {
                    paramMap.put("objId", srcDocID);
                    paramMap.put("businessId", srcDocID);
                }
                String title = "调拨通知单";
                url = "views/asm/supply_notice_form.zul";
                ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 打开故障件返回通知单
     */
    @Command
    public void openRecycleNoticeForm() {
        if (this.warrantyMaintenanceRequest != null && StringUtils.isNotBlank(this.warrantyMaintenanceRequest.getRecycleNoticeId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.warrantyMaintenanceRequest.getRecycleNoticeId();
                if (StringUtils.isNotBlank(srcDocID)) {
                    paramMap.put("objId", srcDocID);
                    paramMap.put("businessId", srcDocID);
                }
                String title = "返回通知单";
                url = "/views/asm/recycle_notice_form.zul";
                ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 刷新表单
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_WARRANTY_MAINTENANCE_FORM)
    @NotifyChange("warrantyMaintenanceRequest")
    public void refreshForm(@BindingParam("objId") String objId) {
        if (objId.equals(this.warrantyMaintenanceRequest.getObjId())) {
            this.warrantyMaintenanceRequest = warrantyMaintenanceService.findOneById(objId);
            this.updateUIState();
        }
    }

}
