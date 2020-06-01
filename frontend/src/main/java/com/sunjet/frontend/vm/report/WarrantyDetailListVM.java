package com.sunjet.frontend.vm.report;


import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.WarrantyPartDetailItem;
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
 * 三包配件明细
 */
public class WarrantyDetailListVM extends ListVM<WarrantyPartDetailItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    private WarrantyPartDetailItem warrantyPartDetailItem = new WarrantyPartDetailItem();   //服务明细

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
        this.setEnableExport(hasPermission("WarrantyPartDetailEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            warrantyPartDetailItem.setDealerCode(getActiveUser().getDealer().getCode());
            warrantyPartDetailItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        warrantyPartDetailItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }
        refreshFirstPage(warrantyPartDetailItem, Order.DESC, "asnCreatedTime");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getWarrantyPartDetailViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(warrantyPartDetailItem, Order.DESC, "asnCreatedTime");
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
        refreshPage(warrantyPartDetailItem);
        //刷新分页
        getPageList();
    }

    /**
     * 重置
     */
    @Command
    @NotifyChange({"warrantyPartDetailItem"})
    public void reset() {
        warrantyPartDetailItem.setPartCode("");
        warrantyPartDetailItem.setDealerCode("");
        warrantyPartDetailItem.setDealerName("");
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<WarrantyPartDetailItem> warrantyPartDetailItemList = reportService.warrantyPartDetailViewToExcel(warrantyPartDetailItem);
        if (warrantyPartDetailItemList.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (warrantyPartDetailItemList.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (WarrantyPartDetailItem detailItem : warrantyPartDetailItemList) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }

        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("配件件号");
        titleList.add("配件名称");
        titleList.add("配件类型");
        titleList.add("故障模式");
        titleList.add("换件原因");
        titleList.add("数量");
        titleList.add("单价");
        titleList.add("供货方式");
        titleList.add("三包时间");
        titleList.add("三包里程");
        titleList.add("服务单号");
        titleList.add("VIN");
        titleList.add("VSN");
        titleList.add("经销商");
        titleList.add("车辆型号");
        titleList.add("购买日期");
        titleList.add("行驶里程");
        titleList.add("发动机号");
        titleList.add("车牌号");
        titleList.add("车主姓名");
        titleList.add("电话");
        titleList.add("备注");
        titleList.add("配件分类");
        titleList.add("是否返回旧件");
        titleList.add("单据类型");
        titleList.add("服务站名称");
        titleList.add("服务站联系人");
        titleList.add("服务站联系电话");
        titleList.add("服务经理");
        titleList.add("质量速报单号");
        titleList.add("费用速报单号");
        titleList.add("省份");
        titleList.add("申请时间");
        titleList.add("进站时间");
        titleList.add("服务站星级");
        titleList.add("调拨单号");
        titleList.add("调拨单申请时间");
        titleList.add("备注");
        titleList.add("供货单号");
        titleList.add("合作商");
        titleList.add("供货数量");
        titleList.add("配件费用");
        titleList.add("应到货时间");
        titleList.add("到货时间");
        titleList.add("发运方式");
        titleList.add("物流单号");
        titleList.add("物流公司");
        titleList.add("提交时间");
        titleList.add("收货地址");
        titleList.add("收货人");
        titleList.add("收货电话");
        titleList.add("三包单状态");
        titleList.add("调拨通知单状态");
        titleList.add("供货通知单状态");

        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("partCode");
        keyList.add("partName");
        keyList.add("partType");
        keyList.add("pattern");
        keyList.add("reason");
        keyList.add("acpAmount");
        keyList.add("price");
        keyList.add("partSupplyType");
        keyList.add("warrantyTime");
        keyList.add("warrantyMileage");
        keyList.add("srcDocNo");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("seller");
        keyList.add("vehicleModel");
        keyList.add("purchaseDate");
        keyList.add("mileage");
        keyList.add("engineNo");
        keyList.add("plate");
        //keyList.add("hour_expense");
        keyList.add("ownerName");
        keyList.add("mobile");
        keyList.add("awmComment");
        keyList.add("partClassify");
        keyList.add("recycle");
        keyList.add("srcDocType");
        keyList.add("dealerName");
        keyList.add("submitterName");
        keyList.add("submitterPhone");
        keyList.add("serviceManager");
        keyList.add("qualityReportDocNo");
        keyList.add("expenseReportDocNo");
        keyList.add("provinceName");
        keyList.add("awmCreatedTime");
        keyList.add("pullInDate");
        keyList.add("dealerStar");
        keyList.add("asnDocNo");
        keyList.add("asnCreatedTime");
        keyList.add("asncomment");
        keyList.add("docNo");
        keyList.add("agencyName");
        //keyList.add("product_date");
        keyList.add("amount");
        keyList.add("money");
        keyList.add("arrivalTime");
        keyList.add("rcvDate");
        keyList.add("transportmodel");
        keyList.add("logisticsNum");
        keyList.add("logistics");
        keyList.add("asdCreatedTime");
        keyList.add("dealerAdderss");
        keyList.add("receive");
        keyList.add("operatorPhone");
        keyList.add("awmStatus");
        keyList.add("asnStatus");
        keyList.add("asdStatus");


        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }

    @Command
    @NotifyChange("warrantyPartDetailItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        warrantyPartDetailItem.setDealerCode(model.getCode());
        warrantyPartDetailItem.setDealerName(model.getName());

    }


}

