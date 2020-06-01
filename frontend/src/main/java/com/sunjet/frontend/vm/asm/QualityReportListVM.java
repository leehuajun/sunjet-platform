package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.QualityReportInfo;
import com.sunjet.dto.asms.asm.QualityReprotItem;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.asm.QualityReportService;
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
 * <p>
 * 质量速报列表VM
 */
public class QualityReportListVM extends ListVM<QualityReprotItem> {

    @WireVariable
    private QualityReportService qualityReportService;

    @Getter
    @Setter
    QualityReprotItem qualityReprotItem = new QualityReprotItem();

    @Getter
    @Setter
    private DealerInfo dealerInfo = new DealerInfo();

    //@Getter
    //@Setter
    //private String docNo;

    //@Getter
    //@Setter
    //private String vehicleType;  // 车辆分类

    //private List<DictionaryInfo> fmvehicleTypes = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("QualityReportEntity:create"));
            this.setEnableUpdate(hasPermission("QualityReportEntity:modify"));
            this.setEnableDelete(hasPermission("QualityReportEntity:delete"));
        }
        this.setTitle("质量速报");
        this.setFormUrl("/views/asm/quality_report_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        qualityReprotItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            qualityReprotItem.setDealerCode(getActiveUser().getDealer().getCode());
            qualityReprotItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        qualityReprotItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(qualityReprotItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = qualityReportService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(qualityReprotItem);
        getPageList();
    }

    /**
     * 刷新、查询列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(qualityReprotItem);
        getPageList();
    }


    /**
     * 刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_QUALITY_REPORT_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        qualityReprotItem.setStatus(DocStatus.ALL.getIndex());
        refreshFirstPage(qualityReprotItem);
        getPageList();
    }


    /**
     * 选择服务站信息
     *
     * @param model
     */
    @Command
    @NotifyChange("qualityReprotItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        qualityReprotItem.setDealerCode(model.getCode());
        qualityReprotItem.setDealerName(model.getName());

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
                QualityReportInfo info = qualityReportService.findOneById(objId);

                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        qualityReportService.delete(objId);
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
        this.qualityReprotItem.setStatus(selectedStatus.getIndex());
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        refreshPage(qualityReprotItem);
    }

    @Command
    @NotifyChange({"qualityReprotItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.qualityReprotItem.setDealerCode("");
            this.qualityReprotItem.setDealerName("");
        }

        this.getDealers().clear();
        this.setSelectedStatus(DocStatus.ALL);
        this.qualityReprotItem.setStatus(DocStatus.ALL.getIndex());
        this.qualityReprotItem.setEndDate(new Date());
        this.qualityReprotItem.setStartDate(DateHelper.getFirstOfYear());
        this.qualityReprotItem.setDocNo("");
        this.qualityReprotItem.setVin("");
        this.qualityReprotItem.setVehicleType("");
    }

    //@Command
    //@NotifyChange({"resultDTO", "currentPageNo"})
    //public void search() {
    //    if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
    //        ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
    //    } else {
    //        filterList();
    //    }
    //}


    //@GlobalCommand(GlobalCommandValues.REFRESH_QUALITY_REPORT_LIST)
    //@NotifyChange("*")
    //public void QualityReportListRefresh() {
    //    initList();
    //}

}
