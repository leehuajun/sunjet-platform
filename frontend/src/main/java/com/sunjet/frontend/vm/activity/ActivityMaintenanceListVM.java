package com.sunjet.frontend.vm.activity;


import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.activity.ActivityMaintenanceItem;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.activity.ActivityMaintenanceService;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
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
import org.zkoss.zul.Tab;

import java.util.Date;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 服务活动通知单 列表 VM
 */
public class ActivityMaintenanceListVM extends ListVM<ActivityMaintenanceItem> {

    @WireVariable
    private ActivityMaintenanceService activityMaintenanceService;

    @Getter
    @Setter
    private ActivityMaintenanceItem activityMaintenanceItem = new ActivityMaintenanceItem();

    @WireVariable
    private ActivityVehicleService activityVehicleService;

    @Getter
    @Setter
    public DocStatus status = DocStatus.ALL;


    @Getter
    @Setter
    private String dealer;


    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("ActivityMaintenanceEntity:create"));
            this.setEnableUpdate(hasPermission("ActivityMaintenanceEntity:modify"));
            this.setEnableDelete(hasPermission("ActivityMaintenanceEntity:delete"));
        }
        this.setTitle("活动服务单");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        this.setFormUrl("/views/asm/activity_maintenance_form.zul");
        activityMaintenanceItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            activityMaintenanceItem.setDealerCode(getActiveUser().getDealer().getCode());
            activityMaintenanceItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        activityMaintenanceItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(activityMaintenanceItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = activityMaintenanceService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(activityMaintenanceItem);
        getPageList();
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(activityMaintenanceItem);
        getPageList();
    }

    /**
     * 关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_MAINTENANCE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
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
                ActivityMaintenanceInfo info = activityMaintenanceService.findOneById(objId);
                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (info.getSubmitter().equals(getActiveUser().getLogId())) {
                        //ActivityVehicleItem vehicleInfo = activityVehicleService.findOneById(info.getActivityVehicleId());
                        //if (vehicleInfo != null) {
                        //    vehicleInfo.setRepair(false);
                        //    vehicleInfo.setActivityMaintenanceId(null);
                        //    activityVehicleService.save(vehicleInfo);
                        //}
                        activityMaintenanceService.deleteByObjId(objId);
                        ZkTabboxUtil.closeOneByObjId(tabList, objId);
                        //getPageList();
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_MAINTENANCE_LIST, null);
                    } else {
                        ZkUtils.showInformation("非创建人不能删除！", "提示");
                    }

                } else {
                    ZkUtils.showInformation("非草稿单据不能删除！", "提示");
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });

    }

    /**
     * 选择状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.activityMaintenanceItem.setStatus(selectedStatus.getIndex());
    }

    /**
     * 选择服务站
     *
     * @param model
     */
    @Command
    @NotifyChange("activityMaintenanceItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        activityMaintenanceItem.setDealerCode(model.getCode());
        activityMaintenanceItem.setDealerName(model.getName());

    }

    @Command
    @NotifyChange({"activityMaintenanceItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.activityMaintenanceItem.setDealerCode("");
            this.activityMaintenanceItem.setDealerName("");
        }

        this.getDealers().clear();
        this.activityMaintenanceItem.setDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.activityMaintenanceItem.setStatus(DocStatus.ALL.getIndex());
        this.activityMaintenanceItem.setAadDocNo("");
        this.activityMaintenanceItem.setAanDocNo("");
        this.activityMaintenanceItem.setSender("");
        this.activityMaintenanceItem.setPlate("");
        this.activityMaintenanceItem.setEndDate(new Date());
        this.activityMaintenanceItem.setStartDate(DateHelper.getFirstOfYear());
        this.activityMaintenanceItem.setVin("");
        //this.vin = "";
    }

    @Override
    @Command
    @NotifyChange({"activityMaintenanceItem", "keyword"})
    public void clearSelectedDealer() {
        this.activityMaintenanceItem.setDealerCode(null);
        this.activityMaintenanceItem.setDealerName(null);
        this.keyword = "";
    }

}