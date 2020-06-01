package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
import com.sunjet.dto.system.base.DocInfo;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ActivityDistributionSelectVehicleListVM extends ListVM {

    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;

    private List<String> selectedVehicleList = new ArrayList<>();
    private List<ActivityVehicleItem> activityvehicleList = new ArrayList<>();  //推送车辆列表
    @Getter
    @Setter
    private ActivityVehicleItem activityVehicleItem = new ActivityVehicleItem();
    @Getter
    @Setter
    private Boolean choice;

    private String activityDistributionId;
    private String activityNoticeId;

    //    @Init(superclass = true)
    @Init
    public void init() {

        activityDistributionId = (String) Executions.getCurrent().getArg().get("activityDistributionId");
        activityNoticeId = (String) Executions.getCurrent().getArg().get("activityNoticeId");
        activityVehicleItem.setActivityNoticeId(activityNoticeId);
        pageParam.setInfoWhere(activityVehicleItem);
        pageParam.setOrder(Order.DESC.toString());
        pageParam.setOrderName("objId");
        pageParam.setPageSize(1500);
        pageResult = activityVehicleService.getActivityVehiclePageListScreenActivityDistributionIdIsNULL(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        activityVehicleItem.setActivityNoticeId(activityNoticeId);
        refreshPage(activityVehicleItem);
        pageParam.setInfoWhere(activityVehicleItem);
        pageParam.setPageSize(1500);
        pageResult = activityVehicleService.getActivityVehiclePageListScreenActivityDistributionIdIsNULL(pageParam);
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        activityVehicleItem.setActivityNoticeId(activityNoticeId);
        pageParam.setInfoWhere(activityVehicleItem);
        pageParam.setPageSize(1500);
        pageResult = activityVehicleService.getActivityVehiclePageListScreenActivityDistributionIdIsNULL(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshPage(activityVehicleItem);
        //刷新分页
        getPageList();
    }

    @Command
    @NotifyChange("activityVehicleItem")
    public void reset() {
        this.activityVehicleItem = new ActivityVehicleItem();
    }

    @Command
    @NotifyChange("item")
    public void selectVehicle(@BindingParam("entity") ActivityVehicleItem item) {
        if (this.selectedVehicleList.contains(item.getVin())) {
            unSelectVehicle(item);
            return;
        }
        this.selectedVehicleList.add(item.getVin());
        this.activityvehicleList.add(item);
    }

    @Command
    @NotifyChange("*")
    public void unSelectVehicle(@BindingParam("entity") ActivityVehicleItem item) {
        if (this.selectedVehicleList.contains(item.getVin())) {
            this.selectedVehicleList.remove(item.getVin());
            this.activityvehicleList.remove(item);
        }
    }

    /**
     * 全选车辆
     *
     * @param chk
     */
    @Command
    @NotifyChange({"pageResult", "choice"})
    public void checkAll(@BindingParam("chk") Boolean chk) {   //全选
        List<DocInfo> pageContent = this.getPageResult().getRows();
        if (chk == true) {
            for (DocInfo de : pageContent) {
                ActivityVehicleItem vehicle = ((ActivityVehicleItem) de);
                if (!this.selectedVehicleList.contains(vehicle.getVin())) {
                    this.selectedVehicleList.add(vehicle.getVin());
                    this.activityvehicleList.add(vehicle);
                }
            }
        } else {
            if (pageContent != null) {
                for (DocInfo de : pageContent) {
                    ActivityVehicleItem vehicle = ((ActivityVehicleItem) de);
                    if (this.selectedVehicleList.contains(vehicle.getVin())) {
                        this.selectedVehicleList.remove(vehicle.getVin());
                        this.activityvehicleList.remove(vehicle);
                    }
                }
            }
        }
        this.choice = chk;
    }

    public Boolean chkIsExist(ActivityVehicleItem activityvehicle) {

        if (this.selectedVehicleList.contains(activityvehicle.getVin())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 提交
     */
    @Command
    public void submit() {
        Map<String, Object> map = new HashMap<>();
        map.put("activityNoticeId", "");
        map.put("activityDistributionId", activityDistributionId);
        List<ActivityVehicleInfo> activityVehicleInfoList = new ArrayList<>();
        for (ActivityVehicleItem info : activityvehicleList) {
            ActivityVehicleInfo activityVehicleInfo = new ActivityVehicleInfo();
            activityVehicleInfo.setObjId(info.getObjId());
            activityVehicleInfo.setVehicleId(info.getVehicleId());
            activityVehicleInfo.setDistribute(true);
            activityVehicleInfo.setActivityNoticeId(activityNoticeId.toString());
            activityVehicleInfo.setActivityDistributionId(activityDistributionId.toString());
            activityVehicleInfoList.add(activityVehicleInfo);
        }
        activityVehicleService.saveActivityDistributionId(activityVehicleInfoList);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST, map);
    }

    /**
     * 模态化窗口位置
     *
     * @return
     */
    public String getWindow_position() {
        return "center,center";
    }

}
