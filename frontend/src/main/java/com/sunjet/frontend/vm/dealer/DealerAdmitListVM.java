package com.sunjet.frontend.vm.dealer;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestItem;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.dealer.DealerAdmitService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DealerListBaseVM;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;

import java.util.Date;
import java.util.List;

/**
 * 服务站准入申请
 * Created by Administrator on 2016/9/2.
 */
public class DealerAdmitListVM extends DealerListBaseVM<DealerAdmitRequestItem> {

    @WireVariable
    private DealerAdmitService dealerAdmitService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DictionaryService dictionaryService;

    @Getter
    @Setter
    private DealerAdmitRequestItem dealerAdmitRequestItem = new DealerAdmitRequestItem();

    @Init(superclass = true)
    public void init() {
        this.setTitle("服务站准入申请");
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("DealerAdmitRequestEntity:create"));
            this.setEnableUpdate(hasPermission("DealerAdmitRequestEntity:modify"));
            this.setEnableDelete(hasPermission("DealerAdmitRequestEntity:delete"));
//            this.setEnableAdd(hasPermission("DealerAdmitRequestEntity:" + OperationEnum.CREATE.getCode()));
        }
        this.setFormUrl("/views/dealer/dealer_admit_form.zul");
        this.setProvinceEntities(regionService.findAllProvince());
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        this.listStar = dictionaryService.findDictionariesByParentCode("10010");
        this.setQualifications(dictionaryService.findDictionariesByParentCode("10020"));
        dealerAdmitRequestItem.setStartDate(dealerItem.getStartDate());
        dealerAdmitRequestItem.setEndDate(dealerItem.getEndDate());
        dealerAdmitRequestItem.setStatus(DocStatus.ALL.getIndex());

        // 判断权限
        if (getActiveUser().getDealer() != null) {
            //服务站
            this.setDealer(getActiveUser().getDealer());
            dealerAdmitRequestItem.setSubmitter(getActiveUser().getLogId());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        dealerAdmitRequestItem.setSubmitter(getActiveUser().getLogId());
                        dealerAdmitRequestItem.setServiceManagerId(getActiveUser().getUserId());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(dealerAdmitRequestItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = dealerAdmitService.getPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        dealerAdmitRequestItem.setStartDate(dealerItem.getStartDate());
        dealerAdmitRequestItem.setEndDate(dealerItem.getEndDate());
        //设置分页参数
        refreshFirstPage(dealerAdmitRequestItem);
        //刷新分页
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
        dealerAdmitRequestItem.setStartDate(dealerItem.getStartDate());
        dealerAdmitRequestItem.setEndDate(dealerItem.getEndDate());
        //设置分页参数
        refreshPage(dealerAdmitRequestItem);
        //刷新分页
        getPageList();
    }

    /**
     * 关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_DEALER_ADMIT_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
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
                DealerAdmitRequestInfo info = dealerAdmitService.findOneById(objId);

                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        dealerService.deleteByObjId(info.getDealerInfo().getObjId());
                        dealerAdmitService.deleteByObjId(objId);
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
     * 选择服务站编号
     */
    @Command
    @NotifyChange({"dealerAdmitRequestInfo", "dealer"})
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        dealer = model;
        dealerAdmitRequestItem.setCode(model.getCode());
        dealerAdmitRequestItem.setName(model.getName());

    }

    /**
     * 选择省份
     */
    @Command
    @NotifyChange("provinceEntities")
    public void selectProvince(@BindingParam("event") Event event) {
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        this.dealerAdmitRequestItem.setProvinceId(selectedProvince.getObjId());
    }

    /**
     * 选择状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectStatus() {
        this.dealerAdmitRequestItem.setStatus(status.getIndex());
    }

    /**
     * 选择服务站星级
     */
    @Command
    @NotifyChange("listStar")
    public void selectDealerStar() {
        this.dealerAdmitRequestItem.setStar(dealerStar.getName());
    }

    /**
     * 选择维修资质
     */
    @Command
    @NotifyChange("qualifications")
    public void selectQualification() {
        this.dealerAdmitRequestItem.setQualification(qualification.getName());
    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.getDealers().clear();
        this.setDealer(null);
        this.setStatus(DocStatus.ALL);
        this.setDealer(getActiveUser().getDealer());
        this.setSelectedProvince(null);
        this.dealerItem.setStartDate(DateHelper.getFirstOfYear());
        this.dealerItem.setEndDate(new Date());
        this.setDealerStar(null);
        this.setQualification(null);
        if (getActiveUser().getDealer() == null) {

            this.dealerAdmitRequestItem.setCode(null);
            this.dealerAdmitRequestItem.setName(null);
        }
        this.dealerAdmitRequestItem.setProvinceId(null);
        this.dealerAdmitRequestItem.setStatus(DocStatus.ALL.getIndex());
        this.dealerAdmitRequestItem.setStar(null);
        this.dealerAdmitRequestItem.setQualification(null);
        this.dealerAdmitRequestItem.setStartDate(dealerItem.getStartDate());
        this.dealerAdmitRequestItem.setEndDate(dealerItem.getEndDate());
    }
}
