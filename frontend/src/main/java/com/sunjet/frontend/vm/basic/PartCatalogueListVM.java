package com.sunjet.frontend.vm.basic;


import com.sunjet.dto.asms.basic.PartCatalogueInfo;
import com.sunjet.frontend.service.basic.PartCatalogueService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
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
public class PartCatalogueListVM {


    @Getter
    @Setter
    private PartCatalogueInfo partCatalogueInfo = new PartCatalogueInfo();


    @Getter
    @Setter
    private List<PartCatalogueInfo> partCatalogueInfoList = new ArrayList<>();

    @Getter
    @Setter
    private Boolean selectPartName = false;

    @Setter
    @Getter
    private boolean selectVehicleModels = false;

    @WireVariable
    private PartCatalogueService partCatalogueService;

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
    @Init
    public void init() {
        selectConditionList.add("查询条件一");
        selectConditionList.add("查询条件二");
    }



    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("partCatalogueInfoList")
    public void refreshData() {
        if (StringUtils.isNotBlank(partCatalogueInfo.getPartName()) || StringUtils.isNotBlank(partCatalogueInfo.getModuleName()) || StringUtils.isNotBlank(partCatalogueInfo.getVehicleModels())) {
            partCatalogueInfoList = partCatalogueService.findAll(partCatalogueInfo);
        } else {
            ZkUtils.showInformation("请输入关键字查询", "提示");
        }
    }



    /**
     * 重置查询条件
     */

    @Command
    @NotifyChange("partCatalogueInfo")
    public void reset() {
        this.partCatalogueInfo.setPartName("");
        this.partCatalogueInfo.setVehicleModels("");
        this.partCatalogueInfo.setModuleName("");
    }


    @Command
    @NotifyChange("*")
    public void changeCondition() {
        switch (selectCondition) {
            case "查询条件二":
                showSelectOne = false;
                showSelectTwo = true;
                this.partCatalogueInfo = new PartCatalogueInfo();
                break;
            default:
                showSelectOne = true;
                showSelectTwo = false;
                this.partCatalogueInfo = new PartCatalogueInfo();

        }
    }



}

