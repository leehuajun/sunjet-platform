package com.sunjet.frontend.vm.dealer;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.dealer.DealerQuitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerQuitRequestItem;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.dealer.DealerQuitService;
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
 * Created by Administrator on 2016/9/8.
 * 服务站退出申请
 */
public class DealerQuitListVM extends DealerListBaseVM<DealerQuitRequestItem> {

    @WireVariable
    private DealerQuitService dealerQuitService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DictionaryService dictionaryService;

    @Getter
    @Setter
    private DealerQuitRequestItem dealerQuitRequestItem = new DealerQuitRequestItem();


    @Init(superclass = true)
    public void init() {
        this.setTitle("服务站退出申请");
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("DealerQuitRequestEntity:create"));
            this.setEnableUpdate(hasPermission("DealerQuitRequestEntity:modify"));
            this.setEnableDelete(hasPermission("DealerQuitRequestEntity:delete"));
        }
        this.setFormUrl("/views/dealer/dealer_quit_form.zul");
        this.setProvinceEntities(regionService.findAllProvince());
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
        this.listStar = dictionaryService.findDictionariesByParentCode("10010");
        this.setQualifications(dictionaryService.findDictionariesByParentCode("10020"));
        dealerQuitRequestItem.setStartDate(dealerItem.getStartDate());
        dealerQuitRequestItem.setEndDate(dealerItem.getEndDate());
        dealerQuitRequestItem.setStatus(DocStatus.ALL.getIndex());

        // 判断权限
        if (getActiveUser().getDealer() != null) {
            //服务站
            this.setDealer(getActiveUser().getDealer());
            dealerQuitRequestItem.setCode(getActiveUser().getDealer().getCode());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        dealerQuitRequestItem.setSubmitter(getActiveUser().getLogId());
                        dealerQuitRequestItem.setServiceManagerId(getActiveUser().getUserId());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(dealerQuitRequestItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = dealerQuitService.getPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        dealerQuitRequestItem.setStartDate(dealerItem.getStartDate());
        dealerQuitRequestItem.setEndDate(dealerItem.getEndDate());
        //设置分页参数
        refreshFirstPage(dealerQuitRequestItem);
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
        dealerQuitRequestItem.setStartDate(dealerItem.getStartDate());
        dealerQuitRequestItem.setEndDate(dealerItem.getEndDate());
        //设置分页参数
        refreshPage(dealerQuitRequestItem);
        //刷新分页
        getPageList();
    }

    /**
     * 关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_DEALER_QUIT_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
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
                DealerQuitRequestInfo info = dealerQuitService.findOneById(objId);
                if (info.getStatus() == DocStatus.DRAFT.getIndex()) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        dealerQuitService.deleteByObjId(objId);
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
        dealerQuitRequestItem.setCode(model.getCode());
        dealerQuitRequestItem.setName(model.getName());

    }

    /**
     * 选择省份
     */
    @Command
    @NotifyChange("provinceEntities")
    public void selectProvince(@BindingParam("event") Event event) {
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        this.dealerQuitRequestItem.setProvinceId(selectedProvince.getObjId());
    }

    /**
     * 选择状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectStatus() {
        this.dealerQuitRequestItem.setStatus(status.getIndex());
    }

    /**
     * 选择服务站星级
     */
    @Command
    @NotifyChange("listStar")
    public void selectDealerStar() {
        this.dealerQuitRequestItem.setStar(dealerStar.getName());
    }

    /**
     * 选择维修资质
     */
    @Command
    @NotifyChange("qualifications")
    public void selectQualification() {
        this.dealerQuitRequestItem.setQualification(qualification.getName());
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

            this.dealerQuitRequestItem.setCode(null);
            this.dealerQuitRequestItem.setName(null);
        }
        this.dealerQuitRequestItem.setProvinceId(null);
        this.dealerQuitRequestItem.setStatus(DocStatus.ALL.getIndex());
        this.dealerQuitRequestItem.setStar(null);
        this.dealerQuitRequestItem.setQualification(null);
        this.dealerQuitRequestItem.setStartDate(dealerItem.getStartDate());
        this.dealerQuitRequestItem.setEndDate(dealerItem.getEndDate());
    }
}
