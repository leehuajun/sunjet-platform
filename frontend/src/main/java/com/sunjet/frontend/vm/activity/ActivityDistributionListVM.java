package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.activity.ActivityDistributionItem;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.activity.ActivityDistributionService;
import com.sunjet.frontend.service.activity.ActivityNoticeService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
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
 * Created by Administrator on 2016/10/26.
 * 活动分配单
 */
public class ActivityDistributionListVM extends ListVM<ActivityDistributionItem> {

    @WireVariable
    private ActivityDistributionService activityDistributionService;
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private SupplyNoticeService supplyNoticeService;

    @Getter
    @Setter
    private ActivityDistributionItem activityDistributionItem = new ActivityDistributionItem();

    @Getter
    @Setter
    public DocStatus status = DocStatus.ALL;
    public DealerInfo dealer;

    //@Getter
    //@Setter
    //private String activityNoticeDocNo;

    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("ActivityDistributionEntity:create"));
            this.setEnableUpdate(hasPermission("ActivityDistributionEntity:modify"));
            this.setEnableDelete(hasPermission("ActivityDistributionEntity:delete"));

        }
        this.setTitle("活动分配单");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        this.setFormUrl("/views/asm/activity_distribution_form.zul");
        activityDistributionItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            activityDistributionItem.setDealerCode(getActiveUser().getDealer().getCode());
            activityDistributionItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        activityDistributionItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(activityDistributionItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = activityDistributionService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(activityDistributionItem);
        getPageList();
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
//        activityDistributionItem.setStartDate(dealerItem.getStartDate());
//        activityDistributionItem.setEndDate(dealerItem.getEndDate());
//        this.activityDistributionItem.getActivityNoticeObjIds().clear();
//
//        if (StringUtils.isNotBlank(activityNoticeDocNo)) {
//            List<String> activityNoticeObjId = activityNoticeService.findAllobjIdByDocNo(activityNoticeDocNo);
//            this.activityDistributionItem.setActivityNoticeObjIds(activityNoticeObjId);
//        }
        //设置分页参数
        refreshFirstPage(activityDistributionItem);
        //刷新分页
        getPageList();
    }

    /**
     * 刷新窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_DISTRBUTION_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
    }

    /**
     * 选择服务站编号
     */
    @Command
    @NotifyChange({"activityDistributionItem", "dealer"})
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        dealer = model;
        activityDistributionItem.setDealerCode(model.getCode());
        activityDistributionItem.setDealerName(model.getName());

    }

    /**
     * 选择状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectStatus() {
        this.activityDistributionItem.setStatus(status.getIndex());
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
                ActivityDistributionInfo info = activityDistributionService.findOneById(objId);
                if (info.getSupplyNoticeId() != null) {
                    ZkUtils.showInformation("该单据已经生产调拨单，不能删除！", "提示");
                    return;
                }
                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        ZkTabboxUtil.closeOneByObjId(tabList, objId);
                        activityDistributionService.deleteByObjId(objId);
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


    @Command
    @NotifyChange({"activityDistributionItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.activityDistributionItem.setDealerName("");
            this.activityDistributionItem.setDealerCode("");
        }
        this.getDealers().clear();
        this.setStatus(DocStatus.ALL);
        this.activityDistributionItem.setActivityNoticeDocNo("");
        this.activityDistributionItem.setStatus(DocStatus.ALL.getIndex());
        this.activityDistributionItem.setEndDate(new Date());
        this.activityDistributionItem.setStartDate(DateHelper.getFirstOfYear());
        this.activityDistributionItem.setDocNo("");
    }

}


