package com.sunjet.frontend.vm.report;


import com.sunjet.dto.asms.report.ActivityNoticeReportItem;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2017-7-13 上午12:00
 * 服务单明细
 */
public class ActivityNoticesReportListVM extends ListVM<ActivityNoticeReportItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    private ActivityNoticeReportItem activityNoticeReportItem = new ActivityNoticeReportItem();   //活动通知单报表

    @Getter
    @Setter
    private String dealerName = "";
    @Getter
    @Setter
    private String type = "";

    @Getter
    @Setter
    private Date startDate = new Date();    // 开始日期，绑定页面搜索的开始日期
    @Getter
    @Setter
    private Date endDate = new Date();      // 结束日期，绑定页面搜索的结束日期
    @Getter
    @Setter
    private List<Map<String, Object>> maps;


    @Init
    public void init() {
        this.setEnableExport(hasPermission("ActivityNoticeReportEntity:export"));
        refreshFirstPage(activityNoticeReportItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getActivityNoticeReportViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(activityNoticeReportItem);
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
        refreshPage(activityNoticeReportItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<ActivityNoticeReportItem> activityNoticeReportItems = reportService.activityNoticeReportToExcel(activityNoticeReportItem);
        if (activityNoticeReportItems.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (activityNoticeReportItems.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (ActivityNoticeReportItem activityNoticeReportItem : activityNoticeReportItems) {
            Map<String, Object> map = BeanUtils.transBean2Map(activityNoticeReportItem);
            maps.add(map);
        }

        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("VIN");
        titleList.add("VSN");
        titleList.add("车型型号");
        titleList.add("用户");
        titleList.add("电话");
        titleList.add("用户地址");
        titleList.add("购车日期");
        titleList.add("行驶里程");
        titleList.add("状态");
        titleList.add("单据编号");
        titleList.add("开始日期");
        titleList.add("结束日期");
        titleList.add("状态");
        titleList.add("标题");
        titleList.add("发布时间");

        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("vehicleModel");
        keyList.add("ownerName");
        keyList.add("mobile");
        keyList.add("address");
        keyList.add("purchaseDate");
        keyList.add("mileage");
        keyList.add("distribute");
        keyList.add("docNo");
        keyList.add("startDate");
        keyList.add("endDate");
        keyList.add("status");
        keyList.add("title");
        keyList.add("createdTime");

        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"activityNoticeReportItem"})
    public void reset() {
        activityNoticeReportItem.setDocNo("");
    }


}

