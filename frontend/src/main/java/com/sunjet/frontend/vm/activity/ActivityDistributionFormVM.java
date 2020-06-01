package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.*;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.activity.ActivityDistributionService;
import com.sunjet.frontend.service.activity.ActivityNoticeService;
import com.sunjet.frontend.service.activity.ActivityPartService;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.common.ExcelImport;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 活动分配单
 * Created by Administrator on 2016/10/26.
 */
public class ActivityDistributionFormVM extends FormVM {

    @Getter
    @Setter
    public Map<String, Object> ids = new HashMap<>();
    public DealerInfo dealer = new DealerInfo();
    @Getter
    @Setter
    protected List<DealerInfo> dealers = new ArrayList<>();
    @WireVariable
    private ActivityDistributionService activityDistributionService;
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private ActivityPartService activityPartService;
    @WireVariable
    private SupplyNoticeService supplyNoticeService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private VehicleService vehicleService;
    @Getter
    @Setter
    private ActivityDistributionInfo activityDistributionRequest = new ActivityDistributionInfo();
    @Getter
    @Setter
    private ActivityDistributionItem activityDistributionItem = new ActivityDistributionItem();
    @Getter
    @Setter
    private ActivityNoticeInfo activityNoticeInfo = new ActivityNoticeInfo();
    @Getter
    @Setter
    private List<ActivityNoticeInfo> noticeInfos = new ArrayList<>();
    @Getter
    @Setter
    private List<ActivityPartInfo> activityParts = new ArrayList<>();
    private List<String> vinList = new ArrayList<>();
    @Getter
    @Setter
    private ActivityPartInfo activityPartInfo = new ActivityPartInfo();//配件
    private List<VehicleInfo> vehicles = new ArrayList<>();
    @Getter
    @Setter
    private DealerInfo dealerInfo = new DealerInfo();
    @Getter
    @Setter
    private String province;
    @Getter
    @Setter
    private Integer getVehicleSize;
    private Window dialog;

