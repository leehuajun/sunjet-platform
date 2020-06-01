package com.sunjet.frontend.vm.agency;

import com.sunjet.dto.asms.agency.AgencyAdmitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestItem;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.frontend.service.agency.AgencyAdmitService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.AgencyListBaseVM;
import com.sunjet.frontend.vm.base.DocStatus;
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
 * Created by Administrator on 2016/9/2.
 * 合作商准入申请 列表
 */
public class AgencyAdmitListVM extends AgencyListBaseVM<AgencyAdmitRequestItem> {

    @WireVariable
    private AgencyAdmitService agencyAdmitService;

    @Setter
    @Getter
    private AgencyAdmitRequestItem requestItem = new AgencyAdmitRequestItem();

    @Init(superclass = true)
    public void init() {
        this.setTitle("合作商准入申请");
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("AgencyAdmitRequestEntity:create"));
            this.setEnableUpdate(hasPermission("AgencyAdmitRequestEntity:modify"));
            this.setEnableDelete(hasPermission("AgencyAdmitRequestEntity:delete"));
        }
        this.setFormUrl("/views/agency/agency_admit_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        requestItem.setStartDate(agencyItem.getStartDate());
        requestItem.setEndDate(agencyItem.getEndDate());
        requestItem.setStatus(DocStatus.ALL.getIndex());

        // 判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            requestItem.setCode(getActiveUser().getAgency().getCode());
        }

        refreshFirstPage(requestItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = agencyAdmitService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        requestItem.setStartDate(agencyItem.getStartDate());
        requestItem.setEndDate(agencyItem.getEndDate());
        refreshPage(requestItem);
        getPageList();
    }

    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        requestItem.setStartDate(agencyItem.getStartDate());
        requestItem.setEndDate(agencyItem.getEndDate());
        refreshPage(requestItem);
        getPageList();
    }

    /**
     * 关闭窗口刷新列表
     */
    @NotifyChange("pageResult")
    @GlobalCommand(GlobalCommandValues.REFRESH_AGENCY_LIST)
    public void refreshList() {
        gotoPageNo(null);
        this.closeDialog();
    }

    @Command
    @NotifyChange("documentStatuses")
    public void selectStatus() {
        this.requestItem.setStatus(status.getIndex());
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
                AgencyAdmitRequestInfo info = agencyAdmitService.findOne(objId);
                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        agencyAdmitService.delete(objId);
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
    @NotifyChange({"requestItem", "agency"})
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        this.agency = agency;
        this.requestItem.setCode(agency.getCode());
        this.requestItem.setName(agency.getName());
    }

    @Command
    @NotifyChange({"requestItem", "agencyItem", "documentStatuses", "agency"})
    public void reset() {
        this.requestItem.setCode("");
        this.requestItem.setName("");
        this.setAgency(null);
        this.getAgencies().clear();
        this.setStatus(DocStatus.ALL);
        this.requestItem.setStatus(DocStatus.ALL.getIndex());
        this.getAgencyItem().setStartDate(DateHelper.getFirstOfYear());
        this.getAgencyItem().setEndDate(new Date());
    }

}
