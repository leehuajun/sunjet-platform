package com.sunjet.frontend.vm.basic;


import com.sunjet.dto.asms.basic.PartSearchInfo;
import com.sunjet.frontend.service.basic.PartSearchService;
import com.sunjet.frontend.service.basic.PartService;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyf
 * @create 2017-7-13 上午12:00
 * 配件目录列表
 */
public class PartSearchListVM {

    @WireVariable
    PartService partService;
    @Getter
    @Setter
    private PartSearchInfo partSearchInfo = new PartSearchInfo();
    @Getter
    @Setter
    private List<PartSearchInfo> partSearchInfoList = new ArrayList<>();
    @WireVariable
    private PartSearchService partSearchService;

    @Setter
    @Getter
    private String selectCondition = "查询条件一";

    @Setter
    @Getter
    private List<String> selectConditionList = new ArrayList<>();//条件列表
    @Setter
    @Getter
    private boolean showSelectOne = true; //查询条件一
    @Setter
    @Getter
    private boolean showSelectTwo = false;//查询条件二
    @Setter
    @Getter
    private boolean showSelectThree = false;//查询条件三

    @Init
    public void init() {
        selectConditionList.add("查询条件一");
        selectConditionList.add("查询条件二");
        selectConditionList.add("查询条件三");
    }


    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("partSearchInfoList")
    public void refreshData() {
        partSearchInfoList = partSearchService.findAll(partSearchInfo);
    }




    /**
     * 重置查询条件
     */

    @Command
    @NotifyChange("partSearchInfo")
    public void reset() {
        partSearchInfo.setPartCode("");
        partSearchInfo.setPartName("");
        partSearchInfo.setVehicleModel("");
        partSearchInfo.setPartCategoryPlatformCode("");
        partSearchInfo.setVin("");
        partSearchInfo.setVsn("");
        partSearchInfo.setProductionDate(null);
    }

    @Command
    @NotifyChange("*")
    public void changeCondition() {
        switch (selectCondition) {
            case "查询条件二":
                showSelectOne = false;
                showSelectTwo = true;
                showSelectThree = false;
                this.partSearchInfo = new PartSearchInfo();
                break;
            case "查询条件三":
                showSelectOne = false;
                showSelectTwo = false;
                showSelectThree = true;
                this.partSearchInfo = new PartSearchInfo();
                break;
            default:
                showSelectOne = true;
                showSelectTwo = false;
                showSelectThree = false;
                this.partSearchInfo = new PartSearchInfo();

        }
    }


}

