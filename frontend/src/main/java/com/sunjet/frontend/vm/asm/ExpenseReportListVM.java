package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.ExpenseReportInfo;
import com.sunjet.dto.asms.asm.ExpenseReprotItem;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.asm.ExpenseReportService;
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
 * 费用速报列表VM
 */
public class ExpenseReportListVM extends ListVM<ExpenseReprotItem> {

    @WireVariable
    private ExpenseReportService expenseReportService;

    @Getter
    @Setter
    private ExpenseReprotItem expenseReprotItem = new ExpenseReprotItem();

    @Getter
    @Setter
    private DealerInfo dealerInfo = new DealerInfo();

    @Getter
    @Setter
    private String docNo;
    @Getter
    @Setter
    private Date startDate;
    @Getter
    @Setter
    private Date endDate;
    @Getter
    @Setter
    private String vehicleType;

    @Init(superclass = true)
    public void init() {
        this.setTitle("费用速报");
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("ExpenseReportEntity:create"));
            this.setEnableUpdate(hasPermission("ExpenseReportEntity:modify"));
            this.setEnableDelete(hasPermission("ExpenseReportEntity:delete"));
        }
        //this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        //this.setBaseService(expenseReportService);
        //this.setEnableDelete(hasPermission(ExpenseReportEntity.class.getSimpleName()+":delete"));
        //this.setEnableUpdate(hasPermission(ExpenseReportEntity.class.getSimpleName()+":modify"));
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        this.setFormUrl("/views/asm/expense_report_form.zul");
        expenseReprotItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            expenseReprotItem.setDealerCode(getActiveUser().getDealer().getCode());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        expenseReprotItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(expenseReprotItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = expenseReportService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(expenseReprotItem);
        getPageList();
    }

    /**
     * 刷新、查询列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(expenseReprotItem);
        getPageList();
    }

    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_EXPENSE_REPORT_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
    }

    /**
     * 选择服务站信息
     *
     * @param model
     */
    @Command
    @NotifyChange("expenseReprotItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        expenseReprotItem.setDealerCode(model.getCode());
        expenseReprotItem.setDealerName(model.getName());

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
                ExpenseReportInfo info = expenseReportService.findOneById(objId);

                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        expenseReportService.delete(objId);
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
        this.expenseReprotItem.setStatus(selectedStatus.getIndex());
    }

    @Command
    @NotifyChange({"expenseReprotItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.expenseReprotItem.setDealerCode("");
            this.expenseReprotItem.setDealerName("");
        }
        this.getDealers().clear();
        this.setSelectedStatus(DocStatus.ALL);
        this.expenseReprotItem.setStatus(DocStatus.ALL.getIndex());
        this.expenseReprotItem.setEndDate(new Date());
        this.expenseReprotItem.setStartDate(DateHelper.getFirstOfYear());
        this.expenseReprotItem.setDocNo("");
        this.expenseReprotItem.setVin("");
        this.expenseReprotItem.setVehicleType("");
    }

    //@AfterCompose
    //public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
    //    Selectors.wireComponents(view, this, false);
    //    initList();
    //}

    //@Command
    //@NotifyChange({"resultDTO", "currentPageNo"})
    //public void search(){
    //    if(this.getEndDate().getTime()<=this.getStartDate().getTime()){
    //        ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
    //    }else {
    //        filterList();
    //    }
    //}

}
