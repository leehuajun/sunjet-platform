package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.VehicleInfoExt;
import com.sunjet.dto.asms.basic.VehicleModelInfo;
import com.sunjet.frontend.service.basic.VehicleModelService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.common.ExcelImport;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 维修项目 导入
 * Created by Administrator on 2017/7/13.
 */
public class VehicleImportModifyListVM extends ListVM<VehicleInfoExt> {

    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private VehicleModelService vehicleModelService;
    private List<VehicleModelInfo> modelInfos = new ArrayList<>();

    @Getter
    private String uploadFilename;

    @Getter
    private List<List<String>> data;
    @Getter
    private List<VehicleInfoExt> infos = new ArrayList<>();

    @Getter
    private Integer pageSize;


    @Init
    public void init() {
        pageSize = CommonHelper.baseGridHeight / CommonHelper.GRID_LINE_HEIGHT;
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {

        modelInfos = vehicleModelService.findAll();

        // 转义后的文件名
        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_OTHER;
        String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
        this.uploadFilename = event.getMedia().getName();    // 原始文件名


        ExcelImport poi = new ExcelImport();
        data = poi.read(path + fileName);
        infos.clear();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        if (data != null) {
            for (int i = 1; i < data.size(); i++) {
//                System.out.print("第" + (i) + "行");
                VehicleInfoExt info = new VehicleInfoExt();
                info.setVin(data.get(i).get(0));
                info.setVehicleModel(data.get(i).get(1) == null ? "" : data.get(i).get(1));
                try {
                    VehicleModelInfo modelInfo = this.modelInfos.stream().filter(item -> item.getModelCode().equals(info.getVehicleModel())).findFirst().get();
                    info.setTypeCode(modelInfo.getTypeCode());
                    info.setTypeName(modelInfo.getTypeName());
                } catch (Exception e) {
                    info.setTypeCode(null);
                    info.setTypeName(null);
                }
                info.setEngineModel(data.get(i).get(2) == null ? "" : data.get(i).get(2));
                info.setEngineNo(data.get(i).get(3) == null ? "" : data.get(i).get(3));
                info.setVsn(data.get(i).get(4) == null ? "" : data.get(i).get(4));
                try {
                    info.setManufactureDate(Date.valueOf(data.get(i).get(5)));
                } catch (Exception e) {
                    info.setManufactureDate(null);
                }
                try {
                    info.setProductDate(Date.valueOf(data.get(i).get(6)));
                } catch (Exception e) {
                    info.setProductDate(null);
                }
                info.setSeller(data.get(i).get(10));
                try {
//                    Date.valueOf()
                    info.setPurchaseDate(Date.valueOf(data.get(i).get(11)));
                } catch (Exception e) {
                    info.setPurchaseDate(null);
                }

                info.setOwnerName(data.get(i).get(12) == null ? "" : data.get(i).get(12));
                info.setAddress(data.get(i).get(13) == null ? "" : data.get(i).get(13));
                info.setPhone(data.get(i).get(14) == null ? "" : data.get(i).get(14));
                info.setMobile(data.get(i).get(15) == null ? "" : data.get(i).get(15));
                info.setModifierId(getActiveUser().getUserId());
                info.setModifierName(getActiveUser().getUsername());
                infos.add(info);
                System.out.println(info);
            }
        }
    }

    @Command
    @NotifyChange("infos")
    public void modifyVehicles() {
//        List<MaintainInfo> tmpInfos = new ArrayList<>();
        infos = vehicleService.modifyVehicles(infos);
        if (infos.size() == 0) {
            ZkUtils.showInformation("修改成功", "提示");
        }
    }

    @Command
    @NotifyChange({"infos", "uploadFilename"})
    public void reset() {
        infos.clear();
        this.uploadFilename = "";
    }
}
