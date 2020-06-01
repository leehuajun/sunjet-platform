package com.sunjet.backend.modules.asms.service.report;

import com.sunjet.backend.modules.asms.entity.report.*;
import com.sunjet.dto.asms.report.*;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 报表
 */
public interface ReportService {


    /**
     * 服务单明细导出
     *
     * @param maintainDetailItem
     * @return
     */
    List<MaintainDetailView> maintainDetailToExcel(MaintainDetailItem maintainDetailItem);

    /**
     * 获取服务单明细分页
     *
     * @param pageParam
     * @return
     */
    PageResult<MaintainDetailView> getMaintainDetailViewPageList(PageParam<MaintainDetailItem> pageParam);

    /**
     * 获取三包配件明细分页
     *
     * @param pageParam
     * @return
     */
    PageResult<WarrantyPartDetailView> getWarrantyPartDetailViewPageList(PageParam<WarrantyPartDetailItem> pageParam);


    /**
     * 获取合作商结算明细分页
     *
     * @param pageParam
     * @return
     */
    PageResult<AgencySettlementDetailView> getAgencySettlementDetailViewPageList(PageParam<AgencySettlementDetailItem> pageParam);


    /**
     * 获取合作商结算年度汇总分页
     *
     * @param pageParam
     * @return
     */
    PageResult<AgencySettlementYearSummaryView> getAgencySettlementYearSummaryViewPageList(PageParam<AgencySettlementYearSummaryItem> pageParam);


    /**
     * 获取合作商结算年度运费汇总分页
     *
     * @param pageParam
     * @return
     */
    PageResult<AgencySettlementYearSummaryFreightView> getAgencySettlementYearSummaryFreightViewPageList(PageParam<AgencySettlementYearSummaryItem> pageParam);


    /**
     * 获取合作商供货明细分页
     *
     * @param pageParam
     * @return
     */
    PageResult<AgencySupplySummaryView> getAgencySupplySummaryViewPageList(PageParam<AgencySupplySummaryItem> pageParam);


    /**
     * 质量费用速报分页
     *
     * @param pageParam
     * @return
     */
    PageResult<QualityExpenseReportDetailView> getQualityExpenseReportDetailViewPageList(PageParam<QualityExpenseReportDetailItem> pageParam);


    /**
     * 故障件返回明细分页
     *
     * @param pageParam
     * @return
     */
    PageResult<RecycleDetailView> getRecycleDetailViewPageList(PageParam<RecycleDetailItem> pageParam);


    /**
     * 故障件汇总分页
     *
     * @param pageParam
     * @return
     */
    PageResult<RecycleSummaryView> getRecycleSummaryViewPageList(PageParam<RecycleSummaryItem> pageParam);

    /**
     * 工时统计分页
     *
     * @param pageParam
     * @return
     */
    PageResult<WorkHoursReportView> getWorkHoursReportViewPageList(PageParam<WorkHoursReportItem> pageParam);


    /**
     * 服务站信息分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerMessageView> getDealerMessageViewPageList(PageParam<DealerMessageItem> pageParam);

    /**
     * 三包自购明细
     *
     * @param pageParam
     * @return
     */
    PageResult<WarrantyPartsSelfPurchaseDetailView> getWarrantyPartsSelfPurchaseDetailViewPageList(PageParam<WarrantyPartsSelfPurchaseDetailItem> pageParam);


    /**
     * 服务站结算费用总计分页
     *
     * @param pageParam
     * @return
     */
    PageResult<SettlementMaintainExpenseView> getSettlementMaintainExpenseViewPageList(PageParam<SettlementMaintainExpenseItem> pageParam);

