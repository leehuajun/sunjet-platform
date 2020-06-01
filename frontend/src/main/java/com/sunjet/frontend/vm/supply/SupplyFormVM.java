package com.sunjet.frontend.vm.supply;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.service.supply.SupplyItemService;
import com.sunjet.frontend.service.supply.SupplyService;
import com.sunjet.frontend.service.supply.SupplyWaitingItemService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.util.*;


/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 配件供货单 表单 VM
 */
public class SupplyFormVM extends FormVM {

    @WireVariable
    private SupplyService supplyService;
    @WireVariable
    private SupplyItemService supplyItemService;
    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;
    @WireVariable
    private PartService partService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private AgencyService agencyService;

    @Getter
    @Setter
    private SupplyInfo supplyRequest = new SupplyInfo();
    //@Getter
    //@Setter
    //private List<SupplyItemInfo> items;     //物料子行列表
    @Getter
    @Setter
    private List<SupplyWaitingItemItem> supplyWaitingItemItems = new ArrayList<>();

    @Getter
    @Setter
    private DealerInfo dealer;
    @Getter
    @Setter
    private Set<AgencyInfo> agencyInfoSet = new HashSet<AgencyInfo>();
    @Getter
    @Setter
    private SupplyItemInfo supplyItem;
    //private Window window;
    //private Map<String, Object> variables = new HashMap<>();

