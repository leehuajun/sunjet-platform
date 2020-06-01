package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.VehiclePlatformInfo;
import com.sunjet.frontend.service.basic.VehiclePlatformService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 车型平台 表单
 * Created by Administrator on 2017/7/13.
 */
public class VehiclePlatformListVM extends ListVM<VehiclePlatformInfo> {

    @WireVariable
    private VehiclePlatformService vehiclePlatformService;

    @Getter
    private List<VehiclePlatformInfo> infos = new ArrayList<>();

    @Getter
    @Setter
    private VehiclePlatformInfo current = new VehiclePlatformInfo();
    @Getter
    @Setter
    private Boolean canAdd = false;
    @Getter
    @Setter
    private Boolean canSave = false;
    @Getter
    @Setter
    private Boolean canDelete = false;

    @Init(superclass = true)
    public void init() {
        this.setCanAdd(hasPermission("VehiclePlatformEntity:create"));
        this.setCanSave(hasPermission("VehiclePlatformEntity:modify"));
        this.setCanDelete(hasPermission("VehiclePlatformEntity:delete"));
        infos = vehiclePlatformService.findAll();
    }


    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange({"infos"})
    public void submit() {
        try {
            if (StringUtils.isBlank(current.getName())) {
                ZkUtils.showExclamation("名称不能为空!", "系统提示");
                return;
            }
            current = vehiclePlatformService.save(current);
            infos = vehiclePlatformService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("infos")
    public void refresh() {
        infos = vehiclePlatformService.findAll();
    }

    @Command
    @NotifyChange({"infos", "current"})
    public void deleteEntity() {
        try {
            if (current == null || StringUtils.isBlank(current.getName())) {
                ZkUtils.showExclamation("未选中对象!", "系统提示");
                return;
            }
            Boolean result = vehiclePlatformService.delete(current.getObjId());
            if (result) {
                ZkUtils.showInformation("删除成功!", "系统提示");
                this.current = new VehiclePlatformInfo();
                infos = vehiclePlatformService.findAll();
            } else {
                ZkUtils.showInformation("删除失败!", "系统提示");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("current")
    public void addVehiclePlatform() {
        current = new VehiclePlatformInfo();
    }

    @Command
    @NotifyChange("current")
    public void selectInfo(@BindingParam("model") VehiclePlatformInfo info) {
        current = info;
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }



}
