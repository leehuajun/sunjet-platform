package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * 车辆 列表
 * Created by Administrator on 2017/7/13.
 */
public class VehicleListVM extends ListVM<VehicleInfo> {

    @WireVariable
    private VehicleService vehicleService;

    @Setter
    @Getter
    private VehicleInfo vehicleInfo = new VehicleInfo(); // 实体
    @Setter
    @Getter
    private String vin;             // 车辆VIN
    @Setter
    @Getter
    private String vsn;             // 车辆VSN
    @Setter
    @Getter
    private String vehicleModel;    // 车型型号
    @Setter
    @Getter
    private String seller;          // 经销商

    @Init
    public void Init() {
        this.setEnableImportAddVehicles(hasPermission("VehicleEntity:create"));
        this.setEnableImportModifyVehicles(hasPermission("VehicleEntity:modify"));
        this.setTitle("车辆管理");
        this.setFormUrl("/views/basic/vehicle_form.zul");

        refreshFirstPage(vehicleInfo);
        getPageList();
    }

    // 分页
    @Command
    public void getPageList() {
        pageResult = vehicleService.getPageList(pageParam);
    }

    // 下一页
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(vehicleInfo);
        getPageList();
    }

    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(vehicleInfo);
        getPageList();
    }

    //@GlobalCommand(GlobalCommandValues.REFRESH_RESOURCE_LIST)
    //@NotifyChange("pageResult")
    //public void refreshList() {
    //    gotoPageNo(null);
    //    //关闭弹出框
    //    this.closeDialog();
    //}


    /**
     * 重置查询条件
     */
    @Command
    @NotifyChange("vehicleInfo")
    public void reset() {
        vehicleInfo.setVin("");
        vehicleInfo.setVsn("");
        vehicleInfo.setVehicleModel("");
        vehicleInfo.setSeller("");
    }

}
