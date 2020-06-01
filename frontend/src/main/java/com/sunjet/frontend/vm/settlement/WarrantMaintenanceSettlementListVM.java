package com.sunjet.frontend.vm.settlement;

import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.settlement.DealerSettlementInfo;
import com.sunjet.dto.asms.settlement.DealerSettlementItem;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.activity.ActivityMaintenanceService;
import com.sunjet.frontend.service.asm.DealerSettlementService;
import com.sunjet.frontend.service.asm.FirstMaintenanceService;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
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
 * 服务结算单 列表
 * Created by zyh on 2016/11/14.
 */
public class WarrantMaintenanceSettlementListVM extends ListVM<DealerSettlementItem> {


    @WireVariable
    private DealerSettlementService dealerSettlementService;//三包费用结算单
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;    //

    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private FirstMaintenanceService firstMaintenanceService;
    @WireVariable
    private ActivityMaintenanceService activityMaintenanceService;

    @Getter
    @Setter
    private DealerSettlementItem dealerSettlementItem = new DealerSettlementItem();
    @Getter
    @Setter
    private DealerSettlementInfo dealerSettlementInfo = new DealerSettlementInfo();


    /**
     * 初始化
     */
    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("DealerSettlementEntity:create"));
            this.setEnableUpdate(hasPermission("DealerSettlementEntity:modify"));
            this.setEnableDelete(hasPermission("DealerSettlementEntity:delete"));
        }
        this.setTitle("服务费用结算单");
        //绑定新增/修改页面地址
        this.setFormUrl("/views/settlement/warrant_maintenance_form.zul");
        dealerSettlementItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            dealerSettlementItem.setDealerCode(getActiveUser().getDealer().getCode());
            dealerSettlementItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        dealerSettlementItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(dealerSettlementItem);

        //获取分页数据
        getPageList();

        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
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
        refreshPage(dealerSettlementItem);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = dealerSettlementService.getPageList(pageParam);
    }


    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_WARRANT_MAINTENANCE_COST_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
    }

    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(dealerSettlementItem);
        //刷新分页
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
                DealerSettlementInfo settlementInfo = dealerSettlementService.findOneById(objId);

                if (settlementInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getLogId().equals(settlementInfo.getSubmitter()) || getActiveUser().getLogId().equals(settlementInfo.getCreaterId())) {
                        List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getPendingsBySettlementID(settlementInfo.getObjId());
                        if (details != null) {
                            for (PendingSettlementDetailInfo detail : details) {
                                detail.setSettlementDocType("服务结算单");
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
                        this.dealerSettlementService.delete(settlementInfo.getObjId());
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


    @Command
    @NotifyChange("dealerSettlementItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        dealerSettlementItem.setDealerCode(model.getCode());
        dealerSettlementItem.setDealerName(model.getName());

    }

    /**
     * 选中单据状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.dealerSettlementItem.setStatus(selectedStatus.getIndex());
    }

    @Command
    @NotifyChange({"dealerSettlementItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.dealerSettlementItem.setDealerCode("");
            this.dealerSettlementItem.setDealerName("");
        }
        this.dealerSettlementItem.setVin("");
        this.dealerSettlementItem.setSrcDocNo("");
        this.getDealers().clear();
        this.dealerSettlementItem.setDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.dealerSettlementItem.setStatus(DocStatus.ALL.getIndex());
        this.dealerSettlementItem.setEndDate(DateHelper.getEndDateTime());
        this.dealerSettlementItem.setStartDate(DateHelper.getFirstOfYear());
    }


    /**
     * 更新服务单状态
     *
     * @param detail
     */
    private void updateWarrantyStatus(PendingSettlementDetailInfo detail) {
        if ("三包服务单".equals(detail.getSrcDocType())) {
            WarrantyMaintenanceInfo warrantyMaintenanceInfo = warrantyMaintenanceService.findOneById(detail.getSrcDocID());
            if (warrantyMaintenanceInfo != null) {
                warrantyMaintenanceInfo.setStatus(detail.getStatus());
                warrantyMaintenanceService.save(warrantyMaintenanceInfo);
            }

        } else if ("首保服务单".equals(detail.getSrcDocType())) {
            FirstMaintenanceInfo firstMaintenanceInfo = firstMaintenanceService.findOneWithGoOutsById(detail.getSrcDocID());
            if (firstMaintenanceInfo != null) {
                firstMaintenanceInfo.setStatus(detail.getStatus());
                firstMaintenanceService.save(firstMaintenanceInfo);
            }
        } else {
            ActivityMaintenanceInfo activityMaintenanceInfo = activityMaintenanceService.findOneById(detail.getSrcDocID());
            if (activityMaintenanceInfo != null) {
                activityMaintenanceInfo.setStatus(detail.getStatus());
                activityMaintenanceService.save(activityMaintenanceInfo);
            }

        }
    }

}
