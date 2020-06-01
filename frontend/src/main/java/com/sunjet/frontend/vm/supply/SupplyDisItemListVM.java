package com.sunjet.frontend.vm.supply;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyDisItemItem;
import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.supply.SupplyAllocationService;
import com.sunjet.frontend.service.supply.SupplyDisItemService;
import com.sunjet.frontend.service.supply.SupplyWaitingItemService;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 二次分配
 */
public class SupplyDisItemListVM extends ListVM<SupplyDisItemItem> {

    @WireVariable
    private SupplyDisItemService supplyDisItemService;  //二次分配service
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private SupplyAllocationService supplyAllocationService;    //调拨分配 service
    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;  //待返回清单service


    @Getter
    @Setter
    public List<AgencyInfo> agencies = new ArrayList<>();

    @Getter
    @Setter
    private SupplyDisItemItem supplyDisItemItem = new SupplyDisItemItem();
    @Getter
    @Setter
    private SupplyDisItemItem currentSupplyDisItem = new SupplyDisItemItem();

    @Getter
    @Setter
    private Boolean enableSaveAllocation = false;  // 保存分配信息按钮状态

    @Getter
    @Setter
    private Boolean enableReturnSupply;   //退回按钮


    /**
     * 初始化
     */
    @Init
    public void init() {
        //agencies = getActiveUser().getAgency();
        //绑定新增/修改页面地址
        //this.setFormUrl("/views/asm/supply_notice_form.zul");
        //获取分页数据
        this.setEnableAdd(false);
        //this.setEnableSaveAllocation(true);
        this.setEnableSaveAllocation(hasPermission("SupplySecond:create"));

        //判断权限
        if ("wuling".equals(getActiveUser().getUserType())) {
            List<RoleInfo> roles = getActiveUser().getRoles();
            for (RoleInfo role : roles) {
                if ("二次分配专员".equals(role.getName()) || "配件分配专员".equals(role.getName())) {
                    this.setEnableSaveAllocation(true);
                    this.setEnableReturnSupply(true);
                    break;
                }
            }
        } else {
            System.out.println(getActiveUser().getUserType());
            if ("agency".equals(getActiveUser().getUserType())) {
                this.setEnableSaveAllocation(false);
                this.setEnableReturnSupply(false);
            }
        }

        refreshFirstPage(supplyDisItemItem);
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
        refreshPage(supplyDisItemItem);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = supplyDisItemService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(supplyDisItemItem);
        //刷新分页
        getPageList();
    }


    /**
     * 选择中配件
     *
     * @param item
     */
    @Command
    public void selectSupplyDisItem(@BindingParam("model") SupplyDisItemItem item) {
        if (item != null) {

            currentSupplyDisItem = item;
        }
    }

    /**
     * 关键字搜搜索经销商
     *
     * @param keyword
     */
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

