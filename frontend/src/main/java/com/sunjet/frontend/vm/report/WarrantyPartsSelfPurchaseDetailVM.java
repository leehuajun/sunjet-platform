package com.sunjet.frontend.vm.report;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.report.WarrantyPartsSelfPurchaseDetailItem;
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
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 三包配件自购明细
 */
public class WarrantyPartsSelfPurchaseDetailVM extends ListVM<WarrantyPartsSelfPurchaseDetailItem> {

    @WireVariable
    private ReportService reportService;

    @Getter
    @Setter
    WarrantyPartsSelfPurchaseDetailItem warrantyPartsSelfPurchaseDetailItem = new WarrantyPartsSelfPurchaseDetailItem();


    @Init
    public void init() {
        this.setEnableExport(hasPermission("WarrantyPartsSelfPurchaseEntity:export"));
        if (getActiveUser().getAgency() != null) {
            //合作商
        } else if (getActiveUser().getDealer() != null) {
            //服务站
            warrantyPartsSelfPurchaseDetailItem.setDealerCode(getActiveUser().getDealer().getCode());
            warrantyPartsSelfPurchaseDetailItem.setDealerName(getActiveUser().getDealer().getName());
        } else {
            //五菱
            List<RoleInfo> roles = getActiveUser().getRoles();
            if (roles != null) {
                for (RoleInfo role : roles) {
                    if ("服务经理".equals(role.getName())) {
                        warrantyPartsSelfPurchaseDetailItem.setServiceManager(getActiveUser().getUsername());
                        break;
                    }
                }
            }
        }
        refreshFirstPage(warrantyPartsSelfPurchaseDetailItem, Order.DESC, "objId");
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = reportService.getWarrantyPartsSelfPurchaseDetailViewPageList(pageParam);
    }

    /**
     * 查询
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(warrantyPartsSelfPurchaseDetailItem);
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
        refreshPage(warrantyPartsSelfPurchaseDetailItem);
        //刷新分页
        getPageList();
    }


    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        List<WarrantyPartsSelfPurchaseDetailItem> warrantyPartsSelfPurchaseDetailItems = reportService.getWarrantyPartsSelfPurchaseDetailToExcel(warrantyPartsSelfPurchaseDetailItem);
        if (warrantyPartsSelfPurchaseDetailItems.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        } else if (warrantyPartsSelfPurchaseDetailItems.size() > 5000) {
            ZkUtils.showInformation("请缩小导出范围", "提示");
            return;
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (WarrantyPartsSelfPurchaseDetailItem partsSelfPurchaseDetailItem : warrantyPartsSelfPurchaseDetailItems) {
            Map<String, Object> map = BeanUtils.transBean2Map(partsSelfPurchaseDetailItem);
            maps.add(map);
        }

        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("配件号");
        titleList.add("配件名称");
        titleList.add("配件类型");
        titleList.add("故障模式");
        titleList.add("换件原因");
        titleList.add("数量");
        titleList.add("价格");
        titleList.add("供货方式");
        titleList.add("三包时间");
        titleList.add("三包里程");
        titleList.add("服务单号");
        titleList.add("车辆vin");
        titleList.add("车辆vsn");
        titleList.add("销售商");
        titleList.add("车辆型号");
        titleList.add("购买日期");
        titleList.add("行驶里程");
        titleList.add("发动机号码");
        titleList.add("车牌号");
        titleList.add("车主姓名");
        titleList.add("车主电话");
        titleList.add("备注");
        titleList.add("配件分类");
        titleList.add("是否返回");
        titleList.add("单据类型");
        titleList.add("服务站名称");
        titleList.add("服务站联系人");
        titleList.add("服务站电话");
        titleList.add("服务经理");
        titleList.add("质量速报");
        titleList.add("费用速报");
        titleList.add("省份");
        titleList.add("提交时间");
        titleList.add("进站时间");
        titleList.add("服务站星级");

        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("partCode");
        keyList.add("partName");
        keyList.add("partType");
        keyList.add("pattern");
        keyList.add("reason");
        keyList.add("amount");
        keyList.add("price");
        keyList.add("partSupplyType");
        keyList.add("warrantyTime");
        keyList.add("warrantyMileage");
        keyList.add("docNo");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("seller");
        keyList.add("vehicleModel");
        keyList.add("purchaseDate");
        keyList.add("mileage");
        keyList.add("engineNo");
        keyList.add("plate");
        keyList.add("ownerName");
        keyList.add("mobile");
        keyList.add("comment");
        keyList.add("partClassify");
        keyList.add("recycle");
        keyList.add("docType");
        keyList.add("dealerName");
        keyList.add("submitterName");
        keyList.add("dealerPhone");
        keyList.add("serviceManager");
        keyList.add("qualityReportDocNo");
        keyList.add("expenseReportDocNo");
        keyList.add("provinceName");
        keyList.add("createdTime");
        keyList.add("pullInDate");
        keyList.add("dealerStar");

        ExcelUtil.listMapToExcel(titleList, keyList, maps);


    }


    /**
     * 重置
     */
    @Command
    @NotifyChange({"warrantyPartsSelfPurchaseDetailItem"})
    public void reset() {
        warrantyPartsSelfPurchaseDetailItem.setDocNo("");
        warrantyPartsSelfPurchaseDetailItem.setDealerCode("");
        warrantyPartsSelfPurchaseDetailItem.setDealerName("");
    }


    @Command
    @NotifyChange("warrantyPartsSelfPurchaseDetailItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        warrantyPartsSelfPurchaseDetailItem.setDealerCode(model.getCode());
        warrantyPartsSelfPurchaseDetailItem.setDealerName(model.getName());

    }


}
