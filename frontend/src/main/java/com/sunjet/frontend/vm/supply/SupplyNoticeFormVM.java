package com.sunjet.frontend.vm.supply;

import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.service.supply.SupplyAllocationService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
import com.sunjet.frontend.service.system.UserService;
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
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zsoup.helper.StringUtil;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.sunjet.frontend.utils.common.CommonHelper.FILTER_PARTS_ERROR;
import static com.sunjet.frontend.utils.common.CommonHelper.FILTER_PARTS_LEN;


/**
 * Created by wfb on 17-8-22.
 * 调拨通知单
 */
public class SupplyNoticeFormVM extends FormVM {
    @WireVariable
    private SupplyNoticeService supplyNoticeService;
    @WireVariable
    private SupplyAllocationService supplyAllocationService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private PartService partService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private VehicleService vehicleService;

    //@WireVariable
    //private ActivityDistributionRepository activityDistributionRepository;
    //
    //@WireVariable
    //private ProcessService processService;

    @Getter
    @Setter
    private SupplyNoticeInfo supplyNoticeRequest = new SupplyNoticeInfo();
    @Getter
    @Setter
    private SupplyNoticeItemInfo supplyNoticeItemInfo;
    @Getter
    @Setter
    private List<SupplyNoticeItemInfo> supplyNoticeItemInfos = new ArrayList<>();
    @Getter
    @Setter
    private WarrantyMaintenanceInfo warrantyMaintenanceInfo;    //三包服务单
    @Getter
    @Setter
    private VehicleInfo warrantyMaintenanceVehicle;     //车辆信息
    @Getter
    @Setter
    private List<PartInfo> parts = new ArrayList<PartInfo>();
    @Getter
    @Setter
    private SupplyNoticeItemInfo supplyNoticeItem;
    @Getter
    @Setter
    private Map<String, Object> variables = new HashMap<>();


    @Init(superclass = true)
    public void init() {

        //this.setBaseService(supplyNoticeService);

        if (StringUtils.isNotBlank(this.getObjId())) {    // 有业务对象

            supplyNoticeRequest = supplyNoticeService.findByOne(this.getBusinessId());

            if (supplyNoticeRequest.getSrcDocNo() != null && supplyNoticeRequest.getSrcDocNo() != "") {
                warrantyMaintenanceInfo = warrantyMaintenanceService.findOneWithOthersBySrcDocNo(supplyNoticeRequest.getSrcDocNo());
            }
            if (supplyNoticeRequest.getStatus() > 0) {
                this.setReadonly(true);
            } else {
                this.setReadonly(false);
            }

            if (StringUtils.isNotBlank(this.supplyNoticeRequest.getDealerCode())) {
                DealerInfo dealerInfo = dealerService.findOneByCode(this.supplyNoticeRequest.getDealerCode());
                if (dealerInfo != null) {
                    this.supplyNoticeRequest.setDealerCode(dealerInfo.getCode());
                    this.supplyNoticeRequest.setDealerName(dealerInfo.getName());
                    this.supplyNoticeRequest.setProvinceName(dealerInfo.getProvinceName());
                }
                //默认带入收货人联系电和收货地址为站长电话
                if (StringUtils.isBlank(this.supplyNoticeRequest.getReceive()) || StringUtils.isBlank(this.supplyNoticeRequest.getOperatorPhone())) {
                    this.supplyNoticeRequest.setReceive(dealerInfo.getStationMaster());
                    this.supplyNoticeRequest.setOperatorPhone(dealerInfo.getStationMasterPhone());
                    this.supplyNoticeRequest.setDealerAdderss(dealerInfo.getAddress());
                }
            }

            if (!StringUtil.isBlank(supplyNoticeRequest.getSrcDocNo())) {
                warrantyMaintenanceVehicle = vehicleService.findOne(warrantyMaintenanceInfo.getVehicleId());    //查询车辆信息
            }

            //获取调拨通知单子行明细
            supplyNoticeItemInfos = supplyNoticeService.findByNoticeId(supplyNoticeRequest.getObjId());
            if (supplyNoticeItemInfos != null) {
                for (SupplyNoticeItemInfo supplyNoticeItemInfo : supplyNoticeItemInfos) {
                    if (supplyNoticeItemInfo.getPartId() != null)
                        supplyNoticeItemInfo.setPart(partService.findOne(supplyNoticeItemInfo.getPartId()));    //查找配件信息
                }
                this.supplyNoticeRequest.setSupplyNoticeItemInfos(supplyNoticeItemInfos);
            }

        } else {        // 业务对象和业务对象Id都为空

            this.supplyNoticeRequest = new SupplyNoticeInfo();
            this.supplyNoticeRequest.setSubmitter(getActiveUser().getLogId());
            this.supplyNoticeRequest.setSubmitterName(getActiveUser().getUsername());
            DealerInfo dealerInfo = userService.findOne(getActiveUser().getUserId()).getDealer();
            if (dealerInfo != null) {
                this.supplyNoticeRequest.setDealerCode(dealerInfo.getCode());
                this.supplyNoticeRequest.setDealerName(dealerInfo.getName());
                this.supplyNoticeRequest.setProvinceName(dealerInfo.getProvinceName());
                this.supplyNoticeRequest.setDealerAdderss(dealerInfo.getAddress());
                //默认带入收货人联系电和收货地址为站长电话
                this.supplyNoticeRequest.setReceive(dealerInfo.getStationMaster());
                this.supplyNoticeRequest.setOperatorPhone(dealerInfo.getStationMasterPhone());
            }
        }
        setActiveUserMsg(this.supplyNoticeRequest);
        //this.setReadonly(false);
        //this.setEntity(supplyNoticeRequest);

    }