    @Getter
    @Setter
    private Boolean showImportWin = false;
    @Getter
    private List<List<String>> data = new ArrayList<>();

    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            activityDistributionRequest = activityDistributionService.findOneById(objId);
            dealerInfo = dealerService.findOneById(activityDistributionRequest.getDealer());
            if (dealerInfo != null) {
                activityDistributionRequest.setProvinceName(dealerInfo.getProvinceName());
            }
            if (StringUtils.isNotBlank(this.activityDistributionRequest.getSupplyNoticeId())) {
                this.canShowOpenSupplyNoticeForm = true;
            }
        } else {
            activityDistributionRequest = new ActivityDistributionInfo();
        }

        this.setActiveUserMsg(this.activityDistributionRequest);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange("activityDistributionRequest")
    public void submit() {
        try {
            if (this.activityDistributionRequest.getDealerCode() == null) {
                ZkUtils.showExclamation("请先选择服务站！", "系统提示");
                return;
            }
            if (this.activityDistributionRequest.getActivityNotice() == null) {
                ZkUtils.showExclamation("请先选择活动通知单！", "系统提示");
                return;
            }
            activityDistributionRequest = activityDistributionService.save(activityDistributionRequest);
            this.updateUIState();
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询服务站
     *
     * @param keyword
     */
    @Command
    @Override
    @NotifyChange("dealers")
    public void searchDealers(@BindingParam("model") String keyword) {
        this.dealers = dealerService.searchDealers(keyword, getActiveUser());
    }

    @Command
    @Override
    @NotifyChange({"dealers", "keyword"})
    public void clearSelectedDealer() {
        dealers.clear();
        this.keyword = "";
    }

    /**
     * 选择服务站编号
     */
    @Command
    @NotifyChange({"activityDistributionRequest", "dealer", "province", "keyword"})
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        dealer = model;

        province = model.getProvinceName();
        activityDistributionRequest.setDealer(model.getObjId());
        activityDistributionRequest.setDealerCode(model.getCode());
        activityDistributionRequest.setDealerName(model.getName());
        activityDistributionRequest.setProvinceName(model.getProvinceName());
        activityDistributionRequest.setServiceManager(model.getServiceManagerName());
        this.activityDistributionRequest = this.activityDistributionService.save(this.activityDistributionRequest);
        this.keyword = "";

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
                if (StringUtils.isNotBlank(this.activityDistributionRequest.getSupplyNoticeId())) {
                    SupplyNoticeInfo supplyNotice = this.supplyNoticeService.findByOne(this.activityDistributionRequest.getSupplyNoticeId());
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

                if (startProcessInstance(this.activityDistributionRequest)) {
                    this.activityDistributionRequest = activityDistributionService.save(this.activityDistributionRequest);

                    Map<String, String> map = activityDistributionService.startProcess(this.activityDistributionRequest, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.activityDistributionRequest = (ActivityDistributionInfo) findInfoById(this.activityDistributionRequest.getObjId());
                    flowDocInfo = this.activityDistributionRequest;
                    this.updateUIState();
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });
    }

    /**
     * 校验表单
     *
     * @return
     */
    @Override
    protected Boolean checkValid() {
        if (this.activityDistributionRequest.getDealer() == null) {
            ZkUtils.showInformation("请选择服务站信息！", "提示");
            return false;
        }

        if (this.activityDistributionRequest.getActivityVehicleItems().size() == 0) {
            ZkUtils.showInformation("请添加车辆信息！", "提示");
            return false;
        }

        List<ActivityPartItem> activityPartItems = activityDistributionRequest.getActivityPartItems();
        if (activityPartItems.size() > 0) {
            if (this.activityDistributionRequest.getSupplyNoticeId() == null) {
                ZkUtils.showInformation("请生成调拨单再提交", "提示");
                return false;
            }
        }


        return true;
    }

    /**
     * 选择活动通知单
     */
    @Command
    @NotifyChange({"activityDistributionRequest", "noticeInfo"})
    public void selectActivityNotice(@BindingParam("model") ActivityNoticeInfo notice) {
        if (StringUtils.isNotBlank(this.activityDistributionRequest.getActivityNoticeId()) && StringUtils.isNotBlank(this.activityDistributionRequest.getObjId())) {
            List<ActivityVehicleItem> activityVehicleItems = this.activityVehicleService.findAllByActivityDistributionObjid(this.activityDistributionRequest.getObjId());
            if (activityVehicleItems.size() > 0) {
                for (ActivityVehicleItem activityVehicleItem : activityVehicleItems) {
                    activityVehicleItem.setActivityDistributionId(null);
                    activityVehicleItem.setDistribute(false);
                    this.activityVehicleService.save(activityVehicleItem);
                }
            }

        }
        this.activityNoticeInfo = activityNoticeService.findOneById(notice.getObjId());
        this.activityDistributionRequest.setActivityNoticeId(activityNoticeInfo.getObjId());
        this.activityDistributionRequest.setActivityNotice(this.activityNoticeInfo);
        this.activityDistributionRequest.setActivityPartItems(this.activityNoticeInfo.getActivityPartItems());
        this.activityDistributionRequest = this.activityDistributionService.save(this.activityDistributionRequest);
        this.keyword = "";
        this.noticeInfos.clear();
    }

    @Command
    @NotifyChange("*")
    public void clearSelectedActivityNotice() {
        // Set<ActivityNoticeEntity> activityNoticeEntities=new HashSet<>();
        this.noticeInfos.clear();
        //activityDistributionRequest = new ActivityDistributionEntity();
        activityDistributionRequest.getActivityVehicleItems().clear();
        activityDistributionRequest.setActivityNotice(null);

    }

    @Command
    @NotifyChange("noticeInfos")
    public void searchActivityNotices() {
        this.noticeInfos = activityNoticeService.searchCloseActivityNotices(keyword.trim());
        this.activityDistributionRequest.getActivityVehicleItems().clear();

    }

    /**
     * 添加车辆完成时
     * 关闭窗口
     */
    @NotifyChange({"pageResult", "activityDistributionRequest"})
    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST)
    public void refreshList() {
        activityDistributionRequest = activityDistributionService.findOneById(activityDistributionRequest.getObjId());
        if (dialog != null) {
            dialog.detach();
        }
    }

    /**
     * 打开车辆选择框
     */
    @Command
    public void openSelectVehicleForm() {
        if (StringUtils.isBlank(this.activityDistributionRequest.getObjId())) {
            ZkUtils.showExclamation("请先保存活动分配单！", "系统提示");
            return;
        }
        if (StringUtils.isBlank(this.activityDistributionRequest.getActivityNoticeId())) {
            ZkUtils.showExclamation("请先选着活动通知单！", "系统提示");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        //map.put("vins", this.vinList);
        map.put("activityDistributionId", activityDistributionRequest.getObjId());
        map.put("activityNoticeId", activityDistributionRequest.getActivityNoticeId());
        dialog = (Window) ZkUtils.createComponents("/views/asm/activity_distribution_select_vehicle.zul", null, map);
        map.put("window", dialog);
        dialog.doModal();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        //win = (Window) view;
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
     * 生成活动调拨单
     */
    @Command
    public void generateSupplyNotice() {
        List<ActivityPartItem> parts = this.activityDistributionRequest.getActivityPartItems();
        List<ActivityVehicleItem> vehicles = activityVehicleService.searchActivityVehicleByActivityDistributionId(activityDistributionRequest.getObjId(), null);
        if (parts.size() < 1) {
            ZkUtils.showInformation("没有配件不能生成调拨单", "提示");
            return;
        }
        if (StringUtils.isBlank(activityDistributionRequest.getObjId())) {
            ZkUtils.showError("活动服务单未保存，不能生成调拨单", "系统提示");
            return;
        }
        if (StringUtils.isNotBlank(activityDistributionRequest.getSupplyNoticeId())) {
            ZkUtils.showError("不允许重复生成调拨通知单/供货通知单", "系统提示");
            return;
        }
        if (activityDistributionRequest.getDealer() == null) {
            ZkUtils.showError("请选择服务站", "系统提示");
            return;
        }
        SupplyNoticeInfo supplyNoticeInfo = new SupplyNoticeInfo();
        supplyNoticeInfo.setSrcDocNo(activityDistributionRequest.getDocNo());
        supplyNoticeInfo.setSrcDocType("活动分配单");
        //supplyNoticeInfo.setDocType("活动分配单");
        supplyNoticeInfo.setSrcDocID(activityDistributionRequest.getObjId());
        supplyNoticeInfo.setDealerCode(activityDistributionRequest.getDealerCode());
        supplyNoticeInfo.setDealerName(activityDistributionRequest.getDealerName());
        supplyNoticeInfo.setProvinceName(activityDistributionRequest.getProvinceName());
        supplyNoticeInfo.setServiceManager(activityDistributionRequest.getServiceManager());
        supplyNoticeInfo.setCreaterId(getActiveUser().getLogId());
        supplyNoticeInfo.setCreaterName(getActiveUser().getUsername());
        supplyNoticeInfo.setSubmitter(getActiveUser().getLogId());
        supplyNoticeInfo.setSubmitterName(getActiveUser().getUsername());
        supplyNoticeInfo.setSubmitterPhone(getActiveUser().getPhone());

        List<SupplyNoticeItemInfo> noticeParts = new ArrayList<>();
        for (ActivityPartItem entity : parts) {

            SupplyNoticeItemInfo item = new SupplyNoticeItemInfo();
            item.setPartId(entity.getPartId());
            item.setPartCode(entity.getCode());
            item.setPartName(entity.getName());
            item.setRequestAmount(Double.valueOf(entity.getAmount()) * this.activityDistributionRequest.getActivityVehicleItems().size());
            item.setSurplusAmount(Double.valueOf(entity.getAmount()) * this.activityDistributionRequest.getActivityVehicleItems().size());
            item.setUnit(entity.getUnit());
            item.setSrcDocID(activityDistributionRequest.getObjId());
            item.setSrcDocNo(activityDistributionRequest.getDocNo());
            item.setWarrantyTime(entity.getWarrantyTime());
            item.setCommissionPartId(entity.getObjId());
            item.setWarrantyMileage(entity.getWarrantyMileage());
            noticeParts.add(item);

        }

//        for (SupplyNoticeItemInfo item : noticeParts) {
//            supplyNoticeInfo.addNoticeItem(item);
//        }

//        this.setBaseService(supplyNoticeService);
//        this.setEntity(supplyNoticeInfo);
//        supplyNoticeService.getRepository().save(supplyNoticeInfo);
//        supplyNoticeInfo = (SupplyNoticeInfo) this.saveEntity(supplyNoticeInfo);
        supplyNoticeInfo.setSupplyNoticeItemInfos(noticeParts);
        SupplyNoticeInfo noticeInfo = supplyNoticeService.save(supplyNoticeInfo);

        this.activityDistributionRequest.setSupplyNoticeId(noticeInfo.getObjId());
        this.activityDistributionRequest.setCanEditSupply(false);
        activityDistributionService.save(this.activityDistributionRequest);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_NOTICE_LIST, null);
        ZkUtils.showInformation("已生成调拨单,请到调拨单列表中查看和提交。", "系统提示");
        this.updateUIState();
    }

    /**
     * 生成调拨通知单权限
     *
     * @return
     */
    @Override
    public Boolean checkCanEditSupply() {
        if (this.activityDistributionRequest.getSubmitter().equals(getActiveUser().getLogId()) && activityDistributionRequest.getCanEditSupply()) {
            if ("admin".equals(getActiveUser().getLogId())) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return activityDistributionService.save((ActivityDistributionInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return activityDistributionService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        Map<String, Object> map = new HashMap<>();
        map.put("activityDistributionId", this.activityDistributionRequest.getObjId());
        this.canShowOpenSupplyNoticeForm = this.activityDistributionRequest.getSupplyNoticeId() == null ? false : true;
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("activityDistributionRequest", "handle", "canHandleTask", "readonly", "canEdit", "noticeInfo", "canShowFlowImage", "canShowOpenSupplyNoticeForm"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_DISTRBUTION_LIST, null);
        //BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST, map);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {

        // 获取活动通知单对应的所有ActivityVehicleInfo
        List<ActivityVehicleInfo> vehicleInfos = activityVehicleService.findVehicleListByActibityNoticeId(this.activityDistributionRequest.getActivityNoticeId());

        // 转义后的文件名
        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_OTHER;
        String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
//        this.uploadFilename = event.getMedia().getName();    // 原始文件名


        ExcelImport poi = new ExcelImport();
        data = poi.read(path + fileName);
        Set<String> vins = new HashSet<>();
        if (data != null) {
            for (int i = 1; i < data.size(); i++) {
                vins.add(data.get(i).get(0));
            }
        }

        // 根据上传的 VIN ，获取对应的车辆信息列表
        List<VehicleInfo> vis = vehicleService.findAllByVinIn(new ArrayList<>(vins));
//
        List<ActivityVehicleInfo> infos = new ArrayList<>();


        for (ActivityVehicleInfo avi : vehicleInfos) {
            if (!avi.getDistribute()) {
                for (VehicleInfo vv : vis) {
                    if (avi.getVehicleId().equals(vv.getObjId())) {
                        avi.setDistribute(true);
                        avi.setActivityDistributionId(activityDistributionRequest.getObjId());
                        infos.add(avi);
                        break;
                    }
                }
            }
        }

        if (infos.size() > 0)
            activityVehicleService.saveList(infos);

        ZkUtils.showInformation(String.format("总记录数：%d 条，有效数据：%d 条,无效数据：%d条.", data.size() - 1, infos.size(), data.size() - 1 - infos.size()), "系统提示");
        this.activityDistributionRequest = activityDistributionService.findOneById(this.activityDistributionRequest.getObjId());
        this.updateUIState();
    }


    /**
     * 删除活动分配车辆,解除关系
     */
    @Command
    @NotifyChange("activityDistributionRequest")
    public void deleteEntity(@BindingParam("activityVehicle") ActivityVehicleItem activityVehicleItem) {
        ZkUtils.showQuestion("您确定删除该对象？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                activityDistributionService.deleteActivityDistributionVehicleItem(activityVehicleItem.getObjId());
                this.activityDistributionRequest = activityDistributionService.findOneById(this.activityDistributionRequest.getObjId());
                this.updateUIState();
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });

    }


    /**
     * 打开活动通知表单
     */
    @Command
    public void openActivityNoticeForm() {
        if (this.activityDistributionRequest.getActivityNoticeId() != null && StringUtils.isNotBlank(this.activityDistributionRequest.getActivityNoticeId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.activityDistributionRequest.getActivityNoticeId();
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

    /**
     * 打开调拨通知单
     */
    @Command
    public void openSupplyNoticeForm() {
        if (this.activityDistributionRequest != null && StringUtils.isNotBlank(this.activityDistributionRequest.getSupplyNoticeId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.activityDistributionRequest.getSupplyNoticeId();
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
     * 刷新表单
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_DISTRBUTION_FORM)
    @NotifyChange("activityDistributionRequest")
    public void refreshForm(@BindingParam("objId") String objId) {
        if (objId.equals(this.activityDistributionRequest.getObjId())) {
            this.activityDistributionRequest = activityDistributionService.findOneById(objId);
            this.updateUIState();
        }
    }

}
