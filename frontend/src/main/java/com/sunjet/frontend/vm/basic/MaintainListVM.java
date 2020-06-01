package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.dto.asms.basic.VehiclePlatformInfo;
import com.sunjet.frontend.service.basic.MaintainService;
import com.sunjet.frontend.service.basic.MaintainTypeService;
import com.sunjet.frontend.service.basic.VehiclePlatformService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 维修项目 列表
 * Created by Administrator on 2017/7/13.
 */
public class MaintainListVM extends ListVM<MaintainInfo> {

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
    @Setter
    private String maintainCode;
    @Getter
    @Setter
    private String maintainName;

    @Getter
    private Boolean showImportWin = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<VehiclePlatformInfo> vehiclePlatformInfos = new ArrayList<>();       // 车型列表
    @Getter
    private List<VehiclePlatformInfo> tmpVehiclePlatformInfos = new ArrayList<>();       // 车型列表
    @Getter
    private List<MaintainTypeInfo> maintainTypeInfos = new ArrayList<>();


    private List<MaintainTypeInfo> vehicleSystems = new ArrayList<>();      // 车辆系统列表
    @Getter
    private List<MaintainTypeInfo> tmpVehicleSystems = new ArrayList<>();      // 车辆系统列表


    private List<MaintainTypeInfo> vehicleSubSystems = new ArrayList<>();   // 子系统列表
    @Getter
    private List<MaintainTypeInfo> tmpVehicleSubSystems = new ArrayList<>();   // 子系统列表

    @Getter
    @Setter
    private String keywordPlatform;
    @Getter
    @Setter
    private String keywordSystem;
    @Getter
    @Setter
    private String keywordSubSystem;

