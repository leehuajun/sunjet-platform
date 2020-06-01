package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.dto.asms.basic.VehiclePlatformInfo;
import com.sunjet.frontend.service.basic.MaintainService;
import com.sunjet.frontend.service.basic.MaintainTypeService;
import com.sunjet.frontend.service.basic.VehiclePlatformService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 维修项目 表单
 * Created by Administrator on 2017/7/13.
 */
public class MaintainFormVM extends FormVM {

    @WireVariable
    private MaintainService maintainService;

    @WireVariable
    private MaintainTypeService maintainTypeService;

    @WireVariable
    private VehiclePlatformService vehiclePlatformService;

    @Getter
    @Setter
    private MaintainInfo maintainInfo = new MaintainInfo();

    @Getter
    private List<VehiclePlatformInfo> vehiclePlatformInfos = new ArrayList<>();       // 车型平台列表
    @Getter
    private List<MaintainTypeInfo> allSystems = new ArrayList<>();  // 所有系统列表（包括车辆系统和子系统）
    @Getter
    private List<MaintainTypeInfo> vehicleSystems = new ArrayList<>();      // 车辆系统列表
    @Getter
    private List<MaintainTypeInfo> vehicleSubSystems = new ArrayList<>();   // 子系统列表

    @Getter
    @Setter
    private VehiclePlatformInfo selectedPlatform;
    @Getter
    @Setter
    private MaintainTypeInfo selectedSystem;
    @Getter
    @Setter
    private MaintainTypeInfo selectedSubSystem;
    @Getter
    @Setter
    private Boolean enableUpdate = false;      // 编辑按钮状态


    @Init(superclass = true)
    public void init() {
        this.setEnableUpdate(hasPermission("MaintainEntity:modify"));
        this.vehiclePlatformInfos = vehiclePlatformService.findAll();
        this.allSystems = maintainTypeService.findAll();

        this.vehicleSystems = this.allSystems.stream().filter(maintainTypeInfo -> {
            if (StringUtils.isBlank(maintainTypeInfo.getParentId())) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());


        if (StringUtils.isNotBlank(objId)) {
            maintainInfo = maintainService.findOneById(objId);

            if (StringUtils.isNotBlank(maintainInfo.getVehicleModelId())) {
                Optional<VehiclePlatformInfo> vpi = vehiclePlatformInfos.stream()
                        .filter(platformInfo -> platformInfo.getObjId().equals(maintainInfo.getVehicleModelId()))
                        .findFirst();
                if (!vpi.equals(Optional.empty())) {
                    this.selectedPlatform = vpi.get();
                }
            }

            if (StringUtils.isNotBlank(maintainInfo.getVehicleSystemId())) {
                this.vehicleSubSystems = this.allSystems.stream()
                        .filter(item -> StringUtils.isNotBlank(item.getParentId()))
                        .filter(item -> item.getParentId().equals(maintainInfo.getVehicleSystemId()))
                        .collect(Collectors.toList());

                Optional<MaintainTypeInfo> vpi = this.vehicleSystems.stream()
                        .filter(item -> item.getObjId().equals(maintainInfo.getVehicleSystemId()))
                        .findFirst();
                if (!vpi.equals(Optional.empty())) {
                    this.selectedSystem = vpi.get();
                }

            }

            if (StringUtils.isNotBlank(maintainInfo.getVehicleSubSystemId())) {
                Optional<MaintainTypeInfo> vpi = this.allSystems.stream()
                        .filter(item -> item.getObjId().equals(maintainInfo.getVehicleSubSystemId()))
                        .findFirst();
                if (!vpi.equals(Optional.empty())) {
                    this.selectedSubSystem = vpi.get();
                }
            }


        } else {
            maintainInfo = new MaintainInfo();
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
    @NotifyChange("maintainInfo")
    public void submit() {
        try {
            if (StringUtils.isBlank(maintainInfo.getCode())) {
                ZkUtils.showInformation("请填写项目编号", "提示");
                return;
            }
            if (StringUtils.isBlank(maintainInfo.getName())) {
                ZkUtils.showInformation("请填写项目名称", "提示");
                return;
            }
            if (maintainInfo.getWorkTime() == null) {
                ZkUtils.showInformation("请填写工时定额", "提示");
                return;
            }
            if (StringUtils.isBlank(maintainInfo.getComment())) {
                ZkUtils.showInformation("请填写备注", "提示");
                return;
            }
            if (StringUtils.isBlank(maintainInfo.getVehicleModelId())) {
                ZkUtils.showInformation("请选择车型平台", "提示");
                return;
            }
            if (StringUtils.isBlank(maintainInfo.getVehicleSystemId())) {
                ZkUtils.showInformation("请选择车辆系统", "提示");
                return;
            }
            if (StringUtils.isBlank(maintainInfo.getVehicleSubSystemId())) {
                ZkUtils.showInformation("请选择车辆子系统", "提示");
                return;
            }

            maintainInfo = maintainService.save(maintainInfo);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_MAINTAIN_LIST, null);
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("maintainInfo")
    public void selectVehiclePlatform(@BindingParam("model") VehiclePlatformInfo vehiclePlatformInfo) {
        this.selectedPlatform = vehiclePlatformInfo;
        maintainInfo.setVehicleSystemId("");
        maintainInfo.setVehicleSystemName("");
        maintainInfo.setVehicleSubSystemId("");
        maintainInfo.setVehicleSubSystemName("");
        this.maintainInfo.setVehicleModelId(this.selectedPlatform.getObjId());
        this.maintainInfo.setVehicleModelName(this.selectedPlatform.getName());
    }

    @Command
    @NotifyChange({"maintainInfo", "vehicleSubSystems"})
    public void selectVehicleSystem(@BindingParam("model") MaintainTypeInfo maintainTypeInfo) {

        this.selectedSystem = maintainTypeInfo;

        maintainInfo.setVehicleSubSystemId("");
        maintainInfo.setVehicleSubSystemName("");
        this.maintainInfo.setVehicleSystemId(this.selectedSystem.getObjId());
        this.maintainInfo.setVehicleSystemName(this.selectedSystem.getName());

        this.vehicleSubSystems = this.allSystems.stream()
                .filter(item -> StringUtils.isNotBlank(item.getParentId()))
                .filter(item -> item.getParentId().equals(this.selectedSystem.getObjId()))
                .collect(Collectors.toList());

    }

    @Command
    @NotifyChange("maintainInfo")
    public void selectVehicleSubSystem(@BindingParam("model") MaintainTypeInfo maintainTypeInfo) {
        this.selectedSubSystem = maintainTypeInfo;

        this.maintainInfo.setVehicleSubSystemId(this.selectedSubSystem.getObjId());
        this.maintainInfo.setVehicleSubSystemName(this.selectedSubSystem.getName());
//        this.vehicleSubSystems = maintainTypeService.findVehicleSubSystems(this.vehicleSystem.getObjId());
//        this.tmpVehicleSubSystems = this.vehicleSubSystems;
    }
}
