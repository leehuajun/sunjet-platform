package com.sunjet.frontend.vm.supply;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyDisItemItem;
import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.supply.SupplyAllocationService;
import com.sunjet.frontend.service.supply.SupplyDisItemService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
import com.sunjet.frontend.service.supply.SupplyWaitingItemService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调拨分配
 */
public class SupplyAllocationListVM extends ListVM<SupplyAllocationItem> {

    @WireVariable
    private SupplyAllocationService supplyAllocationService;    //调拨分配 service
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;  //待发货清单 service
    @WireVariable
    private SupplyDisItemService supplyDisItemService;  //二次分配 service
    @WireVariable
    private SupplyNoticeService supplyNoticeService;

    @Getter
    @Setter
    public List<AgencyInfo> agencies = new ArrayList<>();

    @Getter
    @Setter
    private SupplyAllocationItem supplyAllocationItem = new SupplyAllocationItem();

    @Getter
    @Setter
    private Boolean enableSaveAllocation = false;  // 保存分配信息按钮状态

    @Getter
    @Setter
    private SupplyAllocationItem selectPart = new SupplyAllocationItem();

    @Getter
    @Setter
    private String supplyNoticeDocNo = ""; //调拨通知单编号

    @Getter
    @Setter
    private String vin = ""; // vin

    //@Getter
    //@Setter
    //private SupplyNoticeItemInfo currentsupplyNoticeItem = new SupplyNoticeItemInfo();

    /**
     * 初始化
     */
    @Init
    public void init() {
        this.setEnableAdd(false);
        this.setEnableSaveAllocation(hasPermission("SupplyDisItemEntity:create"));
        supplyAllocationItem.setAllocatedStatus("");

        //获取分页数据
        refreshFirstPage(supplyAllocationItem);
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
        refreshPage(supplyAllocationItem);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = supplyAllocationService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_SUPPLY_ALLOCATION_LIST)
    @Command
    @NotifyChange({"pageResult", "page"})
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(supplyAllocationItem);
        //刷新分页
        getPageList();
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

    //清除已选择合作商信息
    @Command
    @NotifyChange({"pageResult", "page"})
    public void clearSelectedAgencyByItem() {
        selectPart.setAgencyName(null);
        selectPart.setAgencyCode(null);
        selectPart.setDistributionAmount(0);
        this.setKeyword("");
    }

    /**
     * 选择一个经销商
     *
     * @param agency
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void selectAgencyByItem(@BindingParam("model") AgencyInfo agency) {
        selectPart.setAgencyName(agency.getName());
        selectPart.setAgencyCode(agency.getCode());
        selectPart.setSecondaryDistribution(false);
        selectPart.setDistributionAmount(selectPart.getSurplusAmount());
        this.setKeyword("");
    }


    /**
     * 保存分配信息
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void createSupplyAllocation() {
        for (SupplyAllocationItem Item : this.getPageResult().getRows()) {
            SupplyAllocationItem supplyNoticeItem = Item;    //调拨分配单清单

            if (StringUtils.isNotBlank(supplyNoticeItem.getAgencyCode()) && !supplyNoticeItem.getSecondaryDistribution() && supplyNoticeItem.getDistributionAmount() > 0) {
                SupplyWaitingItemItem supplyWaitingItem = new SupplyWaitingItemItem();  //待发货清单
                supplyWaitingItem.setAgencyName(supplyNoticeItem.getAgencyName());
                supplyWaitingItem.setAgencyCode(supplyNoticeItem.getAgencyCode());
                supplyWaitingItem.setPartCode(supplyNoticeItem.getPartCode());
                supplyWaitingItem.setPartName(supplyNoticeItem.getPartName());
                supplyWaitingItem.setRequestAmount(supplyNoticeItem.getDistributionAmount());
                supplyWaitingItem.setSupplyNoticeItemId(supplyNoticeItem.getObjId());
                //supplyWaitingItem.setSentAmount(supplyNoticeItem.getSentAmount());
                supplyWaitingItem.setSurplusAmount(supplyNoticeItem.getDistributionAmount());
                supplyWaitingItem.setArrivalTime(supplyNoticeItem.getArrivalTime());
                supplyWaitingItem.setComment(supplyNoticeItem.getComment());
                supplyWaitingItem.setSupplyNoticeItem(supplyNoticeItem);
                supplyWaitingItem.setEnabled(true);
                supplyWaitingItem.setCreatedTime(new Date());
                supplyWaitingItem.setCreaterName(getActiveUser().getUsername());
                supplyWaitingItem.setCreaterId(getActiveUser().getUserId());
                supplyWaitingItem.setDealerCode(supplyNoticeItem.getSupplyNotice().getDealerCode());
                supplyWaitingItem.setDealerName(supplyNoticeItem.getSupplyNotice().getDealerName());
                supplyWaitingItem.setServiceManager(supplyNoticeItem.getSupplyNotice().getServiceManager());
                supplyWaitingItemService.save(supplyWaitingItem);
            }
            //二次分配
            if (StringUtils.isBlank(supplyNoticeItem.getAgencyCode()) && supplyNoticeItem.getSecondaryDistribution() && supplyNoticeItem.getDistributionAmount() > 0) {
                SupplyDisItemItem supplyDisItem = new SupplyDisItemItem();    //二次分配
                //supplyDisItem.setAgencyName(supplyNoticeItem.getAgencyName());
                //supplyDisItem.setAgencyCode(supplyNoticeItem.getAgencyCode());
                supplyDisItem.setPartCode(supplyNoticeItem.getPartCode());
                supplyDisItem.setPartName(supplyNoticeItem.getPartName());
                supplyDisItem.setRequestAmount(supplyNoticeItem.getDistributionAmount());
                //supplyDisItem.setSentAmount(supplyNoticeItem.getSentAmount());
                supplyDisItem.setSurplusAmount(supplyNoticeItem.getDistributionAmount());
                supplyDisItem.setArrivalTime(supplyNoticeItem.getArrivalTime());
                supplyDisItem.setComment(supplyNoticeItem.getComment());
                supplyDisItem.setSupplyNoticeItem(supplyNoticeItem);
                supplyDisItem.setEnabled(true);
                supplyDisItem.setCreatedTime(new Date());
                supplyDisItem.setCreaterName(getActiveUser().getUsername());
                supplyDisItem.setCreaterId(getActiveUser().getUserId());
                supplyDisItem.setSupplyNoticeItemId(supplyNoticeItem.getObjId());
                supplyDisItemService.save(supplyDisItem);
            }

            //计算剩余分配数量
            if (StringUtils.isNotBlank(supplyNoticeItem.getAgencyCode()) || supplyNoticeItem.getSecondaryDistribution() && supplyNoticeItem.getDistributionAmount() > 0) {
                supplyNoticeItem.setModifiedTime(new Date());
                supplyNoticeItem.setAgencyName(null);
                supplyNoticeItem.setAgencyCode(null);
                supplyNoticeItem.setSurplusAmount(supplyNoticeItem.getSurplusAmount() - supplyNoticeItem.getDistributionAmount());
                supplyNoticeItem.setSentAmount(supplyNoticeItem.getRequestAmount() - supplyNoticeItem.getSurplusAmount());
                supplyNoticeItem.setDistributionAmount(0.0);

                supplyAllocationService.save(supplyNoticeItem);
            }
        }
        refreshData();  //刷新列表
        showDialog();
    }

    /**
     * @param item
     */
    @Command
    public void selectSupplyNoticeItem(@BindingParam("model") SupplyAllocationItem item) {
        if (item != null) {
            selectPart = item;
        }
    }

