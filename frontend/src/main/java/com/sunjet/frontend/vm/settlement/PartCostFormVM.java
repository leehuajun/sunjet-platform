package com.sunjet.frontend.vm.settlement;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.flow.CommentItem;
import com.sunjet.dto.asms.settlement.AgencySettlementInfo;
import com.sunjet.dto.asms.settlement.PartExpenseItemsInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailItems;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.AgencySettlementService;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.supply.SupplyService;
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
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
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
 * 配件结算单 表单 VM
 */
public class PartCostFormVM extends FormVM {

    @Getter
    @Setter
    List<PartExpenseItemsInfo> partExpenseItemsInfos;  //配件结算费用子行列表
    @WireVariable
    private AgencySettlementService agencySettlementService;
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private SupplyService supplyService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private UserService userService;
    @Getter
    @Setter
    private AgencySettlementInfo agencySettlement = new AgencySettlementInfo();
    @Getter
    @Setter
    private AgencyInfo agency;
    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();

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

//    @Getter
//    @Setter
//    private Map<String,Boolean> selectedIds = new HashMap<>();

    @Getter
    @Setter
    private Set<PendingSettlementDetailInfo> selectedInfos = new HashSet<>();


    @Init(superclass = true)
    public void init() {
        //this.setBaseService(agencySettlementService);
        this.agency = new AgencyInfo();
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.agencySettlement = agencySettlementService.findOneById(this.getBusinessId());
            this.agency = agencyService.findOneByCode(this.agencySettlement.getAgencyCode());

            //if (partExpenseItemsInfos == null && agencySettlement.getObjId() != null) {
            //    this.partExpenseItemsInfos = agencySettlementService.findByAgencySettlementId(agencySettlement.getObjId());
            //    this.agencySettlement.setPartExpenseItemsInfos(partExpenseItemsInfos);
            //}

            if (this.agencySettlement.getSubmitter().equals(this.getActiveUser().getLogId())
                    && this.agencySettlement.getStatus() == DocStatus.DRAFT.getIndex()) {
                this.setReadonly(false);
            } else {
                this.setReadonly(true);
            }
        } else {        // 业务对象和业务对象Id都为空
            List<PendingSettlementDetailInfo> details = (List<PendingSettlementDetailInfo>) Executions.getCurrent().getArg().get("pendingSettlement");

            agencySettlement = new AgencySettlementInfo();
            agencySettlement.setRequestDate(new Date());
            agencySettlement.setStartDate(DateHelper.getFirstOfMonth());
            agencySettlement.setEndDate(new Date());
            agencySettlement.setOperator(getActiveUser().getUsername());

            if (details != null) {
                double expenseTotal = 0.0;
                double freightExpense = 0.0;
                double otherExpense = 0.0;
                double partExpense = 0.0;
                double workingExpense = 0.0;
                for (PendingSettlementDetailInfo detail : details) {
                    PartExpenseItemsInfo expenseListEntity = new PartExpenseItemsInfo();
                    expenseListEntity.setExpenseTotal(detail.getExpenseTotal());
                    expenseListEntity.setFreightExpense(detail.getFreightExpense());
                    expenseListEntity.setOtherExpense(detail.getOtherExpense());
                    expenseListEntity.setPartExpense(detail.getPartExpense());
                    expenseListEntity.setWorkingExpense(detail.getWorkingExpense());
                    expenseListEntity.setSrcDocID(detail.getSrcDocID());
                    expenseListEntity.setSrcDocNo(detail.getSrcDocNo());
                    expenseListEntity.setSrcDocType(detail.getSrcDocType());
                    freightExpense = freightExpense + expenseListEntity.getFreightExpense();
                    otherExpense = otherExpense + expenseListEntity.getOtherExpense();
                    partExpense = partExpense + expenseListEntity.getPartExpense();
                    workingExpense = workingExpense + expenseListEntity.getWorkingExpense();
                    expenseTotal = expenseTotal + expenseListEntity.getExpenseTotal();
                    this.agencySettlement.getPartExpenseItemsInfos().add(expenseListEntity);        //配件结算费用列表
                    if (detail.getAgencyCode() != null && this.agencySettlement.getAgencyCode() == null) {
                        AgencyInfo agencyEntity = agencyService.findOneByCode(detail.getAgencyCode());
                        this.setAgency(agencyEntity);
                        this.agencySettlement.setAgencyCode(agencyEntity.getCode());
                        this.agencySettlement.setAgencyName(agencyEntity.getName());
                        if (agencyEntity.getProvinceName() != null) {
                            this.agencySettlement.setProvinceName(agencyEntity.getProvinceName());
                        }
                    }

                }
                agencySettlement.setPartExpense(partExpense);
                agencySettlement.setFreightExpense(freightExpense);
                agencySettlement.setOtherExpense(otherExpense);
                agencySettlement.setPunishmentExpense(0.0);
                agencySettlement.setRewardExpense(0.0);
                agencySettlement.setExpenseTotal(expenseTotal);

                agencySettlement = agencySettlementService.save(agencySettlement);

                this.setReadonly(true);
            } else {
                AgencyInfo agency = userService.findOne(getActiveUser().getUserId()).getAgency();
                if (agency != null) {
                    this.agencySettlement.setAgencyCode(agency.getCode());
                    this.agencySettlement.setAgencyName(agency.getName());
                    this.agencySettlement.setProvinceName(agency.getProvinceName());
                }
                this.setReadonly(false);
            }
        }
        setActiveUserMsg(this.agencySettlement);
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
     * 保存对象
     */
    @Command
    @NotifyChange("*")
    public void submit() {
        if (agency != null) {
            agencySettlement.setAgencyName(agency.getName());
            agencySettlement.setAgencyCode(agency.getCode());
        }

//        //清空单据对应的结算信息
//        List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getPendingsBySettlementID(agencySettlement.getObjId());
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

        //for (PartExpenseItemsInfo part : agencySettlement.getPartExpenseItemsInfos()) {
        //    PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(part.getSrcDocID());
        //    detail.setSettlementDocID(agencySettlement.getObjId());
        //    detail.setSettlementDocNo(agencySettlement.getDocNo());
        //    //detail.setDocNo(agencySettlement.getDocNo());
        //    detail.setSettlementDocType("配件结算单");
        //    detail.setOperator(this.getActiveUser().getUsername());
        //    detail.setOperatorPhone(this.getActiveUser().getPhone());
        //    detail.setStatus(DocStatus.SETTLING.getIndex());
        //    detail.setModifierId(getActiveUser().getLogId());
        //    detail.setModifierName(getActiveUser().getUsername());
        //    detail.setModifiedTime(new Date());
        //    pendingSettlementDetailsService.save(detail);
        //}

        agencySettlement = agencySettlementService.save(this.agencySettlement); //保存配件费用结算
        this.updateUIState();
        showDialog();

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

    @Command
    @NotifyChange("agency")
    public void clearSelectedAgency() {
        if (getActiveUser().getAgency() != null) {
            this.agency = getActiveUser().getAgency();
        } else {
            this.agency = null;
        }
        this.setKeyword("");
        this.agencySettlement.setProvinceName("");
        this.agencySettlement.setOperatorPhone("");
    }

    @Command
    @NotifyChange({"agency", "agencySettlement"})
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.agency = agency;
        if (this.agency.getProvinceName() != null) {
            this.agencySettlement.setProvinceName(this.agency.getProvinceName());
        }
        this.agencySettlement.setOperatorPhone(this.agency.getPhone());

        if (this.agencySettlement.getPartExpenseItemsInfos() != null && this.agencySettlement.getPartExpenseItemsInfos().size() > 0) {
            ZkUtils.showQuestion("更换合作商将会清空已选择的单据是否执行？", "询问", event -> {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    //从内存中删除配件
                    Iterator<PartExpenseItemsInfo> iterator = this.agencySettlement.getPartExpenseItemsInfos().iterator();
                    while (iterator.hasNext()) {
                        PartExpenseItemsInfo partExpenseItemsInfo = iterator.next();
                        //回写待结算列表
                        PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(partExpenseItemsInfo.getSrcDocID());
                        detail.setSettlementDocID(null);
                        detail.setSettlementDocNo(null);
                        detail.setSettlementDocType("配件结算单");
                        detail.setOperator(null);
                        detail.setOperatorPhone(null);
                        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                        detail.setModifierId(getActiveUser().getLogId());
                        detail.setModifierName(getActiveUser().getUsername());
                        detail.setModifiedTime(new Date());
                        pendingSettlementDetailsService.save(detail);
                        //减掉删除的单据费用
                        this.agencySettlement.setExpenseTotal(this.agencySettlement.getExpenseTotal() - partExpenseItemsInfo.getExpenseTotal());
                        this.agencySettlement.setOtherExpense(this.agencySettlement.getOtherExpense() - partExpenseItemsInfo.getOtherExpense());
                        this.agencySettlement.setPartExpense(this.agencySettlement.getPartExpense() - partExpenseItemsInfo.getPartExpense());
                        this.agencySettlement.setFreightExpense(this.agencySettlement.getFreightExpense() - partExpenseItemsInfo.getFreightExpense());
                        iterator.remove();
                    }
                    this.agencySettlementService.save(this.agencySettlement);
                    updateUIState();
                } else {
                    // 用户点击的是取消按钮
                    ZkUtils.showInformation("取消更换", "提示");
                }
            });


        }


    }


    /**
     * 初始化加载窗体
     *
     * @param view
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        //win = (Window) view;
        //if (win != null) {
        //    win.setTitle(win.getTitle() + titleMsg);
        //}
    }

    @Override
    protected Boolean checkValid() {
        Boolean result = false;
        if (agency != null) {
            if (agencySettlement.getPartExpenseItemsInfos().size() < 1) {
                ZkUtils.showInformation("请先获取待结算服务单！", "提示");
            } else {
                result = true;
            }
        } else {
            ZkUtils.showInformation("请先选择经销商！", "提示");
        }
        return result;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
        //if (!checkValid())
        //    return;
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.agencySettlement)) {
                    this.agencySettlement = agencySettlementService.save(this.agencySettlement);
                    flowDocInfo = this.agencySettlement;
                    Map<String, String> map = agencySettlementService.startProcess(this.agencySettlement, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.updateUIState();
                }
                this.agencySettlement = agencySettlementService.findOneById(this.agencySettlement.getObjId());
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });
    }


    /**
     * 计算费用
     */
    @Command
    @NotifyChange("agencySettlement")
    public void changeExpense() {
        if (agencySettlement.getRewardExpense() == null) {
            agencySettlement.setRewardExpense(0.0);
        }
        if (agencySettlement.getPunishmentExpense() == null) {
            agencySettlement.setPunishmentExpense(0.0);
        }
        agencySettlement.setExpenseTotal(agencySettlement.getOtherExpense() + agencySettlement.getPartExpense() + agencySettlement.getFreightExpense() - agencySettlement.getPunishmentExpense() + agencySettlement.getRewardExpense());
    }

    /**
     * 调取费用单据
     */
    @Command
    @NotifyChange("agencySettlement")
    public void ReadItem() {
        double expenseTotal = 0.0;
        double freightExpense = 0.0;
        double otherExpense = 0.0;
        double partExpense = 0.0;
        double workingExpense = 0.0;
        //如果单据已保存再次取数据，需先清空单据对应的结算信息,再添加原有单据子行
        this.agencySettlement.getPartExpenseItemsInfos().clear();
        if (StringUtils.isNotBlank(agencySettlement.getObjId())) {
            List<PartExpenseItemsInfo> expenseListEntityList;
            expenseListEntityList = agencySettlementService.findByAgencySettlementId(agencySettlement.getObjId());
            for (PartExpenseItemsInfo expenseList : expenseListEntityList) {
                this.agencySettlement.getPartExpenseItemsInfos().add(expenseList);
                expenseTotal = expenseTotal + expenseList.getExpenseTotal();
                freightExpense = freightExpense + expenseList.getFreightExpense();
                otherExpense = otherExpense + expenseList.getOtherExpense();
                partExpense = partExpense + expenseList.getPartExpense();
                workingExpense = workingExpense + expenseList.getWorkingExpense();
            }
        }

        if (agency != null && StringUtils.isNotBlank(agency.getCode())) {
            List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getAgencySelttlements(agency.getCode(), agencySettlement.getStartDate(), agencySettlement.getEndDate());
            for (PendingSettlementDetailInfo detail : details) {
                PartExpenseItemsInfo expenseListEntity = new PartExpenseItemsInfo();
                expenseListEntity.setExpenseTotal(detail.getExpenseTotal());
                expenseListEntity.setFreightExpense(detail.getFreightExpense());
                expenseListEntity.setOtherExpense(detail.getOtherExpense());
                expenseListEntity.setPartExpense(detail.getPartExpense());
                expenseListEntity.setWorkingExpense(detail.getWorkingExpense());
                expenseListEntity.setSrcDocID(detail.getSrcDocID());
                expenseListEntity.setSrcDocNo(detail.getSrcDocNo());
                expenseListEntity.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + (expenseListEntity.getFreightExpense() == null ? 0.0 : expenseListEntity.getFreightExpense());
                otherExpense = otherExpense + (expenseListEntity.getOtherExpense() == null ? 0.0 : expenseListEntity.getOtherExpense());
                partExpense = partExpense + expenseListEntity.getPartExpense();
                workingExpense = workingExpense + expenseListEntity.getWorkingExpense();
                expenseTotal = expenseTotal + expenseListEntity.getExpenseTotal();
                this.agencySettlement.getPartExpenseItemsInfos().add(expenseListEntity);
            }
            agencySettlement.setPartExpense(partExpense);
            agencySettlement.setFreightExpense(freightExpense);
            agencySettlement.setOtherExpense(otherExpense);
            agencySettlement.setPunishmentExpense(0.0);
            agencySettlement.setRewardExpense(0.0);
            agencySettlement.setExpenseTotal(expenseTotal);
        } else {
            ZkUtils.showInformation("请先选择经销商！", "提示");
        }
    }

    @Command
    @NotifyChange({"showSelectWin", "pendingItems", "pendingSettlementDetailItems"})
    public void selectPendingBills() {
        if (StringUtils.isEmpty(this.agencySettlement.getObjId())) {
            ZkUtils.showExclamation("请先保存当前单据，再选择待结算单！", "提示");
            return;
        }
        if (agency == null || StringUtils.isEmpty(agency.getCode())) {
            ZkUtils.showExclamation("请先选择经销商！", "提示");
            return;
        }

        this.selectedInfos.clear();

        this.pendingSettlementDetailItems.setSrcDocNo(null);
        this.pendingSettlementDetailItems.setStartDate(DateHelper.getFirstOfYear());
        this.pendingSettlementDetailItems.setEndDate(DateHelper.getEndDateTime());

        this.originPendingItems = pendingSettlementDetailsService.getAgencySelttlements(agency.getCode(), pendingSettlementDetailItems.getStartDate(), pendingSettlementDetailItems.getEndDate());

        this.pendingItems = this.originPendingItems.stream()
                .filter(item -> {
                    if (StringUtils.isNotBlank(this.pendingSettlementDetailItems.getSrcDocNo())) {
                        return item.getSrcDocNo().toUpperCase().contains(this.pendingSettlementDetailItems.getSrcDocNo().toUpperCase());
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());

        this.showSelectWin = true;
    }

    @Command
    @NotifyChange("agencySettlement")
    public void deleteItem(@BindingParam("model") PartExpenseItemsInfo model) {
        //回写待结算列表
        PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(model.getSrcDocID());
        detail.setSettlementDocID(null);
        detail.setSettlementDocNo(null);
        detail.setSettlementDocType("配件结算单");
        detail.setOperator(null);
        detail.setOperatorPhone(null);
        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
        detail.setModifierId(getActiveUser().getLogId());
        detail.setModifierName(getActiveUser().getUsername());
        detail.setModifiedTime(new Date());
        pendingSettlementDetailsService.save(detail);
        updateWarrantyStatus(detail);
        for (PartExpenseItemsInfo item : this.agencySettlement.getPartExpenseItemsInfos()) {
            if (item.getObjId().equals(model.getObjId())) {
                this.agencySettlement.setExpenseTotal(this.agencySettlement.getExpenseTotal() - model.getExpenseTotal());
                this.agencySettlement.setOtherExpense(this.agencySettlement.getOtherExpense() - model.getOtherExpense());
                this.agencySettlement.setPartExpense(this.agencySettlement.getPartExpense() - model.getPartExpense());
                this.agencySettlement.setFreightExpense(this.agencySettlement.getFreightExpense() - model.getFreightExpense());
                this.agencySettlement.getPartExpenseItemsInfos().remove(model);
                break;
            }
        }
        this.agencySettlementService.save(this.agencySettlement);
    }


    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm(@BindingParam("entity") PartExpenseItemsInfo partExpenseItemsInfo) {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        try {

            String srcDocID = partExpenseItemsInfo.getSrcDocID();
            if (StringUtils.isNotBlank(srcDocID)) {
                paramMap.put("objId", srcDocID);
                paramMap.put("businessId", srcDocID);
            }
            String title = partExpenseItemsInfo.getSrcDocType();
            url = "/views/asm/supply_form.zul";
            ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    /**
     * 打印明细
     */
    @Command
    public void printDetail() {
        if (this.agencySettlement.getPartExpenseItemsInfos().size() <= 0) {
            ZkUtils.showExclamation("当前单据没有结算明细！", "提示");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.agencySettlement.getObjId() == null ? "" : this.agencySettlement.getObjId());
        map.put("startDate", dateToString(this.agencySettlement.getStartDate()));
        map.put("endDate", dateToString(this.agencySettlement.getEndDate()));

        window = (Window) ZkUtils.createComponents("/views/report/printPage/settlement/agency_settlement_detail.zul", null, map);
        window.setTitle("打印结算明细");
        window.doModal();

    }


    ///**
    // * 打印页面
    // */
    //@Command
    //public void printReport() {
    //    Map<String, Object> map = new HashMap<>();
    //    map.put("objId", this.agencySettlement.getObjId() == null ? "" : this.agencySettlement.getObjId());
    //    Window window = (Window) ZkUtils.createComponents("/views/report/printPage/settlement/agency_settlement_printPage.zul", null, map);
    //    window.setTitle("打印单据");
    //    window.doModal();
    //
    //}

    /**
     * 打印
     *
     * @throws IOException
     */
    @Command
    public void printReport() throws IOException {
        XWPFTemplate template = XWPFTemplate.compile(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/views/report/printPage/asm/agencySettlement.docx").render(new HashMap<String, Object>() {
            {
                put("agencyCode", new TextRenderData(agencySettlement.getAgencyCode() == null ? "" : agencySettlement.getAgencyCode().toString()));
                put("agencyName", new TextRenderData(agencySettlement.getAgencyName() == null ? "" : agencySettlement.getAgencyName().toString()));
                put("agencyPhone", new TextRenderData(agency.getPhone() == null ? "" : agency.getPhone().toString()));
                put("docNo", new TextRenderData(agencySettlement.getDocNo() == null ? "" : agencySettlement.getDocNo().toString()));
                put("province", new TextRenderData(agency.getProvinceName() == null ? "" : agency.getProvinceName().toString()));
                put("docNum", new TextRenderData(agencySettlement.getPartExpenseItemsInfos().size() + ""));
                put("submitter", new TextRenderData(agencySettlement.getSubmitterName() == null ? "" : agencySettlement.getSubmitterName().toString()));
                put("submitterTime", new TextRenderData(agencySettlement.getCreatedTime() == null ? "" : DateHelper.dateToString(agencySettlement.getCreatedTime())));
                put("startDate", new TextRenderData(agencySettlement.getStartDate() == null ? "" : DateHelper.dateToString(agencySettlement.getStartDate())));
                put("endDate", new TextRenderData(agencySettlement.getEndDate() == null ? "" : DateHelper.dateToString(agencySettlement.getEndDate())));
                put("freightExpense", new TextRenderData(agencySettlement.getFreightExpense() == null ? "" : agencySettlement.getFreightExpense() + ""));
                put("otherExpense", new TextRenderData(agencySettlement.getOtherExpense() == null ? "" : agencySettlement.getOtherExpense() + ""));
                put("partExpense", new TextRenderData(agencySettlement.getPartExpense() == null ? "" : agencySettlement.getPartExpense() + ""));
                put("rewardExpense", new TextRenderData(agencySettlement.getRewardExpense() == null ? "" : agencySettlement.getRewardExpense() + ""));
                put("punishmentExpense", new TextRenderData(agencySettlement.getPunishmentExpense() == null ? "" : agencySettlement.getPunishmentExpense() + ""));
                put("tax", new TextRenderData(agencySettlement.getTax() == null ? "" : agencySettlement.getTax() + ""));
                put("expenseTotal", new TextRenderData(agencySettlement.getExpenseTotal() == null ? "" : agencySettlement.getExpenseTotal() + ""));
                List<CommentItem> commentInfoList = processService.findCommentByProcessInstanceId(agencySettlement.getProcessInstanceId());
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
        return agencySettlementService.save((AgencySettlementInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return agencySettlementService.findOneById(objId);
    }


    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("agencySettlement", "handle", "canDesertTask", "canHandleTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_PART_COST_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

    @Command
    @NotifyChange({"pendingSettlementDetailItems", "documentStatuses"})
    public void reset() {
        this.pendingSettlementDetailItems.setSrcDocNo("");
        this.pendingSettlementDetailItems.setEndDate(DateHelper.getEndDateTime());
        this.pendingSettlementDetailItems.setStartDate(DateHelper.getFirstOfYear());
    }

    @Command
    @NotifyChange("pendingItems")
    public void refreshList() {
        this.originPendingItems = pendingSettlementDetailsService.getAgencySelttlements(agency.getCode(), pendingSettlementDetailItems.getStartDate(), pendingSettlementDetailItems.getEndDate());

        this.pendingItems = this.originPendingItems.stream()
                .filter(item -> {
                    if (StringUtils.isNotBlank(this.pendingSettlementDetailItems.getSrcDocNo())) {
                        return item.getSrcDocNo().toUpperCase().contains(this.pendingSettlementDetailItems.getSrcDocNo().toUpperCase());
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());


    }

    @Command
    @NotifyChange("showSelectWin")
    public void closeSelectWin() {
        this.showSelectWin = false;
    }

    @Command
    @NotifyChange({"showSelectWin", "agencySettlement"})
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

            for (PendingSettlementDetailInfo detail : this.selectedInfos) {
                PartExpenseItemsInfo expenseListEntity = new PartExpenseItemsInfo();
                expenseListEntity.setExpenseTotal(detail.getExpenseTotal());
                expenseListEntity.setFreightExpense(detail.getFreightExpense());
                expenseListEntity.setOtherExpense(detail.getOtherExpense());
                expenseListEntity.setPartExpense(detail.getPartExpense());
                expenseListEntity.setWorkingExpense(detail.getWorkingExpense());
                expenseListEntity.setSrcDocID(detail.getSrcDocID());
                expenseListEntity.setSrcDocNo(detail.getSrcDocNo());
                expenseListEntity.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + (expenseListEntity.getFreightExpense() == null ? 0.0 : expenseListEntity.getFreightExpense());
                otherExpense = otherExpense + (expenseListEntity.getOtherExpense() == null ? 0.0 : expenseListEntity.getOtherExpense());
                partExpense = partExpense + expenseListEntity.getPartExpense();
                workingExpense = workingExpense + expenseListEntity.getWorkingExpense();
                expenseTotal = expenseTotal + expenseListEntity.getExpenseTotal();
                this.agencySettlement.getPartExpenseItemsInfos().add(expenseListEntity);

//            PendingSettlementDetailInfo detailInfo = pendingSettlementDetailsService.getOneBySrcDocID(expenseListEntity.getSrcDocID());
                detail.setSettlementDocID(agencySettlement.getObjId());
                detail.setSettlementDocNo(agencySettlement.getDocNo());
                detail.setSettlementDocType("配件结算单");
                detail.setStatus(DocStatus.SETTLING.getIndex());
                detail.setOperator(this.getActiveUser().getUsername());
                detail.setOperatorPhone(this.getActiveUser().getPhone());
                detail.setModifierId(getActiveUser().getLogId());
                detail.setModifierName(getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.save(detail);
                updateWarrantyStatus(detail);
            }

            agencySettlement.setPartExpense((agencySettlement.getPartExpense() == null ? 0.0 : agencySettlement.getPartExpense()) + partExpense);
            agencySettlement.setFreightExpense((agencySettlement.getFreightExpense() == null ? 0.0 : agencySettlement.getFreightExpense()) + freightExpense);
            agencySettlement.setOtherExpense((agencySettlement.getOtherExpense() == null ? 0.0 : agencySettlement.getOtherExpense()) + otherExpense);
            agencySettlement.setPunishmentExpense(0.0);
            agencySettlement.setRewardExpense(0.0);
            agencySettlement.setExpenseTotal((agencySettlement.getExpenseTotal() == null ? 0.0 : agencySettlement.getExpenseTotal()) + expenseTotal);
            agencySettlement = agencySettlementService.save(this.agencySettlement);
            showSelectWin = false;
        }
    }


    /**
     * 更新服务单状态
     *
     * @param detail
     */
    private void updateWarrantyStatus(PendingSettlementDetailInfo detail) {
        SupplyInfo supplyInfo = supplyService.findSupplyWithPartsById(detail.getSrcDocID());
        if (supplyInfo != null) {
            supplyInfo.setStatus(detail.getStatus());
            supplyService.save(supplyInfo);
        }

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
                agencySettlementService.desertTask(this.agencySettlement.getObjId());
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
