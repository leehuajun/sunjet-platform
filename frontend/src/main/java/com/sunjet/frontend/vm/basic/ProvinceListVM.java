package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
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
 * @create 2017-7-13 上午12:08
 */
public class ProvinceListVM extends ListVM<ProvinceInfo> {

    @WireVariable
    private RegionService regionService;

    private ProvinceInfo provinceInfo;
    @Setter
    @Getter
    private List<ProvinceInfo> provinces = new ArrayList<>();
    @Setter
    @Getter
    private List<ProvinceInfo> coldProvinces = new ArrayList<>();
    @Setter
    @Getter
    private List<ProvinceInfo> normalProvinces = new ArrayList<>();
    @Setter
    @Getter
    private ProvinceInfo coldProvince = null;
    @Setter
    @Getter
    private ProvinceInfo normalProvince = null;

    @Getter
    @Setter
    private Boolean enableAdd = false;  //新增按钮权限

    @Init
    public void init() {
        this.setEnableAdd(hasPermission("ProvinceEntity:add"));
        initProvince();
    }

    //刷新
    @Command
    @NotifyChange("*")
    public void refreshData() {
        this.setTitle("省份管理");
        refreshPage(provinceInfo);
        initProvince();
    }

    private void initProvince() {
        this.provinces = regionService.findAllProvince();
        this.coldProvinces.clear();
        this.normalProvinces.clear();
        for (ProvinceInfo info : provinces) {
            if (info.getCold() != null && info.getCold() == true) {
                coldProvinces.add(info);
            } else {
                normalProvinces.add(info);
            }
        }
        System.out.println(coldProvinces.size());
        System.out.println(normalProvinces.size());
    }

    @Command
    @NotifyChange("*")
    public void addColdProvince() {
        if (enableAdd == false) {
            return;
        }
        if (this.normalProvince == null) {
            ZkUtils.showExclamation("没有选择常规省份", "系统提示");
        } else {
            normalProvince.setCold(true);
            regionService.saveProvince(normalProvince);
            initProvince();
            this.coldProvince = null;
            this.normalProvince = null;
        }
    }

    @Command
    @NotifyChange("*")
    public void removeColdProvince() {
        if (enableAdd == false) {
            return;
        }
        if (this.coldProvince == null) {
            ZkUtils.showExclamation("没有选择严寒省份", "系统提示");
        } else {
            coldProvince.setCold(false);
            regionService.saveProvince(coldProvince);
            initProvince();
            this.coldProvince = null;
            this.normalProvince = null;
        }
    }
}
