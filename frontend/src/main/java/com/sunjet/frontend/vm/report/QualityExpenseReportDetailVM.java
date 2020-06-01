package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.QualityExpenseReportDetailItem;
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
 * 质量费用速报明细
 */
public class QualityExpenseReportDetailVM extends ListVM<QualityExpenseReportDetailItem> {


    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    QualityExpenseReportDetailItem qualityExpenseReportDetailItem = new QualityExpenseReportDetailItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("RecycleDetailReportEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            qualityExpenseReportDetailItem.setDealerCode(getActiveUser().getDealer().getCode());
            qualityExpenseReportDetailItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        qualityExpenseReportDetailItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }
        qualityExpenseReportDetailItem.setType("");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getQualityExpenseReportDetailViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(qualityExpenseReportDetailItem);
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
        refreshPage(qualityExpenseReportDetailItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<QualityExpenseReportDetailItem> qualityExpenseReportDetailItems = reportService.qualityExpenseReportDetailViewToExcel(qualityExpenseReportDetailItem);
        if (qualityExpenseReportDetailItems.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (qualityExpenseReportDetailItems.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (QualityExpenseReportDetailItem expenseReportDetailItem : qualityExpenseReportDetailItems) {
            Map<String, Object> map = BeanUtils.transBean2Map(expenseReportDetailItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("速报标题");
        titleList.add("单据编号");
        titleList.add("状态");
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("车辆分类");
        titleList.add("费用类型");
        titleList.add("速报类型");
        titleList.add("申请人");
        titleList.add("申请人电话");
        titleList.add("服务经理");
        titleList.add("服务经理电话");
        titleList.add("备注");
        titleList.add("故障时行驶状态");
        titleList.add("故障时路面情况");
        titleList.add("故障发生地点");
        titleList.add("初步原因分析");
        titleList.add("处理意见");
        titleList.add("预计费用");
        titleList.add("VIN");
        titleList.add("车主");
        titleList.add("车主电话");
        titleList.add("购车日期");
        titleList.add("行驶里程");
        titleList.add("申请时间");

        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("title");
        keyList.add("docNo");
        keyList.add("status");
        keyList.add("dealerCode");
        keyList.add("dealerName");
        keyList.add("typeName");
        keyList.add("costType");
        keyList.add("reportType");
        keyList.add("submitterName");
        keyList.add("submitterPhone");
        keyList.add("serviceManager");
        keyList.add("serviceManagerPhone");
        keyList.add("comment");
        keyList.add("faultStatus");
        keyList.add("faultRoad");
        keyList.add("faultAddress");
        keyList.add("initialReason");
        keyList.add("decisions");
        keyList.add("estimatedCost");
        //keyList.add("hour_expense");
        keyList.add("vin");
        keyList.add("ownerName");
        keyList.add("mobile");
        keyList.add("purchaseDate");
        keyList.add("mileage");
        keyList.add("createdTime");

        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"qualityExpenseReportDetailItem"})
    public void reset() {
        qualityExpenseReportDetailItem.setCostType("");
        qualityExpenseReportDetailItem.setServiceManager("");
        qualityExpenseReportDetailItem.setReportType("");
        qualityExpenseReportDetailItem.setType("");
    }


    @Command
    @NotifyChange("qualityExpenseReportDetailItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        qualityExpenseReportDetailItem.setDealerCode(model.getCode());
        qualityExpenseReportDetailItem.setDealerName(model.getName());

    }
}
