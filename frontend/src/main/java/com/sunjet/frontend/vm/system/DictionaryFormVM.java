package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.zk.DictionaryTreeUtil;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;

import java.util.List;

/**
 * 数据字典 表单
 *
 * @author zyf
 * @create 2017-7-13 上午10:57
 */
public class DictionaryFormVM extends FormVM {

    @WireVariable
    private DictionaryService dictionaryService;

    @Setter
    @Getter
    private DictionaryInfo dictionaryInfo; //字典数据实体

    @Setter
    @Getter
    private List<DictionaryInfo> allParent; // 父级
    @Setter
    @Getter
    private TreeModel treeModel; // 数据字典树形模型

    @Init(superclass = true)
    public void init() {
        treeModel = new DefaultTreeModel(DictionaryTreeUtil.getRoot(dictionaryService.findAll()));
        if (StringUtils.isNotBlank(objId)) {
            dictionaryInfo = dictionaryService.findOne(objId);
        } else {
            dictionaryInfo = new DictionaryInfo();
        }
        allParent = dictionaryService.findAllParent();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void saveEntity() {
        dictionaryInfo = dictionaryService.save(dictionaryInfo);
        showDialog();
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DICTIONARY_LIST, null);
    }
}