    /**
     * 新增/保存
     */
    @Command
    @NotifyChange("*")
    public void submit() {
        if (this.supplyNoticeRequest.getStatus() > 0) {
            ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
            return;
        }
        if (this.supplyNoticeRequest.getDealerCode() == null || this.supplyNoticeRequest.getDealerCode() == "") {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        if (this.supplyNoticeRequest.getSupplyNoticeItemInfos().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return;
        }

        this.supplyNoticeRequest = supplyNoticeService.save(this.supplyNoticeRequest);
        this.updateUIState();
        showDialog();
    }


    @Command
    @NotifyChange("supplyNoticeRequest")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        supplyNoticeRequest.setDealerCode(dealer.getCode());
        supplyNoticeRequest.setDealerName(dealer.getName());
        supplyNoticeRequest.setProvinceName(dealer.getProvinceName());
        //supplyNoticeRequest.setOperatorPhone(dealer.getPhone());
        supplyNoticeRequest.setDealerAdderss(dealer.getAddress());
        //supplyNoticeRequest.setReceive(dealer.getName());
        if (dealer.getServiceManagerName() != null)
            supplyNoticeRequest.setServiceManager(dealer.getServiceManagerName());
        //默认带入收货人联系电和收货地址为站长电话
        this.supplyNoticeRequest.setReceive(dealer.getStationMaster());
        this.supplyNoticeRequest.setOperatorPhone(dealer.getStationMasterPhone());
    }


    @Command
    public void selectSupplyNoticePart(@BindingParam("model") SupplyNoticeItemInfo part) {
        this.supplyNoticeItem = part;
    }

    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("parts")
    public void searchParts() {
        if (this.keyword.trim().length() >= FILTER_PARTS_LEN) {
            this.parts = partService.findAllByKeyword(this.keyword.trim());
            if (this.parts.size() < 1) {
                ZkUtils.showInformation("未查询到相关物料！", "提示");
            }
        } else {
            ZkUtils.showInformation(FILTER_PARTS_ERROR, "提示");
            return;
        }
    }

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void selectPart(@BindingParam("model") PartInfo partsEntity) {
        supplyNoticeItem.setPartCode(partsEntity.getCode());
        supplyNoticeItem.setPartName(partsEntity.getName());
        supplyNoticeItem.setPart(partsEntity);
        this.parts.clear();
        this.keyword = "";
    }