    @Init(superclass = true)
    public void init() {
        //this.setBaseService(supplyService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.supplyRequest = supplyService.findSupplyWithPartsById(this.getBusinessId());   //查找一个供货单实体对象
        } else {        // 业务对象和业务对象Id都为空
            this.supplyRequest = new SupplyInfo();
            this.supplyRequest.setSupplyDate(new Date());
            AgencyInfo agency = getActiveUser().getAgency();
            if (agency != null) {
                this.supplyRequest.setAgencyCode(agency.getCode());
                this.supplyRequest.setAgencyName(agency.getName());
                this.supplyRequest.setSubmitterName(getActiveUser().getUsername());
                this.supplyRequest.setAgencyAddress(agency.getAddress());
                this.supplyRequest.setSubmitterPhone(getActiveUser().getPhone());
            }
            this.setReadonly(false);
            //从待供货列表传数据
            List<SupplyWaitingItemItem> supplyWaitingItems = (List<SupplyWaitingItemItem>) Executions.getCurrent().getArg().get("supplyWaitingItems");
            if (supplyWaitingItems != null) {
                //this.setReadonly(true);
                List<SupplyItemInfo> supplyItemEntities = new ArrayList<>();
                this.supplyRequest.getItems().clear();
                double expenseTotal = 0;
                for (SupplyWaitingItemItem item : supplyWaitingItems) {
                    SupplyItemInfo supplyItem = new SupplyItemInfo();
                    supplyItem.setPartCode(item.getPartCode());
                    supplyItem.setPartName(item.getPartName());
                    supplyItem.setAmount(item.getSurplusAmount());
                    //supplyItem.setPrice(item.getPrice());
                    supplyItem.setSupplyWaitingItem(item);
                    supplyItem.setSupplyWaitingItemId(item.getObjId());
                    supplyItem.setSupplyNoticeItemId(item.getSupplyNoticeItemId());

                    List<PartInfo> partEntities = partService.findAllByCode(item.getPartCode());
                    if (partEntities.size() > 0) {
                        supplyItem.setPart(partEntities.get(0));
                        supplyItem.setPartId(partEntities.get(0).getObjId());
                        supplyItem.setPrice(partEntities.get(0).getPrice());
                        supplyItem.setMoney(supplyItem.getAmount() * supplyItem.getPrice());
                        supplyItem.setWarrantyTime(partEntities.get(0).getWarrantyTime());
                        supplyItem.setWarrantyMileage(partEntities.get(0).getWarrantyMileage());
                        supplyItem.setUnit(partEntities.get(0).getUnit());
                    }
                    expenseTotal = expenseTotal + supplyItem.getMoney();

                    this.supplyRequest.setDealerCode(item.getSupplyNoticeItem().getSupplyNotice().getDealerCode());
                    this.supplyRequest.setDealerName(item.getSupplyNoticeItem().getSupplyNotice().getDealerName());
                    this.supplyRequest.setDealerAdderss(item.getSupplyNoticeItem().getSupplyNotice().getDealerAdderss());
                    this.supplyRequest.setReceive(item.getSupplyNoticeItem().getSupplyNotice().getReceive());
                    this.supplyRequest.setOperatorPhone(item.getSupplyNoticeItem().getSupplyNotice().getOperatorPhone());

                    this.supplyRequest.setAgencyName(item.getAgencyName());
                    this.supplyRequest.setAgencyCode(item.getAgencyCode());

                    //变更待返清单数量


                    //可分配数量 = 历史可分配数量 - 本次发货数量
                    item.setSurplusAmount(item.getSurplusAmount() - supplyItem.getAmount());
                    //已发数量 = 需求数量 - 可分配数量
                    item.setSentAmount(item.getSentAmount() + supplyItem.getAmount());
                    supplyWaitingItemService.save(item);

                    supplyItemEntities.add(supplyItem);
                }

                this.supplyRequest.setSubmitter(getActiveUser().getLogId());
                this.supplyRequest.setSubmitterName(getActiveUser().getUsername());
                this.supplyRequest.setSubmitterPhone(getActiveUser().getPhone());

                this.supplyRequest.setExpenseTotal(expenseTotal);
                this.supplyRequest.setPartExpense(expenseTotal);
                this.supplyRequest.setItems(supplyItemEntities);
                this.supplyRequest = supplyService.save(supplyRequest);
                this.updateUIState();
            }

        }
        setActiveUserMsg(this.supplyRequest);
    }


    @Command
    @NotifyChange("*")
    public void submit() {
        if (!StringUtils.isNotBlank(this.supplyRequest.getAgencyCode())) {
            ZkUtils.showInformation("请选择经销商！", "提示");
            return;
        }
        if (!StringUtils.isNotBlank(this.supplyRequest.getDealerCode())) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        if (this.supplyRequest.getItems().size() > 0) {
            for (SupplyItemInfo supplyItemInfo : this.supplyRequest.getItems()) {
                if (supplyItemInfo.getAmount() < 1) {
                    ZkUtils.showInformation("发货数量不能小于1", "提示");
                    return;
                } else {
                    SupplyWaitingItemItem supplyWaitingItem = supplyWaitingItemService.findSupplyWaitingItemById(supplyItemInfo.getSupplyWaitingItemId());
                    if (supplyWaitingItem.getSentAmount() > supplyWaitingItem.getRequestAmount()) {
                        ZkUtils.showInformation("发货数量不能大于可以分配的数量", "提示");
                        return;
                    }
                }
            }
        } else {
            ZkUtils.showInformation("请添加配件", "提示");
            return;
        }
        changeCount();
        this.updateUIState();
        showDialog();
    }

    public void changeCount() {

        if (this.supplyRequest.getObjId() != null) {
            //当主表非首次保存时，子表需要手工关联主表
            //for (SupplyItemInfo Item : supplyRequest.getItems()) {
            //    Item.setSupply(supplyRequest);
            //}


            //调拨供货单子行列表
            List<SupplyItemInfo> supplyItemEntities = supplyService.findBySupplyId(supplyRequest.getObjId());

            for (SupplyItemInfo supplyItem : supplyItemEntities) {
                //获取待发货清单
                SupplyWaitingItemItem supplyWaitingItem = supplyWaitingItemService.findSupplyWaitingItemById(supplyItem.getSupplyWaitingItemId());
                if (supplyWaitingItem != null) {
                    //supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() + supplyItem.getAmount());
                    //supplyWaitingItem.setSentAmount(supplyWaitingItem.getRequestAmount() - supplyWaitingItem.getSurplusAmount());

                    //已发货数量等于  =历史发货数量 - 供货单发货数量
                    supplyWaitingItem.setSentAmount(supplyWaitingItem.getSentAmount() - supplyItem.getAmount());
                    //可分配数量 = 历史可分配数量 + 供货发货数量
                    supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() + supplyItem.getAmount());
                    supplyWaitingItem.setModifiedTime(new Date());
                    supplyWaitingItem.setModifierName(getActiveUser().getUsername());
                    supplyWaitingItem.setModifierId(getActiveUser().getUserId());
                    supplyWaitingItemService.save(supplyWaitingItem);
                }
            }
        }
        changePartAmount();
        //supplyRequest.setItems(supplyService.findBySupplyId(supplyRequest.getObjId()));
        this.supplyRequest = supplyService.save(this.supplyRequest);

        for (SupplyItemInfo item : this.supplyRequest.getItems()) {
            //获取待发货清单
            SupplyWaitingItemItem supplyWaitingItem = supplyWaitingItemService.findSupplyWaitingItemById(item.getSupplyWaitingItemId());
            if (supplyWaitingItem != null) {
                //SupplyWaitingItemItem waitingItem = item.getSupplyWaitingItem();
                supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() - item.getAmount());
                supplyWaitingItem.setSentAmount(supplyWaitingItem.getRequestAmount() - supplyWaitingItem.getSurplusAmount());
                supplyWaitingItem.setModifiedTime(new Date());
                supplyWaitingItem.setModifierName(getActiveUser().getUsername());
                supplyWaitingItem.setModifierId(getActiveUser().getUserId());
                supplyWaitingItemService.save(supplyWaitingItem);
            }
        }

        //BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_LIST, null);
    }

    @Override
    protected Boolean checkValid() {
        //if (this.supplyRequest.getStatus() > 0) {
        //    ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
        //    return false;
        //}
        if (this.supplyRequest.getDealerCode() == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (this.supplyRequest.getItems().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }
        if (this.supplyRequest.getLogistics() == null && org.apache.commons.lang.StringUtils.isBlank(this.supplyRequest.getLogistics())) {
            ZkUtils.showInformation("请填写物流名称！", "提示");
            return false;
        }
        if (org.apache.commons.lang.StringUtils.isBlank(this.supplyRequest.getSubmitterPhone())) {
            ZkUtils.showInformation("请填写联系电话！", "提示");
            return false;
        }

        if (org.apache.commons.lang.StringUtils.isBlank(this.supplyRequest.getLogisticsNum())) {
            ZkUtils.showInformation("请填写物流单号！", "提示");
            return false;
        }
        if (org.apache.commons.lang.StringUtils.isBlank(this.supplyRequest.getTransportmodel())) {
            ZkUtils.showInformation("请填写运输方式！", "提示");
            return false;
        }
        if (org.apache.commons.lang.StringUtils.isBlank(this.supplyRequest.getDealerAdderss())) {
            ZkUtils.showInformation("请填写收货地址！", "提示");
            return false;
        }
//        if(this.supplyRequest.getTransportExpense()==0){
//            ZkUtils.showInformation("请填写运输费用！", "提示");
//            return false;
//        }
        if (this.supplyRequest.getLogisticsfile() == null && org.apache.commons.lang.StringUtils.isBlank(this.supplyRequest.getLogisticsfile())) {
            ZkUtils.showInformation("请上传物流附件！", "提示");
            return false;
        }
        for (SupplyItemInfo supplyItemEntity : this.supplyRequest.getItems()) {
            if (supplyItemEntity.getLogisticsNum() == null || org.apache.commons.lang.StringUtils.isBlank(supplyItemEntity.getLogisticsNum())) {
                ZkUtils.showInformation("请填写物流单号！", "提示");
                return false;
            }
            if (org.apache.commons.lang.StringUtils.isBlank(supplyItemEntity.getPartCode())) {
                ZkUtils.showInformation("请选择配件！", "提示");
                return false;
            }
            if (supplyItemEntity.getAmount() == 0) {
                ZkUtils.showInformation("发货数量为0不能提交！", "提示");
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
        changeCount();
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {

                if (startProcessInstance(this.supplyRequest)) {
                    this.supplyRequest = supplyService.save(this.supplyRequest);
                    flowDocInfo = this.supplyRequest;
                    Map<String, String> map = supplyService.startProcess(this.supplyRequest, getActiveUser());
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

    //确认收货控制
    @Override
    protected void completeTask(String outcome, String comment) throws IOException {
        if (getActiveUser().getDealer() != null && "收货".equals(outcome) && this.supplyRequest.getRcvDate() == null) {
            ZkUtils.showInformation("请选择收货日期", "提示");
            return;
        }
        if (this.supplyRequest.getRcvDate() != null) {
            this.supplyRequest.setReceived(true);
        }
        this.supplyRequest = supplyService.save(this.supplyRequest);
        super.completeTask(outcome, comment);
    }


    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("supplyWaitingItemItems")
    public void searchParts() {
        if (StringUtils.isBlank(this.supplyRequest.getAgencyCode())) {
            ZkUtils.showInformation("请选择合作商", "提示");
            return;
        }
        if (!StringUtils.isNotBlank(this.supplyRequest.getDealerCode())) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        //if (this.keyword.length() >= FILTER_PARTS_LEN) {
        this.supplyWaitingItemItems = supplyWaitingItemService.findAllPartByAgency(this.supplyRequest.getAgencyCode(), this.keyword.trim(), this.supplyRequest.getDealerCode());

        if (this.supplyWaitingItemItems.size() < 1) {
            ZkUtils.showInformation("未查询到相关物料！", "提示");
        }
        //} else {
        //    ZkUtils.showInformation(FILTER_PARTS_ERROR, "提示");
        //    return;
        //}
    }

    @Command
    @NotifyChange("supplyRequest")
    public void selectPart(@BindingParam("model") SupplyWaitingItemItem supplyWaitingItemItem) {
        if (StringUtils.isBlank(this.supplyRequest.getObjId())) {
            ZkUtils.showInformation("请先保存单据", "提示");
            return;
        }
        supplyItem.setPartCode(supplyWaitingItemItem.getPartCode());
        supplyItem.setPartName(supplyWaitingItemItem.getPartName());
        //supplyItem.setUnit(partsEntity.getUnit());
        List<PartInfo> partEntities = partService.findAllByKeyword(supplyWaitingItemItem.getPartCode());
        if (partEntities.size() > 0) {
            supplyItem.setPart(partEntities.get(0));
            supplyItem.setPrice(partEntities.get(0).getPrice());
            supplyItem.setMoney(supplyItem.getAmount() * supplyItem.getPrice());
            supplyItem.setPart(partEntities.get(0));
            supplyItem.setPartId(partEntities.get(0).getObjId());
            supplyItem.setUnit(partEntities.get(0).getUnit());
        }
        supplyItem.setAmount(supplyWaitingItemItem.getSurplusAmount());
        supplyItem.setSupplyWaitingItemId(supplyWaitingItemItem.getObjId());
        supplyItem.setSupplyWaitingItem(supplyWaitingItemItem);
        supplyItem.setSupplyNoticeItemId(supplyWaitingItemItem.getSupplyNoticeItemId());

        //更改待发货数量
        supplyWaitingItemItem.setSurplusAmount(0);
        supplyWaitingItemItem.setSentAmount(supplyWaitingItemItem.getSentAmount() + supplyItem.getAmount());
        supplyWaitingItemService.save(supplyWaitingItemItem);
        this.keyword = "";
        this.supplyWaitingItemItems.clear();
        changePartAmount();
        saveInfo(this.supplyRequest);
        this.supplyRequest = (SupplyInfo) findInfoById(this.supplyRequest.getObjId());
        this.updateUIState();
    }


    @Command
    public void selectSupplyPart(@BindingParam("model") SupplyItemInfo part) {
        this.supplyItem = part;
    }


    /**
     * 添加物料配件
     */
    @Command
    @NotifyChange("supplyRequest")
    public void addItemModel() {
        SupplyItemInfo supplyItemEntity = new SupplyItemInfo();

        supplyItemEntity.setLogisticsNum(this.supplyRequest.getLogisticsNum());
        supplyRequest.getItems().add(supplyItemEntity);
    }

    /**
     * 计算费用
     */
    @Command
    @NotifyChange("supplyRequest")
    public void changePartAmount() {
        double totle = 0;
        for (SupplyItemInfo part : supplyRequest.getItems()) {
            if (part.getPartCode() != null) {
                if (part.getPrice() != null && part.getAmount() != null)
                    part.setMoney(part.getAmount() * part.getPrice());
                totle = totle + part.getAmount() * part.getPrice();
            }
        }
        if (this.supplyRequest.getOtherExpense() == null) {
            this.supplyRequest.setOtherExpense(0.0);
        }
        if (this.supplyRequest.getTransportExpense() == null) {
            this.supplyRequest.setTransportExpense(0.0);
        }
        supplyRequest.setPartExpense(totle);
        totle = totle + supplyRequest.getOtherExpense() + supplyRequest.getTransportExpense();
        supplyRequest.setExpenseTotal(totle);
    }

    /**
     * 关键字搜索经销商列表
     *
     * @param keyword
     */
    @Command
    @NotifyChange("agencyInfoSet")
    public void searchAgencys(@BindingParam("model") String keyword) {
        this.agencyInfoSet.clear();
        this.agencyInfoSet.addAll(agencyService.findAllByKeyword(keyword.trim()));
    }

    @Command
    @NotifyChange("supplyRequest")
    public void clearSelectedAgency() {
        supplyRequest.setAgencyName("");
        supplyRequest.setAgencyCode("");
    }

    @Command
    @NotifyChange("supplyRequest")
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        supplyRequest.setAgencyCode(agency.getCode());
        supplyRequest.setAgencyName(agency.getName());
        supplyRequest.setAgencyAddress(agency.getAddress());
        supplyRequest.setAgencyPhone(agency.getPhone());
    }


    @Command
    @NotifyChange("supplyRequest")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        this.setDealer(dealer);
        supplyRequest.setDealerCode(dealer.getCode());
        supplyRequest.setDealerName(dealer.getName());
        supplyRequest.setDealerAdderss(dealer.getAddress());
        supplyRequest.setOperatorPhone(dealer.getPhone());
        supplyRequest.setReceive(dealer.getStationMaster());
        supplyRequest.setOperatorPhone(dealer.getStationMasterPhone());
    }


    /**
     * 物流单号
     */
    @Command
    @NotifyChange("*")
    public void changeLogisticsNum() {
        for (SupplyItemInfo supplyItemEntity : supplyRequest.getItems()) {
            supplyItemEntity.setLogisticsNum(supplyRequest.getLogisticsNum());
        }
    }


    /**
     * 移除物料配件
     *
     * @param model
     */
    @Command
    @NotifyChange("*")
    public void deleteItem(@BindingParam("model") SupplyItemInfo model) {

        if (StringUtils.isBlank(model.getSupplyWaitingItemId())) {
            if (StringUtils.isBlank(model.getObjId())) {
                this.supplyRequest.getItems().remove(model);
            } else {
                this.supplyRequest.getItems().remove(model);
                supplyItemService.deleteByObjId(model.getObjId());
            }

        } else {


            try {
                ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        int clickedButton = (Integer) event.getData();
                        // 用户点击的是确定按钮
                        if (clickedButton == Messagebox.OK) {
                            SupplyItemInfo supplyItem = supplyItemService.findOneByID(model.getObjId());
                            if (supplyItem != null) {
                                //更新待发货数量
                                SupplyWaitingItemItem supplyWaitingItemItem = supplyWaitingItemService.findSupplyWaitingItemById(supplyItem.getSupplyWaitingItemId());
                                //已发货数量等于  =历史发货数量 - 供货单发货数量
                                supplyWaitingItemItem.setSentAmount(supplyWaitingItemItem.getSentAmount() - supplyItem.getAmount());
                                //可分配数量 = 历史可分配数量 + 供货发货数量
                                supplyWaitingItemItem.setSurplusAmount(supplyWaitingItemItem.getSurplusAmount() + supplyItem.getAmount());
                                supplyWaitingItemService.save(supplyWaitingItemItem);
                                //从内存中删除配件
                                Iterator<SupplyItemInfo> iterator = supplyRequest.getItems().iterator();
                                while (iterator.hasNext()) {
                                    SupplyItemInfo supplyItemInfo = iterator.next();
                                    if (supplyItemInfo == model) {
                                        iterator.remove();
                                    }
                                }
                                //删除记录
                                if (StringUtils.isNotBlank(model.getObjId())) {
                                    supplyItemService.deleteByObjId(model.getObjId());
                                }
                                changePartAmount();
                            }
                            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_WAITING, null);
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


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        //win = (Window) view;
        //this.setEntity(supplyRequest);
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
     * 文件路径
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
        //supplyRequest.setLogisticsfilename(event.getMedia().getName());
        String fileName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_ASM);
        supplyRequest.setLogisticsfile(fileName);
    }

    /**
     * 删除上传文件
     */
    @Command
    @NotifyChange("*")
    public void delUploadFile() {
        supplyRequest.setLogisticsfile("");
    }

    /**
     * 打印页面
     */
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.supplyRequest.getObjId() == null ? "" : this.supplyRequest.getObjId());
        Window window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/supply_printPage.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }

    @Override
    public Boolean getCheckCanPrint() {
        return true;
    }


    @Command
    public void expressPrintReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.supplyRequest.getObjId() == null ? "" : this.supplyRequest.getObjId());
        map.put("dealerName", this.supplyRequest.getDealerName() == null ? "" : this.supplyRequest.getDealerName());
        map.put("dealerAdderss", this.supplyRequest.getDealerAdderss() == null ? "" : this.supplyRequest.getDealerAdderss());
        map.put("operatorPhone", this.supplyRequest.getOperatorPhone() == null ? "" : this.supplyRequest.getOperatorPhone());
        map.put("agencyName", this.supplyRequest.getAgencyName() == null ? "" : this.supplyRequest.getAgencyName());
        map.put("agencyAddress", this.supplyRequest.getAgencyAddress() == null ? "" : this.supplyRequest.getAgencyAddress());
        map.put("agencyPhone", this.supplyRequest.getSubmitterPhone() == null ? "" : this.supplyRequest.getSubmitterPhone());
        map.put("receive", this.supplyRequest.getReceive() == null ? "" : this.supplyRequest.getReceive());
        map.put("submitterName", this.supplyRequest.getSubmitterName() == null ? "" : this.supplyRequest.getSubmitterName());
        Window window = (Window) ZkUtils.createComponents("/views/report/printPage/asm/supply_express_print.zul", null, map);
        window.setTitle("快递单打印");
        window.doModal();

    }

    /**
     * 作废
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                supplyService.desertTask(this.supplyRequest.getObjId());
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
        return supplyService.save((SupplyInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return supplyService.findSupplyWithPartsById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("supplyRequest", "handle", "canHandleTask", "readonly", "canDesertTask", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_WAITING, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

}
