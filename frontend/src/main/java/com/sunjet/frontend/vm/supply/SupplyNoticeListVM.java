package com.sunjet.frontend.vm.supply;

import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItem;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.activity.ActivityDistributionService;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.supply.SupplyNoticeService;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调拨通知单 列表 VM
 */
public class SupplyNoticeListVM extends ListVM<SupplyNoticeItem> {
    @WireVariable
    private SupplyNoticeService supplyNoticeService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private ActivityDistributionService activityDistributionService;

    @Getter
    @Setter
    private SupplyNoticeItem supplyNoticeItem = new SupplyNoticeItem();
    //@Getter
    //@Setter
    //private String docNo;
    @Getter
    @Setter
    private DealerInfo dealer;


    @Getter
    @Setter
    private String activityNoticeDocNo = ""; //活动通知单号
    @Getter
    @Setter
    private String vin = "";


    /**
     * 初始化
     */
    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setTitle("调拨通知单");
        //绑定新增/修改页面地址
        this.setFormUrl("views/asm/supply_notice_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        supplyNoticeItem.setStatus(DocStatus.ALL.getIndex());
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("SupplyNoticeEntity:create"));
            this.setEnableUpdate(hasPermission("SupplyNoticeEntity:modify"));
            this.setEnableDelete(hasPermission("SupplyNoticeEntity:delete"));
        }


        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //supplyNoticeItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            supplyNoticeItem.setDealerCode(getActiveUser().getDealer().getCode());
            supplyNoticeItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        supplyNoticeItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(supplyNoticeItem);
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
        refreshPage(supplyNoticeItem);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = supplyNoticeService.getPageList(pageParam);
    }


    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(supplyNoticeItem);
        //刷新分页
        getPageList();
    }


    /**
     * 刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_SUPPLY_NOTICE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        supplyNoticeItem.setStatus(DocStatus.ALL.getIndex());
        refreshFirstPage(supplyNoticeItem);
        getPageList();
    }


    /**
     * 选择服务站信息
     *
     * @param model
     */
    @Command
    @NotifyChange("supplyNoticeItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        supplyNoticeItem.setDealerCode(model.getCode());
        supplyNoticeItem.setDealerName(model.getName());

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
                SupplyNoticeInfo supplyNoticeInfo = supplyNoticeService.findByOne(objId);
                if (supplyNoticeInfo.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getLogId().equals(supplyNoticeInfo.getSubmitter())) {
                        if (StringUtils.isNotBlank(supplyNoticeInfo.getSrcDocType())) {
                            if ("活动分配单".equals(supplyNoticeInfo.getSrcDocType())) {
                                ActivityDistributionInfo activityDistributionInfo = activityDistributionService.findOneById(supplyNoticeInfo.getSrcDocID());
                                if (activityDistributionInfo != null) {
                                    activityDistributionInfo.setSupplyNoticeId(null);
                                    activityDistributionInfo.setCanEditSupply(true);
                                    activityDistributionService.save(activityDistributionInfo);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("objId", activityDistributionInfo.getObjId());
                                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_DISTRBUTION_FORM, map);
                                }
                            } else {
                                WarrantyMaintenanceInfo maintenanceEntity = warrantyMaintenanceService.findOneById(supplyNoticeInfo.getSrcDocID());
                                if (maintenanceEntity != null) {
                                    maintenanceEntity.setSupplyNoticeId(null);
                                    maintenanceEntity.setCanEditSupply(true);
                                    warrantyMaintenanceService.save(maintenanceEntity);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("objId", maintenanceEntity.getObjId());
                                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_WARRANTY_MAINTENANCE_FORM, map);

                                }
                            }
                        }
                        ZkTabboxUtil.closeOneByObjId(tabList, objId);
                        supplyNoticeService.delete(objId);
                    } else {
                        ZkUtils.showInformation("非创建人不能删除！", "提示");
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
        this.supplyNoticeItem.setStatus(selectedStatus.getIndex());
    }

    @Command
    @NotifyChange({"supplyNoticeItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.supplyNoticeItem.setDealerCode("");
            this.supplyNoticeItem.setDealerName("");
        }
        this.getDealers().clear();
        this.supplyNoticeItem.setDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.supplyNoticeItem.setActivityNoticeDocNo("");
        this.supplyNoticeItem.setStatus(DocStatus.ALL.getIndex());
        this.supplyNoticeItem.setEndDate(new Date());
        this.supplyNoticeItem.setStartDate(DateHelper.getFirstOfYear());
        this.activityNoticeDocNo = "";
        this.supplyNoticeItem.setSrcDocNo("");
        this.vin = "";
    }


}
