package com.sunjet.frontend.vm.settlement;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.flow.CommentItem;
import com.sunjet.dto.asms.settlement.DealerSettlementInfo;
import com.sunjet.dto.asms.settlement.ExpenseItemInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailItems;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.activity.ActivityMaintenanceService;
import com.sunjet.frontend.service.asm.DealerSettlementService;
import com.sunjet.frontend.service.asm.FirstMaintenanceService;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.common.UUIDHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zyh
 * @create 2016-11-14 上午11:38
 * <p>
 * 服务结算单 表单 VM
 */
public class WarrantMaintenanceSettlementFormVM extends FormVM {

    @Getter
    @Setter
    List<ExpenseItemInfo> items;    //费用列表
    @WireVariable
    private DealerSettlementService dealerSettlementService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private FirstMaintenanceService firstMaintenanceService;
    @WireVariable
    private ActivityMaintenanceService activityMaintenanceService;
    @Getter
    @Setter
    private DealerSettlementInfo dealerSettlement = new DealerSettlementInfo();     //服务结算
    @Getter
    @Setter
    private DealerInfo dealer;
    @Getter
    @Setter
    private String keyword = "";        // 搜索关键字

    private Window window;

    @Getter
    @Setter
    private Boolean showSelectWin = false;
    @Getter
    @Setter
    private PendingSettlementDetailItems pendingSettlementDetailItems = new PendingSettlementDetailItems();
    @Getter
    @Setter
    private List<PendingSettlementDetailInfo> pendingItems = new ArrayList<>();

    private List<PendingSettlementDetailInfo> originPendingItems = new ArrayList<>();

    @Getter
    private Boolean isCheckAll = false;
    @Getter
    @Setter
    private Set<PendingSettlementDetailInfo> selectedInfos = new HashSet<>();

    @Getter
    private Map<String, DictionaryInfo> vms = new HashMap<>();

    @Getter
    private List<DictionaryInfo> vehicleTypes = new ArrayList<>();

    @Getter
    @Setter
    private DictionaryInfo selectedVehicleType;

    @Init(superclass = true)
    public void init() {
        dictionaryService.findDictionariesByParentCode("15000").forEach(dic -> {
            this.vms.put(dic.getCode(), dic);
            vehicleTypes.add(dic);
        });

        DictionaryInfo info = new DictionaryInfo();
        info.setName("--全部--");
        info.setCode("0");
        info.setValue("0");
        info.setSeq(0);
        this.selectedVehicleType = info;
        vehicleTypes.add(info);

        Collections.sort(vehicleTypes, (o1, o2) -> {
            if (Integer.parseInt(o1.getValue()) > Integer.parseInt(o2.getValue())) {
                return 1;
            } else {
                return -1;
            }
        });

        this.dealer = new DealerInfo();
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.dealerSettlement = dealerSettlementService.findOneById(this.getBusinessId());
            dealer = dealerService.findOneByCode(dealerSettlement.getDealerCode());

        } else {        // 业务对象和业务对象Id都为空
            List<PendingSettlementDetailInfo> details = (List<PendingSettlementDetailInfo>) Executions.getCurrent().getArg().get("pendingSettlement");

            dealerSettlement = new DealerSettlementInfo();
            dealerSettlement.setSettlementType("服务结算单");
            dealerSettlement.setRequestDate(new Date());
            dealerSettlement.setSubmitter(getActiveUser().getLogId());
            dealerSettlement.setSubmitterName(getActiveUser().getUsername());
            dealerSettlement.setOperator(getActiveUser().getUsername());
            dealerSettlement.setOperatorPhone(getActiveUser().getPhone());
            dealerSettlement.setStartDate(DateHelper.getFirstOfMonth());
            dealerSettlement.setEndDate(new Date());

            if (details != null) {
                double expenseTotal = 0.0;
                double freightExpense = 0.0;
                double otherExpense = 0.0;
                double partExpense = 0.0;
                double workingExpense = 0.0;
                double outExpense = 0.0;
                for (PendingSettlementDetailInfo detail : details) {
                    ExpenseItemInfo expenseEntity = new ExpenseItemInfo();
                    expenseEntity.setBusinessDate(detail.getBusinessDate());
                    expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                    expenseEntity.setFreightExpense(detail.getFreightExpense());
                    expenseEntity.setOtherExpense(detail.getOtherExpense());
                    expenseEntity.setPartExpense(detail.getPartExpense());
                    expenseEntity.setWorkingExpense(detail.getWorkingExpense());
                    expenseEntity.setOutExpense(detail.getOutExpense());
                    expenseEntity.setSrcDocID(detail.getSrcDocID());
                    expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                    expenseEntity.setSrcDocType(detail.getSrcDocType());
                    freightExpense = freightExpense + expenseEntity.getFreightExpense();
                    otherExpense = otherExpense + expenseEntity.getOtherExpense();
                    partExpense = partExpense + expenseEntity.getPartExpense();
                    workingExpense = workingExpense + expenseEntity.getWorkingExpense();
                    outExpense = outExpense + expenseEntity.getOutExpense();
                    expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                    this.dealerSettlement.getItems().add(expenseEntity);    //获取费用结算子行列表
                    if (detail.getDealerCode() != null && this.dealerSettlement.getDealerCode() == null) {
                        DealerInfo dealerEntity = dealerService.findOneByCode(detail.getDealerCode());
                        this.setDealer(dealerEntity);
                        this.dealerSettlement.setDealerCode(dealerEntity.getCode());
                        this.dealerSettlement.setDealerName(dealerEntity.getName());
                        this.dealerSettlement.setProvinceName(dealerEntity.getProvinceName());
                    }
                }
                dealerSettlement.setOtherExpense(otherExpense);
                dealerSettlement.setPartExpense(partExpense);
                dealerSettlement.setWorkingExpense(workingExpense);
                dealerSettlement.setOutExpense(outExpense);
                dealerSettlement.setPunishmentExpense(0.0);
                dealerSettlement.setRewardExpense(0.0);
                dealerSettlement.setExpenseTotal(expenseTotal);

                dealerSettlement = dealerSettlementService.save(dealerSettlement);

                this.setReadonly(true);
            } else {
                DealerInfo dealer = userService.findOne(getActiveUser().getUserId()).getDealer();
                if (dealer != null) {
                    this.dealerSettlement.setDealerCode(dealer.getCode());
                    this.dealerSettlement.setDealerName(dealer.getName());
                    this.dealerSettlement.setProvinceName(dealer.getProvinceName());
                }
                this.setReadonly(false);
            }
        }

