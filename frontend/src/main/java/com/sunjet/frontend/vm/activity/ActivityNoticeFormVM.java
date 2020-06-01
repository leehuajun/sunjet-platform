package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.*;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.activity.ActivityNoticeService;
import com.sunjet.frontend.service.activity.ActivityPartService;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.utils.common.MathHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 服务活动通知单 表单 VM
 */
public class ActivityNoticeFormVM extends FormVM {

    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private ActivityPartService activityPartService;
    @WireVariable
    private PartService partService;
    @WireVariable
    private VehicleService vehicleService;
    @Getter
    @Setter
    private ActivityPartItem activityPartItem = new ActivityPartItem();//配件
    @Getter
    @Setter
    private List<PartInfo> partList = new ArrayList<>();

    @Getter
    @Setter
    private ActivityNoticeInfo activityNoticeInfo = new ActivityNoticeInfo();

    @Getter
    @Setter
    private Boolean enableAdd = false;  //新增按钮权限
    @Getter
    @Setter
    private Boolean enableUpdate = false;      // 编辑按钮状态
    @Getter
    @Setter
    private Boolean enableDelete = false;      // 删除按钮状态

    private Window dialog;

    @Getter
    @Setter
    public Map<String, Object> ids = new HashMap<>();

    @Getter
    private List<ActivityVehicleInfo> vehicleInfos = new ArrayList<>();


    @Getter
    @Setter
    private Boolean canDisplay = true; // 判断单据类型是否显示车辆分配状态
    @Getter
    @Setter
    private Boolean isSelectPart = false;

    private Object vm;

