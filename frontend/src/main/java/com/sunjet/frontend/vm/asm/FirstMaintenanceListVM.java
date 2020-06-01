package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.FirstMaintenanceItem;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.asm.FirstMaintenanceService;
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
 * 首保服务单 列表 VM
 */
public class FirstMaintenanceListVM extends ListVM<FirstMaintenanceItem> {
    @WireVariable
    private FirstMaintenanceService firstMaintenanceService;

    @Getter
    @Setter
    private String creator = "";

    @Getter
    @Setter
    FirstMaintenanceItem firstMaintenanceItem = new FirstMaintenanceItem();

    @Init(superclass = true)
    public void init() {
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAdd(false);
        } else {
            this.setEnableAdd(hasPermission("FirstMaintenanceEntity:create"));
            this.setEnableUpdate(hasPermission("FirstMaintenanceEntity:modify"));
            this.setEnableDelete(hasPermission("FirstMaintenanceEntity:delete"));
        }
        //this.setHeaderRows(2);// 设置搜索栏的行数，默认是1行
        this.setFormUrl("/views/asm/first_maintenance_form.zul");
        this.setTitle("首保服务单");
        //this.setBaseService(firstMaintenanceService);
        //initUserList();
        firstMaintenanceItem.setStatus(DocStatus.ALL.getIndex());

        //判断权限
        if (getActiveUser().getAgency() != null) {
            //合作商
            //firstMaintenanceItem.setAgencyName(getActiveUser().getAgency().getName());
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            firstMaintenanceItem.setDealerCode(getActiveUser().getDealer().getCode());
            firstMaintenanceItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        firstMaintenanceItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }

        refreshFirstPage(firstMaintenanceItem);

        //获取分页数据
        getPageList();


    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        setSort(Order.DESC, "createdTime");
        pageResult = firstMaintenanceService.getPageList(pageParam);
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
        refreshPage(firstMaintenanceItem, Order.DESC, "createdTime");
        //刷新分页
        getPageList();
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(firstMaintenanceItem, Order.DESC, "createdTime");
        //刷新分页
        getPageList();
    }

    /**
     * 刷新父窗体并关闭弹出框
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_FIRST_MAINTENANCE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        //重新加载当前页的数据
        gotoPageNo(null);
        //关闭弹出框
        this.closeDialog();
    }

    /**
     * 选中单据状态
     */
    @Command
    @NotifyChange("documentStatuses")
    public void selectedStatus() {
        this.firstMaintenanceItem.setStatus(selectedStatus.getIndex());
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
                FirstMaintenanceInfo info = firstMaintenanceService.findOneWithGoOutsById(objId);

                if (info.getStatus().equals(DocStatus.DRAFT.getIndex())) {
                    if (getActiveUser().getUserId().equals(info.getCreaterId()) || getActiveUser().getLogId().equals(info.getCreaterId())) {
                        firstMaintenanceService.delete(objId);
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
     * 选中服务站信息
     *
     * @param model
     */
    @Command
    @NotifyChange("firstMaintenanceItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        firstMaintenanceItem.setDealerCode(model.getCode());
        firstMaintenanceItem.setDealerName(model.getName());

    }


    @Command
    @NotifyChange({"firstMaintenanceItem", "documentStatuses"})
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.firstMaintenanceItem.setDealerCode("");
            this.firstMaintenanceItem.setDealerName("");
        }
        this.getDealers().clear();
        this.firstMaintenanceItem.setDocNo("");
        this.firstMaintenanceItem.setServiceManager("");
        this.setSelectedStatus(DocStatus.ALL);
        this.firstMaintenanceItem.setVin("");
        this.firstMaintenanceItem.setSender("");
        this.firstMaintenanceItem.setPlate("");
        this.firstMaintenanceItem.setStatus(DocStatus.ALL.getIndex());
        this.firstMaintenanceItem.setEndDate(new Date());
        this.firstMaintenanceItem.setStartDate(DateHelper.getFirstOfYear());
    }

//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
//        Selectors.wireComponents(view, this, false);
//        initList();
//    }
//
//
//    @Command
//    @NotifyChange({"resultDTO", "currentPageNo"})
//    public void search() {
//        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
//            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
//        } else {
//            filterList();
//        }
//    }
//
//    /***
//     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
//     * @param searchDTO
//     */
//    @Override
//    protected void configSearchOrder(SearchDTO searchDTO) {
//        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
//        searchDTO.setSearchOrderList(Arrays.asList(
//                new SearchOrder("createdTime", SearchOrder.OrderType.DESC, 1)
////        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
//        ));
//    }
//
//    /***
//     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
//     * @param searchDTO
//     */
//    @Override
//    protected void configSpecification(SearchDTO searchDTO) {
//        Specification<FirstMaintenanceInfo> specification = (root, query, cb) -> {
//            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
//            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
//            Predicate p07 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
//            Predicate p = cb.and(p04, p05, p07);
//            if (CommonHelper.getActiveUser().getDealer() != null) {
//                Predicate p00 = CustomRestrictions.eq("dealerCode", CommonHelper.getActiveUser().getDealer().getCode(), true).toPredicate(root, query, cb);
//                p = cb.and(p, p00);
//            }
//
//            // 服务站编号
//            if (this.getDealer() != null) {
//                Predicate p01 = CustomRestrictions.eq("dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
//                p = cb.and(p, p01);
//            }
//            //单据编号
//            if (StringUtils.isNotBlank(this.getDocNo())) {
//                Predicate p06 = CustomRestrictions.like("docNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
//                p = cb.and(p, p06);
//            }
//            // 状态
//            if (this.getSelectedStatus() != DocStatus.ALL) {
//                Predicate p03 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
//                p = cb.and(p, p03);
//            }
//
//            return p;
//        };
//        searchDTO.setSpecification(specification);
//    }

    @Override
    @Command
    @NotifyChange({"firstMaintenanceItem", "keyword"})
    public void clearSelectedDealer() {
        if (getActiveUser().getDealer() == null) {
            this.firstMaintenanceItem.setDealerCode(null);
            this.firstMaintenanceItem.setDealerName(null);
        }

        this.keyword = "";
    }
}
