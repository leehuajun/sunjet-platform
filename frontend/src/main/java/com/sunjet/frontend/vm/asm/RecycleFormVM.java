package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.recycle.*;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.recycle.RecycleItemService;
import com.sunjet.frontend.service.recycle.RecycleNoticeItemService;
import com.sunjet.frontend.service.recycle.RecycleService;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 故障件返回单 表单 VM
 */
public class RecycleFormVM extends FormVM {

    @WireVariable
    private RecycleService recycleService;
    @WireVariable
    private RecycleItemService recycleItemService;  //故障件返回单子行 service
    @WireVariable
    protected DealerService dealerService;      // 服务站服务
    @WireVariable
    private UserService userService;
    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;  //故障件返回通知单子行 service
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;

    @Getter
    @Setter
    private RecycleInfo recycleRequest = new RecycleInfo(); //故障件返回单vo
    @Getter
    @Setter
    private RecycleItemInfo recycleItemInfo;    //返回单子行vo

    @Getter
    @Setter
    private List<RecycleItemInfo> recycleItemInfoList;

    //@Getter
    //@Setter
    //private List<RecycleItemInfo> byRecyclePartList;


    @Getter
    @Setter
    private DealerInfo dealerInfo;  //服务站
    @Getter
    @Setter
    private Window window;

    //private Double expenseTotal;


    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            this.recycleRequest = recycleService.findOneById(objId);
            this.setDealerInfo(dealerService.findOneByCode(this.recycleRequest.getDealerCode()));