    /**
     * 二次分配
     *
     * @param item
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void secondAllocation(@BindingParam("model") SupplyAllocationItem item) {
        if (item != null) {
            selectPart = item;
            if (item.getSecondaryDistribution()) {
                selectPart.setAgencyName(null);
                selectPart.setAgencyCode(null);
                selectPart.setDistributionAmount(selectPart.getSurplusAmount());
                this.setKeyword("");
            } else {
                selectPart.setDistributionAmount(0);
            }
        }
    }


    /**
     * 本次分配
     *
     * @param item
     */
    @Command
    @NotifyChange({"pageResult"})
    public void sentAmountChange(@BindingParam("model") SupplyAllocationItem item) {
        if (item != null) {
            selectPart = item;
            if (selectPart.getSurplusAmount() < selectPart.getDistributionAmount()) {
                ZkUtils.showInformation("本次分配数不能大于可分配数量", "提示");
                selectPart.setDistributionAmount(0);
            }
        }
    }

    /**
     * 重置查询条件
     */
    @Command
    @NotifyChange({"supplyAllocationItem", "vin", "supplyNoticeDocNo"})
    public void reset() {
        this.supplyAllocationItem.setSrcDocNo("");
        this.supplyAllocationItem.setPartCode("");
        this.supplyAllocationItem.setEndDate(new Date());
        this.supplyAllocationItem.setStartDate(DateHelper.getFirstOfYear());
        this.supplyAllocationItem.setDocNo("");
        this.supplyNoticeDocNo = "";
        this.vin = "";
    }


    /**
     * 判断已分配完不能编辑
     *
     * @param surplusAmount
     * @return
     */
    public Boolean canAllocation(Double surplusAmount) {
        if (surplusAmount <= 0) {
            return false;
        }
        return true;
    }


    /**
     * 分配状态
     *
     * @return
     */
    public String allocationStatus(String allocatedStatus) {
        String status = "";
        if ("finishAllocated".equals(allocatedStatus)) {
            status = "已分配";
        } else if ("allocated".equals(allocatedStatus)) {
            status = "待分配";
        } else if ("assigning".equals(allocatedStatus)) {
            status = "分配中";
        }
        return status;
    }

    public String getAllocationStatusColor(String allocatedStatus) {
        String color = "";
        if ("finishAllocated".equals(allocatedStatus)) {
            color = "color:#006600;font-weight:700";
        } else if ("allocated".equals(allocatedStatus)) {
            color = "color:#FF9900;font-weight:700";
        } else if ("assigning".equals(allocatedStatus)) {
            color = "color:#F2DA0C;font-weight:700";
        }
        return color;
    }


}
