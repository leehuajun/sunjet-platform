package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.report.AgencySettlementYearSummaryItem;
import com.sunjet.dto.system.base.Order;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.service.report.ReportService;
import com.sunjet.frontend.utils.common.ExcelUtil;
import com.sunjet.frontend.utils.zk.BeanUtils;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商结算年汇总
 */
public class AgencySettlementYearSummaryVM extends ListVM<AgencySettlementYearSummaryItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    AgencySettlementYearSummaryItem agencySettlementYearSummaryItem = new AgencySettlementYearSummaryItem();

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private List<AgencyInfo> agencyList = new ArrayList<>();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("AgencySettlementSummaryEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
            agencySettlementYearSummaryItem.setAgencyCode(getActiveUser().getAgency().getCode());
            agencySettlementYearSummaryItem.setAgencyName(getActiveUser().getAgency().getName());
        }
        this.setType("配件费用");
        refreshFirstPage(agencySettlementYearSummaryItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        PageResult<AgencySettlementYearSummaryItem> agencySettlementYearSummaryViewPageList = reportService.getAgencySettlementYearSummaryViewPageList(pageParam);
        List<AgencySettlementYearSummaryItem> rows = agencySettlementYearSummaryViewPageList.getRows();
        Map<String, AgencySettlementYearSummaryItem> newMap = new HashMap();
        List<AgencySettlementYearSummaryItem> newList = new ArrayList<>();
        rows.forEach(item -> {
            if (newMap.get(item.getAgencyCode()) != null) {
                AgencySettlementYearSummaryItem Expense = newMap.get(item.getAgencyCode());
                Expense.setJanuary(Expense.getJanuary() + item.getJanuary());
                Expense.setFebruary(Expense.getFebruary() + item.getFebruary());
                Expense.setMarch(Expense.getMarch() + item.getMarch());
                Expense.setApril(Expense.getApril() + item.getApril());
                Expense.setMay(Expense.getMay() + item.getMay());
                Expense.setJune(Expense.getJune() + item.getJune());
                Expense.setJuly(Expense.getJuly() + item.getJuly());
                Expense.setAugust(Expense.getAugust() + item.getAugust());
                Expense.setSeptember(Expense.getSeptember() + item.getSeptember());
                Expense.setOctober(Expense.getOctober() + item.getOctober());
                Expense.setNovember(Expense.getNovember() + item.getNovember());
                Expense.setDecember(Expense.getDecember() + item.getDecember());
                newMap.put(item.getAgencyCode(), Expense);
            } else {
                newMap.put(item.getAgencyCode(), item);
            }
        });
        newMap.forEach((key, value) -> {
            newList.add(value);
        });
        agencySettlementYearSummaryViewPageList.setRows(newList);
        pageResult = agencySettlementYearSummaryViewPageList;

    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(agencySettlementYearSummaryItem);
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
        refreshPage(agencySettlementYearSummaryItem);
        //刷新分页
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void selectType() {
        if (type.equals("运费")) {
            pageResult = reportService.getAgencySettlementYearSummaryFreightViewPageList(pageParam);
        } else {
            pageResult = reportService.getAgencySettlementYearSummaryViewPageList(pageParam);
        }

    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {

        List<AgencySettlementYearSummaryItem> agencySettlementYearSummaryItemList = null;

        if (type.equals("配件")) {
            agencySettlementYearSummaryItemList = reportService.agencySettlementYearSummaryViewToExcel(agencySettlementYearSummaryItem);
        } else {
            agencySettlementYearSummaryItemList = reportService.agencySettlementYearSummaryFreightViewToExcel(agencySettlementYearSummaryItem);
        }

        List<AgencySettlementYearSummaryItem> rows = agencySettlementYearSummaryItemList;
        Map<String, AgencySettlementYearSummaryItem> newMap = new HashMap();
        List<AgencySettlementYearSummaryItem> newList = new ArrayList<>();
        rows.forEach(item -> {
            if (newMap.get(item.getAgencyCode()) != null) {
                AgencySettlementYearSummaryItem Expense = newMap.get(item.getAgencyCode());
                Expense.setJanuary(Expense.getJanuary() + item.getJanuary());
                Expense.setFebruary(Expense.getFebruary() + item.getFebruary());
                Expense.setMarch(Expense.getMarch() + item.getMarch());
                Expense.setApril(Expense.getApril() + item.getApril());
                Expense.setMay(Expense.getMay() + item.getMay());
                Expense.setJune(Expense.getJune() + item.getJune());
                Expense.setJuly(Expense.getJuly() + item.getJuly());
                Expense.setAugust(Expense.getAugust() + item.getAugust());
                Expense.setSeptember(Expense.getSeptember() + item.getSeptember());
                Expense.setOctober(Expense.getOctober() + item.getOctober());
                Expense.setNovember(Expense.getNovember() + item.getNovember());
                Expense.setDecember(Expense.getDecember() + item.getDecember());
                newMap.put(item.getAgencyCode(), Expense);
            } else {
                newMap.put(item.getAgencyCode(), item);
            }
        });
        newMap.forEach((key, value) -> {
            newList.add(value);
        });


        if (newList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (newList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();


        for (AgencySettlementYearSummaryItem settlementYearSummaryItem : newList) {
            Map<String, Object> map = BeanUtils.transBean2Map(settlementYearSummaryItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("合作商");
        titleList.add("1月");
        titleList.add("2月");
        titleList.add("3月");
        titleList.add("4月");
        titleList.add("5月");
        titleList.add("6月");
        titleList.add("7月");
        titleList.add("8月");
        titleList.add("9月");
        titleList.add("10月");
        titleList.add("11月");
        titleList.add("12月");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("agencyName");
        keyList.add("january");
        keyList.add("february");
        keyList.add("march");
        keyList.add("april");
        keyList.add("may");
        keyList.add("june");
        keyList.add("july");
        keyList.add("august");
        keyList.add("september");
        keyList.add("october");
        keyList.add("november");
        keyList.add("december");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"agencySettlementYearSummaryItem"})
    public void reset() {
        agencySettlementYearSummaryItem.setAgencyName("");
        this.agencySettlementYearSummaryItem.setAgencyCode("");
    }

    /**
     * 搜索合作商
     */
    @Command
    @NotifyChange("agencyList")
    public void searchAgencies() {
        agencyList = agencyService.findAllByKeyword(keyword);
    }

    /**
     * 选择合作商
     */
    @Command
    @NotifyChange({"agencyList", "keyword", "agencySettlementYearSummaryItem"})
    public void selectAgency(@BindingParam("model") AgencyInfo agencyInfo) {
        this.agencySettlementYearSummaryItem.setAgencyName(agencyInfo.getName());
        this.agencySettlementYearSummaryItem.setAgencyCode(agencyInfo.getCode());

    }

}