        setActiveUserMsg(this.dealerSettlement);

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

//    /**
//     * 初始化后加载窗体
//     *
//     * @param view
//     */
//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
//        Selectors.wireComponents(view, this, false);
//        //win = (Window) view;
//        //if (win != null) {
//        //    win.setTitle(win.getTitle() + titleMsg);
//        //}
//    } else

    /**
     * 保存对象
     */
    @Command
    @NotifyChange("*")
    public void submit() {
        if (dealer != null) {
            dealerSettlement.setDealerName(dealer.getName());
            dealerSettlement.setDealerCode(dealer.getCode());
        }

//        //清空单据对应的结算信息
//        List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getPendingsBySettlementID(dealerSettlement.getObjId());
//        if (details != null) {
//            for (PendingSettlementDetailInfo detail : details) {
//                detail.setSettlementDocID(null);
//                detail.setSettlementDocNo(null);
//                detail.setSettlementDocType(null);
//                detail.setOperator(null);
//                detail.setOperatorPhone(null);
////                    detail.setSettlement(false);
////                    detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
//                detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
//                detail.setModifierId(getActiveUser().getLogId());
//                detail.setModifierName(getActiveUser().getUsername());
//                detail.setModifiedTime(new Date());
//                pendingSettlementDetailsService.save(detail);
//            }
//        }

        //for (ExpenseItemInfo expense : dealerSettlement.getItems()) {
        //    PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(expense.getSrcDocID());
        //    detail.setSettlementDocID(dealerSettlement.getObjId());
        //    detail.setSettlementDocNo(dealerSettlement.getDocNo());
        //    //detail.setDocNo(dealerSettlement.getDocNo());
        //    detail.setSettlementDocType("服务结算单");
        //    detail.setOperator(this.getActiveUser().getUsername());
        //    detail.setOperatorPhone(this.getActiveUser().getPhone());
        //    detail.setStatus(DocStatus.SETTLING.getIndex());
        //    detail.setModifierId(getActiveUser().getLogId());
        //    detail.setModifierName(getActiveUser().getUsername());
        //    detail.setModifiedTime(new Date());
        //    pendingSettlementDetailsService.save(detail);
        //}

        dealerSettlement = dealerSettlementService.save(dealerSettlement);  //保存服务结算单
        this.updateUIState();
        showDialog();
    }


