package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.DealerExpenseSummaryItem;
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
 * 服务站服务费用年度汇总
 */
public class DealerExpenseSummaryVM extends ListVM<DealerExpenseSummaryItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    DealerExpenseSummaryItem dealerExpenseSummaryItem = new DealerExpenseSummaryItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("DealerExpenseSummaryEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            dealerExpenseSummaryItem.setDealerCode(getActiveUser().getDealer().getCode());
            dealerExpenseSummaryItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            //List<RoleInfo> roles = getActiveUser().getRoles();
            //if (roles != null) {
            //    for (RoleInfo role : roles) {
            //        if ("服务经理".equals(role.getName())) {
            //            settlementMaintainExpenseItem.setServiceManager(getActiveUser().getUsername());
            //            break;
            //        }
            //    }
            //}
        }
        dealerExpenseSummaryItem.setType("所有类型总费用");
        refreshFirstPage(dealerExpenseSummaryItem, Order.DESC, "objId");
        getPageList();

    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        PageResult<DealerExpenseSummaryItem> dealerExpenseSummaryItemPageList = reportService.getDealerExpenseSummaryViewPageList(pageParam);
        List<DealerExpenseSummaryItem> rows = dealerExpenseSummaryItemPageList.getRows();
        Map<String, DealerExpenseSummaryItem> newMap = new HashMap();
        List<DealerExpenseSummaryItem> newList = new ArrayList<>();
        rows.forEach(item -> {
            if (newMap.get(item.getDealerCode()) != null) {
                DealerExpenseSummaryItem Expense = newMap.get(item.getDealerCode());
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
                newMap.put(item.getDealerCode(), Expense);
            } else {
                newMap.put(item.getDealerCode(), item);
            }
        });
        newMap.forEach((key, value) -> {
            newList.add(value);
        });
        dealerExpenseSummaryItemPageList.setRows(newList);
        pageResult = dealerExpenseSummaryItemPageList;
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(dealerExpenseSummaryItem);
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
        refreshPage(dealerExpenseSummaryItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {


        List<DealerExpenseSummaryItem> dealerExpenseSummaryItemList = reportService.DealerExpenseSummaryItemToExcel(dealerExpenseSummaryItem);
        Map<String, DealerExpenseSummaryItem> newMap = new HashMap();
        List<DealerExpenseSummaryItem> newList = new ArrayList<>();
        dealerExpenseSummaryItemList.forEach(item -> {
            if (newMap.get(item.getDealerCode()) != null) {
                DealerExpenseSummaryItem Expense = newMap.get(item.getDealerCode());
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
                newMap.put(item.getDealerCode(), Expense);
            } else {
                newMap.put(item.getDealerCode(), item);
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


        for (DealerExpenseSummaryItem expenseSummaryItem : newList) {
            Map<String, Object> map = BeanUtils.transBean2Map(expenseSummaryItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("服务站名称");
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
        keyList.add("dealerName");
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
    @NotifyChange({"dealerExpenseSummaryItem"})
    public void reset() {
        dealerExpenseSummaryItem.setDealerCode("");
        dealerExpenseSummaryItem.setDealerName("");
        dealerExpenseSummaryItem.setType("所有类型总费用");

    }


    @Command
    @NotifyChange("dealerExpenseSummaryItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        dealerExpenseSummaryItem.setDealerCode(model.getCode());
        dealerExpenseSummaryItem.setDealerName(model.getName());

    }

}
