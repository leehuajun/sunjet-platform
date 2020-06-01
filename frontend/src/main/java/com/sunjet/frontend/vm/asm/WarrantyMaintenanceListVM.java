package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceItem;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;

import java.util.Date;
import java.util.List;

/**
 * 三包服务单
 * Created by  on 2017/7/13.
 */
public class WarrantyMaintenanceListVM extends ListVM<WarrantyMaintenanceItem> {


    @WireVariable
    WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    SupplyNoticeService supplyNoticeService;

    @Getter
    @Setter
    private WarrantyMaintenanceItem warrantyMaintenanceItem = new WarrantyMaintenanceItem();

    @Getter
    @Setter
    private DealerInfo dealerInfo = new DealerInfo();

    @Getter
    @Setter
    protected String selectVin = "";


    @Init(superclass = true)
    public void Init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("WarrantyMaintenanceEntity:create"));
            this.setEnableUpdate(hasPermission("WarrantyMaintenanceEntity:modify"));
            this.setEnableDelete(hasPermission("WarrantyMaintenanceEntity:delete"));
        }
        this.setTitle("三包服务单");
        this.setFormUrl("/views/asm/warranty_maintenance_form.zul");
        warrantyMaintenanceItem.setStatus(DocStatus.ALL.getIndex());
        if (getActiveUser().getAgency() != null) {
            //合作商
            //warrantyMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            warrantyMaintenanceItem.setDealerCode(getActiveUser().getDealer().getCode());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        warrantyMaintenanceItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }
        refreshFirstPage(warrantyMaintenanceItem);
        getPageList();

    }


    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(warrantyMaintenanceItem, Order.DESC, "createdTime");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = warrantyMaintenanceService.getPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(warrantyMaintenanceItem);
        getPageList();
    }


    @Command
    @NotifyChange("warrantyMaintenanceItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        warrantyMaintenanceItem.setDealerCode(model.getCode());
        warrantyMaintenanceItem.setDealerName(model.getName());

    }

    /**
     * 选中单据类型
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.warrantyMaintenanceItem.setStatus(selectedStatus.getIndex());
    }

    /**
     * 删除对象
     *
     * @param objId
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId, @BindingParam("tabs") List<Tab> tabList) {
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                WarrantyMaintenanceInfo info = warrantyMaintenanceService.findOneById(objId);
                if (StringUtils.isNotBlank(info.getSupplyNoticeId())) {
                    SupplyNoticeInfo supplyNoticeInfo = this.supplyNoticeService.findByOne(info.getSupplyNoticeId());
                    if (supplyNoticeInfo != null) {
                        //如果调拨通知单已经作废可以删除
                        if (!supplyNoticeInfo.getStatus().equals(DocStatus.OBSOLETE.getIndex())) {
                            ZkUtils.showInformation("已生成调拨通知单不能删除！", "提示");
                            return;
                        }
                        supplyNoticeService.delete(supplyNoticeInfo.getObjId());
                    }
                }
                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getLogId().equals(info.getSubmitter())) {
                        warrantyMaintenanceService.delete(objId);
                        ZkTabboxUtil.closeOneByObjId(tabList, objId);
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
     * 刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_WARRANTY_MAINTENANCE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        warrantyMaintenanceItem.setStatus(DocStatus.ALL.getIndex());
        refreshFirstPage(warrantyMaintenanceItem);
        getPageList();
    }


    @Command
    @NotifyChange({"warrantyMaintenanceItem", "documentStatuses", "selectVin"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.warrantyMaintenanceItem.setDealerCode("");
            this.warrantyMaintenanceItem.setDealerName("");
        }
        this.getDealers().clear();
        this.warrantyMaintenanceItem.setDocNo("");
        this.warrantyMaintenanceItem.setServiceManager("");
        this.setSelectedStatus(DocStatus.ALL);
        this.warrantyMaintenanceItem.setSender("");
        this.warrantyMaintenanceItem.setPlate("");
        this.warrantyMaintenanceItem.setStatus(DocStatus.ALL.getIndex());
        this.warrantyMaintenanceItem.setEndDate(new Date());
        this.warrantyMaintenanceItem.setStartDate(DateHelper.getFirstOfYear());
        this.warrantyMaintenanceItem.setVehicleId("");
        this.selectVin = "";
    }


    /**
     * 选择车辆
     */
    @Command
    @NotifyChange("*")
    public void selectVehicle(@BindingParam("model") VehicleInfo vehicleInfo) {
        this.warrantyMaintenanceItem.setVehicleId(vehicleInfo.getObjId());
        this.selectVin = vehicleInfo.getVin();
    }

    @Override
    @Command
    @NotifyChange({"warrantyMaintenanceItem", "keyword"})
    public void clearSelectedDealer() {
        this.warrantyMaintenanceItem.setDealerCode(null);
        this.warrantyMaintenanceItem.setDealerName(null);
        this.keyword = "";
    }
}
