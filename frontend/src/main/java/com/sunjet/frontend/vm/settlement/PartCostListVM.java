package com.sunjet.frontend.vm.settlement;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.settlement.AgencySettlementInfo;
import com.sunjet.dto.asms.settlement.AgencySettlementItem;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.asm.AgencySettlementService;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.supply.SupplyService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * （配件）合作商结算列表
 * Created by zyh on 2016/11/14.
 */
public class PartCostListVM extends ListVM<AgencySettlementItem> {

    @WireVariable
    private AgencySettlementService agencySettlementService;
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private SupplyService supplyService;

    @Getter
    @Setter
    private AgencySettlementItem agencySettlementItem = new AgencySettlementItem();
    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();
    @Getter
    @Setter
    private AgencyInfo agency;


    /**
     * 初始化
     */
    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("AgencySettlementEntity:create"));
            this.setEnableUpdate(hasPermission("AgencySettlementEntity:modify"));
            this.setEnableDelete(hasPermission("AgencySettlementEntity:delete"));
        }
        this.setTitle("配件费用结算");

        //绑定新增/修改页面地址
        this.setFormUrl("/views/settlement/part_cost_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        agencySettlementItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            agencySettlementItem.setAgencyCode(getActiveUser().getAgency().getCode());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            //agencySettlementItem.setDealerCode(getActiveUser().getDealer().getCode());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        agencySettlementItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(agencySettlementItem);
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
        refreshPage(agencySettlementItem);
        //刷新分页
        getPageList();
    }

    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_PART_COST_LIST)
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
        pageResult = agencySettlementService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(agencySettlementItem);
        //刷新分页
        getPageList();
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgencies(@BindingParam("model") String keyword) {
        if (getActiveUser().getAgency() != null) {   // 合作库用户
            this.agencies.clear();
            this.agencies.add(getActiveUser().getAgency());
        } else if (getActiveUser().getDealer() != null) {  // 服务站用户
            this.agencies.clear();
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
        } else {   // 五菱用户
            this.agencies = agencyService.findAllByKeyword(keyword.trim());
        }
    }

    @Command
    @NotifyChange({"agency", "keyword"})
    public void clearSelectedAgency() {
        this.agency = getActiveUser().getAgency();
        this.setKeyword("");
    }

    @Command
    @NotifyChange("agencySettlementItem")
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.agencySettlementItem.setAgencyCode(agency.getCode());
        this.agencySettlementItem.setAgencyName(agency.getName());
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
                AgencySettlementInfo agencySettlementInfo = agencySettlementService.findOneById(objId);

                if (agencySettlementInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getLogId().equals(agencySettlementInfo.getSubmitter())) {
                        List<PendingSettlementDetailInfo> details = pendingSettlementDetailsService.getPendingsBySettlementID(agencySettlementInfo.getObjId());
                        if (details != null) {
                            for (PendingSettlementDetailInfo detail : details) {
                                detail.setSettlementDocType("配件结算单");
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

                        this.agencySettlementService.delete(agencySettlementInfo.getObjId());
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
        this.agencySettlementItem.setStatus(selectedStatus.getIndex());
    }

    @Command
    @NotifyChange({"agencySettlementItem", "documentStatuses"})
    public void reset() {
        this.agencySettlementItem.setAgencyCode("");
        this.agencySettlementItem.setAgencyName("");
        this.getAgencies().clear();
        this.agencySettlementItem.setDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.agencySettlementItem.setStatus(DocStatus.ALL.getIndex());
        this.agencySettlementItem.setEndDate(DateHelper.getEndDateTime());
        this.agencySettlementItem.setStartDate(DateHelper.getFirstOfYear());
        this.agencySettlementItem.setSrcDocNo("");
        this.agencySettlementItem.setVin("");
    }


    /**
     * 更新服务单状态
     *
     * @param detail
     */
    private void updateWarrantyStatus(PendingSettlementDetailInfo detail) {
        SupplyInfo supplyInfo = supplyService.findSupplyWithPartsById(detail.getSrcDocID());
        if (supplyInfo != null) {
            supplyInfo.setStatus(detail.getStatus());
            supplyService.save(supplyInfo);
        }

    }

}
