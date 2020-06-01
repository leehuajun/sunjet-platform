package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.zk.DictionaryTreeUtil;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;

/**
 * 字典 列表
 *
 * @author zyf
 * @create 2017-7-13 上午10:57
 */
public class DictionaryListVM extends ListVM<DictionaryInfo> {


    @WireVariable
    private DictionaryService dictionaryService;

    @Getter
    @Setter
    private TreeModel treeModel;


    @Init
    public void init() {
        this.setEnableAdd(hasPermission("Data dictionary:create"));
        this.setEnableUpdate(hasPermission("Data dictionary:modify"));
        this.setEnableDelete(hasPermission("Data dictionary:delete"));
        this.setTitle("数据字典管理");
        //CommonTreeUtil<DictionaryInfo> commonTreeUtil = new CommonTreeUtil<>();
        //List<DictionaryInfo> all = dictionaryService.findAll();
        this.treeModel = new DefaultTreeModel(DictionaryTreeUtil.getRoot(dictionaryService.findAll()));
        this.setFormUrl("/views/system/dictionary_form.zul");
    }

    /**
     * 刷新按钮功能
     */
    @Command
    @NotifyChange("treeModel")
    public void refreshData() {
        this.treeModel = new DefaultTreeModel(DictionaryTreeUtil.getRoot(dictionaryService.findAll()));
    }


    /**
     * 删除对象
     *
     * @param objId
     */
    @Command
    @NotifyChange("treeModel")
    public void deleteEntity(@BindingParam("objId") String objId) {
        dictionaryService.delete(objId);
        this.treeModel = new DefaultTreeModel(DictionaryTreeUtil.getRoot(this.dictionaryService.findAll()));
    }

    /**
     * 刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_DICTIONARY_LIST)
    @NotifyChange("treeModel")
    public void refreshList() {
        this.closeDialog();
        this.treeModel = new DefaultTreeModel(DictionaryTreeUtil.getRoot(dictionaryService.findAll()));
    }

}
