package com.sunjet.frontend.vm.supply;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.asms.supply.SupplyItem;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.activity.ActivityNoticeService;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.supply.SupplyItemService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
import com.sunjet.frontend.service.supply.SupplyService;
import com.sunjet.frontend.service.supply.SupplyWaitingItemService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调拨供货单
 */
public class SupplyListVM extends ListVM<SupplyItem> {
    @WireVariable
    private SupplyService supplyService;

    @WireVariable
    private SupplyItemService supplyItemService;
    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private SupplyNoticeService supplyNoticeService;  //调拨通知单
    @WireVariable
    private ActivityNoticeService activityNoticeService; //活动通知单
    @WireVariable


    @Getter
    @Setter
    private SupplyItem supplyItem = new SupplyItem();

    @Getter
    @Setter
    private List<AgencyInfo> agencyList = new ArrayList<>();


    /**
     * 初始化
     */
    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("SupplyEntity:create"));
            this.setEnableUpdate(hasPermission("SupplyEntity:modify"));
            this.setEnableDelete(hasPermission("SupplyEntity:delete"));
        }
        //绑定新增/修改页面地址
        this.setTitle("调拨供货单");
        this.setFormUrl("/views/asm/supply_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        supplyItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            supplyItem.setAgencyCode(getActiveUser().getAgency().getCode());
            this.supplyItem.setAgencyName(getActiveUser().getAgency().getCode());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            supplyItem.setDealerCode(getActiveUser().getDealer().getCode());
            supplyItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        //supplyItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(supplyItem);
        //获取分页数据
        getPageList();
    }


    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_SUPPLY_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
    }

    /**
     * 点击下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        //设置分页参数
        refreshPage(supplyItem);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = supplyService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(supplyItem);
        //刷新分页
        getPageList();
    }

    /**
     * 选择服务站信息
     *
     * @param model
     */
    @Command
    @NotifyChange("supplyItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        supplyItem.setDealerCode(model.getCode());
        supplyItem.setDealerName(model.getName());
    }

    /**
     * 删除对象
     *
     * @param objId
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId) {
        ZkUtils.showQuestion("您确定删除该对象？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {

                SupplyInfo supplyInfo = supplyService.findSupplyWithPartsById(objId);
                if (supplyInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(supplyInfo.getCreaterId()) || getActiveUser().getLogId().equals(supplyInfo.getSubmitter())) {
                        for (SupplyItemInfo item : supplyInfo.getItems()) {
                            SupplyWaitingItemItem supplyWaitingItem = supplyWaitingItemService.findSupplyWaitingItemById(item.getSupplyWaitingItemId());
                            if (supplyWaitingItem != null) {
                                //待发货单需求数量 =  待发货可分配数量 + 供货单发送数量
                                supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() + item.getAmount());
                                //待发货单发货数量 = 待发货单的已发数量 - 供货单发送数量
                                supplyWaitingItem.setSentAmount(supplyWaitingItem.getSentAmount() - item.getAmount());
                                supplyWaitingItem.setModifiedTime(new Date());
                                supplyWaitingItem.setModifierName(getActiveUser().getUsername());
                                supplyWaitingItem.setModifierId(getActiveUser().getUserId());
                                supplyWaitingItemService.save(supplyWaitingItem);
                            }
                        }

                        supplyItemService.deleteBySupplyObjId(supplyInfo.getObjId());
                        supplyService.delete(supplyInfo.getObjId());
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_LIST, null);
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_WAITING, null);
                    } else {
                        ZkUtils.showInformation("该单据不是你创建的，不能删除！", "提示");
                    }
                } else {
                    ZkUtils.showInformation("非草稿单据不能删除！", "提示");
                }
                BindUtils.postNotifyChange(null, null, this, "pageResult");
                getPageList();
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });
    }

    /**
     * 选中单据状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.supplyItem.setStatus(selectedStatus.getIndex());
    }

    @Command
    @NotifyChange({"supplyItem", "agencyList", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.supplyItem.setDealerCode("");
            this.supplyItem.setDealerName("");
        }
        this.getDealers().clear();
        if (getActiveUser().getAgency() == null) {
            this.supplyItem.setAgencyCode("");
            this.supplyItem.setAgencyName("");
            this.getAgencyList().clear();
        }
        this.supplyItem.setDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.supplyItem.setStatus(DocStatus.ALL.getIndex());
        this.supplyItem.setEndDate(new Date());
        this.supplyItem.setStartDate(DateHelper.getFirstOfYear());
        //this.supplyItem.setSrcDocNo("");
        //this.supplyItem.setActivityNoticeDocNo("");
    }


    /**
     * 搜索合作商
     */
    @Command
    @NotifyChange("agencyList")
    public void searchAgencies() {
        if (getActiveUser().getAgency() != null) {
            agencyList = agencyService.findAllByKeyword(getActiveUser().getAgency().getCode());
        } else {
            agencyList = agencyService.findAllByKeyword(keyword);
        }
    }

    /**
     * 选择合作商
     */
    @Command
    @NotifyChange({"agencyList", "keyword", "supplyItem"})
    public void selectAgency(@BindingParam("model") AgencyInfo agencyInfo) {
        this.supplyItem.setAgencyName(agencyInfo.getName());
        this.supplyItem.setAgencyCode(agencyInfo.getCode());

    }


    /**
     * 清除合作商
     */
    @Command
    @NotifyChange({"agencyList", "keyword", "supplyItem"})
    public void clearSelectedAgency() {
        agencyList.clear();
        keyword = "";
        this.supplyItem.setAgencyCode("");
        this.supplyItem.setAgencyName("");
    }

    /**
     * 选择来源单号
     */
    @Command
    @NotifyChange("*")
    public void selectSrcDoc() {


    }


}
