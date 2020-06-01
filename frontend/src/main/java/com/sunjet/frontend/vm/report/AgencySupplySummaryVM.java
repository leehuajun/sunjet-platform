package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.report.AgencySupplySummaryItem;
import com.sunjet.dto.system.base.Order;
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
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 合作商供货汇总
 */
public class AgencySupplySummaryVM extends ListVM<AgencySupplySummaryItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    AgencySupplySummaryItem agencySupplySummaryItem = new AgencySupplySummaryItem();

    @Getter
    @Setter
    private List<AgencyInfo> agencyList = new ArrayList<>();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("AgencySupplySummaryEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
            agencySupplySummaryItem.setAgencyCode(getActiveUser().getAgency().getCode());
            agencySupplySummaryItem.setAgencyName(getActiveUser().getAgency().getName());
        }


        refreshFirstPage(agencySupplySummaryItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getAgencySupplySummaryViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(agencySupplySummaryItem);
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
        refreshPage(agencySupplySummaryItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<AgencySupplySummaryItem> agencySupplySummaryItemList = reportService.agencySupplySummaryViewToExcel(agencySupplySummaryItem);
        if (agencySupplySummaryItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (agencySupplySummaryItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();

        for (AgencySupplySummaryItem supplySummaryItem : agencySupplySummaryItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(supplySummaryItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("合作商编号");
        titleList.add("合作商名称");
        titleList.add("调拨类型");
        titleList.add("调拨单号");
        titleList.add("配件费用");
        titleList.add("运费");
        titleList.add("总费用");
        titleList.add("提交时间");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("agencyCode");
        keyList.add("agencyName");
        keyList.add("srcDocType");
        keyList.add("docNo");
        keyList.add("partExpense");
        keyList.add("transportExpense");
        keyList.add("expenseTotal");
        keyList.add("createdTime");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"agencySupplySummaryItem"})
    public void reset() {
        agencySupplySummaryItem.setAgencyCode("");
        this.agencySupplySummaryItem.setAgencyName("");
        this.agencySupplySummaryItem.setAgencyCode("");
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
    @NotifyChange({"agencyList", "keyword", "agencySupplySummaryItem"})
    public void selectAgency(@BindingParam("model") AgencyInfo agencyInfo) {
        this.agencySupplySummaryItem.setAgencyName(agencyInfo.getName());
        this.agencySupplySummaryItem.setAgencyCode(agencyInfo.getCode());

    }


}
