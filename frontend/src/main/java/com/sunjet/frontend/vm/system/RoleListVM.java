package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.RoleItem;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.system.RoleService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class RoleListVM extends ListVM<RoleItem> {

    @WireVariable
    private RoleService roleService;

    @WireVariable
    private UserService userService;

    @Getter
    @Setter
    private RoleItem roleItem = new RoleItem();

    @Init
    public void init() {
        this.setTitle("角色管理");
        this.setEnableAdd(hasPermission("RoleEntity:create"));
        this.setEnableUpdate(hasPermission("RoleEntity:modify"));
        this.setEnableDelete(hasPermission("RoleEntity:delete"));
        this.setFormUrl("/views/system/role_form.zul");
        refreshFirstPage(roleItem, Order.DESC, "objId");
        getPageList();
    }

    /**
     * 下一页/上一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(roleItem);
        getPageList();
    }

    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(roleItem);
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = roleService.getPageList(pageParam);
    }


    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId) {
        ZkUtils.showQuestion("您确定删除角色？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {

                // 用户点击的是确定按钮
                roleService.deleteByObjId(objId);

                BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ROLE_LIST, null);
            }
        });
    }


    /**
     * 刷新父窗体并关闭弹出框
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_ROLE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        //重新加载当前页的数据
        gotoPageNo(null);
        //关闭弹出框
        this.closeDialog();
    }
}