    @Init
    public void init() {
        this.setEnableImportMaintains(hasPermission("MaintainEntity:import"));
        this.setEnableAdd(hasPermission("MaintainEntity:create"));
        this.setEnableDelete(hasPermission("MaintainEntity:delete"));
        this.setEnableUpdate(hasPermission("MaintainEntity:modify"));
        this.setTitle("维修项目类型管理");
        this.setFormUrl("/views/basic/maintain_form.zul");
        this.vehiclePlatformInfos = vehiclePlatformService.findAll();
        this.tmpVehiclePlatformInfos = this.vehiclePlatformInfos;
        this.maintainTypeInfos = maintainTypeService.findAll();
        this.vehicleSystems = this.maintainTypeInfos.stream()
                .filter(item -> StringUtils.isBlank(item.getParentId()))
                .collect(Collectors.toList());
        this.tmpVehicleSystems = this.vehicleSystems;
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = maintainService.getPageList(pageParam);
    }

    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
//        maintainInfo.setCode(this.maintainCode);
//        maintainInfo.setName(this.maintainName);
//        maintainInfo.setVehicleModelId(this.vehicleModel==null?"":this.vehicleModel.getObjId());
//        maintainInfo.setVehicleModelName(this.vehicleModel==null?"":this.vehicleModel.getName());
//        maintainInfo.setVehicleSystemId(this.vehicleSystem==null?"":this.vehicleSystem.getObjId());
//        maintainInfo.setVehicleSystemName(this.vehicleSystem==null?"":this.vehicleSystem.getName());
//        maintainInfo.setVehicleSubSystemId(this.vehicleSubSystem==null?"":this.vehicleSubSystem.getObjId());
//        maintainInfo.setVehicleSubSystemName(this.vehicleSubSystem==null?"":this.vehicleSubSystem.getName());
        //设置分页参数
        refreshFirstPage(maintainInfo);
        //刷新分页
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
        refreshPage(maintainInfo);
        //刷新分页
        getPageList();
    }

    /**
     * 关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_MAINTAIN_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
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
        ZkUtils.showQuestion("您确定删除该对象？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                maintainService.deleteByObjId(objId);
                getPageList();
                BindUtils.postNotifyChange(null, null, this, "pageResult");
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });
    }

    /**
     * 重置查询条件
     */

    @Command
    @NotifyChange("maintainInfo")
    public void reset() {
        maintainInfo.setCode("");
        maintainInfo.setName("");
        maintainInfo.setVehicleModelId("");
        maintainInfo.setVehicleModelName("");
        maintainInfo.setVehicleSystemId("");
        maintainInfo.setVehicleSystemName("");
        maintainInfo.setVehicleSubSystemId("");
        maintainInfo.setVehicleSubSystemName("");
//        this.tmpVehicleModels.clear();
//        this.tmpVehicleSystems.clear();
//        this.tmpVehicleSubSystems.clear();
        this.vehicleSubSystems.clear();
    }

    @Command
    @NotifyChange("tmpVehiclePlatformInfos")
    public void searchVehiclePlatform() {
        if (StringUtils.isBlank(keywordPlatform)) {
            this.tmpVehiclePlatformInfos = this.vehiclePlatformInfos;
        } else {
            this.tmpVehiclePlatformInfos = this.vehiclePlatformInfos.stream().filter(platformInfo -> {
                if (platformInfo.getName().contains(keywordPlatform)) {
                    return true;
                } else {
                    return false;
                }
            }).collect(Collectors.toList());
        }
    }

    @Command
    @NotifyChange("tmpVehicleSystems")
    public void searchVehicleSystem() {
        if (StringUtils.isBlank(keywordSystem)) {
            this.tmpVehicleSystems = this.vehicleSystems;
        } else {
            this.tmpVehicleSystems = this.vehicleSystems.stream().filter(maintainTypeInfo -> {
                if (maintainTypeInfo.getName().contains(keywordSystem)) {
                    return true;
                } else {
                    return false;
                }
            }).collect(Collectors.toList());
        }

    }

    @Command
    @NotifyChange("tmpVehicleSubSystems")
    public void searchVehicleSubSystem() {


        if (StringUtils.isBlank(keywordSubSystem)) {
            this.tmpVehicleSubSystems = this.vehicleSubSystems;
        } else {
            this.tmpVehicleSubSystems = this.vehicleSubSystems.stream().filter(new Predicate<MaintainTypeInfo>() {
                @Override
                public boolean test(MaintainTypeInfo maintainTypeInfo) {
                    if (maintainTypeInfo.getName().contains(keywordSubSystem)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }).collect(Collectors.toList());
        }
    }

    @Command
    @NotifyChange("*")
    public void selectVehiclePlatform(@BindingParam("model") VehiclePlatformInfo platformInfo) {
        maintainInfo.setVehicleSystemId("");
        maintainInfo.setVehicleSystemName("");
        maintainInfo.setVehicleSubSystemId("");
        maintainInfo.setVehicleSubSystemName("");
        this.maintainInfo.setVehicleModelId(platformInfo.getObjId());
        this.maintainInfo.setVehicleModelName(platformInfo.getName());
        this.tmpVehicleSystems = this.vehicleSystems;
        this.vehicleSubSystems.clear();
    }

    @Command
    @NotifyChange("*")
    public void selectVehicleSystem(@BindingParam("model") MaintainTypeInfo vehicleSystem) {
        maintainInfo.setVehicleSubSystemId("");
        maintainInfo.setVehicleSubSystemName("");
        this.maintainInfo.setVehicleSystemId(vehicleSystem.getObjId());
        this.maintainInfo.setVehicleSystemName(vehicleSystem.getName());

//        this.vehicleSubSystems = maintainTypeService.findVehicleSubSystems(vehicleSystem.getObjId());

        this.vehicleSubSystems = this.maintainTypeInfos.stream()
                .filter(item -> StringUtils.isNotBlank(item.getParentId()))
                .filter(item -> item.getParentId().equals(this.maintainInfo.getVehicleSystemId()))
                .collect(Collectors.toList());
        this.tmpVehicleSubSystems = this.vehicleSubSystems;
    }

    @Command
    @NotifyChange("*")
    public void selectVehicleSubSystem(@BindingParam("model") MaintainTypeInfo vehicleSubSystem) {
        this.maintainInfo.setVehicleSubSystemId(vehicleSubSystem.getObjId());
        this.maintainInfo.setVehicleSubSystemName(vehicleSubSystem.getName());
//        this.vehicleSubSystems = maintainTypeService.findVehicleSubSystems(this.vehicleSystem.getObjId());
//        this.tmpVehicleSubSystems = this.vehicleSubSystems;
    }

    @Command
    @NotifyChange("*")
    public void clearVehicleSubSystem() {
        this.maintainInfo.setVehicleSubSystemId("");
        this.maintainInfo.setVehicleSubSystemName("");
    }

    @Command
    @NotifyChange("*")
    public void clearVehicleSystem() {
        maintainInfo.setVehicleSystemId("");
        maintainInfo.setVehicleSystemName("");
        maintainInfo.setVehicleSubSystemId("");
        maintainInfo.setVehicleSubSystemName("");
        this.tmpVehicleSubSystems.clear();
        this.vehicleSubSystems.clear();
    }

//    @Command
//    @NotifyChange("*")
//    public void clearVehicleModel() {
////        maintainInfo.setVehicleModelId("");
////        maintainInfo.setVehicleModelName("");
//        maintainInfo.setVehicleSystemId("");
//        maintainInfo.setVehicleSystemName("");
//        maintainInfo.setVehicleSubSystemId("");
//        maintainInfo.setVehicleSubSystemName("");
//        this.tmpVehicleSubSystems.clear();
//        this.vehicleSubSystems.clear();
//        this.tmpVehicleSystems.clear();
//        this.vehicleSystems.clear();
//    }

    @Command
    @NotifyChange("*")
    public void clearVehiclePlatform() {
        maintainInfo.setVehicleModelId("");
        maintainInfo.setVehicleModelName("");
    }

    @Command
    public void openImportWin() {
        showImportWin = true;
    }
}
