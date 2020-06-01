package com.sunjet.backend.controller.report;

import com.sunjet.backend.modules.asms.entity.report.*;
import com.sunjet.backend.modules.asms.service.report.ReportService;
import com.sunjet.dto.asms.report.*;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author SUNJET_WS
 * Created by SUNJET_WS on 2017/8/17.
 * 报表
 */
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportService reportService;


    /**
     * 查询服务明细分页
     *
     * @param
     * @return
     */
    @PostMapping("/getMaintainDetailViewPageList")
    public PageResult<MaintainDetailView> getMaintainDetailViewPageList(@RequestBody PageParam<MaintainDetailItem> pageParam) {

        return reportService.getMaintainDetailViewPageList(pageParam);
    }


    /**
     * 根据条件查出需要导出excel的数据
     *
     * @param maintainDetailItem
     * @return
     */
    @PostMapping("getMaintainDetailToExcel")
    public List<MaintainDetailView> maintainDetailToExcel(@RequestBody MaintainDetailItem maintainDetailItem) {
        return reportService.maintainDetailToExcel(maintainDetailItem);
    }

    /**
     * 三包配件分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("getWarrantyPartDetailViewPageList")
    public PageResult<WarrantyPartDetailView> getWarrantyPartDetailViewPageList(@RequestBody PageParam<WarrantyPartDetailItem> pageParam) {
        return reportService.getWarrantyPartDetailViewPageList(pageParam);
    }

    /**
     * 获取合作商结算明细分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getAgencySettlementDetailViewPageList")
    public PageResult<AgencySettlementDetailView> getAgencySettlementDetailViewPageList(@RequestBody PageParam<AgencySettlementDetailItem> pageParam) {
        return reportService.getAgencySettlementDetailViewPageList(pageParam);
    }


    /**
     * 获取合作商结算年汇总视图分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getAgencySettlementYearSummaryViewPageList")
    public PageResult<AgencySettlementYearSummaryView> getAgencySettlementYearSummaryViewPageList(@RequestBody PageParam<AgencySettlementYearSummaryItem> pageParam) {
        return reportService.getAgencySettlementYearSummaryViewPageList(pageParam);
    }


    /**
     * 获取合作商结算年汇总运费视图分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getAgencySettlementYearSummaryFreightViewPageList")
    public PageResult<AgencySettlementYearSummaryFreightView> getAgencySettlementYearSummaryFreightViewPageList(@RequestBody PageParam<AgencySettlementYearSummaryItem> pageParam) {
        return reportService.getAgencySettlementYearSummaryFreightViewPageList(pageParam);
    }


    /**
     * 获取合作商供货明细分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getAgencySupplySummaryViewPageList")
    public PageResult<AgencySupplySummaryView> getAgencySupplySummaryViewPageList(@RequestBody PageParam<AgencySupplySummaryItem> pageParam) {
        return reportService.getAgencySupplySummaryViewPageList(pageParam);
    }

    /**
     * 获取质量费用速报明细分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getQualityExpenseReportDetailViewPageList")
    public PageResult<QualityExpenseReportDetailView> getQualityExpenseReportDetailViewPageList(@RequestBody PageParam<QualityExpenseReportDetailItem> pageParam) {
        return reportService.getQualityExpenseReportDetailViewPageList(pageParam);
    }


    /**
     * 获取故障件明细分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getRecycleDetailViewPageList")
    public PageResult<RecycleDetailView> getRecycleDetailViewPageList(@RequestBody PageParam<RecycleDetailItem> pageParam) {
        return reportService.getRecycleDetailViewPageList(pageParam);
    }


    /**
     * 获取故障件汇总分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getRecycleSummaryViewPageList")
    public PageResult<RecycleSummaryView> getRecycleSummaryViewPageList(@RequestBody PageParam<RecycleSummaryItem> pageParam) {
        return reportService.getRecycleSummaryViewPageList(pageParam);
    }


    /**
     * 获取工时统计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getWorkHoursReportViewPageList")
    public PageResult<WorkHoursReportView> getWorkHoursReportViewPageList(@RequestBody PageParam<WorkHoursReportItem> pageParam) {
        return reportService.getWorkHoursReportViewPageList(pageParam);
    }


    /**
     * 获取服务站信息分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerMessageViewPageList")
    public PageResult<DealerMessageView> getDealerMessageViewPageList(@RequestBody PageParam<DealerMessageItem> pageParam) {
        return reportService.getDealerMessageViewPageList(pageParam);
    }


    /**
     * 三包自购分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getWarrantyPartsSelfPurchaseDetailViewPageList")
    public PageResult<WarrantyPartsSelfPurchaseDetailView> getWarrantyPartsSelfPurchaseDetailViewPageList(@RequestBody PageParam<WarrantyPartsSelfPurchaseDetailItem> pageParam) {
        return reportService.getWarrantyPartsSelfPurchaseDetailViewPageList(pageParam);
    }


    /**
     * 服务站结算分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getSettlementMaintainExpenseViewPageList")
    public PageResult<SettlementMaintainExpenseView> getSettlementMaintainExpenseViewPageList(@RequestBody PageParam<SettlementMaintainExpenseItem> pageParam) {
        return reportService.getSettlementMaintainExpenseViewPageList(pageParam);
    }

    /**
     * 服务站年度费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpenseSummaryViewPageList")
    public PageResult<DealerExpenseSummaryView> getDealerExpenseSummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {

        return reportService.getDealerExpenseSummaryViewPageList(pageParam);

    }


    /**
     * 服务站三包费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpenseWarrantySummaryViewPageList")
    public PageResult<DealerExpenseWarrantySummaryView> getDealerExpenseWarrantySummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {

        return reportService.getDealerExpenseWarrantySummaryViewPageList(pageParam);

    }


    /**
     * 服务站活动费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpenseActivitySummaryViewPageList")
    public PageResult<DealerExpenseActivitySummaryView> getDealerExpenseActivitySummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {

        return reportService.getDealerExpenseActivitySummaryViewPageList(pageParam);

    }


    /**
     * 服务站首保费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpenseFirstSummaryViewPageList")
    public PageResult<DealerExpenseFirstSummaryView> getDealerExpenseFirstSummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {

        return reportService.getDealerExpenseFirstSummaryViewPageList(pageParam);

    }


    /**
     * 服务站故障件返回费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpenseFreightSummaryViewPageList")
    public PageResult<DealerExpenseFreightSummaryView> getDealerExpenseFreightSummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {

        return reportService.getDealerExpenseFreightSummaryViewPageList(pageParam);

    }


    /**
     * 服务站故障件外出费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpenseGoOutSummaryViewPageList")
    public PageResult<DealerExpenseGoOutSummaryView> getDealerExpenseGoOutSummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {
        return reportService.getDealerExpenseGoOutSummaryViewPageList(pageParam);

    }


    /**
     * 服务站故障件配件费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpensePartSummaryViewPageList")
    public PageResult<DealerExpensePartSummaryView> getDealerExpensePartSummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {
        return reportService.getDealerExpensePartSummaryViewPageList(pageParam);

    }


    /**
     * 服务站故障件配件费用总计分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getDealerExpenseWorkHoursSummaryViewPageList")
    public PageResult<DealerExpenseWorkHoursSummaryView> getDealerExpenseWorkHoursSummaryViewPageList(@RequestBody PageParam<DealerExpenseSummaryItem> pageParam) {
        return reportService.getDealerExpenseWorkHoursSummaryViewPageList(pageParam);

    }


    /**
     * 三包配件导出excel的数据
     *
     * @param warrantyPartsSelfPurchaseDetailItem
     * @return
     */
    @PostMapping("getWarrantyPartsSelfPurchaseDetailToExcel")
    public List<WarrantyPartsSelfPurchaseDetailView> getWarrantyPartsSelfPurchaseDetailToExcel(@RequestBody WarrantyPartsSelfPurchaseDetailItem warrantyPartsSelfPurchaseDetailItem) {
        return reportService.warrantyPartsSelfPurchaseDetailViewToExcel(warrantyPartsSelfPurchaseDetailItem);
    }


    /**
     * 质量费用速报明细导出excel的数据
     *
     * @param qualityExpenseReportDetailItem
     * @return
     */
    @PostMapping("qualityExpenseReportDetailViewToExcel")
    public List<QualityExpenseReportDetailView> qualityExpenseReportDetailViewToExcel(@RequestBody QualityExpenseReportDetailItem qualityExpenseReportDetailItem) {
        return reportService.qualityExpenseReportDetailViewToExcel(qualityExpenseReportDetailItem);
    }


    /**
     * 故障件明细导出excel的数据
     *
     * @param recycleDetailItem
     * @return
     */
    @PostMapping("recycleDetailViewToExcel")
    public List<RecycleDetailView> recycleDetailViewToExcel(@RequestBody RecycleDetailItem recycleDetailItem) {
        return reportService.recycleDetailViewToExcel(recycleDetailItem);
    }


    /**
     * 故障件汇总导出excel的数据
     *
     * @param recycleSummaryItem
     * @return
     */
    @PostMapping("recycleSummaryViewToExcel")
    public List<RecycleSummaryView> recycleSummaryViewToExcel(@RequestBody RecycleSummaryItem recycleSummaryItem) {
        return reportService.recycleSummaryViewToExcel(recycleSummaryItem);
    }


    /**
     * 服务站结算导出excel的数据
     *
     * @param settlementMaintainExpenseItem
     * @return
     */
    @PostMapping("settlementMaintainExpenseViewToExcel")
    public List<SettlementMaintainExpenseView> settlementMaintainExpenseViewToExcel(@RequestBody SettlementMaintainExpenseItem settlementMaintainExpenseItem) {
        return reportService.settlementMaintainExpenseViewToExcel(settlementMaintainExpenseItem);
    }


    /**
     * 合作商结算导出excel的数据
     *
     * @param agencySettlementDetailItem
     * @return
     */
    @PostMapping("agencySettlementDetailViewToExcel")
    public List<AgencySettlementDetailView> agencySettlementDetailViewToExcel(@RequestBody AgencySettlementDetailItem agencySettlementDetailItem) {
        return reportService.agencySettlementDetailViewToExcel(agencySettlementDetailItem);
    }


    /**
     * 合作商供货导出excel的数据
     *
     * @param agencySupplySummaryItem
     * @return
     */
    @PostMapping("agencySupplySummaryViewToExcel")
    public List<AgencySupplySummaryView> agencySupplySummaryViewToExcel(@RequestBody AgencySupplySummaryItem agencySupplySummaryItem) {
        return reportService.agencySupplySummaryViewToExcel(agencySupplySummaryItem);
    }


    /**
     * 合作商配件年费导出excel的数据
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */
    @PostMapping("agencySettlementYearSummaryViewToExcel")
    public List<AgencySettlementYearSummaryView> agencySettlementYearSummaryViewToExcel(@RequestBody AgencySettlementYearSummaryItem agencySettlementYearSummaryItem) {
        return reportService.agencySettlementYearSummaryViewToExcel(agencySettlementYearSummaryItem);
    }

    /**
     * 合作商运费年费导出excel的数据
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */
    @PostMapping("agencySettlementYearSummaryFreightViewToExcel")
    public List<AgencySettlementYearSummaryFreightView> agencySettlementYearSummaryFreightViewToExcel(@RequestBody AgencySettlementYearSummaryItem agencySettlementYearSummaryItem) {
        return reportService.agencySettlementYearSummaryFreightViewToExcel(agencySettlementYearSummaryItem);
    }


    /**
     * 工时明细导出
     *
     * @param workHoursReportItem
     * @return
     */
    @PostMapping("workHoursReportToExcel")
    public List<WorkHoursReportView> workHoursReportToExcel(@RequestBody WorkHoursReportItem workHoursReportItem) {
        return reportService.workHoursReportToExcel(workHoursReportItem);
    }


    /**
     * 服务站信息导出
     *
     * @param dealerMessageItem
     * @return
     */
    @PostMapping("dealerMessageViewToExcel")
    public List<DealerMessageView> dealerMessageViewToExcel(@RequestBody DealerMessageItem dealerMessageItem) {
        return reportService.dealerMessageViewToExcel(dealerMessageItem);
    }


    /**
     * 三包配件导出
     *
     * @param warrantyPartDetailItem
     * @return
     */
    @PostMapping("warrantyPartDetailViewToExcel")
    public List<WarrantyPartDetailView> warrantyPartDetailViewToExcel(@RequestBody WarrantyPartDetailItem warrantyPartDetailItem) {
        return reportService.warrantyPartDetailViewToExcel(warrantyPartDetailItem);
    }


    /**
     * 服务站年度总费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpenseSummaryViewToExcel")
    public List<DealerExpenseSummaryView> dealerExpenseSummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpenseSummaryViewToExcel(dealerExpenseSummaryItem);
    }


    /**
     * 服务站年度活动费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpenseActivitySummaryViewToExcel")
    public List<DealerExpenseActivitySummaryView> dealerExpenseActivitySummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpenseActivitySummaryViewToExcel(dealerExpenseSummaryItem);
    }


    /**
     * 服务站年度三包费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpenseWarrantySummaryViewToExcel")
    public List<DealerExpenseWarrantySummaryView> dealerExpenseWarrantySummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpenseWarrantySummaryViewToExcel(dealerExpenseSummaryItem);
    }


    /**
     * 服务站年度首保费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpenseFirstSummaryViewToExcel")
    public List<DealerExpenseFirstSummaryView> dealerExpenseFirstSummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpenseFirstSummaryViewToExcel(dealerExpenseSummaryItem);
    }


    /**
     * 服务站年度故障件返回运费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpenseFreightSummaryViewToExcel")
    public List<DealerExpenseFreightSummaryView> dealerExpenseFreightSummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpenseFreightSummaryViewToExcel(dealerExpenseSummaryItem);
    }

    /**
     * 服务站年度外出费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpenseGoOutSummaryViewToExcel")
    public List<DealerExpenseGoOutSummaryView> dealerExpenseGoOutSummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpenseGoOutSummaryViewToExcel(dealerExpenseSummaryItem);
    }


    /**
     * 服务站年度配件费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpensePartSummaryViewToExcel")
    public List<DealerExpensePartSummaryView> dealerExpensePartSummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpensePartSummaryViewToExcel(dealerExpenseSummaryItem);
    }


    /**
     * 服务站年度工时费用导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @PostMapping("dealerExpenseWorkHoursSummaryViewToExcel")
    public List<DealerExpenseWorkHoursSummaryView> dealerExpenseWorkHoursSummaryViewToExcel(@RequestBody DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        return reportService.dealerExpenseWorkHoursSummaryViewToExcel(dealerExpenseSummaryItem);
    }

    /**
     * 获取活动通知单报表分页
     *
     * @param pageParam
     * @return
     */
    @PostMapping("getActivityNoticeReportViewPageList")
    public PageResult<ActivitiNoticeReportView> getActivityNoticeReportViewPageList(@RequestBody PageParam<ActivityNoticeReportItem> pageParam) {
        return reportService.getActivityNoticeReportViewPageList(pageParam);
    }

    /**
     * 活动通知单报表导出excel
     *
     * @param activityNoticeReportItem
     * @return
     */
    @PostMapping("activityNoticeReportToExcel")
    public List<ActivitiNoticeReportView> activityNoticeReportToExcel(@RequestBody ActivityNoticeReportItem activityNoticeReportItem) {
        return reportService.activityNoticeReportToExcel(activityNoticeReportItem);
    }


    /**
     * 打印页面
     *
     * @param map
     * @return
     */
    @PostMapping("printPage")
    public byte[] printPage(@RequestBody Map<String, Object> map) {
        return reportService.printPage(map);
    }

    /**
     * 打印故障件标签
     *
     * @param map
     * @return
     */
    @PostMapping("printRecycleLabel")
    public byte[] printRecycleLabel(@RequestBody Map<String, String> map) {
        return reportService.printRecycleLabel(map);
    }

    /**
     * 打印三包故障件标签
     *
     * @param map
     * @return
     */
    @PostMapping("warrantyPrintRecycleLabel")
    public byte[] warrantyPrintRecycleLabel(@RequestBody Map<String, String> map) {
        return reportService.warrantyPrintRecycleLabel(map);
    }

}
