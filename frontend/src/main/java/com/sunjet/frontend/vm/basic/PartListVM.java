package com.sunjet.frontend.vm.basic;


import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * @author zyf
 * @create 2017-7-13 上午12:00
 * 配件目录列表
 */
public class PartListVM extends ListVM<PartInfo> {

    @Getter
    @Setter
    private PartInfo partInfo = new PartInfo();

    @WireVariable
    PartService partService;

    @Init
    public void init() {
        this.setEnableAdd(hasPermission("PartEntity:create"));
        this.setEnableImportAddParts(hasPermission("PartEntity:import"));
        this.setEnableImportModifyParts(hasPermission("PartEntity:import"));
        this.setTitle("配件目录");
        this.setFormUrl("/views/basic/part_form.zul");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = partService.getPageList(pageParam);
    }


    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(partInfo);
        getPageList();
    }


    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(partInfo);
        getPageList();
    }

    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_PART_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        gotoPageNo(null);
        this.closeDialog();
    }


    /**
     * 重置查询条件
     */

    @Command
    @NotifyChange("partInfo")
    public void reset() {
        partInfo.setCode("");
        partInfo.setName("");
    }


}

