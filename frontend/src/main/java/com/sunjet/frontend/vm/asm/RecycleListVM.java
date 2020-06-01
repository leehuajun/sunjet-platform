package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.recycle.RecycleInfo;
import com.sunjet.dto.asms.recycle.RecycleItem;
import com.sunjet.dto.asms.recycle.RecycleItemInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.recycle.RecycleItemService;
import com.sunjet.frontend.service.recycle.RecycleNoticeItemService;
import com.sunjet.frontend.service.recycle.RecycleService;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;

import java.util.Date;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 故障件返回单 列表 VM
 */
public class RecycleListVM extends ListVM<RecycleItem> {
    @WireVariable
    private RecycleService recycleService;
    @WireVariable
    private RecycleItemService recycleItemService;  //故障件返回单子行  service

    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;

    @Getter
    @Setter
    private String srcDocNo;
    @Getter
    @Setter
    String vin;

    @Getter
    @Setter
    private RecycleItem recycleItem = new RecycleItem();    //故障件返回单 列表vo

    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("RecycleEntity:create"));
            this.setEnableUpdate(hasPermission("RecycleEntity:modify"));
            this.setEnableDelete(hasPermission("RecycleEntity:delete"));
        }
        this.setTitle("故障件返回单");
        this.setFormUrl("/views/asm/recycle_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        recycleItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            recycleItem.setDealerCode(getActiveUser().getDealer().getCode());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        recycleItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(recycleItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = recycleService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(recycleItem);
        getPageList();
    }

    /**
     * 刷新/ 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        this.recycleItem.getObjIds().clear();
        //来源的单据不为null
        if (StringUtils.isNotBlank(srcDocNo)) {
            List<String> objIds = recycleService.findAllRecycleObjIdsBySrcDocNo(srcDocNo);
            if (objIds != null && objIds.size() > 0) {
                this.recycleItem.setObjIds(objIds);
            }
        }
        //Vin不为null
        if (StringUtils.isNotBlank(vin)) {
            List<String> objIds = recycleService.findAllRecycleObjIdsByVin(vin);
            if (objIds != null && objIds.size() > 0) {
                this.recycleItem.setObjIds(objIds);
            }
        }


        refreshFirstPage(recycleItem);
        getPageList();
    }


    @Command
    @NotifyChange("recycleItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        recycleItem.setDealerCode(model.getCode());
        recycleItem.setDealerName(model.getName());

    }


    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_RECYCLE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        //initList();
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
                RecycleInfo recycleInfo = recycleService.findOneById(objId);

                if (recycleInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getLogId().equals(recycleInfo.getSubmitter())) {
                        //通过故障件返回单objId 查出所有子行明细
                        List<RecycleItemInfo> RecyclePartList = recycleItemService.findByRecyclePartList(objId);
                        if (RecyclePartList != null && RecyclePartList.size() > 0) {
                            for (RecycleItemInfo recycleItemInfo : RecyclePartList) {
                                RecycleNoticeItemInfo recycleNoticeItem = recycleNoticeItemService.findOneByObjid(recycleItemInfo.getNoticeItemId());
                                //返回单已发数量回写    已发数量 = 返回单已发数量 - 故障件本次已返回数量
                                recycleNoticeItem.setBackAmount(recycleNoticeItem.getBackAmount() - recycleItemInfo.getBackAmount());
                                //返回单未返回数量回写   未返回数量 = 未返回数量 + 故障件本次已返回数量
                                recycleNoticeItem.setCurrentAmount(recycleNoticeItem.getCurrentAmount() + recycleItemInfo.getBackAmount());
                                recycleNoticeItemService.save(recycleNoticeItem);
                            }
                        }
                        //删除父表和子表
                        recycleService.delete(objId);
                        ZkTabboxUtil.closeOneByObjId(tabList, objId);
                        getPageList();
                        BindUtils.postNotifyChange(null, null, this, "pageResult");
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_NOTICE_PENDING, null);
                    } else {
                        ZkUtils.showInformation("该单据不是你创建的，不能删除！", "提示");
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
     * 选中单据状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.recycleItem.setStatus(selectedStatus.getIndex());
    }


    @Command
    @NotifyChange({"recycleItem", "documentStatuses", "srcDocNo", "vin"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.recycleItem.setDealerCode("");
            this.recycleItem.setDealerName("");
        }

        this.getDealers().clear();
        this.recycleItem.setDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.recycleItem.setStatus(DocStatus.ALL.getIndex());
        this.recycleItem.setEndDate(new Date());
        this.recycleItem.setStartDate(DateHelper.getFirstOfYear());
        this.recycleItem.getObjIds().clear();
        this.srcDocNo = "";
        this.vin = "";
    }

}
