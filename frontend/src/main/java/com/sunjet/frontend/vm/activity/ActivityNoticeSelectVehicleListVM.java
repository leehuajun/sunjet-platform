package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ActivityNoticeSelectVehicleListVM extends ListVM {

    @WireVariable
    private VehicleService vehicleService;

    @WireVariable
    private ActivityVehicleService activityVehicleService;

    @Getter
    @Setter
    private VehicleInfo vehicleInfo = new VehicleInfo();

    private Object activityNoticeId;


    @Getter
    @Setter
    private Boolean choice = false;

    @Getter
    private List<String> originVins = new ArrayList<>();   // 从活动通知单传递过来的已选择的VIN列表
    @Getter
    private Set<VehicleInfo> selectedVehicleInfos = new HashSet<>();    // 当前选中的车辆


    @Init(superclass = true)
    public void init() {
        activityNoticeId = Executions.getCurrent().getArg().get("activityNoticeId");
        originVins = (List<String>) Executions.getCurrent().getArg().get("vins");
        getPageList();
        this.checkingAll();

    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange({"pageResult", "choice"})
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(vehicleInfo);
        getPageList();
        this.checkingAll();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = vehicleService.getPageList(pageParam);
    }


    /**
     * 选择车辆
     *
     * @param entity
     * @param check
     */
    @Command
    @NotifyChange("entity")
    public void selectVehicle(@BindingParam("entity") VehicleInfo entity, @BindingParam("check") boolean check) {
        if (check) {
            selectedVehicleInfos.add(entity);
        } else {
            selectedVehicleInfos.remove(entity);
        }
    }

    /**
     * 提交车辆
     */
    @Command
    public void submit() {
        if (this.selectedVehicleInfos.size() > 0) {
            List<ActivityVehicleInfo> activityVehicleInfoList = new ArrayList<>();

            this.selectedVehicleInfos.forEach(vi -> {
                ActivityVehicleInfo activityVehicleInfo = new ActivityVehicleInfo();
                activityVehicleInfo.setVehicleId(vi.getObjId());
                activityVehicleInfo.setTypeCode(vi.getTypeCode());
                activityVehicleInfo.setActivityNoticeId(activityNoticeId.toString());
                activityVehicleInfoList.add(activityVehicleInfo);
            });
            activityVehicleService.saveList(activityVehicleInfoList);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST, null);
        }
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        if (this.vehicleInfo.getManufactureDateStart() != null && vehicleInfo.getManufactureDateEnd() != null) {
            if (this.vehicleInfo.getManufactureDateStart().getTime() > this.vehicleInfo.getManufactureDateEnd().getTime()) {
                ZkUtils.showInformation("生产日期开始时间不能大于结束时间", "提示");
                return;
            }
        }

        if (this.vehicleInfo.getPurchaseDateStart() != null && this.vehicleInfo.getPurchaseDateEnd() != null) {
            if (this.vehicleInfo.getPurchaseDateStart().getTime() > this.vehicleInfo.getPurchaseDateEnd().getTime()) {
                ZkUtils.showInformation("购买日期开始时间不能大于结束时间", "提示");
                return;
            }
        }

        //设置分页参数
        refreshFirstPage(vehicleInfo);
        //刷新分页
        getPageList();
    }


    /**
     * 全选车辆
     *
     * @param chk
     */
    @Command
    @NotifyChange({"pageResult"})
    public void checkAll(@BindingParam("chk") Boolean chk) {   //全选
        List<VehicleInfo> pageContent = this.getPageResult().getRows();
        if (chk == true) {
            for (VehicleInfo info : pageContent) {
                if (!this.originVins.contains(info.getVin())) {
                    this.selectedVehicleInfos.add(info);
                }
            }
        } else {
            if (pageContent != null) {
                for (VehicleInfo info : pageContent) {
                    Iterator<VehicleInfo> iterator = this.selectedVehicleInfos.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().equals(info)) {
                            iterator.remove();
                        }
                    }

                }
            }
        }
        //this.choice = chk;
    }

    /**
     * 重置
     */
    @Command
    @NotifyChange("vehicleInfo")
    public void reset() {
        this.vehicleInfo = new VehicleInfo();
    }


    /**
     * 检查是否全选状态
     */
    public void checkingAll() {
        //判断全选
        List<VehicleInfo> pageContent = this.getPageResult().getRows();
        for (VehicleInfo info : pageContent) {
            if (!originVins.contains(info.getVin())) {
                this.choice = false;
                return;
            } else {
                this.choice = true;
            }
        }

    }






}
