package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.OperationItem;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.system.OperationService;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * 操作列表
 *
 * @author zyf
 * @create 2017-7-13 上午10:50
 */
public class OperationListVM extends ListVM<OperationItem> {

    @WireVariable
    private OperationService operationService;

    @Getter
    @Setter
    private OperationItem operationItem = new OperationItem();

    /**
     * 获取列表集合
     */
    @Init
    public void init() {
        this.setEnableAdd(hasPermission("OperationEntity:create"));
        this.setEnableUpdate(hasPermission("OperationEntity:modify"));
        this.setEnableDelete(hasPermission("OperationEntity:delete"));
        this.setTitle("操作列表管理");
        this.setFormUrl("/views/system/operation_form.zul");
        refreshFirstPage(operationItem, Order.ASC, "seq");
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshFirstPage(operationItem, Order.ASC, "seq");
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = operationService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(operationItem, Order.ASC, "seq");
        getPageList();
    }


    /**
     * 关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_OPERATION_LIST)
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
    public void deleteEntity(@BindingParam("objId") String objId) {
        operationService.deleteByObjId(objId);
        getPageList();

    }

}
