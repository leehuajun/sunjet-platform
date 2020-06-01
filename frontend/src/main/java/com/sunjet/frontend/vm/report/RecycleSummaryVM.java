package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.RecycleSummaryItem;
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
 * 故障件汇总
 */
public class RecycleSummaryVM extends ListVM<RecycleSummaryItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    RecycleSummaryItem recycleSummaryItem = new RecycleSummaryItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("RecycleSummaryEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            recycleSummaryItem.setDealerCode(getActiveUser().getDealer().getCode());
            recycleSummaryItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        recycleSummaryItem.setServiceManager(getActiveUser().getUsername());
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
        pageResult = reportService.getRecycleSummaryViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(recycleSummaryItem);
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
        refreshPage(recycleSummaryItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<RecycleSummaryItem> recycleSummaryItemList = reportService.recycleSummaryViewToExcel(recycleSummaryItem);
        if (recycleSummaryItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (recycleSummaryItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();

        for (RecycleSummaryItem summaryItem : recycleSummaryItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(summaryItem);
            maps.add(map);
        }


        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("故障返回单单号");
        //titleList.add("服务单号");
        titleList.add("运费");
        titleList.add("提交时间");


        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("dealerCode");
        keyList.add("dealerName");
        keyList.add("docNo");
        //keyList.add("srcDocNo");
        keyList.add("transportExpense");
        keyList.add("createdTime");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }

    /**
     * 重置
     */
    @Command
    @NotifyChange({"recycleSummaryItem"})
    public void reset() {
        recycleSummaryItem.setDealerName("");
    }


    @Command
    @NotifyChange("recycleSummaryItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        recycleSummaryItem.setDealerCode(model.getCode());
        recycleSummaryItem.setDealerName(model.getName());

    }


}
