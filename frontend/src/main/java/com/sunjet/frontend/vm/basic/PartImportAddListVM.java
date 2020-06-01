package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.PartInfoExt;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.common.ExcelImport;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 维修项目 导入
 * Created by Administrator on 2017/7/13.
 */
public class PartImportAddListVM extends ListVM<PartInfoExt> {

    @WireVariable
    private PartService partService;


    @Getter
    private String uploadFilename;

    @Getter
    private List<List<String>> data;
    @Getter
    private List<PartInfoExt> infos = new ArrayList<>();

    @Getter
    private Integer pageSize;


    @Init
    public void init() {
        pageSize = CommonHelper.baseGridHeight / CommonHelper.GRID_LINE_HEIGHT;
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        // 转义后的文件名
        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_OTHER;
        String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
        this.uploadFilename = event.getMedia().getName();    // 原始文件名

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

        ExcelImport poi = new ExcelImport();
        data = poi.read(path + fileName);
        infos.clear();
        if (data != null) {
            for (int i = 1; i < data.size(); i++) {
                PartInfoExt info = new PartInfoExt();
                info.setEnabled(true);
                if (StringUtils.isNotBlank(data.get(i).get(0))) {
                    info.setCode(excelStringFormat(data.get(i).get(0)));
                    info.setName(data.get(i).get(1));
                    info.setWarrantyTime(data.get(i).get(2));
                    info.setWarrantyMileage(data.get(i).get(3));
                    info.setPrice(Double.parseDouble(df.format(Double.parseDouble(data.get(i).get(4)))));
                    info.setPartType(data.get(i).get(5));
                    info.setCreaterId(getActiveUser().getUserId());
                    info.setCreaterName(getActiveUser().getUsername());
                    infos.add(info);
                }
            }
        }
    }

    @Command
    @NotifyChange("infos")
    public void importVehicles() {
        infos = partService.importParts(infos);
        if (infos.size() == 0) {
            ZkUtils.showInformation("导入成功", "提示");
        }


    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.uploadFilename = "";
        this.infos.clear();
    }

    /**
     * 处理导入数据为纯数字带小数点
     *
     * @param str
     * @return
     */
    private String excelStringFormat(String str) {
        String result = "";
        if (StringUtils.isNotBlank(str) && str.contains(".0")) {
            result = str.split("\\.")[0];
        } else {
            result = str;
        }
        return result;
    }


}
