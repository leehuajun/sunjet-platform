package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 车辆 表单
 * Created by Administrator on 2017/7/13.
 */
public class VehicleFormVM extends FormVM {

    @WireVariable
    private VehicleService vehicleService;

    @Setter
    @Getter
    private VehicleInfo vehicleInfo;

    @Getter
    @Setter
    protected String maintenanceHistoryUrl;
    @Getter
    @Setter
    private String title = "维修历史";

    @Init(superclass = true)
    public void init() {
        this.setMaintenanceHistoryUrl("/views/asm/maintenance_history.zul");
        if (StringUtils.isNotBlank(objId)) {
            vehicleInfo = (VehicleInfo) vehicleService.findOne(objId);
        } else {
            vehicleInfo = new VehicleInfo();
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 维修历史页面
     *
     * @param vehicleInfo
     * @param url
     */
    @Command
    @NotifyChange("vehicleInfo")
    public void openMaintenanceHistory(@BindingParam("vehicleInfo") VehicleInfo vehicleInfo, @BindingParam("url") String url, @BindingParam("title") String title) {

        Map<String, Object> paramMap = new HashMap<>();
        if (vehicleInfo.getObjId() != null) {
            paramMap.put("vehicleInfo", vehicleInfo);
        }

        try {
            if (this.getVehicleInfo().getVin() == null) {
                ZkUtils.showExclamation("请先选择车辆！", "系统提示");
                return;
            }
            ZkTabboxUtil.newTab(vehicleInfo.getObjId() == null ? URLEncoder.encode(title, "UTF-8") : vehicleInfo.getObjId() + "mh", title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (TabDuplicateException ex) {
            ex.printStackTrace();
        }
    }

}
