package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.AgencyItem;
import com.sunjet.dto.asms.basic.CityInfo;
import com.sunjet.dto.asms.basic.CountyInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.util.ArrayList;
import java.util.List;

/**
 * 合作商 列表
 * Created by Administrator on 2017/7/13.
 */
public class AgencyListVM extends ListVM<AgencyItem> {

    @WireVariable
    AgencyService agencyService;


    @WireVariable
    RegionService regionService;    //地区

    @Getter
    @Setter
    private AgencyItem agencyItem = new AgencyItem();  //合作商

    @Getter
    @Setter
    private List<ProvinceInfo> provinceEntities = new ArrayList<>();  // 省份/直辖市 集合

    @Getter
    @Setter
    private List<CityInfo> cityEntities = new ArrayList<>();          // 选中的省份/直辖市的下属城市集合
    @Getter
    @Setter
    private List<CountyInfo> countyEntities = new ArrayList<>();      // 选中的城市的下属县/区集合

    @Getter
    @Setter
    private ProvinceInfo selectedProvince;        // 选中的 省份/直辖市
    @Getter
    @Setter
    private CityInfo selectedCity;                // 选中的 城市
    @Getter
    @Setter
    private CountyInfo selectedCounty;            // 选中的 县/区

    @Getter
    @Setter
    private String agencyCode = StringUtils.EMPTY;
    @Getter
    @Setter
    private String agencyName = StringUtils.EMPTY;

    @Init
    public void Init() {
        this.setTitle("合作商管理");
        this.setFormUrl("/views/basic/agency_form.zul");

        //初始化省份
        provinceEntities = regionService.findAllProvince();

        refreshFirstPage(agencyItem, Order.DESC, "objId");
        //获取分页
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = agencyService.getPageList(pageParam);
    }


    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(agencyItem);
        getPageList();
    }


    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(agencyItem);
        getPageList();
    }


    /**
     * 关闭窗口刷新列表
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_AGENCY_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        gotoPageNo(null);
        this.closeDialog();
    }

    /**
     * 选择省份
     */
    @Command
    @NotifyChange({"provinceEntities", "cityEntities"})
    public void selectProvince(@BindingParam("event") Event event) {
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        if (selectedProvince != null) {
            this.selectedCity = null;
            this.selectedCounty = null;
            this.cityEntities = regionService.findCitiesByProvinceId(this.selectedProvince.getObjId());
            agencyItem.setProvinceId(selectedProvince.getObjId());
        }
    }

    /**
     * 选择市区
     */
    @Command
    @NotifyChange("cityEntities")
    public void selectedCity(@BindingParam("event") Event event) {
        this.selectedCity = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        agencyItem.setCityId(selectedCity.getObjId());
    }


    /**
     * 重置方法
     */
    @Command
    @NotifyChange({"provinceEntities", "cityEntities", "agencyItem"})
    public void reset() {
        agencyItem.setCode("");
        agencyItem.setName("");
        agencyItem.setProvinceId("");
        agencyItem.setCityId("");
        this.setSelectedProvince(null);
        this.setSelectedCity(null);
        this.setCityEntities(null);
    }

}
