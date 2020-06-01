package com.sunjet.frontend.vm.settlement;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailItems;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 待结算单据列表(配件／服务／运费)
 * Created by zyh on 2016/11/14.
 */
public class PendingSettleMentDetailsListVM extends ListVM<PendingSettlementDetailItems> {

    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private AgencyService agencyService;


    @Getter
    @Setter
    private PendingSettlementDetailItems pendingSettlementDetailItems = new PendingSettlementDetailItems();
    @Getter
    @Setter
    List<PendingSettlementDetailInfo> pendingSettlementDetailInfo = new ArrayList<>();
    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();
    @Getter
    @Setter
    private AgencyInfo agency;
    @Getter
    @Setter
    private DealerInfo dealer;
    @Getter
    @Setter
    private String doctype = "";
    @Getter
    @Setter
    private List<PendingSettlementDetailItems> pendingSettlementService = new ArrayList<>();
    @Getter
    @Setter
    private List<PendingSettlementDetailItems> pendingSettlementPart = new ArrayList<>();
    @Getter
    @Setter
    private List<PendingSettlementDetailItems> pendingSettlementFreight = new ArrayList<>();

    @Getter
    @Setter
    private Boolean enableWarrantSettlement = false; // 创建服务结算单单按钮状态
    @Getter
    @Setter
    private Boolean enableFreightSettlement = false; // 创建运费结算单按钮状态
    @Getter
    @Setter
    private Boolean enablePartSettlement = false; // 创建配件结算单按钮状态

    @Getter
    private List<DictionaryInfo> vehicleTypes = new ArrayList<>();
    @Getter
    @Setter
    private DictionaryInfo selectedVehicleType;

    @Getter
    private Map<String, DictionaryInfo> vms = new HashMap<>();

    /**
     * 初始化
     */
    @Init(superclass = true)
    public void init() {
        doctype = Executions.getCurrent().getArg().get("doctype").toString();
        dictionaryService.findDictionariesByParentCode("15000").forEach(dic -> {
            this.vms.put(dic.getCode(), dic);
            vehicleTypes.add(dic);
        });

        DictionaryInfo info = new DictionaryInfo();
        info.setName("--全部--");
        info.setCode("0");
        info.setValue("0");
        info.setSeq(0);
        this.selectedVehicleType = info;
        vehicleTypes.add(info);

        Collections.sort(vehicleTypes, (o1, o2) -> {
            if (Integer.parseInt(o1.getValue()) > Integer.parseInt(o2.getValue())) {
                return 1;
            } else {
                return -1;
            }
        });

        this.setEnableAdd(false);

        this.setDocumentStatuses(DocStatus.getListSettlementStatus());

        if (doctype != null) {

            if (doctype.contains("服务结算单")) {
                //新增按钮
                this.setEnableAdd(false);
                //按钮判断
                if ("admin".equals(getActiveUser().getLogId())) {
                    this.setEnableWarrantSettlement(false);
                } else {
                    this.setEnableWarrantSettlement(hasPermission("FWPendingEntity:create"));
                }
                pendingSettlementDetailItems.setSettlementDocType("服务结算单");
                setTitle("服务费用结算单");


            }
            if (doctype.contains("运费结算单")) {
                //按钮判断
                if ("admin".equals(getActiveUser().getLogId())) {
                    this.setEnableFreightSettlement(false);
                } else {
                    this.setEnableFreightSettlement(hasPermission("FreightPendingEntity:create"));
                }
                pendingSettlementDetailItems.setSettlementDocType("运费结算单");
                setTitle("故障件运费结算单");
            }
            if (doctype.contains("配件结算单")) {
                //按钮判断
                if ("admin".equals(getActiveUser().getLogId())) {
                    this.setEnablePartSettlement(false);
                } else {
                    this.setEnablePartSettlement(hasPermission("PartPendingEntity:create"));
                }
                pendingSettlementDetailItems.setSettlementDocType("配件结算单");
                setTitle("配件费用结算");
            }
        }
//        this.pendingSettlementDetailItems.setSettlementStatus(DocStatus.ALL.getIndex());
        this.pendingSettlementDetailItems.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            pendingSettlementDetailItems.setAgencyCode(getActiveUser().getAgency().getCode());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            pendingSettlementDetailItems.setDealerCode(getActiveUser().getDealer().getCode());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        //pendingSettlementDetailItems.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(pendingSettlementDetailItems);
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
        refreshPage(pendingSettlementDetailItems);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = pendingSettlementDetailsService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
//        pendingSettlementDetailItems.setTypeCode();
        //设置分页参数
        refreshFirstPage(pendingSettlementDetailItems);
        //刷新分页
        getPageList();
    }

