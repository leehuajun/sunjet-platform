package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.ActivityNoticeInfo;
import com.sunjet.dto.asms.activity.ActivityNoticeItem;
import com.sunjet.frontend.service.activity.ActivityNoticeService;
import com.sunjet.frontend.service.activity.ActivityPartService;
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
public class ActivityNoticeListVM extends ListVM<ActivityNoticeItem> {

    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private ActivityPartService activityPartService;

    @Getter
    @Setter
    private ActivityNoticeItem activityNoticeItem = new ActivityNoticeItem();

    @Getter
    @Setter
    public DocStatus status = DocStatus.ALL;

    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableDelete(false);
        } else {
            this.setEnableAdd(hasPermission("ActivityNoticeEntity:create"));
            this.setEnableUpdate(hasPermission("ActivityNoticeEntity:modify"));
            this.setEnableDelete(hasPermission("ActivityNoticeEntity:delete"));
        }
        this.setTitle("活动通知单");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        this.setFormUrl("/views/asm/activity_notice_form.zul");
        activityNoticeItem.setStatus(DocStatus.ALL.getIndex());
        //activityNoticeItem.setSubmitter(getActiveUser().getLogId());
        refreshFirstPage(activityNoticeItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = activityNoticeService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(activityNoticeItem);
        getPageList();
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        activityNoticeItem.setDocNo(activityNoticeItem.getDocNo());
        activityNoticeItem.setTitle(activityNoticeItem.getTitle());
        activityNoticeItem.setStartDate(activityNoticeItem.getStartDate());
        activityNoticeItem.setEndDate(activityNoticeItem.getEndDate());
        refreshFirstPage(activityNoticeItem);
        getPageList();
    }

    /**
     * 刷新窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_NOTICE_LIST)
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
                ActivityNoticeInfo info = activityNoticeService.findOneById(objId);
                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        activityNoticeService.deleteByObjId(objId);
                        activityVehicleService.deleteByactivityNoticeId(objId);
                        activityPartService.deleteByactivityNoticeId(objId);
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
     * 选择状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectStatus() {
        this.activityNoticeItem.setStatus(status.getIndex());
    }


    @Command
    @NotifyChange({"activityNoticeItem", "documentStatuses"})
    public void reset() {
        this.setStatus(DocStatus.ALL);
        this.activityNoticeItem.setStatus(DocStatus.ALL.getIndex());
        this.activityNoticeItem.setEndDate(new Date());
        this.activityNoticeItem.setStartDate(DateHelper.getFirstOfYear());
        this.activityNoticeItem.setDocNo("");
        this.activityNoticeItem.setTitle("");
    }
}