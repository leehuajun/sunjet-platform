package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.WorkHoursReportItem;
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
 * 工时
 */
public class WorkHoursReportVM extends ListVM<WorkHoursReportItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    WorkHoursReportItem workHoursReportItem = new WorkHoursReportItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("WorkHoursReportEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            workHoursReportItem.setDealerCode(getActiveUser().getDealer().getCode());
            workHoursReportItem.setDealerName(getActiveUser().getDealer().getName());
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
        refreshFirstPage(workHoursReportItem, Order.ASC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getWorkHoursReportViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(workHoursReportItem);
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
        refreshPage(workHoursReportItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<WorkHoursReportItem> workHoursReportItemList = reportService.workHoursReportToExcel(workHoursReportItem);
        if (workHoursReportItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (workHoursReportItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();

        for (WorkHoursReportItem hoursReportItem : workHoursReportItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(hoursReportItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("项目编号");
        titleList.add("项目名称");
        titleList.add("工时定额");
        titleList.add("维修措施");
        titleList.add("夜间工时补贴费用");
        titleList.add("省份");
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("服务单号");
        titleList.add("VIN");
        titleList.add("车型");
        titleList.add("发动机型号");
        titleList.add("发动机号");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("code");
        keyList.add("name");
        keyList.add("workTime");
        keyList.add("measure");
        keyList.add("nightExpense");
        keyList.add("provinceName");
        keyList.add("dealerCode");
        keyList.add("dealerName");
        keyList.add("docNo");
        keyList.add("vin");
        keyList.add("vehicleModel");
        keyList.add("engineModel");
        keyList.add("engineNo");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"workHoursReportItem"})
    public void reset() {
        workHoursReportItem.setName("");
        workHoursReportItem.setDealerCode("");
        workHoursReportItem.setDealerName("");
    }

    @Command
    @NotifyChange("workHoursReportItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        workHoursReportItem.setDealerCode(model.getCode());
        workHoursReportItem.setDealerName(model.getName());

    }

}
