package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.*;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务站 列表
 * Created by Administrator on 2017/7/13.
 */
public class DealerListVM extends ListVM<DealerItem> {

    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DictionaryService dictionaryService;

    @Getter
    @Setter
    private DealerItem dealerItem = new DealerItem();
    @Getter
    @Setter
    private List<DictionaryInfo> listStar = new ArrayList<>();      // 星级
    //@Getter @Setter
    //private List<DictionaryInfo> listStarDE = new ArrayList<>();
    @Getter
    @Setter
    private DictionaryInfo dealerStar;
    @Getter
    @Setter
    private String dealerLevel = "";
    @Getter
    @Setter
    private List<ProvinceInfo> provinceEntities = new ArrayList<>();  // 省份/直辖市 集合
    @Getter
    @Setter
    private ProvinceInfo selectedProvince;        // 选中的 省份/直辖市
    @Getter
    @Setter
    private List<CityInfo> cityEntities = new ArrayList<>();          // 选中的省份/直辖市的下属城市集合
    @Getter
    @Setter
    private CityInfo selectedCity;                // 选中的 城市
    @Getter
    @Setter
    private List<CountyInfo> countyEntities = new ArrayList<>();      // 选中的城市的下属县/区集合
    @Getter
    @Setter
    private CountyInfo selectedCounty;            // 选中的 县/区
    @Getter
    @Setter
    private List<DealerInfo> parentDealers = new ArrayList<>();       // 列出所有父级服务站
    @Getter
    @Setter
    private DealerInfo parentDealer;              // 父级服务站
    @Getter
    @Setter
    private String serviceManager = "";


    @Init
    public void Init() {
        this.setTitle("服务站管理");
        this.setFormUrl("/views/basic/dealer_form.zul");
        provinceEntities = regionService.findAllProvince();
        listStar = dictionaryService.findDictionariesByParentCode("10010");

        //for (DictionaryInfo de : listStarDE) {
        //    listStar.add(de.getName());
        //}

        refreshFirstPage(dealerItem, Order.DESC, "objId");
        getPageList();

    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = dealerService.getPageList(pageParam);
    }

    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        this.setTitle("服务站管理");
        //设置分页参数
        refreshPage(dealerItem, Order.DESC, "objId");
        //刷新分页
        getPageList();
    }

    /**
     * 点击下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        //设置分页参数
        refreshPage(dealerItem);
        //刷新分页
        getPageList();
    }

    /**
     * 关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_DEALER_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
        getPageList();
    }

    /**
     * 选择省份
     */
    @Command
    @NotifyChange("*")
    public void selectProvince(@BindingParam("event") Event event) {
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        this.dealerItem.setCityId(null);
        this.parentDealers.clear();
        this.parentDealer = null;
        if (selectedProvince != null) {
            this.selectedCity = null;
            this.selectedCounty = null;
            this.cityEntities = regionService.findCitiesByProvinceId(this.selectedProvince.getObjId());
            this.dealerItem.setProvinceId(this.selectedProvince.getObjId());
        }
    }

    /**
     * 选择城市
     */
    @Command
    @NotifyChange("*")
    public void selectCity(@BindingParam("event") Event event) {
        this.selectedCity = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        this.parentDealers.clear();
        this.parentDealer = null;
        if (selectedCity != null) {
            this.selectedCounty = null;
            this.countyEntities = regionService.findCountiesByCityId(this.selectedCity.getObjId());
            this.dealerItem.setCityId(this.selectedCity.getObjId());
        }
    }

    /**
     * 选择服务站星级
     */
    @Command
    @NotifyChange("listStar")
    public void selectDealerStar() {
        this.dealerItem.setStar(dealerStar.getName());
    }

    /**
     * 选择服务站等级
     */
    @Command
    @NotifyChange()
    public void selectdealerLevel() {
        this.dealerItem.setLevel(dealerLevel);
    }

    /**
     * 搜索父节点服务站
     */
    @Command
    @NotifyChange("parentDealers")
    public void searchParentDealers() {
        parentDealers = dealerService.findAllDealerParent(keyword);
    }

    /**
     * 选择父节点服务站
     *
     * @param dealer
     */
    @Command
    @NotifyChange("parentDealer")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        this.parentDealer = dealer;
        this.dealerItem.setParentId(parentDealer.getObjId());
    }

    @Command
    @NotifyChange("parentDealer")
    public void clearSelectedDealer2() {
        this.setParentDealer(null);
        this.dealerItem.setParentId(null);
    }


    /**
     * 删除对象
     *
     * @param objId
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId) {
        dealerService.deleteByObjId(objId);
        getPageList();

    }

    /**
     * 重置查询条件
     */

    @Command
    @NotifyChange("*")
    public void reset() {
        this.dealerItem.setCode("");
        this.dealerItem.setName("");
        this.dealerItem.setStar("");
        this.dealerItem.setLevel("");
        this.dealerItem.setParentId("");
        this.dealerItem.setProvinceId("");
        this.dealerItem.setCityId("");
        this.dealerItem.setServiceManagerName("");
        this.setDealerStar(null);
        this.setDealerLevel(null);
        this.setSelectedProvince(null);
        this.setCityEntities(null);
        this.setSelectedCity(null);
        this.setParentDealer(null);
    }


}