    /**
     * 调取服务单据
     */
    @Command
    @NotifyChange("dealerSettlement")
    public void ReadItem() {
        double expenseTotal = 0.0;
        double freightExpense = 0.0;
        double otherExpense = 0.0;
        double partExpense = 0.0;
        double workingExpense = 0.0;
        double outExpense = 0.0;


        //如果单据已保存再次取数据，需先清空单据对应的结算信息,再添加原有单据子行
        this.dealerSettlement.getItems().clear();
        if (StringUtils.isNotBlank(dealerSettlement.getObjId())) {
            List<ExpenseItemInfo> expenseListEntityList;
            expenseListEntityList = dealerSettlementService.findByDealerSettlementId(dealerSettlement.getObjId());
            for (ExpenseItemInfo expenseList : expenseListEntityList) {
                this.dealerSettlement.getItems().add(expenseList);
                expenseTotal = expenseTotal + expenseList.getExpenseTotal();
                freightExpense = freightExpense + expenseList.getFreightExpense();
                otherExpense = otherExpense + expenseList.getOtherExpense();
                partExpense = partExpense + expenseList.getPartExpense();
                workingExpense = workingExpense + expenseList.getWorkingExpense();
                outExpense = outExpense + expenseList.getOutExpense();
            }
        }


        if (dealer != null && StringUtils.isNotBlank(dealer.getCode())) {
            List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getDealerSelttlements(dealer.getCode(), dealerSettlement.getStartDate(), dealerSettlement.getEndDate());
            for (PendingSettlementDetailInfo detail : details) {
                ExpenseItemInfo expenseEntity = new ExpenseItemInfo();
                expenseEntity.setBusinessDate(detail.getBusinessDate());
                expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                expenseEntity.setFreightExpense(detail.getFreightExpense());
                expenseEntity.setOtherExpense(detail.getOtherExpense());
                expenseEntity.setPartExpense(detail.getPartExpense());
                expenseEntity.setWorkingExpense(detail.getWorkingExpense());
                expenseEntity.setOutExpense(detail.getOutExpense());
                expenseEntity.setSrcDocID(detail.getSrcDocID());
                expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                expenseEntity.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + expenseEntity.getFreightExpense();
                otherExpense = otherExpense + expenseEntity.getOtherExpense();
                partExpense = partExpense + expenseEntity.getPartExpense();
                workingExpense = workingExpense + expenseEntity.getWorkingExpense();
                outExpense = outExpense + expenseEntity.getOutExpense();
                expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                this.dealerSettlement.getItems().add(expenseEntity);
            }
            dealerSettlement.setOtherExpense(otherExpense);
            dealerSettlement.setPartExpense(partExpense);
            dealerSettlement.setWorkingExpense(workingExpense);
            dealerSettlement.setOutExpense(outExpense);
            dealerSettlement.setPunishmentExpense(0.0);
            dealerSettlement.setRewardExpense(0.0);
            dealerSettlement.setExpenseTotal(expenseTotal);
        } else {
            ZkUtils.showInformation("请先选择服务站！", "提示");
        }
    }

    @Command
    @NotifyChange({"showSelectWin", "pendingItems", "pendingSettlementDetailItems"})
    public void selectPendingBills() {
        if (StringUtils.isEmpty(this.dealerSettlement.getObjId())) {
            ZkUtils.showExclamation("请先保存当前单据，再选择待结算单！", "提示");
            return;
        }
        if (dealer == null || StringUtils.isEmpty(dealer.getCode())) {
            ZkUtils.showExclamation("请先选择服务站！", "提示");
            return;
        }
        this.selectedInfos.clear();


        this.pendingSettlementDetailItems.setSrcDocNo(null);
        this.pendingSettlementDetailItems.setStartDate(DateHelper.getFirstOfYear());
        this.pendingSettlementDetailItems.setEndDate(DateHelper.getEndDateTime());

        this.originPendingItems = pendingSettlementDetailsService.getDealerSelttlements(dealer.getCode(), pendingSettlementDetailItems.getStartDate(), pendingSettlementDetailItems.getEndDate());
//        if (StringUtils.isNotBlank(this.pendingSettlementDetailItems.getSrcDocNo())) {
//            this.pendingItems = this.originPendingItems.stream()
//                    .filter(item -> item.getSrcDocNo().toUpperCase().contains(this.pendingSettlementDetailItems.getSrcDocNo().toUpperCase()))
//                    .collect(Collectors.toList());
//        } else {
//            this.pendingItems = this.originPendingItems;
//        }

        this.pendingItems = this.originPendingItems.stream()
                .filter(item -> {
                    if (StringUtils.isNotBlank(this.pendingSettlementDetailItems.getSrcDocNo())) {
                        return item.getSrcDocNo().toUpperCase().contains(this.pendingSettlementDetailItems.getSrcDocNo().toUpperCase());
                    } else {
                        return true;
                    }
                })
                .filter(item -> {
                    if (this.selectedVehicleType.getCode().equals("0")) {
                        return true;
                    } else if (StringUtils.isBlank(item.getTypeCode())) {
                        return false;
                    } else {
                        return item.getTypeCode().equals(this.selectedVehicleType.getCode());
                    }
                })
                .collect(Collectors.toList());

        this.showSelectWin = true;
    }

