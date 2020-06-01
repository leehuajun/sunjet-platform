package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.ResourceItem;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.system.ResourceService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class ResourceListVM extends ListVM<ResourceItem> {

    @WireVariable
    private ResourceService resourceService;

    @Getter
    @Setter
    private ResourceItem resourceItem = new ResourceItem();


    /**
     * 初始化
     */
    @Init
    public void init() {
        this.setEnableAdd(hasPermission("ResourceEntity:create"));
        this.setEnableUpdate(hasPermission("ResourceEntity:modify"));
        this.setEnableDelete(hasPermission("ResourceEntity:delete"));
        this.setTitle("资源管理");
        //绑定新增/修改页面地址
        this.setFormUrl("/views/system/resource_form.zul");

        refreshFirstPage(resourceItem, Order.DESC, "objId");
        //获取分页数据
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
        //设置分页参数
        refreshPage(resourceItem);
        //刷新分页
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = resourceService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(resourceItem);
        //刷新分页
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
        ZkUtils.showQuestion("【谨慎!】您正准备删除资源!" +
                "\r\n\r\n删除资源将会同时删除该资源对应的访问列表," +
                "\r\n同时也清除所有角色拥有对该资源的访问权限。" +
                "\r\n\r\n是否确定删除?", "询问", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    resourceService.delete(objId);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RESOURCE_LIST, null);
                }
            }
        });
    }

    /**
     * 刷新父窗体并关闭弹出框
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_RESOURCE_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        //重新加载当前页的数据
        gotoPageNo(null);
        //关闭弹出框
        this.closeDialog();
    }


}
