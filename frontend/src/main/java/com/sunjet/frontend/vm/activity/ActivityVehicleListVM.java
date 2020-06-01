package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.activity.ActivityNoticeInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.activity.ActivityNoticeService;
import com.sunjet.frontend.service.activity.ActivityDistributionService;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.Map;

/**
 * 活动车辆
 *
 * @author zyf
 * @create 2015-12-30 上午11:38
 */
public class ActivityVehicleListVM extends ListVM<ActivityVehicleItem> {

    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private ActivityNoticeFormVM activityNoticeFormVM;

    @Getter
    @Setter
    private ActivityVehicleInfo activityVehicleInfo = new ActivityVehicleInfo();
    @Getter
    @Setter
    private ActivityVehicleItem activityVehicleItem = new ActivityVehicleItem();

    @WireVariable
    private ActivityNoticeService activityNoticeService;

    @WireVariable
    private ActivityDistributionService activityDistributionService;

    @Getter
    @Setter
    private Map<String, Object> ids;

    @Getter
    @Setter
    private Boolean canDelete = true; // 是否能删除

    @Getter
    @Setter
    private Boolean canDisplay = true; // 判断单据类型是否显示车辆分配状态

    @Init(superclass = true)
    public void init() {
        ids = (Map<String, Object>) Executions.getCurrent().getArg().get("ids");
        if (ids.size() == 0) {
            activityVehicleItem = new ActivityVehicleItem();
            //传车辆ID过去是为了查不到数据，默认查出来的是所有活动车辆，新增的时候不需要数据
            activityVehicleItem.setVehicleId("123321");
            pageParam.setOrder(Order.DESC.toString());
            pageParam.setOrderName("objId");
            pageParam.setInfoWhere(activityVehicleItem);
            pageResult = activityVehicleService.getPageList(pageParam);
        }
        if (ids.size() != 0 && ids.get("activityDistributionId").toString() != null && ids.get("activityDistributionId").toString() != "") {
            this.canDisplay = false;
            activityVehicleItem.setActivityDistributionId(ids.get("activityDistributionId").toString());
            pageParam.setOrder(Order.DESC.toString());
            pageParam.setOrderName("objId");
            pageParam.setInfoWhere(activityVehicleItem);
            pageResult = activityVehicleService.getPageList(pageParam);

            ActivityDistributionInfo activityDistributionInfo = activityDistributionService.findOneById(ids.get("activityDistributionId").toString());
            //非草稿状态可以编辑
            if (activityDistributionInfo != null && !activityDistributionInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                this.canDelete = false;
            } else {
                if (!getActiveUser().getLogId().equals(activityDistributionInfo.getSubmitter())) {
                    this.canDelete = false;
                }
                //生成调拨通知单 就不能删除车辆
                if (StringUtils.isNotBlank(activityDistributionInfo.getSupplyNoticeId())) {
                    this.canDelete = false;
                }
            }
            BindUtils.postNotifyChange(null, null, this, "canDelete");

        } else if (ids.size() != 0 && ids.get("activityNoticeId").toString() != null && ids.get("activityNoticeId").toString() != "") {
            this.canDisplay = true;
            activityVehicleItem.setActivityNoticeId(ids.get("activityNoticeId").toString());
            pageParam.setOrder(Order.DESC.toString());
            pageParam.setOrderName("objId");
            pageParam.setInfoWhere(activityVehicleItem);
            pageResult = activityVehicleService.getPageList(pageParam);

            ActivityNoticeInfo activityNoticeInfo = activityNoticeService.findOneById(ids.get("activityNoticeId").toString());
            if (activityNoticeInfo != null && !activityNoticeInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                this.canDelete = false;
            } else {
                if (!getActiveUser().getLogId().equals(activityNoticeInfo.getSubmitter())) {
                    this.canDelete = false;
                }
            }
            BindUtils.postNotifyChange(null, null, this, "canDelete");

        }

    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = activityVehicleService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(activityVehicleItem);
        getPageList();
    }

    /**
     * 刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST)
    @NotifyChange("pageResult")
    public void refreshList(@BindingParam("activityNoticeId") String activityNoticeId, @BindingParam("activityDistributionId") String activityDistributionId) {
        ids.put("activityNoticeId", activityNoticeId);
        ids.put("activityDistributionId", activityDistributionId);
        if (activityDistributionId != null && !activityDistributionId.equals("")) {
            this.canDisplay = false;
            activityVehicleItem.setActivityDistributionId(activityDistributionId);
            pageParam.setOrder(Order.DESC.toString());
            pageParam.setOrderName("objId");
            pageParam.setInfoWhere(activityVehicleItem);
            pageResult = activityVehicleService.getPageList(pageParam);
            ActivityDistributionInfo activityDistributionInfo = activityDistributionService.findOneById(activityDistributionId);
            if (activityDistributionInfo != null && !activityDistributionInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                this.canDelete = false;
            } else {
                if (!getActiveUser().getLogId().equals(activityDistributionInfo.getSubmitter())) {
                    this.canDelete = false;
                }
                //生成调拨通知单 就不能删除车辆
                if (StringUtils.isNotBlank(activityDistributionInfo.getSupplyNoticeId())) {
                    this.canDelete = false;
                }
            }
            BindUtils.postNotifyChange(null, null, this, "canDelete");

        }
        if (activityNoticeId != null && !activityNoticeId.equals("")) {
            this.canDisplay = true;
            activityVehicleItem.setActivityNoticeId(activityNoticeId);
            pageParam.setOrder(Order.DESC.toString());
            pageParam.setOrderName("objId");
            pageParam.setInfoWhere(activityVehicleItem);
            pageResult = activityVehicleService.getPageList(pageParam);

            ActivityNoticeInfo activityNoticeInfo = activityNoticeService.findOneById(activityNoticeId);
            if (activityNoticeInfo != null && !activityNoticeInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                this.canDelete = false;
            } else {
                if (!getActiveUser().getLogId().equals(activityNoticeInfo.getSubmitter())) {
                    this.canDelete = false;
                }

            }
            BindUtils.postNotifyChange(null, null, this, "canDelete");

        }
    }

    /**
     * 删除对象
     *
     * @param objId
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId) {
        if (ids.get("activityDistributionId") != null && ids.get("activityDistributionId") != "") {
            ZkUtils.showQuestion("您确定删除该对象？", "询问", event -> {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    activityVehicleService.deleteRels(objId);
                } else {
                    // 用户点击的是取消按钮
                    ZkUtils.showInformation("取消删除", "提示");
                }
            });
        } else if (ids.get("activityNoticeId") != null && ids.get("activityNoticeId") != "") {
            ZkUtils.showQuestion("您确定删除该对象？", "询问", event -> {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    activityVehicleService.deleteById(objId);
                } else {
                    // 用户点击的是取消按钮
                    ZkUtils.showInformation("取消删除", "提示");
                }
            });
        }
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_FORM, null);
        getPageList();

    }
}