    @Command
    @NotifyChange("dealerSettlement")
    public void deleteItem(@BindingParam("model") ExpenseItemInfo model) {
        //回写待结算列表
        PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(model.getSrcDocID());
        detail.setSettlementDocID(null);
        detail.setSettlementDocNo(null);
//        detail.setSettlement(false);
        detail.setSettlementDocType("服务结算单");
        detail.setOperator(null);
        detail.setOperatorPhone(null);
//        detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
        detail.setModifierId(getActiveUser().getLogId());
        detail.setModifierName(getActiveUser().getUsername());
        detail.setModifiedTime(new Date());
        pendingSettlementDetailsService.save(detail);
        updateWarrantyStatus(detail);
        for (ExpenseItemInfo item : this.dealerSettlement.getItems()) {
            if (item.equals(model)) {
                this.dealerSettlement.setExpenseTotal(this.dealerSettlement.getExpenseTotal() - model.getExpenseTotal());
                this.dealerSettlement.setOtherExpense(this.dealerSettlement.getOtherExpense() - model.getOtherExpense());
                this.dealerSettlement.setWorkingExpense(this.dealerSettlement.getWorkingExpense() - model.getWorkingExpense());
                this.dealerSettlement.setOutExpense(this.dealerSettlement.getOutExpense() - model.getOutExpense());
                this.dealerSettlement.setPartExpense(this.dealerSettlement.getPartExpense() - model.getPartExpense());

                if (StringUtils.isNotBlank(model.getTypeCode())) {
                    if (model.getTypeCode().equals("15001")) {
                        this.dealerSettlement.setRefitExpense(this.dealerSettlement.getRefitExpense() - model.getExpenseTotal());
                    } else if (detail.getTypeCode().equals("15002")) {
                        this.dealerSettlement.setBusExpense(this.dealerSettlement.getBusExpense() - model.getExpenseTotal());
                    } else if (detail.getTypeCode().equals("15003")) {
                        this.dealerSettlement.setNonRoadExpense(this.dealerSettlement.getNonRoadExpense() - model.getExpenseTotal());
                    }
                }

                this.dealerSettlement.getItems().remove(model);
                break;
            }
        }
        dealerSettlementService.save(this.dealerSettlement);

    }