    //@Command
    //@NotifyChange("resultDTO")
    //public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
    //    if (flowEntity.getStatus().equals(0)) {
    //        List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getPendingsBySettlementID(flowEntity.getObjId());
    //        if (details != null) {
    //            for (PendingSettlementDetailsEntity detail : details) {
    //                detail.setSettlementDocID(null);
    //                detail.setSettlementDocNo(null);
    //                detail.setSettlementDocType(null);
    //                detail.setSettlement(false);
    //                detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
    //                detail.setModifierId(CommonHelper.getActiveUser().getLogId());
    //                detail.setModifierName(CommonHelper.getActiveUser().getUsername());
    //                detail.setModifiedTime(new Date());
    //                pendingSettlementDetailsService.getRepository().save(detail);
    //            }
    //        }
    //
    //        this.getBaseService().getRepository().delete(flowEntity.getObjId());
    //        initList();
    //    } else {
    //        ZkUtils.showInformation("非草稿单据不能删除！", "提示");
    //    }
    //}


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

    /**
     * 选择合作商
     *
     * @param agency
     */
    @Command
    @NotifyChange("pendingSettlementDetailItems")
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.agency = agency;
        this.pendingSettlementDetailItems.setAgencyCode(agency.getCode());
        this.pendingSettlementDetailItems.setAgencyName(agency.getName());
    }

    /**
     * 选择服务站
     *
     * @param model
     */
    @Command
    @NotifyChange("pendingSettlementDetailItems")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        this.dealer = model;
        pendingSettlementDetailItems.setDealerCode(model.getCode());
        pendingSettlementDetailItems.setDealerName(model.getName());

    }

    @Override
    @Command
    @NotifyChange({"pendingSettlementDetailItems", "dealer"})
    public void clearSelectedDealer() {
        this.dealer = null;
        pendingSettlementDetailItems.setDealerCode(null);
        pendingSettlementDetailItems.setDealerName(null);
    }


    @Command
    public void selectPendingSettlementService(@BindingParam("model") PendingSettlementDetailItems Item, @BindingParam("check") boolean check) {
        if (this.getDealer() == null) {
            ZkUtils.showInformation("请先选择服务站查询数据", "提示");
            return;
        }
        if (check && pendingSettlementService != null) {
            if (this.pendingSettlementService.size() > 0) {
                for (PendingSettlementDetailItems detail : this.pendingSettlementService) {
                    if (!detail.getDealerCode().equals(Item.getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个服务站的单据", "提示");
                        return;
                    }
                }
                this.pendingSettlementService.add(Item);
            } else {
                this.pendingSettlementService.add(Item);
            }

        } else {
            this.pendingSettlementService.remove(Item);
        }
    }

    /**
     * 创建服务结算单
     */
    @Command
    public void createWarrantSettlement() {
        if (pendingSettlementService.isEmpty()) {
            ZkUtils.showInformation("请选择待结算清单！", "提示");
        } else {

            Map<String, List<PendingSettlementDetailInfo>> map = new HashMap<>();
            for (PendingSettlementDetailItems settlementDetailItems : pendingSettlementService) {
                PendingSettlementDetailInfo detailInfo = pendingSettlementDetailsService.findOneById(settlementDetailItems.getObjId());
                pendingSettlementDetailInfo.add(detailInfo);
            }

            String url = "views/settlement/warrant_maintenance_form.zul";
            map.put("pendingSettlement", pendingSettlementDetailInfo);

            try {
                ZkTabboxUtil.newTab(URLEncoder.encode(this.getTitle(), "UTF-8"), this.getTitle(), "", true, ZkTabboxUtil.OverFlowType.AUTO, url, map);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }


    @Command
    public void selectPendingSettlementFreight(@BindingParam("model") PendingSettlementDetailItems Item, @BindingParam("check") boolean check) {
        if (this.getDealer() == null) {
            ZkUtils.showInformation("请先选择服务站查询数据", "提示");
            return;
        }
        if (check && pendingSettlementFreight != null) {
            if (this.pendingSettlementFreight.size() > 0) {
                for (PendingSettlementDetailItems detail : this.pendingSettlementFreight) {
                    if (!detail.getDealerCode().equals(Item.getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个服务站的单据", "提示");
                        return;
                    }
                }
                this.pendingSettlementFreight.add(Item);
            } else {
                this.pendingSettlementFreight.add(Item);
            }
        } else {
            this.pendingSettlementFreight.remove(Item);
        }
    }

    /**
     * 创建运费结算单
     */
    @Command
    public void createFreightSettlement() {

        if (pendingSettlementFreight.isEmpty()) {
            ZkUtils.showInformation("请选择待结算清单！", "提示");
        } else {

            Map<String, List<PendingSettlementDetailInfo>> map = new HashMap<>();
            for (PendingSettlementDetailItems settlementDetailItems : pendingSettlementFreight) {
                PendingSettlementDetailInfo detailInfo = pendingSettlementDetailsService.findOneById(settlementDetailItems.getObjId());
                pendingSettlementDetailInfo.add(detailInfo);
            }

            String url = "views/settlement/freight_cost_form.zul";
            map.put("pendingSettlement", pendingSettlementDetailInfo);

            try {
                ZkTabboxUtil.newTab(URLEncoder.encode(this.getTitle(), "UTF-8"), this.getTitle(), "", true, ZkTabboxUtil.OverFlowType.AUTO, url, map);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    @Command
    public void selectPendingSettlementPart(@BindingParam("model") PendingSettlementDetailItems Item, @BindingParam("check") boolean check) {
        if (this.getAgency() == null) {
            ZkUtils.showInformation("请先选择合作经销商查询数据", "提示");
            return;
        }
        if (check && pendingSettlementPart != null) {
            if (this.pendingSettlementService.size() > 0) {
                for (PendingSettlementDetailItems detail : this.pendingSettlementPart) {
                    if (!detail.getDealerCode().equals(Item.getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个合作经销商的单据", "提示");
                        return;
                    }
                }
                this.pendingSettlementPart.add(Item);
            } else {
                this.pendingSettlementPart.add(Item);
            }

        } else {
            this.pendingSettlementPart.remove(Item);
        }
    }

    /**
     * 创建配件结算单
     */
    @Command
    public void createPartSettlement() {

        if (pendingSettlementPart.isEmpty()) {
            ZkUtils.showInformation("请选择待结算清单！", "提示");
        } else {

            Map<String, List<PendingSettlementDetailInfo>> map = new HashMap<>();
            for (PendingSettlementDetailItems settlementDetailItems : pendingSettlementPart) {
                PendingSettlementDetailInfo detailInfo = pendingSettlementDetailsService.findOneById(settlementDetailItems.getObjId());
                pendingSettlementDetailInfo.add(detailInfo);
            }

            String url = "views/settlement/part_cost_form.zul";
            map.put("pendingSettlement", pendingSettlementDetailInfo);

            try {
                ZkTabboxUtil.newTab(URLEncoder.encode(this.getTitle(), "UTF-8"), this.getTitle(), "", true, ZkTabboxUtil.OverFlowType.AUTO, url, map);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }


    public Boolean chkStatus(Integer statusIdx) {
        if (StringUtils.isBlank(this.pendingSettlementDetailItems.getTypeCode()) || this.pendingSettlementDetailItems.getTypeCode().equals("0")) {
            return true;
        } else {
            if (statusIdx == null) {
                return false;
            } else if (statusIdx == DocStatus.WAITING_SETTLE.getIndex()) {
                return false;
            } else if (statusIdx == DocStatus.SETTLING.getIndex()) {
                return true;
            } else if (statusIdx == DocStatus.SETTLED.getIndex()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 选中单据状态
     */
    @Command
    @NotifyChange({"documentStatuses", "pageResult"})
    public void selectedStatus() {
        this.pendingSettlementDetailItems.setStatus(selectedStatus.getIndex());
//        this.pendingSettlementDetailItems.setSettlementStatus(selectedStatus.getIndex());
        refreshData();
    }

    @Command
    @NotifyChange("pageResult")
    public void selectVehicleType() {
        if (!"0".equals(this.selectedVehicleType.getCode())) {
            this.pendingSettlementDetailItems.setTypeCode(this.selectedVehicleType.getCode());
        } else {
            this.pendingSettlementDetailItems.setTypeCode("");
        }
        refreshData();
    }

    @Command
    @NotifyChange({"pendingSettlementDetailItems", "documentStatuses"})
    public void reset() {
        this.pendingSettlementDetailItems.setAgencyCode("");
        this.pendingSettlementDetailItems.setAgencyName("");
        this.getAgencies().clear();
        this.pendingSettlementDetailItems.setDealerCode("");
        this.pendingSettlementDetailItems.setDealerName("");
        this.getDealers().clear();
        this.pendingSettlementDetailItems.setSrcDocNo("");
        this.setSelectedStatus(DocStatus.ALL);
        this.pendingSettlementDetailItems.setStatus(DocStatus.ALL.getIndex());
//        this.pendingSettlementDetailItems.setSettlementStatus(DocStatus.ALL.getIndex());
        this.pendingSettlementDetailItems.setEndDate(DateHelper.getEndDateTime());
        this.pendingSettlementDetailItems.setStartDate(DateHelper.getFirstOfYear());
    }
}
