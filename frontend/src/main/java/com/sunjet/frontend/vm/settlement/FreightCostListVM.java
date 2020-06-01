package com.sunjet.frontend.vm.settlement;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.recycle.RecycleInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementItem;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.asm.FreightSettlementService;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.service.recycle.RecycleService;
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
 * 返回件运费单列表
 * Created by zyh on 2016/11/14.
 */
public class FreightCostListVM extends ListVM<FreightSettlementItem> {

    @WireVariable
    private FreightSettlementService freightSettlementService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private RecycleService recycleService;
    @Getter
    @Setter
    private FreightSettlementItem freightSettlementItem = new FreightSettlementItem();
    @Getter
    @Setter
    private FreightSettlementInfo freightSettlementInfo;


    /**
     * 初始化
     */
    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("FreightSettlementEntity:create"));
            this.setEnableUpdate(hasPermission("FreightSettlementEntity:modify"));
            this.setEnableDelete(hasPermission("FreightSettlementEntity:delete"));
        }
        this.setTitle("故障件运费结算单");
        //绑定新增/修改页面地址
        this.setFormUrl("/views/settlement/freight_cost_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        freightSettlementItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            freightSettlementItem.setDealerCode(getActiveUser().getDealer().getCode());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        freightSettlementItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(freightSettlementItem);
        //获取分页数据
        getPageList();
    }

    /**
     * 点击下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        //设置分页参数
        refreshPage(freightSettlementItem);
        //刷新分页
        getPageList();
    }


    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_FREIGHT_COST_LIST)
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
        pageResult = freightSettlementService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(freightSettlementItem);
        //刷新分页
        getPageList();
    }


    @Command
    @NotifyChange("freightSettlementItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        freightSettlementItem.setDealerCode(model.getCode());
        freightSettlementItem.setDealerName(model.getName());

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
                FreightSettlementInfo freightSettlementInfo = freightSettlementService.findOneById(objId);

                if (freightSettlementInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(freightSettlementInfo.getCreaterId()) || getActiveUser().getLogId().equals(freightSettlementInfo.getCreaterId())) {
                        List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getPendingsBySettlementID(freightSettlementInfo.getObjId());
                        if (details != null) {
                            for (PendingSettlementDetailInfo detail : details) {
                                detail.setSettlementDocType("运费结算单");
                                detail.setSettlementDocID(null);
                                detail.setSettlementDocNo(null);
                                detail.setOperator(null);
                                detail.setOperatorPhone(null);
//                                detail.setSettlement(false);
//                                detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
                                detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                                detail.setModifierId(getActiveUser().getLogId());
                                detail.setModifierName(getActiveUser().getUsername());
                                detail.setModifiedTime(new Date());
                                updateWarrantyStatus(detail);
                                pendingSettlementDetailsService.save(detail);
                            }
                        }
                        this.freightSettlementService.delete(freightSettlementInfo.getObjId());
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
     * 选中单据状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.freightSettlementItem.setStatus(selectedStatus.getIndex());
    }

    @Command
    @NotifyChange({"freightSettlementItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.freightSettlementItem.setDealerCode("");
            this.freightSettlementItem.setDealerName("");
        }
        this.getDealers().clear();
        this.freightSettlementItem.setDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.freightSettlementItem.setStatus(DocStatus.ALL.getIndex());
        this.freightSettlementItem.setEndDate(DateHelper.getEndDateTime());
        this.freightSettlementItem.setStartDate(DateHelper.getFirstOfYear());
    }


    /**
     * 更改故障件返回单状态
     *
     * @param detail
     */
    private void updateWarrantyStatus(PendingSettlementDetailInfo detail) {
        if ("故障件返回单".equals(detail.getSrcDocType())) {
            RecycleInfo recycleInfo = recycleService.findOneById(detail.getSrcDocID());
            if (recycleInfo != null) {
                recycleInfo.setStatus(detail.getStatus());
                recycleService.save(recycleInfo);
            }

        }

    }

}
