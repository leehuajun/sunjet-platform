package com.sunjet.frontend.vm.agency;


import com.sunjet.dto.asms.agency.AgencyAlterRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAlterRequestItem;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.frontend.service.agency.AgencyAlterService;
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
 * Created by Administrator on 2016/9/5.
 * 合作商变更申请 列表
 */
public class AgencyAlterListVM extends AgencyListBaseVM<AgencyAlterRequestItem> {

    @WireVariable
    private AgencyAlterService agencyAlterService;

    @Getter
    @Setter
    private AgencyAlterRequestItem agencyAlterRequestItem = new AgencyAlterRequestItem();

    @Init(superclass = true)
    public void init() {
        //按钮判断
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("AgencyAlterRequestEntity:create"));
            this.setEnableUpdate(hasPermission("AgencyAlterRequestEntity:modify"));
            this.setEnableDelete(hasPermission("AgencyAlterRequestEntity:delete"));
        }

        this.setTitle("合作商变更申请");
        this.setFormUrl("/views/agency/agency_alter_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        agencyAlterRequestItem.setStartDate(agencyItem.getStartDate());
        agencyAlterRequestItem.setEndDate(agencyItem.getEndDate());
        agencyAlterRequestItem.setStatus(DocStatus.ALL.getIndex());

        // 判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            agencyAlterRequestItem.setCode(getActiveUser().getAgency().getCode());
        }


        refreshFirstPage(agencyAlterRequestItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = agencyAlterService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        agencyAlterRequestItem.setStartDate(agencyItem.getStartDate());
        agencyAlterRequestItem.setEndDate(agencyItem.getEndDate());
        refreshPage(agencyAlterRequestItem);
        getPageList();
    }

    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        agencyAlterRequestItem.setStartDate(agencyItem.getStartDate());
        agencyAlterRequestItem.setEndDate(agencyItem.getEndDate());
        refreshPage(agencyAlterRequestItem);
        getPageList();
    }

    /**
     * 关闭窗口刷新列表
     */
    @NotifyChange("pageResult")
    @GlobalCommand(GlobalCommandValues.REFRESH_AGENCY_ALTER_LIST)
    public void refreshList() {
        gotoPageNo(null);
        this.closeDialog();
    }

    @Command
    @NotifyChange("documentStatuses")
    public void selectStatus() {
        this.agencyAlterRequestItem.setStatus(status.getIndex());
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
                AgencyAlterRequestInfo info = agencyAlterService.findOne(objId);
                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        agencyAlterService.delete(objId);
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
    @NotifyChange({"agencyAlterRequestItem", "agency"})
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        this.agency = agency;
        this.agencyAlterRequestItem.setCode(agency.getCode());
        this.agencyAlterRequestItem.setName(agency.getName());
    }

    @Command
    @NotifyChange({"agencyAlterRequestItem", "agencyItem", "documentStatuses", "agency"})
    public void reset() {
        this.agencyAlterRequestItem.setCode("");
        this.agencyAlterRequestItem.setName("");
        this.setAgency(null);
        this.getAgencies().clear();
        this.setStatus(DocStatus.ALL);
        this.agencyAlterRequestItem.setStatus(DocStatus.ALL.getIndex());
        this.getAgencyItem().setStartDate(DateHelper.getFirstOfYear());
        this.getAgencyItem().setEndDate(new Date());
    }

}