            //this.recycleItemInfoList = recycleItemService.findByRecyclePartList(objId);
            //recycleRequest.setRecycleItemInfoList(this.recycleItemInfoList);

        } else {
            recycleRequest = new RecycleInfo();
            DealerInfo dealerEntity = getActiveUser().getDealer();
            //this.setReadonly(false);
            if (dealerEntity != null) {
                this.recycleRequest.setDealerCode(dealerEntity.getCode());
                this.recycleRequest.setDealerName(dealerEntity.getName());
                this.recycleRequest.setProvinceName(dealerEntity.getProvinceName());
                this.recycleRequest.setServiceManager(dealerEntity.getServiceManagerName());
                this.recycleRequest.setOperatorPhone(dealerEntity.getPhone());
            }
            updateSelectedRecycleNoticeList();
        }
        setActiveUserMsg(this.recycleRequest);
    }


    @Command
    @NotifyChange("*")
    public void submit() {
        for (RecycleItemInfo item : recycleRequest.getRecycleItemInfoList()) {
            if (item.getBackAmount() > item.getWaitAmount()) {
                ZkUtils.showInformation("返回数量不能大于应返回数量", "错误");
                return;
            } else if (item.getBackAmount() < 1) {
                ZkUtils.showInformation("返回数量不能小于1", "错误");
                return;
            }
        }

        changeCount();
        showDialog();
    }


    public void changeCount() {

        // 如果objid为空，表示新增
        if (StringUtils.isBlank(this.recycleRequest.getObjId())) {
            this.recycleRequest = this.recycleService.save(this.recycleRequest);
        } else {
            //获取历史配件
            List<RecycleItemInfo> oldRecyclePartList = this.recycleItemService.findByRecyclePartList(this.recycleRequest.getObjId());

            if (oldRecyclePartList != null && oldRecyclePartList.size() > 0) {
                for (RecycleItemInfo itemInfo : oldRecyclePartList) {
                    RecycleNoticeItemInfo oldRecycleNoticeItemInfo = recycleNoticeItemService.findOneByObjid(itemInfo.getNoticeItemId());
                    //返回单已发数量回写    已发数量 = 返回单已发数量 - 故障件本次已返回数量
                    oldRecycleNoticeItemInfo.setBackAmount(oldRecycleNoticeItemInfo.getBackAmount() - itemInfo.getBackAmount());
                    //返回单未返回数量回写   未返回数量 = 未返回数量 + 故障件本次已返回数量
                    oldRecycleNoticeItemInfo.setCurrentAmount(oldRecycleNoticeItemInfo.getCurrentAmount() + itemInfo.getBackAmount());
                    recycleNoticeItemService.save(oldRecycleNoticeItemInfo);
                }
            }
            //更新更改数据
            if (this.recycleRequest.getRecycleItemInfoList() != null && this.recycleRequest.getRecycleItemInfoList().size() > 0) {
                for (RecycleItemInfo itemInfo : this.recycleRequest.getRecycleItemInfoList()) {
                    //更新故障件返回通知单子行配件数量
                    RecycleNoticeItemInfo recycleNoticeItem = this.recycleNoticeItemService.findOneByObjid(itemInfo.getNoticeItemId());

                    //已返回数量 = 历史已返回数量+已返回数量
                    recycleNoticeItem.setBackAmount(recycleNoticeItem.getBackAmount() + itemInfo.getBackAmount());

                    //未返回数量=应返回数量-已返数量
                    recycleNoticeItem.setCurrentAmount(recycleNoticeItem.getAmount() - recycleNoticeItem.getBackAmount());

                    this.recycleNoticeItemService.save(recycleNoticeItem);
                }

            }
            this.recycleRequest = this.recycleService.save(this.recycleRequest);

        }
        this.updateUIState();


    }


    /**
     * 初始化后加载窗体
     *
     * @param view
     */
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

    @Command
    @NotifyChange("recycleRequest")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        this.dealerInfo = dealer;
        this.recycleRequest.setDealerCode(dealer.getCode());
        this.recycleRequest.setDealerName(dealer.getName());
    }

    /**
     * 添加故障件配件
     */
    @Command
    @NotifyChange("recycleRequest")
    public void addItemModel() {
        if (StringUtils.isBlank(this.recycleRequest.getDealerCode())) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", 101010);
        map.put("dealerCode", this.recycleRequest.getDealerCode());
        window = (Window) ZkUtils.createComponents("/views/asm/select_recycle_form.zul", null, map);
        window.setTitle("选择故障件");
        window.doModal();

    }

    /**
     * 删除故障件
     */
    @Command
    @NotifyChange("recycleRequest")
    public void deleteItem(@BindingParam("model") RecycleItemInfo model) {

        Iterator<RecycleItemInfo> iterator = this.recycleRequest.getRecycleItemInfoList().iterator();
        while (iterator.hasNext()) {
            RecycleItemInfo recycleItemInfo = iterator.next();
            if (recycleItemInfo == model) {
                iterator.remove();
            }
        }
        //检查故障件返回单配件是否还存在,避免标签打开两个重复删除
        RecycleItemInfo recycleItem = this.recycleItemService.findOne(model.getObjId());
        if (recycleItem != null) {
            //this.recycleRequest.getRecycleItemInfoList().remove(model);
            //更新故障件返回通知单子行配件数量
            RecycleNoticeItemInfo recycleNoticeItem = this.recycleNoticeItemService.findOneByObjid(model.getNoticeItemId());

            //已返回数量 = 历史已返回数量 - 已返回数量
            recycleNoticeItem.setBackAmount(recycleNoticeItem.getBackAmount() - recycleItem.getBackAmount());

            //未返回数量=应返回数量+已返数量
            recycleNoticeItem.setCurrentAmount(recycleNoticeItem.getAmount() + recycleNoticeItem.getBackAmount());

            this.recycleNoticeItemService.save(recycleNoticeItem);

            if (StringUtils.isNotBlank(model.getObjId())) {
                this.recycleItemService.delete(model.getObjId());
            }
        }

        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_NOTICE_PENDING, null);
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
        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
        if (type.equalsIgnoreCase("file01")) {
            this.recycleRequest.setRecyclefile(fileName);
            this.recycleRequest.setRecyclefileName(event.getMedia().getName());
        } else if (type.equalsIgnoreCase("file02")) {
            this.recycleRequest.setLogisticsfile(fileName);
            this.recycleRequest.setLogisticsfileName(event.getMedia().getName());
        }
    }

    /**
     * 删除上传文件
     *
     * @param type
     */
    @Command
    @NotifyChange("recycleRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("file01")) {
            this.recycleRequest.setRecyclefile("");
        } else if (type.equalsIgnoreCase("file02")) {
            this.recycleRequest.setLogisticsfile("");
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
     * 选择故障件返回单配件（点击确定按钮时）
     *
     * @param recycleNoticeItemsList
     */
    @GlobalCommand
    @NotifyChange("recycleRequest")
    public void updateSelectedRecycleList(@BindingParam("recycleNoticeItem") List<RecycleNoticePendingInfo> recycleNoticeItemsList) {
        if (recycleNoticeItemsList != null || recycleNoticeItemsList.size() > 0) {
            for (RecycleNoticePendingInfo item : recycleNoticeItemsList) {
                RecycleItemInfo recycleItem = new RecycleItemInfo();
                recycleItem.setPartCode(item.getPartCode());
                recycleItem.setPartName(item.getPartName());
                recycleItem.setSrcDocType(item.getSrcDocType());
                recycleItem.setSrcDocNo(item.getSrcDocNo());
                recycleItem.setLogisticsNum(recycleRequest.getLogisticsNum());
                recycleItem.setWarrantyTime(item.getWarrantyTime());
                recycleItem.setWarrantyMileage(item.getWarrantyMileage());
                recycleItem.setWaitAmount(item.getAmount() - item.getBackAmount());  // 还剩应返回的数量
                recycleItem.setBackAmount(item.getAmount() - item.getBackAmount());
                recycleItem.setReturnDate(item.getReturnDate());
                recycleItem.setComment(item.getComment());
                recycleItem.setPattern(item.getPattern());
                recycleItem.setReason(item.getReason());
                recycleItem.setNoticeItemId(item.getObjId());

                //recycleRequest.getRecycleItemInfoList().add(recycleItem);
                this.recycleRequest.getRecycleItemInfoList().add(recycleItem);


                //更新故障件返回通知单子行配件数量
                RecycleNoticeItemInfo recycleNoticeItem = this.recycleNoticeItemService.findOneByObjid(item.getObjId());
                //已返回数量 = 历史已返回数量+未返回数量
                recycleNoticeItem.setBackAmount(item.getBackAmount() + item.getCurrentAmount());

                //未返回数量=应返回数量-已返数量
                recycleNoticeItem.setCurrentAmount(item.getAmount() - recycleNoticeItem.getBackAmount());
                this.recycleNoticeItemService.save(recycleNoticeItem);
            }
            this.recycleRequest = this.recycleService.save(this.recycleRequest);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_NOTICE_PENDING, null);
        }

        if (window != null) {
            window.detach();
        }
    }

    //待返推送更新
    @NotifyChange("*")
    public void updateSelectedRecycleNoticeList() {
        //得到待返配件
        List<RecycleNoticePendingItem> recycleNoticeItemsList = (List<RecycleNoticePendingItem>) Executions.getCurrent().getArg().get("recycleNoticeItem");
        if (recycleNoticeItemsList != null) {
            this.recycleRequest.getRecycleItemInfoList().clear();
            for (RecycleNoticePendingItem item : recycleNoticeItemsList) {
                RecycleItemInfo recycleItem = new RecycleItemInfo();
                recycleItem.setPartCode(item.getPartCode());   // 配件号
                recycleItem.setPartName(item.getPartName());    // 配给名称
                recycleItem.setWaitAmount(item.getAmount() - item.getBackAmount());  // 还剩应返回的数量
                recycleItem.setBackAmount(item.getAmount() - item.getBackAmount());  // 还剩应返回的数量
                recycleItem.setAcceptAmount(recycleItem.getBackAmount());
                recycleItem.setSrcDocNo(item.getSrcDocNo());   // 来源单据编号
                recycleItem.setSrcDocType(item.getSrcDocType()); // 单据来源类型
                recycleItem.setLogisticsNum(recycleRequest.getLogisticsNum());  // 物流号码
                recycleItem.setWarrantyTime(item.getWarrantyTime());        // 三包时间
                recycleItem.setWarrantyMileage(item.getWarrantyMileage()); // 三包里程
                recycleItem.setPattern(item.getPattern());  // 故障模式
                recycleItem.setReason(item.getReason());  //   换件原因
                recycleItem.setReturnDate(item.getReturnDate());   // 要求返回时间
                recycleItem.setNoticeItemId(item.getObjId());
                this.recycleRequest.setDealerCode(item.getDealerCode());   // 服务站编号
                this.recycleRequest.setDealerName(item.getDealerName());  //  服务站名称
                this.recycleRequest.getRecycleItemInfoList().add(recycleItem);
                //更新故障件返回通知单子行配件数量
                RecycleNoticeItemInfo recycleNoticeItem = this.recycleNoticeItemService.findOneByObjid(item.getObjId());
                //已返回数量 = 历史已返回数量+未返回数量
                recycleNoticeItem.setBackAmount(item.getBackAmount() + item.getCurrentAmount());

                //未返回数量=应返回数量-已返数量
                recycleNoticeItem.setCurrentAmount(item.getAmount() - recycleNoticeItem.getBackAmount());
                this.recycleNoticeItemService.save(recycleNoticeItem);

            }
            setActiveUserMsg(this.recycleRequest);
            this.recycleRequest = this.recycleService.save(this.recycleRequest);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_NOTICE_PENDING, null);
        } else {
            return;
        }

    }


    @Command
    @NotifyChange("*")
    public void changeLogisticsNum() {
        for (RecycleItemInfo item : this.recycleRequest.getRecycleItemInfoList()) {
            item.setLogisticsNum(this.recycleRequest.getLogisticsNum());
        }
    }

    /**
     * 打印页面
     */
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.recycleRequest.getObjId() == null ? "" : this.recycleRequest.getObjId());
        //map.put("printType", "recyclePrintPage.jasper");
        Window window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/recycle_printPage.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();

    }

    /**
     * 打印故障件标签
     */
    @Command
    public void printRecycleItemReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.recycleRequest.getObjId() == null ? "" : this.recycleRequest.getObjId());
        //map.put("printType", "recycleLabel.jasper");
        window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/recycle_label.zul", null, map);
        window.setTitle("打印故障件标签");
        window.doModal();
    }


    /**
     * 计算运费
     */
    @Command
    @NotifyChange("recycleRequest")
    public void changeExpense() {
        if (this.recycleRequest.getOtherExpense() == null) {
            this.recycleRequest.setOtherExpense(0.0);
        }
        if (this.recycleRequest.getTransportExpense() == null) {
            this.recycleRequest.setTransportExpense(0.0);
        }
        recycleRequest.setExpenseTotal(recycleRequest.getOtherExpense() + recycleRequest.getTransportExpense());
    }


    /**
     * 校验
     *
     * @return
     */
    @Override
    protected Boolean checkValid() {

        if (StringUtils.isBlank(this.recycleRequest.getDealerCode())) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (this.recycleRequest.getArriveDate() != null) {
            if (this.recycleRequest.getCreatedTime().getTime() >= this.recycleRequest.getArriveDate().getTime()) {
                ZkUtils.showInformation("送达时间不能小于创建时间", "提示");
                return false;
            }
        }
        if (StringUtils.isBlank(this.recycleRequest.getLogistics())) {
            ZkUtils.showInformation("请填写物流名称", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.recycleRequest.getLogisticsNum())) {
            ZkUtils.showInformation("请填写物流单号", "提示");
            return false;
        }
        if (this.recycleRequest.getTransportExpense() <= 0) {
            ZkUtils.showInformation("运输费用不能小于0！", "提示");
            return false;
        }
        if (this.recycleRequest.getRecyclefile() == null) {
            ZkUtils.showInformation("请上传故障件图片！", "提示");
            return false;
        }
        if (this.recycleRequest.getLogisticsfile() == null) {
            ZkUtils.showInformation("请上传物流凭证！", "提示");
            return false;
        }

        if (this.recycleRequest.getRecycleItemInfoList().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }
        for (RecycleItemInfo item : recycleRequest.getRecycleItemInfoList()) {
            if (item.getBackAmount() > item.getWaitAmount()) {
                ZkUtils.showInformation("本次返回数量大于应返回数量", "错误");
                return false;
            }
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
                changeCount();

                if (startProcessInstance(this.recycleRequest)) {
                    this.recycleRequest = recycleService.save(this.recycleRequest);
                    flowDocInfo = this.recycleRequest;
                    Map<String, String> map = recycleService.startProcess(this.recycleRequest, getActiveUser());
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

    //@Command
    //@Override
    //public void showHandleForm() {
    //    if (getActiveUser().getDealer() == null && this.recycleRequest.getArriveDate() == null) {
    //        ZkUtils.showInformation("请填写送达时间", "提示");
    //        return;
    //    }
    //    super.showHandleForm();
    //}

    @Override
    protected void completeTask(String outcome, String comment) throws IOException {
        if ("同意".equals(outcome) && getActiveUser().getDealer() == null && this.recycleRequest.getArriveDate() == null) {
            ZkUtils.showInformation("请填写送达时间", "提示");
            return;
        }
        if ("同意".equals(outcome) && this.recycleRequest.getArriveDate() != null) {
            for (RoleInfo roleInfo : this.getActiveUser().getRoles()) {
                if ("role017".equals(roleInfo.getRoleId())) {
                    for (RecycleItemInfo itemInfo : this.recycleRequest.getRecycleItemInfoList()) {
                        if (itemInfo.getAcceptAmount() == 0) {
                            ZkUtils.showInformation("请填写接收数量", "提示");
                            return;
                        }
                    }
                }

            }
        }
        super.completeTask(outcome, comment);
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
                recycleService.desertTask(this.recycleRequest.getObjId());
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
        return recycleService.save((RecycleInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return recycleService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("recycleRequest", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_NOTICE_PENDING, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm(@BindingParam("entity") RecycleItemInfo recycleItemInfo) {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        String srcDocID = "";
        try {
            String title = recycleItemInfo.getSrcDocType();
            if ("首保服务单".equals(title)) {
                url = "/views/asm/first_maintenance_form.zul";
                ZkUtils.showInformation("功能开发中", "提示");
                return;
            } else if ("三包服务单".equals(title)) {
                WarrantyMaintenanceInfo warrantyMaintenanceInfo = warrantyMaintenanceService.findOneWithOthersBySrcDocNo(recycleItemInfo.getSrcDocNo());
                srcDocID = warrantyMaintenanceInfo.getObjId();
                url = "/views/asm/warranty_maintenance_form.zul";
            } else if ("活动服务单".equals(title)) {
                ZkUtils.showInformation("功能开发中", "提示");
                url = "/views/asm/activity_maintenance_form.zul";
                return;
            }
            if (StringUtils.isNotBlank(srcDocID)) {
                paramMap.put("objId", srcDocID);
                paramMap.put("businessId", srcDocID);
            } else {
                return;
            }
            ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