    @Init(superclass = true)
    public void init() {
        //按钮判断
        this.setEnableAdd(hasPermission("ActivityNoticeEntity:create"));
        this.setEnableUpdate(hasPermission("ActivityNoticeEntity:modify"));
        this.setEnableDelete(hasPermission("ActivityNoticeEntity:delete"));
        if (StringUtils.isNotBlank(objId)) {
            activityNoticeInfo = activityNoticeService.findOneById(objId);
        } else {
            activityNoticeInfo = new ActivityNoticeInfo();
        }

        this.setActiveUserMsg(this.activityNoticeInfo);
        vm = this;
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange("activityNoticeInfo")
    public void submit() {
        try {
            activityNoticeInfo = activityNoticeService.save(activityNoticeInfo);
            this.updateUIState();
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * 打开车辆选择框
     */
    @Command
    public void openSelectVehicleForm() {
        if (activityNoticeInfo.getObjId() == null) {
            ZkUtils.showExclamation("请先保存活动通知单！", "系统提示");
            return;
        }
//        this.vinList.clear();
//        this.vehicles.clear();
//
//        if(activityNoticeInfo.getObjId()!=null) {
//            activityNoticeInfo.setActivityVehicleInfos(activityVehicleService.findVehicleListByActibityNoticeId(activityNoticeInfo.getObjId()));
//        }
//        if(this.activityNoticeInfo.getActivityVehicleInfos()!=null && this.activityNoticeInfo.getActivityVehicleInfos().size()>0) {
//            for (ActivityVehicleInfo av : this.activityNoticeInfo.getActivityVehicleInfos()) {
//                vehicleInfo = vehicleService.findOne(av.getVehicleId());
//                this.vinList.add(vehicleInfo.getVin());
//            }
//        }


        Map<String, Object> map = new HashMap<>();
        List<String> vins = this.activityNoticeInfo.getActivityVehicleItems().stream().map(v -> v.getVin()).collect(Collectors.toList());
        map.put("vins", vins);
        map.put("activityNoticeId", this.activityNoticeInfo.getObjId());
        dialog = (Window) ZkUtils.createComponents("/views/asm/activity_notice_select_vehicle.zul", null, map);
        dialog.doModal();
    }

    @Command
    public void openImportVehicleForm() {
        if (activityNoticeInfo.getObjId() == null) {
            ZkUtils.showExclamation("请先保存活动通知单！", "系统提示");
            return;
        }
//        this.vinList.clear();
//        this.vehicles.clear();
//        if(activityNoticeInfo.getObjId()!=null) {
//            activityNoticeInfo.setActivityVehicleInfos(activityVehicleService.findVehicleListByActibityNoticeId(activityNoticeInfo.getObjId()));
//        }
//        if(this.activityNoticeInfo.getActivityVehicleInfos()!=null && this.activityNoticeInfo.getActivityVehicleInfos().size()>0) {
//            for (ActivityVehicleInfo av : this.activityNoticeInfo.getActivityVehicleInfos()) {
//                vehicleInfo = vehicleService.findOne(av.getVehicleId());
//                this.vinList.add(vehicleInfo.getVin());
//            }
//        }
        Map<String, Object> map = new HashMap<>();
        List<String> vins = this.activityNoticeInfo.getActivityVehicleItems().stream().map(v -> v.getVin()).collect(Collectors.toList());
        map.put("vins", vins);
        map.put("activityNoticeId", this.activityNoticeInfo.getObjId());
        dialog = (Window) ZkUtils.createComponents("/views/asm/activity_notice_import_vehicle.zul", null, map);
        dialog.doModal();
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

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        //win = (Window) view;
    }

    /**
     * 计算总人工成本
     */
    @Command
    @NotifyChange({"activityNoticeInfo"})
    public void computeCost() {

        if (activityNoticeInfo.getObjId() == null) {
            ZkUtils.showExclamation("请先保存活动通知单！", "系统提示");
            activityNoticeInfo.setPerLaberCost(0.0);
            return;
        }
        this.activityNoticeInfo.setPerLaberCost(this.activityNoticeInfo.getPerLaberCost());

        this.activityNoticeInfo.setPerPartCost(0.0);

        //总共有多少台车
        Integer vehicleSize = activityVehicleService.findCountVehicleByActivityNoticeId(activityNoticeInfo.getObjId());

        // 总人工成本
        Double amountLabelCost = vehicleSize * this.activityNoticeInfo.getPerLaberCost();
        this.activityNoticeInfo.setAmountLaberCost(amountLabelCost);

        // 计算单台车使用的配件成本
        Double perPartCost = 0.0;
        for (ActivityPartItem partItem : this.activityNoticeInfo.getActivityPartItems()) {
            if (partItem.getCode() != null) {
                if (partItem.getAmount() == null) {
                    partItem.setAmount(0);
                }
                perPartCost = perPartCost + partItem.getAmount() * partItem.getPrice();
            }
        }
        this.activityNoticeInfo.setPerPartCost(perPartCost);

        // 计算总配件成本
        Double amountPartCost = vehicleSize * perPartCost;
        this.activityNoticeInfo.setAmountPartCost(amountPartCost);
//        this.activityNoticeInfo.set(activityNoticeInfo.getActivityPartInfos());

        // 计算  总成本 = 总人工成本 + 总配件成本
        this.activityNoticeInfo.setAmount(
                MathHelper.getDoubleAndTwoDecimalPlaces(this.activityNoticeInfo.getAmountLaberCost()
                        + this.activityNoticeInfo.getAmountPartCost()));


        this.activityNoticeInfo = this.activityNoticeService.save(this.activityNoticeInfo);

    }

    @Command
    @NotifyChange("*")
    public void updatePartAmount(@BindingParam("model") ActivityPartItem partItem) {
        ActivityPartInfo info = activityPartService.findOneById(partItem.getObjId());
        info.setAmount(partItem.getAmount());
        activityPartService.save(info);
        computeCost();
    }

    /**
     * 添加车辆时
     * 刷新总人工成本并且关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST)
    @NotifyChange({"activityNoticeInfo"})
    public void refreshList() {
//        activityNoticeInfo = activityNoticeService.save(activityNoticeInfo);
        computeCost();
        if (dialog != null) {
            dialog.detach();
        }
    }

    /**
     * 删除车辆时
     * 刷新总人工承办并且关闭窗口
     */
//    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_FORM)
    @Command
    @NotifyChange("activityNoticeInfo")
    public void deleteActivityVehicle(@BindingParam("model") ActivityVehicleItem activityVehicleItem) {

        ZkUtils.showQuestion(String.format("确定删除车辆[%s]?", activityVehicleItem.getVin()), "询问", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    activityNoticeInfo.getActivityVehicleItems().remove(activityVehicleItem);
                    activityVehicleService.deleteActivityVehicleById(activityVehicleItem.getObjId());
                    computeCost();
                    BindUtils.postNotifyChange(null, null, vm, "activityNoticeInfo");
//                    BindUtils.postGlobalCommand(null,null,GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST,null);
                } else {
                    return;
                }
            }
        });

    }

    /**
     * 添加物料
     */
    @Command
    @NotifyChange({"activityNoticeInfo", "isSelectPart"})
    public void addPart() {
        if (activityNoticeInfo.getObjId() == null) {
            ZkUtils.showExclamation("请先保存活动通知单！", "系统提示");
            return;
        }
        isSelectPart = true;
    }

    /**
     * 选中配件行
     *
     * @param noticePart
     */
    @Command
    public void selectNoticePartItem(@BindingParam("model") ActivityPartItem noticePart) {
        this.activityPartItem = noticePart;
    }

    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("partList")
    public void searchParts() {
        if (this.keyword.trim().length() >= CommonHelper.FILTER_PARTS_LEN) {
            this.partList = partService.findAllByKeyword(this.keyword.trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_PARTS_ERROR, "提示");
        }
    }

