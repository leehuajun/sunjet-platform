package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.asm.CommissionPartItem;
import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.frontend.service.activity.ActivityMaintenanceService;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.service.asm.*;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyf
 * @create 2017-10-23
 * <p>
 * 维修历史 VM
 */
public class MaintenanceHistoryVM extends ListVM {

    @WireVariable
    private FirstMaintenanceService firstMaintenanceService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private ActivityMaintenanceService activityMaintenanceService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private CommissionPartService commissionPartService;

    @Getter
    @Setter
    private VehicleInfo vehicleInfo;

    @Getter
    @Setter
    private FirstMaintenanceInfo firstMaintenanceInfo;  // 首保服务单实体

    @Getter
    @Setter
    private List<WarrantyMaintenanceInfo> warrantyMaintenanceInfoList = new ArrayList<>();  // 三包服务单实体集合

    @Getter
    @Setter
    private List<String> warrantyMaintenanceIdList = new ArrayList<>();  // 三包服务单id集合

    @Getter
    @Setter
    private List<ActivityVehicleInfo> activityVehicleInfoList = new ArrayList<>();

    @Getter
    @Setter
    private List<ActivityMaintenanceInfo> activityMaintenanceInfoList = new ArrayList<>();  // 活动服务单实体集合

    @Getter
    @Setter
    private List<String> activityMaintenanceIdList = new ArrayList<>();  // 活动服务单id集合

    @Getter
    @Setter
    private List<String> vehicleIds = new ArrayList<>();

    @Getter
    @Setter
    private List<CommissionPartItem> sourceCommissionPartItemList = new ArrayList<CommissionPartItem>();  // 开始拿到的所有配件信息，用来遍历
    @Getter
    @Setter
    private List<CommissionPartItem> targetCommissionPartItemList = new ArrayList<>();  // 遍历后的配件信息，用来列表展示

    @Init(superclass = true)
    public void init() {
        //拿到传过来的车辆实体
        vehicleInfo = (VehicleInfo) Executions.getCurrent().getArg().get("vehicleInfo");
        if (vehicleInfo.getFmDate() != null) {
            //拿到首保实体
            firstMaintenanceInfo = firstMaintenanceService.findOneByVehicleId(vehicleInfo.getObjId());
        }

        //拿到三包实体List
        warrantyMaintenanceInfoList = warrantyMaintenanceService.findAllByVehicleId(vehicleInfo.getObjId());
        for (WarrantyMaintenanceInfo warrantyMaintenanceInfo : warrantyMaintenanceInfoList) {
            warrantyMaintenanceIdList.add(warrantyMaintenanceInfo.getObjId());
        }

        sourceCommissionPartItemList.clear();
        targetCommissionPartItemList.clear();
        if (warrantyMaintenanceIdList.size() > 0) {
            sourceCommissionPartItemList = commissionPartService.findAllByWarrantyMaintenanceIdList(warrantyMaintenanceIdList);
            if (sourceCommissionPartItemList.size() > 0) {
                targetCommissionPartItemList.addAll(sourceCommissionPartItemList);
            }
        }


        activityVehicleInfoList = activityVehicleService.findAllByVehicleId(vehicleInfo.getObjId());
        if (activityVehicleInfoList.size() > 0) {
            for (ActivityVehicleInfo activityVehicleInfo : activityVehicleInfoList) {
                vehicleIds.add(activityVehicleInfo.getObjId());
            }
            activityMaintenanceInfoList = activityMaintenanceService.findAllByVehicleIds(vehicleIds);
            for (ActivityMaintenanceInfo activityMaintenanceInfo : activityMaintenanceInfoList) {
                activityMaintenanceIdList.add(activityMaintenanceInfo.getObjId());
            }
        }

    }

    @Command
    @NotifyChange("*")
    public void getWarrantyMaintenanceParts() {
        sourceCommissionPartItemList.clear();
        targetCommissionPartItemList.clear();
        if (warrantyMaintenanceIdList.size() > 0) {
            sourceCommissionPartItemList.addAll(commissionPartService.findAllByWarrantyMaintenanceIdList(warrantyMaintenanceIdList));
        }
        if (sourceCommissionPartItemList.size() > 0) {
            targetCommissionPartItemList.addAll(sourceCommissionPartItemList);
        }
    }

    @Command
    @NotifyChange("*")
    public void getActivityMaintenanceParts() {
        sourceCommissionPartItemList.clear();
        targetCommissionPartItemList.clear();
        if (activityMaintenanceIdList.size() > 0) {
            sourceCommissionPartItemList.addAll(commissionPartService.findAllByActivityMaintenanceIdList(activityMaintenanceIdList));
        }
        if (sourceCommissionPartItemList.size() > 0) {
            targetCommissionPartItemList.addAll(sourceCommissionPartItemList);
        }
    }

    @Command
    @NotifyChange("targetCommissionPartItemList")
    public void searchCommissionPartInfo(@BindingParam("srcdocNo") String srcdocNo) {
        targetCommissionPartItemList.clear();
        for (CommissionPartItem commissionPartItem : sourceCommissionPartItemList) {
            if (commissionPartItem.getDocNo().equals(srcdocNo)) {
                targetCommissionPartItemList.add(commissionPartItem);
            }
        }
    }


}