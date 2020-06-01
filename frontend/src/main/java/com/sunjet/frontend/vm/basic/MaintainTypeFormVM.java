package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.frontend.service.basic.MaintainService;
import com.sunjet.frontend.service.basic.MaintainTypeService;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 维修项目 表单
 * Created by Administrator on 2017/7/13.
 */
public class MaintainTypeFormVM extends FormVM {

    @WireVariable
    private MaintainService maintainService;

    @WireVariable
    private MaintainTypeService maintainTypeService;

    @Getter
    @Setter
    private MaintainTypeInfo current = new MaintainTypeInfo();
    @Getter
    private MaintainTypeInfo parent = new MaintainTypeInfo();

    @Getter
    @Setter
    private String keywordModel;
    @Getter
    @Setter
    private String keywordSystem;
    @Getter
    @Setter
    private String keywordSubSystem;

    @Getter
    private List<MaintainTypeInfo> infos = new ArrayList<>();
    private List<MaintainTypeInfo> tmpInfos = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            current = maintainTypeService.findOne(objId);
            if (StringUtils.isNotBlank(current.getParentId())) {
                infos = maintainTypeService.findParents(current.getParentId());
                this.tmpInfos = this.infos;
                parent = maintainTypeService.findOne(current.getParentId());
            }
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange("current")
    public void submit() {
        try {
            current = maintainTypeService.save(current);
//            System.out.println(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Command
    @NotifyChange("tmpInfos")
    public void searchParent() {
        if (org.apache.commons.lang3.StringUtils.isBlank(keywordModel)) {
            this.tmpInfos = this.infos;
        } else {
            this.tmpInfos = this.infos.stream().filter(new Predicate<MaintainTypeInfo>() {
                @Override
                public boolean test(MaintainTypeInfo maintainTypeInfo) {
                    if (maintainTypeInfo.getName().contains(keywordModel))
                        return true;
                    else
                        return false;
                }
            }).collect(Collectors.toList());
        }
    }

    @Command
    @NotifyChange("*")
    public void selectParent(@BindingParam("model") MaintainTypeInfo model) {
        this.parent = model;
        this.current.setParentId(model.getObjId());
    }

    @Command
    @NotifyChange("*")
    public void clearParent() {
        this.current.setParentId("");
    }
}
