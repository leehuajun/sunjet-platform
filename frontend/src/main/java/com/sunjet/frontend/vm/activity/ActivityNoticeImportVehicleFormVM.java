package com.sunjet.frontend.vm.activity;

import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.frontend.service.activity.ActivityVehicleService;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.common.ExcelImport;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ActivityNoticeImportVehicleFormVM extends ListVM {

    @WireVariable
    private VehicleService vehicleService;

    @WireVariable
    private ActivityVehicleService activityVehicleService;

    @Getter
    private String uploadFilename;

    @Getter
    private List<List<String>> data = new ArrayList<>();

    // 上传得到的原始的所有VIN列表
    @Getter
    private List<String> originVins = new ArrayList<>();


    // 获取到的vin列表对应的vehicle列表信息
    @Getter
    private List<VehicleInfo> vehicleInfos = new ArrayList<>();

    private String activityNoticeId;

    private List<String> uploadVins = new ArrayList<>();
    private List<ActivityVehicleInfo> infos = new ArrayList<>();


    @Init(superclass = true)
    public void init() {
        activityNoticeId = Executions.getCurrent().getArg().get("activityNoticeId").toString();
        originVins = (List<String>) Executions.getCurrent().getArg().get("vins");
        System.out.println(originVins);
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        // 转义后的文件名
        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_OTHER;
        String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
        this.uploadFilename = event.getMedia().getName();    // 原始文件名


        ExcelImport poi = new ExcelImport();
        data = poi.read(path + fileName);
        uploadVins.clear();
        if (data != null) {
            for (int i = 1; i < data.size(); i++) {
                if (!originVins.contains(data.get(i).get(0))) {
                    uploadVins.add(data.get(i).get(0));
                }
            }
        }
        vehicleInfos = vehicleService.findAllByVinIn(uploadVins);
    }

    @Command
    public void submit() {
        infos.clear();
        for (VehicleInfo info : vehicleInfos) {
            ActivityVehicleInfo activityVehicleInfo = new ActivityVehicleInfo();
            activityVehicleInfo.setVehicleId(info.getObjId());
            activityVehicleInfo.setTypeCode(info.getTypeCode());
            activityVehicleInfo.setActivityNoticeId(activityNoticeId.toString());
            infos.add(activityVehicleInfo);
        }
        activityVehicleService.saveList(infos);

        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ACTIVITY_VEHICLE_LIST, null);
    }
}
