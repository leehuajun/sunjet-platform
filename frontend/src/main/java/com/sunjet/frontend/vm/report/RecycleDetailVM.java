package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.RecycleDetailItem;
import com.sunjet.dto.system.admin.RoleInfo;
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
 * 故障件返回明细
 */
public class RecycleDetailVM extends ListVM<RecycleDetailItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    RecycleDetailItem recycleDetailItem = new RecycleDetailItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("QualityExpenseReportEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            recycleDetailItem.setDealerCode(getActiveUser().getDealer().getCode());
            recycleDetailItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        recycleDetailItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getRecycleDetailViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(recycleDetailItem);
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
        refreshPage(recycleDetailItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<RecycleDetailItem> recycleDetailItemList = reportService.recycleDetailViewToExcel(recycleDetailItem);
        if (recycleDetailItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (recycleDetailItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();

        for (RecycleDetailItem detailItem : recycleDetailItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("返回通知单单号");
        titleList.add("通知单提交时间");
        titleList.add("故障件返回单号");
        titleList.add("返回单提交时间");
        titleList.add("服务单号");
        titleList.add("服务经理");
        titleList.add("配件件号");
        titleList.add("配件名称");
        titleList.add("故障模式");
        titleList.add("换件原因");
        titleList.add("需返回数量");
        titleList.add("已返回数量");
        titleList.add("供货方式");
        titleList.add("三包时间");
        titleList.add("三包里程");
        titleList.add("应返时间");
        titleList.add("实返时间");
        titleList.add("物流单号");
        titleList.add("物流公司");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("dealerCode");
        keyList.add("dealerName");
        keyList.add("docNo");
        keyList.add("createdTime");
        keyList.add("ardDocNo");
        keyList.add("ardCreateTime");
        keyList.add("srcDocNo");
        keyList.add("serviceManager");
        keyList.add("partCode");
        keyList.add("partName");
        keyList.add("pattern");
        keyList.add("reason");
        keyList.add("amount");
        keyList.add("backAmount");
        keyList.add("partSupplyType");
        keyList.add("warrantyTime");
        keyList.add("warrantyMileage");
        keyList.add("returnDate");
        keyList.add("arriveDate");
        //keyList.add("hour_expense");
        keyList.add("logisticsNum");
        keyList.add("logistics");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"recycleDetailItem"})
    public void reset() {
        recycleDetailItem.setDealerName("");
        recycleDetailItem.setDealerCode("");
        recycleDetailItem.setPartCode("");
        recycleDetailItem.setPartName("");
    }

    @Command
    @NotifyChange("qualityExpenseReportDetailItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        recycleDetailItem.setDealerCode(model.getCode());
        recycleDetailItem.setDealerName(model.getName());

    }


}
