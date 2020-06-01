package com.sunjet.frontend.vm.supply;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
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
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 待发货清单
 */
public class SupplyWaitingListVM extends ListVM<SupplyWaitingItemItem> {
    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;
    @WireVariable
    private SupplyAllocationService supplyAllocationService;
    @WireVariable
    private SupplyDisItemService supplyDisItemService;

    @WireVariable
    private AgencyService agencyService;

    @Getter
    @Setter
    private SupplyWaitingItemItem supplyWaitingItemItem = new SupplyWaitingItemItem();
    @Getter
    @Setter
    private List<SupplyWaitingItemItem> supplyWaitingItems = new ArrayList<>();
    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();
    @Getter
    @Setter
    private AgencyInfo agency;
    @Getter
    @Setter
    private String srcDocNo; // 来源单号

    @Getter
    @Setter
    private String supplyNotcieDocNo;//调拨通知单据编号
    @Getter
    @Setter
    private String vin; // 车辆vin

    @Getter
    @Setter
    private Boolean enableReturnSupply;   //退回按钮

    /**
     * 初始化
     */
    @Init(superclass = true)
    public void init() {
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableSaveSupply(false);
        } else {
            this.setEnableSaveSupply(hasPermission("SupplyWaitingItemEntity:create"));
            this.setEnableReturnSupply(hasPermission("SupplyWaitingItemEntity:delete"));
        }

