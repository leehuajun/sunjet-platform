package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.frontend.service.basic.MaintainTypeService;
import com.sunjet.frontend.utils.zk.MaintainTreeUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 车辆系统
 * Created by Administrator on 2017/7/13.
 *
 * @author lhj
 */
public class MaintainTypeListVM extends ListVM<MaintainTypeInfo> {

    @WireVariable
    private MaintainTypeService maintainTypeService;

    @Getter
    @Setter
    private String keyword;

    @Getter
    private List<MaintainTypeInfo> tmpInfos = new ArrayList<>();

    private List<MaintainTypeInfo> infoList = new ArrayList<>();

    @Getter
    private List<MaintainTypeInfo> systemInfos = new ArrayList<>();

    @Getter
    private TreeModel treeModel;

    @Getter
    @Setter
    private MaintainTypeInfo current;
    @Getter
    @Setter
    private MaintainTypeInfo selectedSystem;
    @Getter
    @Setter
    private Boolean canAdd = false;
    @Getter
    @Setter
    private Boolean canSave = false;
    @Getter
    @Setter
    private Boolean canDelete = false;

    @Init
    public void init() {
        this.setCanAdd(hasPermission("VehicleSystemEntity:create"));
        this.setCanSave(hasPermission("VehicleSystemEntity:modify"));
        this.setCanDelete(hasPermission("VehicleSystemEntity:delete"));

        infoList = maintainTypeService.findAll();

        this.systemInfos = infoList.stream()
                .filter(item -> StringUtils.isBlank(item.getParentId()))
                .collect(Collectors.toList());
        this.tmpInfos = this.systemInfos;

        this.treeModel = new DefaultTreeModel(MaintainTreeUtil.getRoot(infoList));
    }

    @Command
    @NotifyChange({"current", "selectedSystem"})
    public void selectMaintainType(@BindingParam("model") MaintainTypeInfo maintainTypeInfo) {
        current = maintainTypeInfo;
        selectedSystem = null;
        if (StringUtils.isNotBlank(current.getParentId())) {
            this.systemInfos.forEach(item -> {
                if (item.getObjId().equals(current.getParentId())) {
                    selectedSystem = item;
                }
            });
        }
    }

    @Command
    @NotifyChange({"current", "selectedSystem"})
    public void add() {
        current = new MaintainTypeInfo();
        this.selectedSystem = null;
    }


    @Command
    @NotifyChange("tmpInfos")
    public void searchSystem() {
        if (org.apache.commons.lang3.StringUtils.isBlank(keyword)) {
            this.tmpInfos = this.systemInfos;
        } else {
            this.tmpInfos = this.systemInfos.stream().filter(new Predicate<MaintainTypeInfo>() {
                @Override
                public boolean test(MaintainTypeInfo maintainTypeInfo) {
                    if (maintainTypeInfo.getName().contains(keyword)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }).collect(Collectors.toList());
        }
    }

    @Command
    @NotifyChange({"current", "selectedSystem"})
    public void clearSystem() {
        if (this.current != null && this.selectedSystem != null) {
            this.current.setParentId(null);
            this.selectedSystem = null;
        }

    }

    @Command
    @NotifyChange("selectedSystem")
    public void selectSystem(@BindingParam("model") MaintainTypeInfo maintainTypeInfo) {
        selectedSystem = maintainTypeInfo;
        this.current.setParentId(selectedSystem.getObjId());
    }

    /**
     * 表单提交
     */
    @Command
    public void submit() {
        try {
            if (this.current == null
                    || (StringUtils.isBlank(this.current.getObjId()) && StringUtils.isBlank(this.current.getName()))) {
                ZkUtils.showInformation("不能保存空对象！", "系统提示");
                return;
            }

            boolean isNew = StringUtils.isBlank(current.getObjId()) ? true : false;
            MaintainTypeInfo tmp = maintainTypeService.findOne(current.getObjId());
            current = maintainTypeService.save(current);
            if (isNew) {
                this.infoList = maintainTypeService.findAll();
                this.treeModel = new DefaultTreeModel(MaintainTreeUtil.getRoot(this.infoList));
                if (StringUtils.isBlank(current.getParentId())) {
                    this.systemInfos = infoList.stream()
                            .filter(item -> StringUtils.isBlank(item.getParentId()))
                            .collect(Collectors.toList());
                    this.tmpInfos = this.systemInfos;
                    this.keyword = "";
                    BindUtils.postNotifyChange(null, null, this, "tmpInfos");
                    BindUtils.postNotifyChange(null, null, this, "keyword");

                }
                BindUtils.postNotifyChange(null, null, this, "current");
                BindUtils.postNotifyChange(null, null, this, "treeModel");

            } else {
                if (!tmp.getParentId().equals(current.getParentId())) {
                    BindUtils.postNotifyChange(null, null, this, "current");
                    BindUtils.postNotifyChange(null, null, this, "treeModel");
                } else {
                    BindUtils.postNotifyChange(null, null, this, "current");
                }
            }

//            BindUtils.postNotifyChange(null, null, this, "treeModel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Command
    public void delete() {
        if (this.current == null || StringUtils.isBlank(this.current.getObjId())) {
            ZkUtils.showInformation("没有选中的对象！", "系统提示");
            return;
        }

        List<MaintainTypeInfo> infos = infoList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getParentId()))
                .filter(item -> item.getParentId().equals(current.getObjId()))
                .collect(Collectors.toList());

        if (StringUtils.isBlank(this.current.getParentId()) && infos.size() > 0) {
            ZkUtils.showInformation("当前项存在子项，不能删除！", "系统提示");
            return;
        }

        ZkUtils.showQuestion("确定删除该对象？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                // 用户点击的是确定按钮
                maintainTypeService.delete(current.getObjId());
                infoList = maintainTypeService.findAll();
                this.current = null;
                selectedSystem = null;
                this.treeModel = new DefaultTreeModel(MaintainTreeUtil.getRoot(maintainTypeService.findAll()));
                BindUtils.postNotifyChange(null, null, this, "current");
                BindUtils.postNotifyChange(null, null, this, "treeModel");
                BindUtils.postNotifyChange(null, null, this, "selectedSystem");
            }
        });
    }
}
