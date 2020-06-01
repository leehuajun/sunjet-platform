package com.sunjet.frontend.vm.settlement;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.flow.CommentItem;
import com.sunjet.dto.asms.recycle.RecycleInfo;
import com.sunjet.dto.asms.settlement.FreightExpenseInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailItems;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.FreightSettlementService;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.recycle.RecycleService;
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
 * 返回件运费单 表单 VM
 */
public class FreightCostFormVM extends FormVM {

    @WireVariable
    private FreightSettlementService freightSettlementService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private RecycleService recycleService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private DealerService dealerService;

    @Getter
    @Setter
    private FreightSettlementInfo freightSettlement = new FreightSettlementInfo();      //运费结算info
    @Getter
    @Setter
    private List<FreightExpenseInfo> freightExpenseInfos;
    @Getter
    @Setter
    private DealerInfo dealer;

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


    @Init(superclass = true)
    public void init() {
        this.dealer = new DealerInfo();
        //this.setBaseService(freightSettlementService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.freightSettlement = freightSettlementService.findOneById(this.getBusinessId());
            this.dealer = dealerService.findOneByCode(freightSettlement.getDealerCode());

            //if (freightExpenseInfos == null && freightSettlement.getObjId() != null) {
            //    freightExpenseInfos = freightSettlementService.findByFreightSettlementId(freightSettlement.getObjId());
            //    this.freightSettlement.setFreightExpenseInfos(freightExpenseInfos);
            //}

            if (this.freightSettlement.getSubmitter().equals(this.getActiveUser().getLogId())
                    && this.freightSettlement.getStatus() == DocStatus.DRAFT.getIndex()) {
                this.setReadonly(false);
            } else {
                this.setReadonly(true);
            }
        } else {
            List<PendingSettlementDetailInfo> details = (List<PendingSettlementDetailInfo>) Executions.getCurrent().getArg().get("pendingSettlement");

            freightSettlement = new FreightSettlementInfo();
            freightSettlement.setSettlementType("运费结算单");
            freightSettlement.setRequestDate(new Date());
            freightSettlement.setStartDate(DateHelper.getFirstOfMonth());
            freightSettlement.setEndDate(new Date());
            freightSettlement.setOperator(getActiveUser().getUsername());

            if (details != null) {
                double expenseTotal = 0.0;
                double freightExpense = 0.0;
                double otherExpense = 0.0;
                for (PendingSettlementDetailInfo detail : details) {
                    FreightExpenseInfo expenseEntity = new FreightExpenseInfo();
                    expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                    expenseEntity.setFreightExpense(detail.getFreightExpense());
                    expenseEntity.setOtherExpense(detail.getOtherExpense());
                    expenseEntity.setSrcDocID(detail.getSrcDocID());
                    expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                    expenseEntity.setSrcDocType(detail.getSrcDocType());
                    freightExpense = freightExpense + expenseEntity.getFreightExpense();
                    otherExpense = otherExpense + expenseEntity.getOtherExpense();
                    expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                    this.freightSettlement.getFreightExpenseInfos().add(expenseEntity);     //获取运费结算子行
                    if (detail.getDealerCode() != null && this.freightSettlement.getDealerCode() == null) {
                        DealerInfo dealerEntity = dealerService.findOneByCode(detail.getDealerCode());
                        this.setDealer(dealerEntity);
                        this.freightSettlement.setDealerCode(dealerEntity.getCode());
                        this.freightSettlement.setDealerName(dealerEntity.getName());
                        this.freightSettlement.setProvinceName(dealerEntity.getProvinceName());
                    }
                }
                freightSettlement.setFreightExpense(freightExpense);
                freightSettlement.setOtherExpense(otherExpense);
                freightSettlement.setExpenseTotal(expenseTotal);

                freightSettlement = freightSettlementService.save(freightSettlement);

                this.setReadonly(true);
            } else {
                // 业务对象和业务对象Id都为空
                DealerInfo dealer = userService.findOne(getActiveUser().getUserId()).getDealer();
                if (dealer != null) {
                    this.freightSettlement.setDealerCode(dealer.getCode());
                    this.freightSettlement.setDealerName(dealer.getName());
                    this.freightSettlement.setProvinceName(dealer.getProvinceName());
                }
                this.setReadonly(false);
            }
        }
        this.setActiveUserMsg(this.freightSettlement);
        //this.setEntity(freightSettlement);
    }

    /**
     * 启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        //if (!checkValid())
        //    return;
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.freightSettlement)) {
                    this.freightSettlement = freightSettlementService.save(this.freightSettlement);
                    flowDocInfo = this.freightSettlement;
                    Map<String, String> map = freightSettlementService.startProcess(this.freightSettlement, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.updateUIState();
                    this.freightSettlement = freightSettlementService.findOneById(this.freightSettlement.getObjId());

                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });
    }

    @Override
    protected Boolean checkValid() {
        Boolean result = false;
        if (dealer != null) {
            if (freightSettlement.getFreightExpenseInfos().size() < 1) {
                ZkUtils.showInformation("请先获取运费待结算单单！", "提示");
            } else {
                result = true;
            }
        } else {
            ZkUtils.showInformation("请先选择服务站！", "提示");
        }
        return result;
    }

    /**
     * 保存对象
     */
    @Command
    @NotifyChange("*")
    public void submit() {


        if (dealer != null) {
            freightSettlement.setDealerName(dealer.getName());
            freightSettlement.setDealerCode(dealer.getCode());
        }

//        //清空单据对应的结算信息
//        List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getPendingsBySettlementID(freightSettlement.getObjId());
//        if (details != null) {
//            for (PendingSettlementDetailInfo detail : details) {
//                detail.setSettlementDocID(null);
//                detail.setSettlementDocNo(null);
//                detail.setSettlementDocType(null);
//                detail.setOperator(null);
//                detail.setOperatorPhone(null);
////                    detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
////                    detail.setSettlement(false);
//                detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
//                detail.setModifierId(getActiveUser().getLogId());
//                detail.setModifierName(getActiveUser().getUsername());
//                detail.setModifiedTime(new Date());
//                pendingSettlementDetailsService.save(detail);
//            }
//        }

        //for (FreightExpenseInfo expense : freightSettlement.getFreightExpenseInfos()) {
        //    PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(expense.getSrcDocID());
        //    detail.setSettlementDocID(freightSettlement.getObjId());
        //    detail.setSettlementDocNo(freightSettlement.getDocNo());
        //    //detail.setDocNo(freightSettlement.getDocNo());
        //    detail.setSettlementDocType("运费结算单");
        //    detail.setOperator(this.getActiveUser().getUsername());
        //    detail.setOperatorPhone(this.getActiveUser().getPhone());
        //    detail.setStatus(DocStatus.SETTLING.getIndex());
        //    detail.setModifierId(getActiveUser().getLogId());
        //    detail.setModifierName(getActiveUser().getUsername());
        //    detail.setModifiedTime(new Date());
        //    pendingSettlementDetailsService.save(detail);
        //}

        freightSettlement = freightSettlementService.save(this.freightSettlement);  //保存运费结算
        this.updateUIState();
        showDialog();
    }


    /**
     * 调取故障件返回单
     */
    @Command
    @NotifyChange("freightSettlement")
    public void ReadItem() {
        double expenseTotal = 0.0;
        double freightExpense = 0.0;
        double otherExpense = 0.0;
        //如果单据已保存再次取数据，需先清空单据对应的结算信息,再添加原有单据子行
        this.freightSettlement.getFreightExpenseInfos().clear();
        if (StringUtils.isNotBlank(freightSettlement.getObjId())) {
            List<FreightExpenseInfo> freightExpenseInfos = freightSettlementService.findByFreightSettlementId(freightSettlement.getObjId());
            for (FreightExpenseInfo expenseList : freightExpenseInfos) {
                this.freightSettlement.getFreightExpenseInfos().add(expenseList);
                expenseTotal = expenseTotal + expenseList.getExpenseTotal();
                freightExpense = freightExpense + expenseList.getFreightExpense();
                otherExpense = otherExpense + expenseList.getOtherExpense();
            }
        }

        if (dealer != null && StringUtils.isNotBlank(dealer.getCode())) {

            List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getFreightSelttlements(dealer.getCode(), freightSettlement.getStartDate(), freightSettlement.getEndDate());
            for (PendingSettlementDetailInfo detail : details) {
                FreightExpenseInfo expenseEntity = new FreightExpenseInfo();
                expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                expenseEntity.setFreightExpense(detail.getFreightExpense());
                expenseEntity.setOtherExpense(detail.getOtherExpense());
                expenseEntity.setSrcDocID(detail.getSrcDocID());
                expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                expenseEntity.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + expenseEntity.getFreightExpense();
                otherExpense = otherExpense + expenseEntity.getOtherExpense();
                expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                this.freightSettlement.getFreightExpenseInfos().add(expenseEntity);
            }

            freightSettlement.setFreightExpense(freightExpense);
            freightSettlement.setOtherExpense(otherExpense);
            freightSettlement.setExpenseTotal(expenseTotal);
        } else {
            ZkUtils.showInformation("请先选择服务站！", "提示");
        }
    }


    @Command
    @NotifyChange("freightSettlement")
    public void deleteItem(@BindingParam("model") FreightExpenseInfo model) {
        //回写待结算列表
        PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(model.getSrcDocID());
        detail.setSettlementDocID(null);
        detail.setSettlementDocNo(null);
        detail.setSettlementDocType("运费结算单");
        detail.setOperator(null);
        detail.setOperatorPhone(null);
        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
        detail.setModifierId(getActiveUser().getLogId());
        detail.setModifierName(getActiveUser().getUsername());
        detail.setModifiedTime(new Date());
        pendingSettlementDetailsService.save(detail);
        updateWarrantyStatus(detail);
        for (FreightExpenseInfo item : this.freightSettlement.getFreightExpenseInfos()) {
            if (item.getObjId().equals(model.getObjId())) {
                this.freightSettlement.setExpenseTotal(this.freightSettlement.getExpenseTotal() - model.getExpenseTotal());
                this.freightSettlement.setOtherExpense(this.freightSettlement.getOtherExpense() - model.getOtherExpense());
                this.freightSettlement.setFreightExpense(this.freightSettlement.getFreightExpense() - model.getFreightExpense());
                this.freightSettlement.getFreightExpenseInfos().remove(model);
                break;
            }
        }
        freightSettlementService.save(this.freightSettlement);
    }

    @Command
    @NotifyChange("freightSettlement")
    public void changeExpense() {
        freightSettlement.setExpenseTotal(freightSettlement.getOtherExpense() + freightSettlement.getFreightExpense());
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
    @NotifyChange("*")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        this.setKeyword("");
        this.dealers.clear();
        this.dealer = dealer;
        if (dealer.getProvinceName() != null) {
            this.freightSettlement.setDealerCode(dealer.getCode());
            this.freightSettlement.setDealerName(dealer.getName());
            this.freightSettlement.setProvinceName(dealer.getProvinceName());
        }
        if (this.freightSettlement.getFreightExpenseInfos() != null && this.freightSettlement.getFreightExpenseInfos().size() > 0) {
            ZkUtils.showQuestion("更换服务站将会清空已选择的单据是否执行？", "询问", event -> {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    //从内存中删除配件
                    Iterator<FreightExpenseInfo> iterator = this.freightSettlement.getFreightExpenseInfos().iterator();
                    while (iterator.hasNext()) {
                        FreightExpenseInfo freightExpenseInfo = iterator.next();
                        //回写待结算列表
                        PendingSettlementDetailInfo detail = pendingSettlementDetailsService.getOneBySrcDocID(freightExpenseInfo.getSrcDocID());
                        detail.setSettlementDocID(null);
                        detail.setSettlementDocNo(null);
                        detail.setSettlementDocType("运费结算单");
                        detail.setOperator(null);
                        detail.setOperatorPhone(null);
                        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                        detail.setModifierId(getActiveUser().getLogId());
                        detail.setModifierName(getActiveUser().getUsername());
                        detail.setModifiedTime(new Date());
                        pendingSettlementDetailsService.save(detail);
                        //减掉删除的单据费用
                        this.freightSettlement.setExpenseTotal(this.freightSettlement.getExpenseTotal() - freightExpenseInfo.getExpenseTotal());
                        this.freightSettlement.setOtherExpense(this.freightSettlement.getOtherExpense() - freightExpenseInfo.getOtherExpense());
                        this.freightSettlement.setFreightExpense(this.freightSettlement.getFreightExpense() - freightExpenseInfo.getFreightExpense());
                        iterator.remove();
                    }
                    freightSettlementService.save(this.freightSettlement);

                    updateUIState();
                } else {
                    // 用户点击的是取消按钮
                    ZkUtils.showInformation("取消更换", "提示");
                }
            });


        }

    }


    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm(@BindingParam("entity") FreightExpenseInfo freightExpenseInfo) {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        try {

            String srcDocID = freightExpenseInfo.getSrcDocID();
            if (StringUtils.isNotBlank(srcDocID)) {
                paramMap.put("objId", srcDocID);
                paramMap.put("businessId", srcDocID);
            }
            String title = freightExpenseInfo.getSrcDocType();
            url = "/views/asm/recycle_form.zul";
            ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


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
        //if (win != null) {
        //    win.setTitle(win.getTitle() + titleMsg);
        //}
    }

    ///**
    // * 打印页面
    // */
    //@Command
    //public void printReport() {
    //    Map<String, Object> map = new HashMap<>();
    //    map.put("objId", this.freightSettlement.getObjId() == null ? "" : this.freightSettlement.getObjId());
    //    map.put("docNum", this.freightSettlement.getFreightExpenseInfos().size());
    //    map.put("dealerPhone", this.dealer.getPhone());
    //    map.put("serviceManagerName", this.dealer.getServiceManagerName());
    //    Window window = (Window) ZkUtils.createComponents("/views/report/printPage/settlement/freight_settlement_printPage.zul", null, map);
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
        XWPFTemplate template = XWPFTemplate.compile(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/views/report/printPage/asm/freightSettlement.docx").render(new HashMap<String, Object>() {
            {
                put("dealerCode", new TextRenderData(freightSettlement.getDealerCode() == null ? "" : freightSettlement.getDealerCode().toString()));
                put("dealerName", new TextRenderData(freightSettlement.getDealerName() == null ? "" : freightSettlement.getDealerName().toString()));
                put("dealerPhone", new TextRenderData(dealer.getPhone() == null ? "" : dealer.getPhone().toString()));
                put("docNo", new TextRenderData(freightSettlement.getDocNo() == null ? "" : freightSettlement.getDocNo().toString()));
                put("serviceManager", new TextRenderData(dealer.getServiceManagerName() == null ? "" : dealer.getServiceManagerName().toString()));
                put("province", new TextRenderData(dealer.getProvinceName() == null ? "" : dealer.getProvinceName().toString()));
                put("docNum", new TextRenderData(freightSettlement.getFreightExpenseInfos().size() + ""));
                put("submitter", new TextRenderData(freightSettlement.getSubmitterName() == null ? "" : freightSettlement.getSubmitterName().toString()));
                put("submitterTime", new TextRenderData(freightSettlement.getCreatedTime() == null ? "" : DateHelper.dateToString(freightSettlement.getCreatedTime())));
                put("startDate", new TextRenderData(freightSettlement.getStartDate() == null ? "" : DateHelper.dateToString(freightSettlement.getStartDate())));
                put("endDate", new TextRenderData(freightSettlement.getEndDate() == null ? "" : DateHelper.dateToString(freightSettlement.getEndDate())));
                put("freightExpense", new TextRenderData(freightSettlement.getFreightExpense() == null ? "" : freightSettlement.getFreightExpense() + ""));
                put("otherExpense", new TextRenderData(freightSettlement.getOtherExpense() == null ? "" : freightSettlement.getOtherExpense() + ""));
                put("rewardExpense", new TextRenderData(freightSettlement.getRewardExpense() == null ? "" : freightSettlement.getRewardExpense() + ""));
                put("punishmentExpense", new TextRenderData(freightSettlement.getPunishmentExpense() == null ? "" : freightSettlement.getPunishmentExpense() + ""));
                put("tax", new TextRenderData(freightSettlement.getTax() == null ? "" : freightSettlement.getTax() + ""));
                put("expenseTotal", new TextRenderData(freightSettlement.getExpenseTotal() == null ? "" : freightSettlement.getExpenseTotal() + ""));
                List<CommentItem> commentInfoList = processService.findCommentByProcessInstanceId(freightSettlement.getProcessInstanceId());
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
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.freightSettlement.getObjId() == null ? "" : this.freightSettlement.getObjId());
        map.put("startDate", dateToString(this.freightSettlement.getStartDate()));
        map.put("endDate", dateToString(this.freightSettlement.getEndDate()));
        window = (Window) ZkUtils.createComponents("/views/report/printPage/settlement/freight_settlement_detail.zul", null, map);
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
        return freightSettlementService.save((FreightSettlementInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return freightSettlementService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("freightSettlement", "handle", "canDesertTask", "canHandleTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FREIGHT_COST_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

    @Command
    @NotifyChange({"showSelectWin", "pendingItems", "pendingSettlementDetailItems"})
    public void selectPendingBills() {
        if (StringUtils.isEmpty(this.freightSettlement.getObjId())) {
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

        this.originPendingItems = pendingSettlementDetailsService.getFreightSelttlements(dealer.getCode(), pendingSettlementDetailItems.getStartDate(), pendingSettlementDetailItems.getEndDate());
        if (StringUtils.isNotBlank(this.pendingSettlementDetailItems.getSrcDocNo())) {
            this.pendingItems = this.originPendingItems.stream()
                    .filter(item -> item.getSrcDocNo().toUpperCase().contains(this.pendingSettlementDetailItems.getSrcDocNo().toUpperCase()))
                    .collect(Collectors.toList());
        } else {
            this.pendingItems = this.originPendingItems;
        }

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
    @NotifyChange("pendingItems")
    public void refreshList() {
        this.originPendingItems = pendingSettlementDetailsService.getFreightSelttlements(dealer.getCode(), pendingSettlementDetailItems.getStartDate(), pendingSettlementDetailItems.getEndDate());

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

    @Command
    @NotifyChange({"showSelectWin", "freightSettlement"})
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

            for (PendingSettlementDetailInfo detail : this.selectedInfos) {
                FreightExpenseInfo freightExpenseInfo = new FreightExpenseInfo();
                freightExpenseInfo.setExpenseTotal(detail.getExpenseTotal());
                freightExpenseInfo.setFreightExpense(detail.getFreightExpense());
                freightExpenseInfo.setOtherExpense(detail.getOtherExpense());
                freightExpenseInfo.setSrcDocID(detail.getSrcDocID());
                freightExpenseInfo.setSrcDocNo(detail.getSrcDocNo());
                freightExpenseInfo.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + (freightExpenseInfo.getFreightExpense() == null ? 0.0 : freightExpenseInfo.getFreightExpense());
                otherExpense = otherExpense + (freightExpenseInfo.getOtherExpense() == null ? 0.0 : freightExpenseInfo.getOtherExpense());
                expenseTotal = expenseTotal + freightExpenseInfo.getExpenseTotal();
                this.freightSettlement.getFreightExpenseInfos().add(freightExpenseInfo);

                detail.setSettlementDocID(freightSettlement.getObjId());
                detail.setSettlementDocNo(freightSettlement.getDocNo());
                detail.setSettlementDocType("运费结算单");
                detail.setStatus(DocStatus.SETTLING.getIndex());
                detail.setOperator(this.getActiveUser().getUsername());
                detail.setOperatorPhone(this.getActiveUser().getPhone());
                detail.setModifierId(getActiveUser().getLogId());
                detail.setModifierName(getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.save(detail);
                updateWarrantyStatus(detail);
            }

            freightSettlement.setFreightExpense((freightSettlement.getFreightExpense() == null ? 0.0 : freightSettlement.getFreightExpense()) + freightExpense);
            freightSettlement.setOtherExpense((freightSettlement.getOtherExpense() == null ? 0.0 : freightSettlement.getOtherExpense()) + otherExpense);
            freightSettlement.setPunishmentExpense(0.0);
            freightSettlement.setRewardExpense(0.0);
            freightSettlement.setExpenseTotal((freightSettlement.getExpenseTotal() == null ? 0.0 : freightSettlement.getExpenseTotal()) + expenseTotal);
            freightSettlement = freightSettlementService.save(this.freightSettlement);
            showSelectWin = false;
        }
    }

    /**
     * 更改故障件返回单状态
     *
     * @param detail
     */
    private void updateWarrantyStatus(PendingSettlementDetailInfo detail) {
        if ("故障件返回单".equals(detail.getSrcDocType())) {
            RecycleInfo recycleInfo = recycleService.findOneById(detail.getSrcDocID());
            if (recycleInfo != null) {
                recycleInfo.setStatus(detail.getStatus());
                recycleService.save(recycleInfo);
            }

        }

    }

    @Command
    @NotifyChange({"pendingSettlementDetailItems", "documentStatuses"})
    public void reset() {
        this.pendingSettlementDetailItems.setSrcDocNo("");
        this.pendingSettlementDetailItems.setEndDate(DateHelper.getEndDateTime());
        this.pendingSettlementDetailItems.setStartDate(DateHelper.getFirstOfYear());
    }

    /**
     * 作废单据
     */
    @Command
    public void desertTask() {

        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                freightSettlementService.desertTask(this.freightSettlement.getObjId());
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