    /**
     * 服务站年度总费用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpenseSummaryView> getDealerExpenseSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 服务站年度三包费用用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpenseWarrantySummaryView> getDealerExpenseWarrantySummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 服务站年度活动费用用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpenseActivitySummaryView> getDealerExpenseActivitySummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 服务站年度首保费用用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpenseFirstSummaryView> getDealerExpenseFirstSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 服务站年度故障运费用用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpenseFreightSummaryView> getDealerExpenseFreightSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 服务站年度外出费用用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpenseGoOutSummaryView> getDealerExpenseGoOutSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 服务站年度工时费用用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpenseWorkHoursSummaryView> getDealerExpenseWorkHoursSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 服务站年度配件费用用分页
     *
     * @param pageParam
     * @return
     */
    PageResult<DealerExpensePartSummaryView> getDealerExpensePartSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam);


    /**
     * 三包配件自购导出
     *
     * @param warrantyPartsSelfPurchaseDetailItem
     * @return
     */
    List<WarrantyPartsSelfPurchaseDetailView> warrantyPartsSelfPurchaseDetailViewToExcel(WarrantyPartsSelfPurchaseDetailItem warrantyPartsSelfPurchaseDetailItem);


    /**
     * 质量速报费用速报导出
     *
     * @param qualityExpenseReportDetailItem
     * @return
     */
    List<QualityExpenseReportDetailView> qualityExpenseReportDetailViewToExcel(QualityExpenseReportDetailItem qualityExpenseReportDetailItem);


    /**
     * 故障件明细导出
     *
     * @param recycleDetailItem
     * @return
     */
    List<RecycleDetailView> recycleDetailViewToExcel(RecycleDetailItem recycleDetailItem);

    /**
     * 故障件汇总导出
     *
     * @param recycleSummaryItem
     * @return
     */
    List<RecycleSummaryView> recycleSummaryViewToExcel(RecycleSummaryItem recycleSummaryItem);

    /**
     * 服务站结算明细导出
     *
     * @param settlementMaintainExpenseItem
     * @return
     */
    List<SettlementMaintainExpenseView> settlementMaintainExpenseViewToExcel(SettlementMaintainExpenseItem settlementMaintainExpenseItem);

    /**
     * 合作商结算明细导出
     *
     * @param agencySettlementDetailItem
     * @return
     */
    List<AgencySettlementDetailView> agencySettlementDetailViewToExcel(AgencySettlementDetailItem agencySettlementDetailItem);

    /**
     * 合作商供货明细导出
     *
     * @param agencySupplySummaryItem
     * @return
     */
    List<AgencySupplySummaryView> agencySupplySummaryViewToExcel(AgencySupplySummaryItem agencySupplySummaryItem);


    /**
     * 合作商配件年费导出
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */
    List<AgencySettlementYearSummaryView> agencySettlementYearSummaryViewToExcel(AgencySettlementYearSummaryItem agencySettlementYearSummaryItem);

    /**
     * 合作商运费年费导出
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */
    List<AgencySettlementYearSummaryFreightView> agencySettlementYearSummaryFreightViewToExcel(AgencySettlementYearSummaryItem agencySettlementYearSummaryItem);

    /**
     * 工时明细导出
     *
     * @param workHoursReportItem
     * @return
     */
    List<WorkHoursReportView> workHoursReportToExcel(WorkHoursReportItem workHoursReportItem);

    /**
     * 服务站信息明细导出
     *
     * @param dealerMessageItem
     * @return
     */
    List<DealerMessageView> dealerMessageViewToExcel(DealerMessageItem dealerMessageItem);

    /**
     * 三包配件明细导出
     *
     * @param warrantyPartDetailItem
     * @return
     */
    List<WarrantyPartDetailView> warrantyPartDetailViewToExcel(WarrantyPartDetailItem warrantyPartDetailItem);

    List<DealerExpenseSummaryView> dealerExpenseSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    List<DealerExpenseWarrantySummaryView> dealerExpenseWarrantySummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    List<DealerExpenseActivitySummaryView> dealerExpenseActivitySummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    List<DealerExpenseFirstSummaryView> dealerExpenseFirstSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    List<DealerExpenseFreightSummaryView> dealerExpenseFreightSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    List<DealerExpenseGoOutSummaryView> dealerExpenseGoOutSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    List<DealerExpensePartSummaryView> dealerExpensePartSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    List<DealerExpenseWorkHoursSummaryView> dealerExpenseWorkHoursSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem);

    PageResult<ActivitiNoticeReportView> getActivityNoticeReportViewPageList(PageParam<ActivityNoticeReportItem> pageParam);

    List<ActivitiNoticeReportView> activityNoticeReportToExcel(ActivityNoticeReportItem activityNoticeReportItem);

    byte[] printPage(Map<String, Object> variable);

    byte[] printRecycleLabel(Map<String, String> variable);

    byte[] warrantyPrintRecycleLabel(Map<String, String> variable);
}
