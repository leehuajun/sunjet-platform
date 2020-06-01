package com.sunjet.frontend.vm.report;


import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.MaintainDetailItem;
import com.sunjet.dto.system.admin.RoleInfo;
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
public class MaintainDetailVM extends ListVM<MaintainDetailItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    private MaintainDetailItem maintainDetailItem = new MaintainDetailItem();   //服务明细
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
    @Getter
    @Setter
    private List<MaintainDetailItem> maintainDetailItems;


    @Init
    public void init() {
        this.setEnableExport(hasPermission("MaintainDetailEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            maintainDetailItem.setDealer_code(getActiveUser().getDealer().getCode());
            maintainDetailItem.setDealer_name(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        maintainDetailItem.setService_manager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }
        maintainDetailItem.setDoc_type("");
        refreshFirstPage(maintainDetailItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getMaintainDetailViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(maintainDetailItem);
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
        refreshPage(maintainDetailItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<MaintainDetailItem> maintainDetailItems = reportService.maintainDetailToExcel(maintainDetailItem);
        if (maintainDetailItems.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (maintainDetailItems.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (MaintainDetailItem detailItem : maintainDetailItems) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }

        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("单据编号");
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("经办人");
        titleList.add("经办人电话");
        titleList.add("省份");
        titleList.add("服务经理");
        titleList.add("申请时间");
        titleList.add("进站时间");
        titleList.add("出站时间");
        titleList.add("服务站星级");
        titleList.add("单据类型");
        titleList.add("质量速报");
        titleList.add("费用速报");
        titleList.add("活动单");
        titleList.add("夜间补贴");
        titleList.add("首保费用标准");
        titleList.add("工时单价");
        titleList.add("项目工时费用");
        titleList.add("外出工时补贴");
        titleList.add("外出费用合计");
        titleList.add("其他费用");
        titleList.add("辅料费用合计");
        titleList.add("配件费用合计");
        titleList.add("费用合计");
        titleList.add("应结算辅料费");
        titleList.add("应结算配件费用");
        titleList.add("应结算费用");
        titleList.add("送修人");
        titleList.add("送修人电话");
        titleList.add("开工日期");
        titleList.add("完工日期");
        titleList.add("主修人");
        titleList.add("维修类别");
        titleList.add("故障描述");
        titleList.add("VIN");
        titleList.add("VSN");
        titleList.add("经销商");
        titleList.add("车型型号");
        titleList.add("生产日期");
        titleList.add("购车日期");
        titleList.add("发动机型号");
        titleList.add("发动机号/电动机号");
        titleList.add("行驶里程");
        titleList.add("车牌号");
        titleList.add("车主");
        titleList.add("服务里程");
        titleList.add("电话");
        titleList.add("详细地址");
        titleList.add("外出地点");
        titleList.add("单向里程");
        titleList.add("交通费用");
        titleList.add("拖车里程");
        titleList.add("拖车费用");
        titleList.add("外出人数");
        titleList.add("外出天数");
        titleList.add("人员补贴");
        titleList.add("住宿补贴");
        titleList.add("外出费用");
        titleList.add("当前状态");
        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("doc_no");
        keyList.add("dealer_code");
        keyList.add("dealer_name");
        keyList.add("submitter_name");
        keyList.add("submitter_phone");
        keyList.add("province_name");
        keyList.add("service_manager");
        keyList.add("createdTime");
        keyList.add("pull_in_date");
        keyList.add("pull_out_date");
        keyList.add("dealer_star");
        keyList.add("doc_type");
        keyList.add("quality_report_doc_no");
        keyList.add("expense_report_doc_no");
        keyList.add("activity_distribution_doc_no");
        keyList.add("night_expense");
        keyList.add("first_expense");
        keyList.add("hour_price");
        keyList.add("maintain_work_time_expense");
        //keyList.add("hour_expense");
        keyList.add("out_work_time_expense");
        keyList.add("amount_cost");
        keyList.add("other_expense");
        keyList.add("accessories_expense");
        keyList.add("part_expense");
        keyList.add("expense_Total");
        keyList.add("settlement_accesories_expense");
        keyList.add("settlement_part_expense");
        keyList.add("settlement_totle_expense");
        keyList.add("sender");
        keyList.add("sender_phone");
        keyList.add("start_date");
        keyList.add("end_date");
        keyList.add("repairer");
        keyList.add("repair_type");
        keyList.add("fault");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("seller");
        keyList.add("vehicle_model");
        keyList.add("manufacture_date");
        keyList.add("purchase_date");
        //keyList.add("product_date");
        keyList.add("engine_Model");
        keyList.add("engine_no");
        keyList.add("mileage");
        keyList.add("plate");
        keyList.add("owner_name");
        keyList.add("vmt");
        keyList.add("mobile");
        keyList.add("address");
        keyList.add("place");
        keyList.add("ago_mileage");
        keyList.add("tran_costs");
        keyList.add("trailer_mileage");
        keyList.add("trailer_cost");
        keyList.add("out_go_num");
        keyList.add("out_go_day");
        keyList.add("personnel_subsidy");
        keyList.add("night_subsidy");
        keyList.add("amount_cost");
        keyList.add("status");

        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }

    /**
     * 重置
     */
    @Command
    @NotifyChange({"maintainDetailItem"})
    public void reset() {
        maintainDetailItem.setDoc_type("");
        maintainDetailItem.setDealer_name("");
        maintainDetailItem.setDealer_code("");
        maintainDetailItem.setDealer_name("");
    }


    @Command
    @NotifyChange("maintainDetailItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        maintainDetailItem.setDealer_code(model.getCode());
        maintainDetailItem.setDealer_name(model.getName());

    }


}

