package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.UserItem;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class UserListVM extends ListVM<UserItem> {

    @WireVariable
    UserService userService;

    @Getter
    @Setter
    private UserItem userItem = new UserItem();

    @Getter
    @Setter
    private Window window;

    @Init
    public void init() {
        this.setTitle("用户管理");
        this.setEnableAdd(hasPermission("UserEntity:create"));
        this.setEnableUpdate(hasPermission("UserEntity:modify"));
        this.setEnableDelete(hasPermission("UserEntity:delete"));
        this.setFormUrl("/views/system/user_form.zul");
        refreshFirstPage(userItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(userItem);
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = userService.getPageList(pageParam);
    }

    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(userItem);
        getPageList();
    }

    /**
     * 删除用户信息
     *
     * @param objId
     * @return
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId) {
        ZkUtils.showQuestion("您确定删除用户？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                // 用户点击的是确定按钮
                userService.delete(objId);
//                roleService.updateRoles();
//                initList();
//                Events.postEvent("onClick", btnRefreshUser, null);
                BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_USER_LIST, null);
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });
    }

    ///**
    // * zk 页面格式化数据
    // *
    // * @param roleInfos
    // * @return
    // */
    //public String formatRoles(List<RoleInfo> roleInfos) {
    //
    //    String format = "";
    //    try {
    //        if(roleInfos!=null&&roleInfos.size()>0) {
    //            for (int i = 0; i < roleInfos.size(); i++) {
    //                RoleInfo info = JsonHelper.map2Bean(roleInfos.get(i), RoleInfo.class);
    //                format += info.getName();
    //                if (i < (roleInfos.size() - 1)) {
    //                    format += ",";
    //                }
    //            }
    //        }
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return format;
    //}

    @GlobalCommand(GlobalCommandValues.REFRESH_USER_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        //重新加载当前页的数据
        gotoPageNo(null);
        //关闭弹出框
        this.closeDialog();
    }

    /**
     * 修改密码
     */
    @Command
    public void changePassword(@BindingParam("objId") String objId) {
        Map<String, String> map = new HashMap<>();
        map.put("objId", objId);
        window = (Window) ZkUtils.createComponents("/views/system/change_password_form.zul", null, map);
        window.doModal();
    }

}