    /**
     * 选中配件
     *
     * @param partInfo
     */
    @Command
    @NotifyChange({"activityNoticeInfo", "isSelectPart"})
    public void selectPart(@BindingParam("model") PartInfo partInfo) {

        ActivityPartInfo activityPartInfo = new ActivityPartInfo();
        activityPartInfo.setActivityNoticeId(this.activityNoticeInfo.getObjId());
        activityPartInfo.setPartId(partInfo.getObjId());
        activityPartInfo.setAmount(1);


        this.activityPartService.save(activityPartInfo);

        ActivityPartItem item = new ActivityPartItem();

        item.setPartId(partInfo.getObjId());
        item.setCode(partInfo.getCode());
        item.setName(partInfo.getName());
        item.setUnit(partInfo.getUnit());
        item.setPrice(partInfo.getPrice());
        item.setAmount(activityPartInfo.getAmount());
        item.setWarrantyTime(partInfo.getWarrantyTime());
        item.setWarrantyMileage(partInfo.getWarrantyMileage());
        item.setActivityNoticeId(activityNoticeInfo.getObjId());
        this.activityNoticeInfo.getActivityPartItems().add(item);
        computeCost();
//        this.activityNoticeInfo = this.activityNoticeService.save(this.activityNoticeInfo);
        this.keyword = "";
        this.partList.clear();

        this.isSelectPart = false;

//        this.activityNoticeInfo = this.activityNoticeService.findOneById(this.activityNoticeInfo.getObjId());
    }

    /**
     * 删除配件行
     *
     * @param partItem
     */
    @Command
    @NotifyChange("*")
    public void deletePart(@BindingParam("model") ActivityPartItem partItem) {
        this.activityNoticeInfo.getActivityPartItems().remove(partItem);
        activityPartService.deleteByObjId(partItem.getObjId());
        computeCost();

    }

    /**
     * 上传图片
     *
     * @param event
     */
    @Command
    @NotifyChange("activityNoticeInfo")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
        this.activityNoticeInfo.setNoticeFile(fileName);
    }

    /**
     * 删除图片
     */
    @Command
    @NotifyChange("activityNoticeInfo")
    public void delUploadFile() {
        this.activityNoticeInfo.setNoticeFile("");
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
                if (startProcessInstance(this.activityNoticeInfo)) {
                    this.activityNoticeInfo = activityNoticeService.save(this.activityNoticeInfo);
                    Map<String, String> map = activityNoticeService.startProcess(this.activityNoticeInfo, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.activityNoticeInfo = (ActivityNoticeInfo) findInfoById(this.activityNoticeInfo.getObjId());
                    this.flowDocInfo = this.activityNoticeInfo;
                    this.updateUIState();
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });
    }

    @Override
    protected Boolean checkValid() {
        if (this.activityNoticeInfo.getTitle() == null || this.activityNoticeInfo.getTitle().trim().length() < 1) {
            ZkUtils.showInformation("请填写标题信息！", "提示");
            return false;
        }

        if (this.activityNoticeInfo.getStartDate().getTime() > this.activityNoticeInfo.getEndDate().getTime()) {
            ZkUtils.showInformation("开始时间不能大于结束时间", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.activityNoticeInfo.getNoticeFile())) {
            ZkUtils.showInformation("活动文件未上传", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.activityNoticeInfo.getTitle())
                || StringUtils.isBlank(this.activityNoticeInfo.getComment())) {
            ZkUtils.showInformation("请填写基本信息！", "提示");
            return false;
        }
        if (this.activityNoticeInfo.getActivityVehicleItems().size() < 1) {
            ZkUtils.showInformation("请添加车辆！", "提示");
            return false;
        }
        //if (this.activityNoticeInfo.getActivityPartInfos().size() > 0) {
        //    for (ActivityPartInfo partEntity : this.activityNoticeInfo.getActivityPartInfos()) {
        //        if (partEntity.getPart() == null) {
        //            this.activityNoticeRequest.getActivityParts().remove(partEntity);
        //            activityNoticeService.save(this.activityNoticeRequest);
        //        }
        //    }
        //}
        return true;
    }


    /**
     * 打印页面
     */
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.activityNoticeInfo.getObjId() == null ? "" : this.activityNoticeInfo.getObjId());
        map.put("printType", "warrantyMaintenance.jasper");
        Window window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/activity_notice_printPage.zul", null, map);
        window.setTitle("打印表单");
        window.doModal();
    }

    /**
     * 检查打印按钮
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
                this.activityNoticeService.desertTask(this.activityNoticeInfo.getObjId());
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
        return activityNoticeService.save((ActivityNoticeInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return activityNoticeService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        Map<String, Object> map = new HashMap<>();
        map.put("activityNoticeId", this.activityNoticeInfo.getObjId());
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("activityNoticeInfo", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_NOTICE_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST, map);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


}


