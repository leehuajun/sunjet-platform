package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.CityInfo;
import com.sunjet.dto.asms.basic.CountyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.asms.report.DealerMessageItem;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.report.ReportService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.common.ExcelUtil;
import com.sunjet.frontend.utils.zk.BeanUtils;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * <p>
 * 服务站信息统计
 */
public class DealerMessageVM extends ListVM<DealerMessageItem> {


    @WireVariable
    private ReportService reportService;

    @WireVariable
    private DictionaryService dictionaryService;
    @WireVariable
    private RegionService regionService;

    @Getter
    @Setter
    DealerMessageItem dealerMessageItem = new DealerMessageItem();


    @Getter
    @Setter
    private String dealerLevel = StringUtils.EMPTY;   // 服务站等级

    @Getter
    @Setter
    private List<DictionaryInfo> listStar = new ArrayList<>();      // 星级

    @Getter
    @Setter
    private DictionaryInfo dealerStar;    //服务站星级

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
    private String serviceManager = StringUtils.EMPTY;


    @Init
    public void init() {
        this.setEnableExport(hasPermission("DealerMessageEntity:export"));
        provinceEntities = regionService.findAllProvince();
        listStar = dictionaryService.findDictionariesByParentCode("10010");
        refreshFirstPage(dealerMessageItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getDealerMessageViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(dealerMessageItem, Order.DESC, "objId");
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
        refreshPage(dealerMessageItem);
        //刷新分页
        getPageList();
    }


    /**
     * 选择省份
     */
    @Command
    @NotifyChange("*")
    public void selectProvince() {
        this.parentDealers.clear();
        this.parentDealer = null;
        if (selectedProvince != null) {
            this.selectedCity = null;
            this.selectedCounty = null;
            this.cityEntities = regionService.findCitiesByProvinceId(this.selectedProvince.getObjId());
            this.dealerMessageItem.setProvinceId(this.selectedProvince.getObjId());
        }
    }

    /**
     * 选择城市
     */
    @Command
    @NotifyChange("*")
    public void selectCity() {
        this.parentDealers.clear();
        this.parentDealer = null;
        if (selectedCity != null) {
            this.selectedCounty = null;
            this.countyEntities = regionService.findCountiesByCityId(this.selectedCity.getObjId());
            this.dealerMessageItem.setCityId(this.selectedCity.getObjId());
        }
    }


    /**
     * 选择服务站星级
     */
    @Command
    @NotifyChange("listStar")
    public void selectDealerStar() {
        this.dealerMessageItem.setStar(dealerStar.getName());
    }

    /**
     * 选择服务站等级
     */
    @Command
    @NotifyChange()
    public void selectdealerLevel() {
        this.dealerMessageItem.setLevel(dealerLevel);
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
    public void selectParentDealer(@BindingParam("model") DealerInfo dealer) {
        this.parentDealer = dealer;
        this.dealerMessageItem.setParentId(parentDealer.getObjId());
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<DealerMessageItem> dealerMessageItemList = reportService.dealerMessageViewToExcel(dealerMessageItem);
        if (dealerMessageItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (dealerMessageItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();

        for (DealerMessageItem messageItem : dealerMessageItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(messageItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("电话");
        titleList.add("传真");
        titleList.add("地址");
        titleList.add("所在省");
        titleList.add("市");
        titleList.add("SGMW体系");
        titleList.add("申请等级");
        titleList.add("维修资质");
        titleList.add("服务站级别");
        titleList.add("父级服务站");
        titleList.add("组织机构代码");
        titleList.add("纳税人识别号");
        titleList.add("开户银行");
        titleList.add("银行账号");
        titleList.add("营业执照号");
        titleList.add("服务经理");
        titleList.add("其他合作内容");
        titleList.add("法人代表");
        titleList.add("法人电话");
        titleList.add("站长");
        titleList.add("站长电话");
        titleList.add("技术主管");
        titleList.add("技术主管电话");
        titleList.add("索赔主管");
        titleList.add("索赔主管电话");
        titleList.add("配件主管");
        titleList.add("配件主管电话");
        titleList.add("财务经理");
        titleList.add("财务经理电话");
        titleList.add("员工总数");
        titleList.add("接待员数量");
        titleList.add("配件员数量");
        titleList.add("维修工数量");
        titleList.add("质检员数量");
        titleList.add("结算员数量");
        titleList.add("停车面积");
        titleList.add("接待室");
        titleList.add("综合维修区");
        titleList.add("总成维修区");
        titleList.add("配件库总面积");
        titleList.add("五菱库总面积");
        titleList.add("旧件库面积");
        titleList.add("五菱旧件库面积");
        titleList.add("其他车辆维修条件");
        titleList.add("其他品牌");
        titleList.add("维修产品");
        titleList.add("情况说明");
        //titleList.add("启用状态");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("code");
        keyList.add("name");
        keyList.add("phone");
        keyList.add("fax");
        keyList.add("address");
        keyList.add("provinceName");
        keyList.add("cityName");
        keyList.add("sgmwSystem");
        keyList.add("star");
        keyList.add("qualification");
        keyList.add("level");
        keyList.add("parentName");
        keyList.add("orgCode");
        keyList.add("taxpayerCode");
        keyList.add("bank");
        keyList.add("bankAccount");
        keyList.add("businessLicenseCode");
        keyList.add("serviceManagerName");
        keyList.add("otherCollaboration");
        keyList.add("legalPerson");
        keyList.add("legalPersonPhone");
        keyList.add("stationMaster");
        keyList.add("stationMasterPhone");
        keyList.add("technicalDirector");
        keyList.add("technicalDirectorPhone");
        keyList.add("claimDirector");
        keyList.add("claimDirectorPhone");
        keyList.add("partDirector");
        keyList.add("partDirectorPhone");
        keyList.add("financeDirector");
        keyList.add("financeDirectorPhone");
        keyList.add("employeeCount");
        keyList.add("receptionistCount");
        keyList.add("partKeeyperCount");
        keyList.add("maintainerCount");
        keyList.add("qcInspectorCount");
        keyList.add("clerkCount");
        keyList.add("parkingArea");
        keyList.add("receptionArea");
        keyList.add("generalArea");
        keyList.add("assemblyArea");
        keyList.add("storageArea");
        keyList.add("storageWulingArea");
        keyList.add("storageUserdPartArea");
        keyList.add("storageWulingUserdPartArea");
        keyList.add("otherMaintainCondition");
        keyList.add("otherBrand");
        keyList.add("productsOfMaintain");
        keyList.add("otherProducts");
        //keyList.add("enabled");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }

    /**
     * 重置
     */
    @Command
    @NotifyChange({"dealerMessageItem", "dealerStar", "dealerLevel", "selectedProvince", "selectedCity", "selectedCounty", "parentDealer"})
    public void reset() {
        dealerMessageItem.setName("");
        dealerMessageItem.setCode("");
        dealerMessageItem.setStar("");
        dealerLevel = "";
        dealerStar = null;
        selectedProvince = null;
        selectedCity = null;
        selectedCounty = null;
        parentDealer = null;
        dealerMessageItem.setParentId("");
        dealerMessageItem.setParentName("");
    }


}
