//package com.sunjet.frontend.vm.asm;
//
//import com.sunjet.dto.asms.basic.PartInfo;
//import com.sunjet.frontend.service.basic.PartService;
//import com.sunjet.frontend.utils.common.CommonHelper;
//import com.sunjet.frontend.utils.zk.ZkUtils;
//import com.sunjet.frontend.vm.base.FormVM;
//import lombok.Getter;
//import lombok.Setter;
//import org.zkoss.bind.annotation.*;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.select.Selectors;
//import org.zkoss.zk.ui.select.annotation.Wire;
//import org.zkoss.zk.ui.select.annotation.WireVariable;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author lhj
// * @create 2015-12-30 上午11:38
// */
//public class checkResultVM extends FormVM {
//
//    @WireVariable
//    private PartService partService;
//    @Wire
//    @Getter
//    @Setter
//    private PartInfo selectedPart;
//    @Getter
//    @Setter
//    private List<PartInfo> selectedPartList = new ArrayList<>();
//    @Getter
//    @Setter
//    private List<PartInfo> partList = new ArrayList<>();
//
//    @Init(superclass = true)
//    public void init() {
//
////        logger.info(Executions.getCurrent().getArg().get("id").toString());
////        logger.info(Executions.getCurrent().getArg().get("name").toString());
//
//
//    }
//
//    @Command
//    @NotifyChange("*")
//    public void searchReportPart() {
//
//        if (this.keyword.length() >= CommonHelper.FILTER_PARTS_LEN) {
//            this.partList = partService.findAllByKeyword(this.keyword.trim());
//            if (this.partList.size() < 1) {
//                ZkUtils.showInformation("未查询到相关物料！", "提示");
//            }
//        } else {
//            ZkUtils.showInformation(CommonHelper.FILTER_PARTS_ERROR, "提示");
//        }
//    }
//
//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
//        Selectors.wireComponents(view, this, false);
//    }
//
//}