    /**
     * 重选经销商信息
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void clearSelectedAgencyByItem() {
        currentSupplyDisItem.setAgencyName(null);
        currentSupplyDisItem.setAgencyCode(null);
        currentSupplyDisItem.setDistributionAmount(0);
        this.setKeyword("");
    }

    /**
     * 选择经销商
     *
     * @param agency
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void selectAgencyByItem(@BindingParam("model") AgencyInfo agency) {
        currentSupplyDisItem.setAgencyName(agency.getName());
        currentSupplyDisItem.setAgencyCode(agency.getCode());
        currentSupplyDisItem.setDistributionAmount(currentSupplyDisItem.getSurplusAmount());
        this.setKeyword("");
    }

    /**
     * 本次分配
     *
     * @param item
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void sentAmountChange(@BindingParam("model") SupplyDisItemItem item) {
        if (item != null) {
            currentSupplyDisItem = item;
            if (currentSupplyDisItem.getSurplusAmount() < currentSupplyDisItem.getDistributionAmount()) {
                ZkUtils.showInformation("本次分配数不能答应可分配数量", "提示");
                currentSupplyDisItem.setDistributionAmount(0);
            }
        }
    }


    /**
     * 保存分配信息
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void createSupplyAllocation() {
        for (SupplyDisItemItem Item : this.getPageResult().getRows()) {
            SupplyDisItemItem supplyDisItem = Item;
            if (StringUtils.isNotBlank(supplyDisItem.getAgencyCode()) && supplyDisItem.getDistributionAmount() > 0) {
                SupplyWaitingItemItem supplyWaitingItem = new SupplyWaitingItemItem();  //待发货清单vo
                supplyWaitingItem.setAgencyName(supplyDisItem.getAgencyName());
                supplyWaitingItem.setAgencyCode(supplyDisItem.getAgencyCode());
                supplyWaitingItem.setPartCode(supplyDisItem.getPartCode());
                supplyWaitingItem.setPartName(supplyDisItem.getPartName());
                supplyWaitingItem.setRequestAmount(supplyDisItem.getDistributionAmount());
                //supplyWaitingItem.setSentAmount(supplyNoticeItem.getSentAmount());
                supplyWaitingItem.setSurplusAmount(supplyDisItem.getDistributionAmount());
                supplyWaitingItem.setArrivalTime(supplyDisItem.getArrivalTime());
                supplyWaitingItem.setComment(supplyDisItem.getComment());
                supplyWaitingItem.setSupplyNoticeItem(supplyDisItem.getSupplyNoticeItem());
                //写入调拨通知单子行objId
                supplyWaitingItem.setSupplyNoticeItemId(supplyDisItem.getSupplyNoticeItemId());
                supplyWaitingItem.setSupplyDisItem(supplyDisItem);
                //写入二次分配objId
                supplyWaitingItem.setSupplyDisItemId(supplyDisItem.getObjId());
                supplyWaitingItem.setEnabled(true);
                supplyWaitingItem.setCreatedTime(new Date());
                supplyWaitingItem.setCreaterName(getActiveUser().getUsername());
                supplyWaitingItem.setCreaterId(getActiveUser().getUserId());
                supplyWaitingItem.setDealerName(supplyDisItem.getSupplyNoticeItem().getSupplyNotice().getDealerName());
                supplyWaitingItem.setDealerCode(supplyDisItem.getSupplyNoticeItem().getSupplyNotice().getDealerCode());
                supplyWaitingItem.setServiceManager(supplyDisItem.getSupplyNoticeItem().getSupplyNotice().getServiceManager());
                supplyWaitingItemService.save(supplyWaitingItem);
            }

            if (StringUtils.isNotBlank(supplyDisItem.getAgencyCode()) && supplyDisItem.getDistributionAmount() > 0) {
                supplyDisItem.setAgencyCode(null);
                supplyDisItem.setAgencyName(null);
                supplyDisItem.setModifiedTime(new Date());
                supplyDisItem.setSurplusAmount(supplyDisItem.getSurplusAmount() - supplyDisItem.getDistributionAmount());
                supplyDisItem.setSentAmount(supplyDisItem.getRequestAmount() - supplyDisItem.getSurplusAmount());
                supplyDisItem.setDistributionAmount(0.0);
                supplyDisItemService.save(supplyDisItem);
            }
        }
        refreshData();
    }

    /**
     * 退回
     *
     * @param supplyDisItemEntity
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void returnSecondSupply(@BindingParam("entity") SupplyDisItemItem supplyDisItemEntity) {

        SupplyAllocationItem supplyNoticeItemEntity = supplyAllocationService.findOne(supplyDisItemEntity.getSupplyNoticeItemId());

        Double SurplusAmount = supplyNoticeItemEntity.getSurplusAmount() + supplyDisItemEntity.getSurplusAmount();
        supplyNoticeItemEntity.setSurplusAmount(SurplusAmount);
        supplyNoticeItemEntity.setSecondaryDistribution(false);
        supplyAllocationService.save(supplyNoticeItemEntity);
        supplyDisItemService.delete(supplyDisItemEntity.getObjId());
        refreshData();
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_NOTICE_LIST, null);
    }

    @Command
    @NotifyChange({"supplyDisItemItem"})
    public void reset() {
        this.supplyDisItemItem.setPartCode("");
        this.supplyDisItemItem.setEndDate(new Date());
        this.supplyDisItemItem.setStartDate(DateHelper.getFirstOfYear());
        this.supplyDisItemItem.setSrcDocNo("");
        this.supplyDisItemItem.setDocNo("");
    }

    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm(@BindingParam("entity") SupplyDisItemItem supplyDisItemItem) {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        String srcDocID = "";
        try {
            String title = supplyDisItemItem.getSupplyNoticeItem().getSupplyNotice().getSrcDocType();
            srcDocID = supplyDisItemItem.getSupplyNoticeItem().getSupplyNotice().getSrcDocID();
            if ("首保服务单".equals(title)) {
                url = "/views/asm/first_maintenance_form.zul";
            } else if ("三包服务单".equals(title)) {
                url = "/views/asm/warranty_maintenance_form.zul";
            } else if ("活动分配单".equals(title)) {
                url = "/views/asm/activity_distribution_form.zul";
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(srcDocID)) {
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
