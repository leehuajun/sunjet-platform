package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.report.AgencySettlementDetailItem;
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
 * 合作商结算明细
 */
public class AgencySettlementDetailVM extends ListVM<AgencySettlementDetailItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    AgencySettlementDetailItem agencySettlementDetailItem = new AgencySettlementDetailItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("AgencySettlementReportEntity:export"));
        agencySettlementDetailItem.setStatus("-3");
        refreshData();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getAgencySettlementDetailViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(agencySettlementDetailItem);
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
        refreshPage(agencySettlementDetailItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<AgencySettlementDetailItem> agencySettlementDetailItemList = reportService.agencySettlementDetailViewToExcel(agencySettlementDetailItem);
        if (agencySettlementDetailItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (agencySettlementDetailItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();

        for (AgencySettlementDetailItem agencySettlement : agencySettlementDetailItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(agencySettlement);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("合作商");
        titleList.add("结算单号");
        titleList.add("配件费用");
        titleList.add("运费");
        titleList.add("总费用");
        titleList.add("提交时间");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("agencyName");
        keyList.add("docNo");
        keyList.add("partExpense");
        keyList.add("freightExpense");
        keyList.add("expenseTotal");
        keyList.add("createdTime");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }

    /**
     * 重置
     */
    @Command
    @NotifyChange({"agencySettlementDetailItem"})
    public void reset() {
        agencySettlementDetailItem.setAgencyCode("");
    }

}