    @Command
    @NotifyChange("*")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        this.setKeyword("");
        this.dealers.clear();
        this.dealer = dealer;
        dealerSettlement.setDealerName(dealer.getName());
        dealerSettlement.setDealerCode(dealer.getCode());
        dealerSettlement.setTaxRate(dealer.getTaxRate());
        if (dealer.getProvinceName() != null) {
            dealerSettlement.setProvinceName(dealer.getProvinceName());
        }
        if (this.dealerSettlement.getItems() != null && this.dealerSettlement.getItems().size() > 0) {
            ZkUtils.showQuestion("更换服务站将会清空已选择的单据是否执行？", "询问", event -> {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    //从内存中删除配件
                    Iterator<ExpenseItemInfo> iterator = this.dealerSettlement.getItems().iterator();
                    while (iterator.hasNext()) {
                        ExpenseItemInfo expenseItem = iterator.next();
                        //回写待结算列表
                        PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(expenseItem.getSrcDocID());
                        detail.setSettlementDocID(null);
                        detail.setSettlementDocNo(null);
                        detail.setSettlementDocType("服务结算单");
                        detail.setOperator(null);
                        detail.setOperatorPhone(null);
                        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                        detail.setModifierId(getActiveUser().getLogId());
                        detail.setModifierName(getActiveUser().getUsername());
                        detail.setModifiedTime(new Date());
                        pendingSettlementDetailsService.save(detail);
                        //减掉删除的单据费用
                        this.dealerSettlement.setExpenseTotal(this.dealerSettlement.getExpenseTotal() - expenseItem.getExpenseTotal());
                        this.dealerSettlement.setOtherExpense(this.dealerSettlement.getOtherExpense() - expenseItem.getOtherExpense());
                        this.dealerSettlement.setWorkingExpense(this.dealerSettlement.getWorkingExpense() - expenseItem.getWorkingExpense());
                        this.dealerSettlement.setOutExpense(this.dealerSettlement.getOutExpense() - expenseItem.getOutExpense());
                        this.dealerSettlement.setPartExpense(this.dealerSettlement.getPartExpense() - expenseItem.getPartExpense());

                        if (StringUtils.isNotBlank(expenseItem.getTypeCode())) {
                            if (expenseItem.getTypeCode().equals("15001")) {
                                this.dealerSettlement.setRefitExpense(this.dealerSettlement.getRefitExpense() - expenseItem.getExpenseTotal());
                            } else if (detail.getTypeCode().equals("15002")) {
                                this.dealerSettlement.setBusExpense(this.dealerSettlement.getBusExpense() - expenseItem.getExpenseTotal());
                            } else if (detail.getTypeCode().equals("15003")) {
                                this.dealerSettlement.setNonRoadExpense(this.dealerSettlement.getNonRoadExpense() - expenseItem.getExpenseTotal());
                            }
                        }
                        iterator.remove();
                    }
                    dealerSettlementService.save(this.dealerSettlement);
                    updateUIState();
                } else {
                    // 用户点击的是取消按钮
                    ZkUtils.showInformation("取消更换", "提示");
                }
            });

        }

    }



    /**
     * 计算费用
     */
    @Command
    @NotifyChange("dealerSettlement")
    public void changeExpense() {
        if (dealerSettlement.getRewardExpense() == null) {
            dealerSettlement.setRewardExpense(0.0);
        }
        if (dealerSettlement.getPunishmentExpense() == null) {
            dealerSettlement.setPunishmentExpense(0.0);
        }
        dealerSettlement.setExpenseTotal(
                dealerSettlement.getOtherExpense()
                        + dealerSettlement.getWorkingExpense()
                        + dealerSettlement.getOutExpense()
                        + dealerSettlement.getPartExpense()
                        - dealerSettlement.getPunishmentExpense()
                        + dealerSettlement.getRewardExpense());
    }

    @Override
    protected Boolean checkValid() {
        //Boolean result = false;
        if (dealer != null) {
            if (dealerSettlement.getItems().size() < 1) {
                ZkUtils.showInformation("请先获取待结算服务单！", "提示");
                return false;
            }
        } else {
            ZkUtils.showInformation("请先选择服务站！", "提示");
            return false;
        }
        //if (dealerSettlement.getTax() <= 0.0) {
        //    ZkUtils.showInformation("请填写税额！", "提示");
        //    return false;
        //}
        //if (dealerSettlement.getNonTaxAmount() <= 0.0) {
        //    ZkUtils.showInformation("请填写未税金额！", "提示");
        //    return false;
        //}


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
                if (startProcessInstance(this.dealerSettlement)) {
                    this.dealerSettlement = dealerSettlementService.save(this.dealerSettlement);
                    flowDocInfo = this.dealerSettlement;
                    Map<String, String> map = dealerSettlementService.startProcess(this.dealerSettlement, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.updateUIState();
                    this.dealerSettlement = dealerSettlementService.findOneById(this.dealerSettlement.getObjId());
                }

            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });
    }

    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm(@BindingParam("entity") ExpenseItemInfo expenseItemInfo) {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        try {

            String srcDocID = expenseItemInfo.getSrcDocID();
            if (StringUtils.isNotBlank(srcDocID)) {
                paramMap.put("objId", srcDocID);
                paramMap.put("businessId", srcDocID);
            }
            String title = expenseItemInfo.getSrcDocType();
            if ("首保服务单".equals(title)) {
                url = "/views/asm/first_maintenance_form.zul";
            } else if ("三包服务单".equals(title)) {
                url = "/views/asm/warranty_maintenance_form.zul";
            } else if ("活动服务单".equals(title)) {
                url = "/views/asm/activity_maintenance_form.zul";
            }
            ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打印
     *
     * @throws IOException
     */
    @Command
    public void printReport() throws IOException {
        XWPFTemplate template = XWPFTemplate.compile(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/views/report/printPage/asm/dealerSettlement.docx").render(new HashMap<String, Object>() {
            {
                put("dealerCode", new TextRenderData(dealerSettlement.getDealerCode() == null ? "" : dealerSettlement.getDealerCode().toString()));
                put("dealerName", new TextRenderData(dealerSettlement.getDealerName() == null ? "" : dealerSettlement.getDealerName().toString()));
                put("dealerPhone", new TextRenderData(dealer.getPhone() == null ? "" : dealer.getPhone().toString()));
                put("docNo", new TextRenderData(dealerSettlement.getDocNo() == null ? "" : dealerSettlement.getDocNo().toString()));
                put("serviceManager", new TextRenderData(dealer.getServiceManagerName() == null ? "" : dealer.getServiceManagerName().toString()));
                put("province", new TextRenderData(dealer.getProvinceName() == null ? "" : dealer.getProvinceName().toString()));
                put("docNum", new TextRenderData(dealerSettlement.getItems().size() + ""));
                put("submitter", new TextRenderData(dealerSettlement.getSubmitterName() == null ? "" : dealerSettlement.getSubmitterName().toString()));
                put("submitterTime", new TextRenderData(dealerSettlement.getCreatedTime() == null ? "" : DateHelper.dateToString(dealerSettlement.getCreatedTime())));
                put("startDate", new TextRenderData(dealerSettlement.getStartDate() == null ? "" : DateHelper.dateToString(dealerSettlement.getStartDate())));
                put("endDate", new TextRenderData(dealerSettlement.getEndDate() == null ? "" : DateHelper.dateToString(dealerSettlement.getEndDate())));
                put("busExpense", new TextRenderData(dealerSettlement.getBusExpense() == null ? "" : dealerSettlement.getBusExpense() + ""));
                put("nonRoadExpense", new TextRenderData(dealerSettlement.getNonRoadExpense() == null ? "" : dealerSettlement.getNonRoadExpense() + ""));
                put("refitExpense", new TextRenderData(dealerSettlement.getRefitExpense() == null ? "" : dealerSettlement.getRefitExpense() + ""));
                put("partExpense", new TextRenderData(dealerSettlement.getPartExpense() == null ? "" : dealerSettlement.getPartExpense() + ""));
                put("workingExpense", new TextRenderData(dealerSettlement.getWorkingExpense() == null ? "" : dealerSettlement.getWorkingExpense() + ""));
                put("outExpense", new TextRenderData(dealerSettlement.getOutExpense() == null ? "" : dealerSettlement.getOutExpense() + ""));
                put("otherExpense", new TextRenderData(dealerSettlement.getOtherExpense() == null ? "" : dealerSettlement.getOtherExpense() + ""));
                put("rewardExpense", new TextRenderData(dealerSettlement.getRewardExpense() == null ? "" : dealerSettlement.getRewardExpense() + ""));
                put("punishmentExpense", new TextRenderData(dealerSettlement.getPunishmentExpense() == null ? "" : dealerSettlement.getPunishmentExpense() + ""));
                put("tax", new TextRenderData(dealerSettlement.getTax() == null ? "" : dealerSettlement.getTax() + ""));
                put("expenseTotal", new TextRenderData(dealerSettlement.getExpenseTotal() == null ? "" : dealerSettlement.getExpenseTotal() + ""));
                List<CommentItem> commentInfoList = processService.findCommentByProcessInstanceId(dealerSettlement.getProcessInstanceId());
                put("commentInfoList", new TableRenderData(new ArrayList<RenderData>() {{
                    add(new TextRenderData("d0d0d0", "序号"));
                    add(new TextRenderData("d0d0d0", "审批时间"));
                    add(new TextRenderData("d0d0d0", "审批人"));
                    add(new TextRenderData("d0d0d0", "审批意见"));
                }}, new ArrayList<Object>() {{
                    if (commentInfoList != null) {
                        Integer index = 0;
                        for (CommentItem commentItem : commentInfoList) {
                            index++;
                            add(index.toString() + ";"
                                    + DateHelper.getTimeToString(commentItem.getDoDate()) + ";"
                                    + commentItem.getUsername() + ";"
                                    + commentItem.getMessage().replace(";", "；") + ";"
                            );
                        }
                    }

                }}, "no datas", 10500));

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
     * 打印明细
     */
    @Command
    public void printDetail() {
        if (this.dealerSettlement.getItems().size() <= 0) {
            ZkUtils.showExclamation("当前单据没有结算明细！", "提示");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.dealerSettlement.getObjId() == null ? "" : this.getDealerSettlement().getObjId());
        map.put("startDate", dateToString(this.getDealerSettlement().getStartDate()));
        map.put("endDate", dateToString(this.getDealerSettlement().getEndDate()));

        window = (Window) ZkUtils.createComponents("/views/report/printPage/settlement/dealer_settlement_detail.zul", null, map);
        window.setTitle("打印结算明细");
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
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return dealerSettlementService.save((DealerSettlementInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return dealerSettlementService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("dealerSettlement", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_WARRANT_MAINTENANCE_COST_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    @Command
    @NotifyChange({"pendingSettlementDetailItems", "documentStatuses", "selectedVehicleType"})
    public void reset() {
        this.pendingSettlementDetailItems.setSrcDocNo("");
        this.pendingSettlementDetailItems.setEndDate(DateHelper.getEndDateTime());
        this.pendingSettlementDetailItems.setStartDate(DateHelper.getFirstOfYear());
        this.selectedVehicleType = this.vehicleTypes.stream().filter(item -> item.getCode().equals("0")).findFirst().get();
    }

    @Command
    @NotifyChange("pendingItems")
    public void refreshList() {
        this.originPendingItems = pendingSettlementDetailsService.getDealerSelttlements(dealer.getCode(), pendingSettlementDetailItems.getStartDate(), pendingSettlementDetailItems.getEndDate());
        this.pendingItems = this.originPendingItems.stream()
                .filter(item -> {
                    if (StringUtils.isNotBlank(this.pendingSettlementDetailItems.getSrcDocNo())) {
                        return item.getSrcDocNo().toUpperCase().contains(this.pendingSettlementDetailItems.getSrcDocNo().toUpperCase());
                    } else {
                        return true;
                    }
                })
                .filter(item -> {
                    if (this.selectedVehicleType.getCode().equals("0")) {
                        return true;
                    } else if (StringUtils.isBlank(item.getTypeCode())) {
                        return false;
                    } else {
                        return item.getTypeCode().equals(this.selectedVehicleType.getCode());
                    }
                })
                .collect(Collectors.toList());

    }

    @Command
    @NotifyChange("showSelectWin")
    public void closeSelectWin() {
        this.showSelectWin = false;
    }

    @Command
    @NotifyChange({"showSelectWin", "dealerSettlement"})
    public void saveSelected() {
//        if(this.selectedInfos.size()<=0){
//            ZkUtils.showQuestion("当前没有选择待结算单，是否确定关闭？", "提示", new EventListener() {
//                @Override
//                public void onEvent(Event event) throws Exception {
//                    int clickedButton = (Integer) event.getData();
//                    if (clickedButton == Messagebox.OK) {
//                        showSelectWin = false;
//                        BindUtils.postNotifyChange(null,null,this,"showSelectWin");
//                    }
//                }
//            });
        this.isCheckAll = false;
        if (this.selectedInfos.size() > 0) {
            double expenseTotal = 0.0;
            double freightExpense = 0.0;
            double otherExpense = 0.0;
            double partExpense = 0.0;
            double workingExpense = 0.0;
            double outExpense = 0.0;
            double busExpense = 0.0;
            double nonRoadExpense = 0.0;
            double refitExpense = 0.0;

            for (PendingSettlementDetailInfo detail : this.selectedInfos) {
                ExpenseItemInfo expenseItemInfo = new ExpenseItemInfo();
                expenseItemInfo.setBusinessDate(detail.getBusinessDate());
                expenseItemInfo.setExpenseTotal(detail.getExpenseTotal());
                expenseItemInfo.setFreightExpense(detail.getFreightExpense());
                expenseItemInfo.setOtherExpense(detail.getOtherExpense());
                expenseItemInfo.setPartExpense(detail.getPartExpense());
                expenseItemInfo.setWorkingExpense(detail.getWorkingExpense());
                expenseItemInfo.setOutExpense(detail.getOutExpense());
                expenseItemInfo.setSrcDocID(detail.getSrcDocID());
                expenseItemInfo.setSrcDocNo(detail.getSrcDocNo());
                expenseItemInfo.setSrcDocType(detail.getSrcDocType());
                expenseItemInfo.setTypeCode(detail.getTypeCode());
                expenseItemInfo.setVin(detail.getVin());
                freightExpense = freightExpense + expenseItemInfo.getFreightExpense();
                otherExpense = otherExpense + expenseItemInfo.getOtherExpense();
                partExpense = partExpense + expenseItemInfo.getPartExpense();
                workingExpense = workingExpense + expenseItemInfo.getWorkingExpense();
                outExpense = outExpense + expenseItemInfo.getOutExpense();
                expenseTotal = expenseTotal + expenseItemInfo.getExpenseTotal();

                if (StringUtils.isNotBlank(detail.getTypeCode())) {
                    if (detail.getTypeCode().equals("15001")) {
                        refitExpense = refitExpense + expenseItemInfo.getExpenseTotal();
                    } else if (detail.getTypeCode().equals("15002")) {
                        busExpense = busExpense + expenseItemInfo.getExpenseTotal();
                    } else if (detail.getTypeCode().equals("15003")) {
                        nonRoadExpense = nonRoadExpense + expenseItemInfo.getExpenseTotal();
                    }
                }

                this.dealerSettlement.getItems().add(expenseItemInfo);

//            PendingSettlementDetailInfo detailInfo = pendingSettlementDetailsService.getOneBySrcDocID(expenseListEntity.getSrcDocID());
                detail.setSettlementDocID(dealerSettlement.getObjId());
                detail.setSettlementDocNo(dealerSettlement.getDocNo());
                detail.setSettlementDocType("服务结算单");
                detail.setStatus(DocStatus.SETTLING.getIndex());
                detail.setOperator(this.getActiveUser().getUsername());
                detail.setOperatorPhone(this.getActiveUser().getPhone());
                detail.setModifierId(getActiveUser().getLogId());
                detail.setModifierName(getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.save(detail);
                updateWarrantyStatus(detail);

            }

//            dealerSettlement.setPartExpense((dealerSettlement.getPartExpense() == null ? 0.0 : dealerSettlement.getPartExpense()) + partExpense);
////            dealerSettlement.setFreightExpense((dealerSettlement.getFreightExpense() == null ? 0.0 : dealerSettlement.getFreightExpense()) + freightExpense);
//            dealerSettlement.setOtherExpense((dealerSettlement.getOtherExpense() == null ? 0.0 : dealerSettlement.getOtherExpense()) + otherExpense);
//            dealerSettlement.setPunishmentExpense(0.0);
//            dealerSettlement.setRewardExpense(0.0);
//            dealerSettlement.setExpenseTotal((dealerSettlement.getExpenseTotal() == null ? 0.0 : dealerSettlement.getExpenseTotal()) + expenseTotal);
            dealerSettlement.setStartDate(this.pendingSettlementDetailItems.getStartDate());
            dealerSettlement.setEndDate(this.pendingSettlementDetailItems.getEndDate());
            dealerSettlement.setOtherExpense((dealerSettlement.getOtherExpense() == null ? 0.0 : dealerSettlement.getOtherExpense()) + otherExpense);
            dealerSettlement.setPartExpense((dealerSettlement.getPartExpense() == null ? 0.0 : dealerSettlement.getPartExpense()) + partExpense);
            dealerSettlement.setWorkingExpense((dealerSettlement.getWorkingExpense() == null ? 0.0 : dealerSettlement.getWorkingExpense()) + workingExpense);
            dealerSettlement.setOutExpense((dealerSettlement.getOutExpense() == null ? 0.0 : dealerSettlement.getOutExpense()) + outExpense);
            dealerSettlement.setPunishmentExpense(0.0);
            dealerSettlement.setRewardExpense(0.0);
            dealerSettlement.setRefitExpense((dealerSettlement.getRefitExpense() == null ? 0.0 : dealerSettlement.getRefitExpense()) + refitExpense);
            dealerSettlement.setBusExpense((dealerSettlement.getBusExpense() == null ? 0.0 : dealerSettlement.getBusExpense()) + busExpense);
            dealerSettlement.setNonRoadExpense((dealerSettlement.getNonRoadExpense() == null ? 0.0 : dealerSettlement.getNonRoadExpense()) + nonRoadExpense);
            dealerSettlement.setExpenseTotal((dealerSettlement.getExpenseTotal() == null ? 0.0 : dealerSettlement.getExpenseTotal()) + expenseTotal);
            dealerSettlement = dealerSettlementService.save(this.dealerSettlement);
            showSelectWin = false;
        }
    }


    /**
     * 更新服务单状态
     *
     * @param detail
     */
    private void updateWarrantyStatus(PendingSettlementDetailInfo detail) {
        if ("三包服务单".equals(detail.getSrcDocType())) {
            WarrantyMaintenanceInfo warrantyMaintenanceInfo = warrantyMaintenanceService.findOneById(detail.getSrcDocID());
            if (warrantyMaintenanceInfo != null) {
                warrantyMaintenanceInfo.setStatus(detail.getStatus());
                warrantyMaintenanceService.save(warrantyMaintenanceInfo);
            }

        } else if ("首保服务单".equals(detail.getSrcDocType())) {
            FirstMaintenanceInfo firstMaintenanceInfo = firstMaintenanceService.findOneWithGoOutsById(detail.getSrcDocID());
            if (firstMaintenanceInfo != null) {
                firstMaintenanceInfo.setStatus(detail.getStatus());
                firstMaintenanceService.save(firstMaintenanceInfo);
            }
        } else {
            ActivityMaintenanceInfo activityMaintenanceInfo = activityMaintenanceService.findOneById(detail.getSrcDocID());
            if (activityMaintenanceInfo != null) {
                activityMaintenanceInfo.setStatus(detail.getStatus());
                activityMaintenanceService.save(activityMaintenanceInfo);
            }

        }
    }

    @Command
    @NotifyChange("selectedVehicleType")
    public void selectVehicleType() {
        if (!"0".equals(this.selectedVehicleType.getCode())) {
            this.pendingSettlementDetailItems.setTypeCode(this.selectedVehicleType.getCode());
        } else {
            this.pendingSettlementDetailItems.setTypeCode("");
        }
//        refreshData();
    }

    @Command
    @NotifyChange({"pendingItems"})
    public void checkAll(@BindingParam("check") Boolean check) {
        if (check) {
            this.pendingItems.stream().forEach(item -> this.selectedInfos.add(item));
        } else {
            this.selectedInfos.clear();
        }
    }

    public Boolean checkSelected(PendingSettlementDetailInfo info) {
        List<PendingSettlementDetailInfo> collect = this.selectedInfos.stream()
                .filter(item -> item.getObjId().equals(info.getObjId())).collect(Collectors.toList());
        if (collect.size() == 1) {
            return true;
        }
        return false;
    }

    @Command
    public void checkOne(@BindingParam("model") PendingSettlementDetailInfo model, @BindingParam("check") Boolean check) {
        if (check) {
            this.selectedInfos.add(model);
        } else {
            this.selectedInfos.remove(model);
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
                dealerSettlementService.desertTask(this.dealerSettlement.getObjId());
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