        //if ("wuling".equals(getActiveUser().getUserType())) {
        //    List<RoleInfo> roles = getActiveUser().getRoles();
        //    for (RoleInfo role : roles) {
        //        if ("二次分配专员".equals(role.getName()) || "配件分配专员".equals(role.getName())) {
        //            this.setEnableReturnSupply(true);
        //            break;
        //        }
        //    }
        //
        //} else {
        //    System.out.println(getActiveUser().getUserType());
        //    if ("agency".equals(getActiveUser().getUserType())) {
        //        this.setEnableReturnSupply(true);
        //    }
        //}
        //agencies = getActiveUser().getAgency();
        //绑定新增/修改页面地址
        //this.setFormUrl("/views/asm/supply_notice_form.zul");
        this.setTitle("调拨供货单");

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            supplyWaitingItemItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            supplyWaitingItemItem.setDealerCode(getActiveUser().getDealer().getCode());
            supplyWaitingItemItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        supplyWaitingItemItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(supplyWaitingItemItem);
        //获取分页数据
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
        refreshPage(supplyWaitingItemItem);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = supplyWaitingItemService.getPageList(pageParam);
    }


    /**
     * 刷新
     * 查询
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_SUPPLY_WAITING)
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //this.supplyWaitingItemItem.getObjIds().clear();
        ////来源单据不等于null
        //if (StringUtils.isNotBlank(srcDocNo)) {
        //    List<String> objIds = supplyWaitingItemService.findAllObjIdsBySrcDocNo(srcDocNo);
        //    if (objIds != null && objIds.size() > 0) {
        //        this.supplyWaitingItemItem.setObjIds(objIds);
        //    }
        //}
        ////VIN不等于null
        //if (StringUtils.isNotBlank(vin)) {
        //    List<String> objIds = supplyWaitingItemService.findAllObjIdsByVin(vin);
        //    if (objIds != null && objIds.size() > 0) {
        //        this.supplyWaitingItemItem.setObjIds(objIds);
        //    }
        //
        //}
        ////调拨通知单号不等于null
        //if (StringUtils.isNotBlank(supplyNotcieDocNo)) {
        //    List<String> objIds = supplyWaitingItemService.findAllObjIdsBySupplyNoticeDocNo(supplyNotcieDocNo);
        //    if (objIds != null && objIds.size() > 0) {
        //        this.supplyWaitingItemItem.setObjIds(objIds);
        //    }
        //}
        //清空选择的配件
        this.supplyWaitingItems.clear();
        //设置分页参数
        refreshFirstPage(supplyWaitingItemItem);
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
        } else {   // 五菱用户
            this.agencies = agencyService.findAllByKeyword(keyword.trim());
        }
    }

    @Command
    @NotifyChange({"supplyWaitingItemItem", "agency", "keyword"})
    public void clearSelectedAgency() {
        supplyWaitingItemItem.setAgencyCode("");
        supplyWaitingItemItem.setAgencyName("");
        this.agency = null;

        this.setKeyword("");
    }

    @Command
    @NotifyChange("supplyWaitingItemItem")
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.supplyWaitingItemItem.setAgencyName(agency.getName());
    }

    /**
     * 服务站信息
     *
     * @param dealer
     */
    @Command
    @NotifyChange("supplyWaitingItemItem")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        supplyWaitingItemItem.setDealerCode(dealer.getCode());
        supplyWaitingItemItem.setDealerName(dealer.getName());
    }

    /**
     * 选择
     *
     * @param supplyWaitingItem
     * @param check
     */
    @Command
    public void selectSupplyWaiting(@BindingParam("model") SupplyWaitingItemItem supplyWaitingItem, @BindingParam("check") boolean check) {
        boolean result = true;
        if (check == result && supplyWaitingItem != null) {
            if (this.supplyWaitingItems.size() > 0) {
                for (SupplyWaitingItemItem waitingItem : this.supplyWaitingItems) {

                    if (!waitingItem.getSupplyNoticeItem().getSupplyNotice().getDealerCode().equals(supplyWaitingItem.getSupplyNoticeItem().getSupplyNotice().getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个服务站的单据", "提示");
                        return;
                    }
                }
                this.supplyWaitingItems.add(supplyWaitingItem);
            } else {
                this.supplyWaitingItems.add(supplyWaitingItem);
            }

        } else {
            this.supplyWaitingItems.remove(supplyWaitingItem);
        }
    }


    @Command
    @NotifyChange({"supplyWaitingItemItem", "srcDocNo", "supplyNotcieDocNo"})
    public void reset() {
        if (getActiveUser().getAgency() == null) {
            this.supplyWaitingItemItem.setAgencyName("");
        }
        this.agencies.clear();
        this.supplyWaitingItemItem.setPartCode("");
        if (getActiveUser().getDealer() == null) {
            this.supplyWaitingItemItem.setDealerCode("");
            this.supplyWaitingItemItem.setDealerName("");
        }

        this.dealers.clear();
        this.supplyWaitingItemItem.setEndDate(DateHelper.getEndDateTime());
        this.supplyWaitingItemItem.setStartDate(DateHelper.getFirstOfYear());
        this.supplyWaitingItemItem.setSrcDocNo("");
        this.supplyWaitingItemItem.setDocNo("");

    }


    /**
     * 创建调拨供货单
     */
    @Command
    @NotifyChange({"pageResult", "page"})
    public void createSupply() {
        if (supplyWaitingItems.isEmpty()) {
            ZkUtils.showInformation("没有选择物料！", "提示");
        } else {
            List<SupplyWaitingItemItem> selectList = new ArrayList<>();
            for(SupplyWaitingItemItem sw:supplyWaitingItems){
                List<SupplyWaitingItemItem> supplyWaitingItemItemList = supplyWaitingItemService.findAllPartByAgency(sw.getAgencyCode(),sw.getPartName(),sw.getDealerCode());
                if(supplyWaitingItemItemList.size()>0){
                    for(SupplyWaitingItemItem swii:supplyWaitingItemItemList){
                        if(swii.getSurplusAmount()>0){
                            selectList.add(swii);
                        }
                    }
                }else{
                    ZkUtils.showInformation("待发货清单中没有"+sw.getPartName()+"该物料", "提示");
                }
            }
            if(selectList.size()>=supplyWaitingItems.size()){
                Map<String, Object> map = new HashMap<>();
                map.put("supplyWaitingItems", supplyWaitingItems);
                String url = "/views/asm/supply_form.zul";
                try {
                    ZkTabboxUtil.newTab(URLEncoder.encode(this.getTitle(), "UTF-8"), this.getTitle() + "-" + "新增", "", true, ZkTabboxUtil.OverFlowType.AUTO, url, map);
                } catch (TabDuplicateException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                ZkUtils.showInformation("待发货清单中缺少物料,请重新选择", "提示");
            }
        }
        supplyWaitingItems.clear();
        refreshData();
    }


    //退回待发货
    @Command
    @NotifyChange({"pageResult", "page"})
    public void returnSupply(@BindingParam("entity") SupplyWaitingItemItem Entity) {

        if ("配件销售质保".equals(Entity.getSupplyNoticeItem().getDocType())) {
            ZkUtils.showInformation("此配件属于销售质保，不能退回！", "提示");
            return;
        }
        SupplyWaitingItemItem supplyWaitingItem = supplyWaitingItemService.findSupplyWaitingItemById(Entity.getObjId());
        if (supplyWaitingItem != null) {
            if (supplyWaitingItem.getSentAmount() > 0) {
                ZkUtils.showInformation("此待供货请求已开始供货，不能退回！", "提示");
                return;
            }
            //返回分配单
            if (StringUtils.isBlank(supplyWaitingItem.getSupplyDisItemId())) {
                SupplyAllocationItem supplyAllocationItem = supplyAllocationService.findOne(supplyWaitingItem.getSupplyNoticeItemId());
                supplyAllocationItem.setSurplusAmount(supplyAllocationItem.getSurplusAmount() + supplyWaitingItem.getRequestAmount());
                supplyAllocationItem.setSentAmount(supplyAllocationItem.getRequestAmount() - supplyAllocationItem.getSurplusAmount());
                supplyAllocationItem.setModifiedTime(new Date());
                supplyAllocationItem.setModifierName(getActiveUser().getUsername());
                supplyAllocationItem.setModifierId(getActiveUser().getUserId());
                supplyAllocationService.save(supplyAllocationItem);
            } else {
                //返回二次分配
                SupplyDisItemItem supplyDisItemInfo = supplyDisItemService.findOne(supplyWaitingItem.getSupplyDisItemId());
                supplyDisItemInfo.setSurplusAmount(supplyDisItemInfo.getSurplusAmount() + supplyWaitingItem.getRequestAmount());
                supplyDisItemInfo.setSentAmount(supplyDisItemInfo.getRequestAmount() - supplyDisItemInfo.getSurplusAmount());
                supplyDisItemInfo.setAgencyCode(null);
                supplyDisItemInfo.setAgencyName(null);
                supplyDisItemInfo.setModifiedTime(new Date());
                supplyDisItemInfo.setModifierName(getActiveUser().getUsername());
                supplyDisItemInfo.setModifierId(getActiveUser().getUserId());
                supplyDisItemService.save(supplyDisItemInfo);

            }

            supplyWaitingItemService.delete(supplyWaitingItem.getObjId());
            refreshData(); //刷新列表
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_ALLOCATION_LIST, null);
        }
    }


    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm(@BindingParam("entity") SupplyWaitingItemItem supplyWaitingItemItem) {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        String srcDocID = "";
        try {
            String title = supplyWaitingItemItem.getSupplyNoticeItem().getSupplyNotice().getSrcDocType();
            srcDocID = supplyWaitingItemItem.getSupplyNoticeItem().getSupplyNotice().getSrcDocID();
            if ("首保服务单".equals(title)) {
                url = "/views/asm/first_maintenance_form.zul";
            } else if ("三包服务单".equals(title)) {
                url = "/views/asm/warranty_maintenance_form.zul";
            } else if ("活动分配单".equals(title)) {
                url = "/views/asm/activity_distribution_form.zul";
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
