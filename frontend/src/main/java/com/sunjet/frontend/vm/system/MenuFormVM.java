package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.dto.system.admin.PermissionInfo;
import com.sunjet.frontend.service.system.MenuService;
import com.sunjet.frontend.service.system.PermissionService;
import com.sunjet.frontend.utils.zk.MenuTreeUtil;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyf
 * @create 2017-7-13 上午10:53
 */
public class MenuFormVM extends FormVM {

    @WireVariable
    MenuService menuService;   // 菜单service
    @WireVariable
    PermissionService permissionService;     //权限service


    @Getter
    @Setter
    private MenuInfo menuInfo;    //菜单实体
    //@Getter
    //@Setter
    //private String objId;
    @Getter
    @Setter
    private MenuInfo parentMenu;   //父类菜单
    @Getter
    @Setter
    private TreeModel treeModel;    // 菜单树形模型
    @Getter
    @Setter
    private List<PermissionInfo> permissionInfoList = new ArrayList<>();   //权限info列表
    @Getter
    @Setter
    private PermissionInfo permissionInfo;    // 权限管理

    @Init(superclass = true)
    public void init() {
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));
        //objId = (String) Executions.getCurrent().getArg().get("objId");
        if (StringUtils.isBlank(objId)) {
            //新增
            menuInfo = new MenuInfo();
            menuInfo.setParent(parentMenu);
        } else {
            //修改
            menuInfo = menuService.findOneById(objId);
            parentMenu = menuInfo.getParent();
        }

        //获取所有的权限列表
        permissionInfoList = permissionService.findAll();

        if (menuInfo.getPermissionCode() != null) {
            for (PermissionInfo model : permissionInfoList) {
                if (menuInfo.getPermissionCode() != null) {
                    if (menuInfo.getPermissionCode().equals(model.getPermissionCode())) {
                        this.permissionInfo = model;
                        break;
                    }
                }
            }
        }


    }


    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange("menuEntity")
    public void submit() {

        try {
            menuInfo.setParent(parentMenu);
            if (permissionInfo != null) {
                menuInfo.setPermissionCode(permissionInfo.getPermissionCode());
                menuInfo.setPermissionName(permissionInfo.toString());
            }
            menuInfo = menuService.save(menuInfo);
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_MENU_LIST, null);
    }


    /**
     * 变更父节点菜单
     *
     * @param parent
     * @param component
     */
    @Command
    @NotifyChange({"parentMenu"})
    public void changeParent(@BindingParam("parent") MenuInfo parent, @BindingParam("component") Component
            component) {
//        menuEntity.setParent(parent);
        parentMenu = parent;
        //((Bandbox) component).close();
    }

}
