package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.MaintainInfoExt;
import com.sunjet.frontend.service.basic.MaintainService;
import com.sunjet.frontend.service.basic.MaintainTypeService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 维修项目 导入
 * Created by Administrator on 2017/7/13.
 */
public class MaintainImportFormVM extends ListVM<MaintainInfoExt> {

    @WireVariable
    private MaintainService maintainService;

    @WireVariable
    private MaintainTypeService maintainTypeService;

    @Getter
    private String uploadFilename;

    @Getter
    private List<List<String>> data;
    @Getter
    private List<MaintainInfoExt> infos = new ArrayList<>();

    @Getter
    private Integer pageSize;

    @Init
    public void init() {
        pageSize = CommonHelper.baseGridHeight / CommonHelper.GRID_LINE_HEIGHT;
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);
//        System.out.println(event.getMedia().getContentType());
        // 转义后的文件名
        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_OTHER;
        String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
        this.uploadFilename = event.getMedia().getName();    // 原始文件名
//        System.out.println(path + fileName);

        //        public static void main(String[] args) {
//            try {
//                readExcel(new File("D:\\test.xlsx"));
//                // readExcel(new File("D:\\test.xls"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        try {
//            ExcelReader.readExcel(new File(path + fileName));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        ExcelImport poi = new ExcelImport();
        // List<List<String>> list = poi.read("d:/aaa.xls");
        data = poi.read(path + fileName);
        infos.clear();
        if (data != null) {
            for (int i = 1; i < data.size(); i++) {
//                System.out.print("第" + (i) + "行");
                MaintainInfoExt info = new MaintainInfoExt();
                info.setCode(data.get(i).get(0));
                info.setName(data.get(i).get(1));
                info.setVehicleModelName(data.get(i).get(2));
                info.setVehicleSystemName(data.get(i).get(3));
                info.setVehicleSubSystemName(data.get(i).get(4));
                info.setWorkTime(Double.parseDouble(data.get(i).get(5)));
                info.setComment(data.get(i).get(6));
                info.setCreaterId(getActiveUser().getUserId());
                info.setCreaterName(getActiveUser().getUsername());
                infos.add(info);
                System.out.println(info);
//                List<String> cellList = data.get(i);
//                for (int j = 0; j < cellList.size(); j++) {
//                    // System.out.print("    第" + (j + 1) + "列值：");
//                    System.out.print("    " + cellList.get(j));
//                }
//                System.out.println();
            }
        }
    }

    @Command
    @NotifyChange("infos")
    public void importMaintains() {
//        List<MaintainInfo> tmpInfos = new ArrayList<>();
        infos = maintainService.importMaintains(infos);

    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.uploadFilename = "";
        this.infos.clear();
    }
}
