//package com.sunjet.frontend.vm.asm;
//
//import com.sunjet.dto.asms.basic.VehicleInfo;
//import com.sunjet.frontend.service.basic.VehicleService;
//import com.sunjet.frontend.utils.common.CommonHelper;
//import com.sunjet.frontend.utils.zk.ZkUtils;
//import com.sunjet.frontend.vm.base.FormVM;
//import lombok.Getter;
//import lombok.Setter;
//import org.zkoss.bind.annotation.*;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.select.Selectors;
//import org.zkoss.zk.ui.select.annotation.WireVariable;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author lhj
// * @create 2015-12-30 上午11:38
// */
//public class SelectVehicleFormVM extends FormVM {
//
//    @WireVariable
//    private VehicleService vehicleService;
//    @WireVariable
//    @Getter
//    @Setter
//    private List selectedVehicleList = new ArrayList<>();
//    @Getter
//    @Setter
//    private List<VehicleInfo> vehicleEntities = new ArrayList<>();
//
//    @Init(superclass = true)
//    public void init() {
//
//    }
//
//    @Command
//    @NotifyChange("*")
//    public void searchVehicle() {
//        if (this.keyword.trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
//            this.vehicleEntities = vehicleService.findAllByKeyword(this.keyword.trim());
//        } else {
//            ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
//        }
//    }
//
//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
//        Selectors.wireComponents(view, this, false);
//    }
//
//    /**
//     * 获取日期
//     *
//     * @param date
//     * @return
//     */
//    public String getDate(Date date) {
//        String strDate = "";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        if (date != null) {
//            strDate = dateFormat.format(date);
//        }
//        return strDate;
//    }
//
//}
