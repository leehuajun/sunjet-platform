package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItem;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.recycle.RecycleNoticeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 故障件返回通知单 列表 VM
 */
public class RecycleNoticeListVM extends ListVM<RecycleNoticeItem> {

    @WireVariable
    private RecycleNoticeService recycleNoticeService;

    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;

    @Getter
    @Setter
    private String vin;
    @Getter
    @Setter
    private RecycleNoticeItem recycleNoticeItem = new RecycleNoticeItem();

    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("RecycleNoticeEntity:create"));
            this.setEnableUpdate(hasPermission("RecycleNoticeEntity:modify"));
            this.setEnableDelete(hasPermission("RecycleNoticeEntity:delete"));
        }
        this.setTitle("返回通知单");
        this.setFormUrl("/views/asm/recycle_notice_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        recycleNoticeItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getDealer() != null) {
            //服务站
            recycleNoticeItem.setDealerCode(getActiveUser().getDealer().getCode());
        }

        refreshFirstPage(recycleNoticeItem);
        getPageList();
    }


    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_RECYCLE_NOTICE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = recycleNoticeService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(recycleNoticeItem);
        getPageList();
    }

    /**
     * 刷新/ 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(recycleNoticeItem);
        getPageList();
    }

    /**
     * 删除
     *
     * @param objId
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId, @BindingParam("tabs") List<Tab> tabList) {
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                RecycleNoticeInfo recycleNoticeInfo = recycleNoticeService.findOneById(objId);

                if (recycleNoticeInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getLogId().equals(recycleNoticeInfo.getSubmitter())) {
                        if ("三包服务单".equals(recycleNoticeInfo.getSrcDocType())) {
                            WarrantyMaintenanceInfo warrantyMaintenanceInfo = warrantyMaintenanceService.findOneById(recycleNoticeInfo.getSrcDocID());
                            warrantyMaintenanceInfo.setRecycleNoticeId(null);
                            warrantyMaintenanceInfo.setCanEditRecycle(true);
                            warrantyMaintenanceService.save(warrantyMaintenanceInfo);
                            //关闭标签
                            ZkTabboxUtil.closeOneByObjId(tabList, warrantyMaintenanceInfo.getObjId());
                            recycleNoticeService.delete(objId);
                            //刷新三包表单
                            Map<String, Object> map = new HashMap<>();
                            map.put("objId", warrantyMaintenanceInfo.getObjId());
                            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_WARRANTY_MAINTENANCE_FORM, map);
                            //关闭标签
                            ZkTabboxUtil.closeOneByObjId(tabList, objId);
                        } else {
                            //关闭标签
                            ZkTabboxUtil.closeOneByObjId(tabList, objId);
                            recycleNoticeService.delete(objId);
                        }
                    } else {
                        ZkUtils.showInformation("该单据不是你创建的，不能删除！", "提示");
                    }
                } else {
                    ZkUtils.showInformation("非草稿单据不能删除！", "提示");
                }
                getPageList();
                BindUtils.postNotifyChange(null, null, this, "pageResult");
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });


    }

    /**
     * 选中单据状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.recycleNoticeItem.setStatus(selectedStatus.getIndex());
    }


    @Command
    @NotifyChange("recycleNoticeItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        recycleNoticeItem.setDealerCode(model.getCode());
        recycleNoticeItem.setDealerName(model.getName());

    }

    @Command
    @NotifyChange({"recycleNoticeItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.recycleNoticeItem.setDealerCode("");
            this.recycleNoticeItem.setDealerName("");
        }

        this.getDealers().clear();
        this.recycleNoticeItem.setDocNo("");
        this.recycleNoticeItem.setSrcDocNo("");
        this.recycleNoticeItem.setSrcDocType("");
        this.setSelectedStatus(DocStatus.ALL);
        this.recycleNoticeItem.setStatus(DocStatus.ALL.getIndex());
        this.recycleNoticeItem.setEndDate(new Date());
        this.recycleNoticeItem.setStartDate(DateHelper.getFirstOfYear());
    }

}