    /**
     * 添加物料配件信息
     */
    @Command
    @NotifyChange("supplyNoticeRequest")
    public void addItemModel() {
        SupplyNoticeItemInfo supplyNoticeItemEntity = new SupplyNoticeItemInfo();
        supplyNoticeItemEntity.setArrivalTime(new Date());
        supplyNoticeItemEntity.setSurplusAmount(supplyNoticeItemEntity.getRequestAmount());
        supplyNoticeRequest.getSupplyNoticeItemInfos().add(supplyNoticeItemEntity);
//        supplyNoticeRequest.addNoticeItem(supplyNoticeItemEntity);
    }

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void deleteItem(@BindingParam("model") SupplyNoticeItemInfo partsEntity) {
        for (SupplyNoticeItemInfo item : supplyNoticeRequest.getSupplyNoticeItemInfos()) {
            if (item == partsEntity) {
                supplyNoticeRequest.getSupplyNoticeItemInfos().remove(item);
                return;
            }
        }
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
     * 启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        //checkValid();
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.supplyNoticeRequest)) {
                    this.supplyNoticeRequest = supplyNoticeService.save(this.supplyNoticeRequest);
                    flowDocInfo = this.supplyNoticeRequest;
                    Map<String, String> map = supplyNoticeService.startProcess(this.supplyNoticeRequest, getActiveUser());
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
     * 校验表单信息
     *
     * @return
     */
    @Override
    protected Boolean checkValid() {
        if (StringUtil.isBlank(this.supplyNoticeRequest.getDealerCode())) {
            ZkUtils.showInformation("请填写服务站", "提示");
            return false;
        }
        if (StringUtil.isBlank(this.supplyNoticeRequest.getReceive())) {
            ZkUtils.showInformation("请填写收货人", "提示");
            return false;
        }
        if (StringUtil.isBlank(this.supplyNoticeRequest.getDealerAdderss())) {
            ZkUtils.showInformation("请填写收货地址", "提示");
            return false;
        }
        if (StringUtil.isBlank(this.supplyNoticeRequest.getOperatorPhone())) {
            ZkUtils.showInformation("请填写联系电话", "提示");
            return false;
        }
        for (SupplyNoticeItemInfo supplyNoticeItem : this.supplyNoticeRequest.getSupplyNoticeItemInfos()) {
            if (supplyNoticeItem.getRequestAmount().equals(0.0)) {

                ZkUtils.showInformation("配件数量小于0,不能提交", "提示");
                return false;
            } else if (supplyNoticeItem.getArrivalTime() == null) {
                ZkUtils.showInformation("请选择要求到货时间", "提示");
                return false;
            }

        }
        if (this.supplyNoticeRequest.getSupplyNoticeItemInfos().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }


        return true;
    }

    /**
     * 打印页面
     */
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.supplyNoticeRequest.getObjId() == null ? "" : this.supplyNoticeRequest.getObjId());
        Window window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/supply_notice_printPage.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();

    }


    /**
     * 作废
     */
    @Command
    public void desertTask(@BindingParam("tabs") List<Tab> tabList) {
        ZkUtils.showQuestion(CommonHelper.DESERT_TASK_MESSAGE, "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                ZkTabboxUtil.closeOneByObjId(tabList, this.supplyNoticeRequest.getObjId());
                ZkTabboxUtil.closeOneByObjId(tabList, this.supplyNoticeRequest.getSrcDocID());
                boolean desertTask = supplyNoticeService.desertTask(this.supplyNoticeRequest.getObjId());
                if (desertTask) {
                    canHandleTask = false;
                    canDesertTask = false;
                    canEdit = false;
                    this.updateUIState();
                    showDialog();
                } else {
                    ZkUtils.showInformation("此单据不能作废", "提示");
                }

            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消作废", "提示");
            }
        });

    }

    @Override
    public boolean checkCanDesert() {
        if ((this.flowDocInfo.getStatus().equals(DocStatus.REJECT.getIndex()) || this.flowDocInfo.getStatus().equals(DocStatus.CLOSED.getIndex()) || this.flowDocInfo.getStatus().equals(DocStatus.WITHDRAW.getIndex()))
                && getActiveUser().getLogId().equals(this.flowDocInfo.getSubmitter())) {
            for (SupplyNoticeItemInfo noticeItemInfo : this.supplyNoticeRequest.getSupplyNoticeItemInfos()) {
                if (noticeItemInfo.getSentAmount() > 0) {
                    return false;
                } else {
                    canDesertTask = true;
                }
            }
        } else {
            canDesertTask = false;
        }
        return canDesertTask;
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
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return supplyNoticeService.save((SupplyNoticeInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return supplyNoticeService.findByOne(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("supplyNoticeRequest", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_NOTICE_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm() {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        try {

            String srcDocID = this.supplyNoticeRequest.getSrcDocID();
            if (StringUtils.isNotBlank(srcDocID)) {
                paramMap.put("objId", srcDocID);
                paramMap.put("businessId", srcDocID);
            }
            String title = this.supplyNoticeRequest.getSrcDocType();
            if ("三包服务单".equals(title)) {
                url = "/views/asm/warranty_maintenance_form.zul";
            } else if ("活动分配单".equals(title)) {
                url = "/views/asm/activity_distribution_form.zul";
            }

            ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 打开来源质量速报速报
     */
    @Command
    public void openSrcQualityReportForm() {
        if (warrantyMaintenanceInfo != null && StringUtils.isNotBlank(warrantyMaintenanceInfo.getQualityReportId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.warrantyMaintenanceInfo.getQualityReportId();
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
     * 打开来源质量速报速报
     */
    @Command
    public void openSrcExpenseReportForm() {
        if (warrantyMaintenanceInfo != null && StringUtils.isNotBlank(warrantyMaintenanceInfo.getExpenseReportId())) {
            Map<String, Object> paramMap = new HashMap<>();
            String url = "";
            try {
                String srcDocID = this.warrantyMaintenanceInfo.getExpenseReportId();
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
}
