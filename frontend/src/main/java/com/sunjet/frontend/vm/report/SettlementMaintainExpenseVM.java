package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.SettlementMaintainExpenseItem;
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
 * 服务站结算明细
 */
public class SettlementMaintainExpenseVM extends ListVM<SettlementMaintainExpenseItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    SettlementMaintainExpenseItem settlementMaintainExpenseItem = new SettlementMaintainExpenseItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("SettlementMaintainReportEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            settlementMaintainExpenseItem.setDealerCode(getActiveUser().getDealer().getCode());
            settlementMaintainExpenseItem.setDealerName(getActiveUser().getDealer().getName());
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
        refreshFirstPage(settlementMaintainExpenseItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getSettlementMaintainExpenseViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(settlementMaintainExpenseItem);
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
        refreshPage(settlementMaintainExpenseItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<SettlementMaintainExpenseItem> settlementMaintainExpenseItemList = reportService.settlementMaintainExpenseViewToExcel(settlementMaintainExpenseItem);
        if (settlementMaintainExpenseItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (settlementMaintainExpenseItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();


        for (SettlementMaintainExpenseItem maintainExpenseItem : settlementMaintainExpenseItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(maintainExpenseItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("结算单号");
        titleList.add("三包费用");
        titleList.add("首保费用");
        titleList.add("活动费用");
        titleList.add("故障件运费");
        titleList.add("奖惩费用");
        titleList.add("总费用");
        titleList.add("提交时间");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("dealerCode");
        keyList.add("dealerName");
        keyList.add("docNo");
        keyList.add("warrantyExpenseTotal");
        keyList.add("firstExpenseTotal");
        keyList.add("activityExpenseTotal");
        keyList.add("freightExpenseTotal");
        keyList.add("rewardPunishmentExpense");
        keyList.add("expenseTotal");
        keyList.add("createdTime");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"settlementMaintainExpenseItem"})
    public void reset() {
        settlementMaintainExpenseItem.setDealerName("");
    }


    @Command
    @NotifyChange("settlementMaintainExpenseItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        settlementMaintainExpenseItem.setDealerCode(model.getCode());
        settlementMaintainExpenseItem.setDealerName(model.getName());

    }


}
