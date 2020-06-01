package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.frontend.service.system.MenuService;
import com.sunjet.frontend.utils.zk.MenuTreeUtil;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;

/**
 * @author zyf
 * @create 2017-7-13 上午10:53
 */
public class MenuListVM extends ListVM<MenuInfo> {

    @Getter
    @Setter
    private TreeModel treeModel;    // 菜单树形模型
    //@Getter
    //@Setter
    //private Window formDialog;        // 用于显示编辑对话框
    @Getter
    @Setter
    private MenuInfo selectedEntity; // 当前选中的实体对象
    //@Getter
    //@Setter
    //private String formUrl = "";        // 设置编辑对话框的Url

    @WireVariable
    MenuService menuService;

    @Init
    public void init() {
        this.setEnableAdd(hasPermission("MenuEntity:create"));
        this.setEnableUpdate(hasPermission("MenuEntity:modify"));
        this.setEnableDelete(hasPermission("MenuEntity:delete"));
        this.setTitle("菜单管理");
        this.setFormUrl("/views/system/menu_form.zul");
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));

    }


    /**
     * 刷新按钮功能
     */
    @Command
    @NotifyChange("treeModel")
    public void refreshData() {
        //CommonTreeUtil<DictionaryInfo> commonTreeUtil = new CommonTreeUtil<>();
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));
    }

    /**
     * 删除实体
     *
     * @param objId
     */
    @Command
    @NotifyChange("treeModel")
    public void deleteEntity(@BindingParam("objId") String objId) {
        menuService.delete(objId);
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));

    }


    /**
     * 刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_MENU_LIST)
    @NotifyChange("treeModel")
    public void refreshList() {
        this.closeDialog();
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));
    }


}
