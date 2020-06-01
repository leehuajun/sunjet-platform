package com.sunjet.frontend.service.report;

import com.sunjet.dto.asms.report.*;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 统计报表
 */
@Slf4j
@Service("reportService")
public class ReportService {

    @Autowired
    private RestClient restClient;


    /**
     * 获取服务单明细报表分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<MaintainDetailItem> getMaintainDetailViewPageList(PageParam<MaintainDetailItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getMaintainDetailViewPageList:success");
            return restClient.getPageList("/report/getMaintainDetailViewPageList", pageParam, new ParameterizedTypeReference<PageResult<MaintainDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getMaintainDetailViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 根据条件导出excel
     *
     * @param maintainDetailItem
     * @return
     */

    public List<MaintainDetailItem> maintainDetailToExcel(MaintainDetailItem maintainDetailItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(maintainDetailItem, null);
            log.info("ReportServicelmpl:maintainDetailToExcel:success");
            return restClient.findAll("/report/getMaintainDetailToExcel", requestEntity, new ParameterizedTypeReference<List<MaintainDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:maintainDetailToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取三包配件单分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<WarrantyPartDetailItem> getWarrantyPartDetailViewPageList(PageParam<WarrantyPartDetailItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getWarrantyPartDetailViewPageList:success");
            return restClient.getPageList("/report/getWarrantyPartDetailViewPageList", pageParam, new ParameterizedTypeReference<PageResult<WarrantyPartDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getWarrantyPartDetailViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取合作商结算明细分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<AgencySettlementDetailItem> getAgencySettlementDetailViewPageList(PageParam<AgencySettlementDetailItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getAgencySettlementDetailViewPageList:success");
            return restClient.getPageList("/report/getAgencySettlementDetailViewPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencySettlementDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getAgencySettlementDetailViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取合作商年汇总明细分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<AgencySettlementYearSummaryItem> getAgencySettlementYearSummaryViewPageList(PageParam<AgencySettlementYearSummaryItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getAgencySettlementYearSummaryViewPageList:success");
            return restClient.getPageList("/report/getAgencySettlementYearSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencySettlementYearSummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getAgencySettlementYearSummaryViewPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取合作商运费年汇总明细
     *
     * @param pageParam
     * @return
     */

    public PageResult<AgencySettlementYearSummaryItem> getAgencySettlementYearSummaryFreightViewPageList(PageParam<AgencySettlementYearSummaryItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getAgencySettlementYearSummaryFreightViewPageList:success");
            return restClient.getPageList("/report/getAgencySettlementYearSummaryFreightViewPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencySettlementYearSummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getAgencySettlementYearSummaryFreightViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取合作商供货分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<AgencySupplySummaryItem> getAgencySupplySummaryViewPageList(PageParam<AgencySupplySummaryItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getAgencySupplySummaryViewPageList:success");
            return restClient.getPageList("/report/getAgencySupplySummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencySupplySummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getAgencySupplySummaryViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取质量费用速报分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<QualityExpenseReportDetailItem> getQualityExpenseReportDetailViewPageList(PageParam<QualityExpenseReportDetailItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getQualityExpenseReportDetailViewPageList:success");
            return restClient.getPageList("/report/getQualityExpenseReportDetailViewPageList", pageParam, new ParameterizedTypeReference<PageResult<QualityExpenseReportDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getQualityExpenseReportDetailViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 故障件返回明细分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<RecycleDetailItem> getRecycleDetailViewPageList(PageParam<RecycleDetailItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getRecycleDetailViewPageList:success");
            return restClient.getPageList("/report/getRecycleDetailViewPageList", pageParam, new ParameterizedTypeReference<PageResult<RecycleDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getRecycleDetailViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 故障件返回汇总分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<RecycleSummaryItem> getRecycleSummaryViewPageList(PageParam<RecycleSummaryItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getRecycleSummaryViewPageList:success");
            return restClient.getPageList("/report/getRecycleSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<RecycleSummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getRecycleSummaryViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取工时统计分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<WorkHoursReportItem> getWorkHoursReportViewPageList(PageParam<WorkHoursReportItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getWorkHoursReportViewPageList:success");
            return restClient.getPageList("/report/getWorkHoursReportViewPageList", pageParam, new ParameterizedTypeReference<PageResult<WorkHoursReportItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getWorkHoursReportViewPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取服务站信息分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<DealerMessageItem> getDealerMessageViewPageList(PageParam<DealerMessageItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getDealerMessageViewPageList:success");
            return restClient.getPageList("/report/getDealerMessageViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerMessageItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getDealerMessageViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取三包自购分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<WarrantyPartsSelfPurchaseDetailItem> getWarrantyPartsSelfPurchaseDetailViewPageList(PageParam<WarrantyPartsSelfPurchaseDetailItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getWarrantyPartsSelfPurchaseDetailViewPageList:success");
            return restClient.getPageList("/report/getWarrantyPartsSelfPurchaseDetailViewPageList", pageParam, new ParameterizedTypeReference<PageResult<WarrantyPartsSelfPurchaseDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getWarrantyPartsSelfPurchaseDetailViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取服务站结算分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<SettlementMaintainExpenseItem> getSettlementMaintainExpenseViewPageList(PageParam<SettlementMaintainExpenseItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getSettlementMaintainExpenseViewPageList:success");
            return restClient.getPageList("/report/getSettlementMaintainExpenseViewPageList", pageParam, new ParameterizedTypeReference<PageResult<SettlementMaintainExpenseItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getSettlementMaintainExpenseViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 服务站费用总计
     *
     * @param pageParam
     * @return
     */

    public PageResult<DealerExpenseSummaryItem> getDealerExpenseSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        try {
            DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
            if ("三包费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:getDealerExpenseWarrantySummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpenseWarrantySummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            } else if ("活动费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:getDealerExpenseFirstSummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpenseFirstSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            } else if ("故障件运费".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:getDealerExpenseFreightSummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpenseFreightSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            } else if ("首保费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:getDealerExpenseFirstSummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpenseFirstSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            } else if ("外出费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:getDealerExpenseGoOutSummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpenseGoOutSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            } else if ("配件费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:getDealerExpensePartSummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpensePartSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            } else if ("工时费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:getDealerExpenseWorkHoursSummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpenseWorkHoursSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            } else {
                log.info("ReportServicelmpl:getDealerExpenseSummaryViewPageList:success");
                return restClient.getPageList("/report/getDealerExpenseSummaryViewPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerExpenseSummaryItem>>() {
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getDealerExpenseSummaryViewPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 三包自购明细导出
     *
     * @param warrantyPartsSelfPurchaseDetailItem
     * @return
     */

    public List<WarrantyPartsSelfPurchaseDetailItem> getWarrantyPartsSelfPurchaseDetailToExcel(WarrantyPartsSelfPurchaseDetailItem warrantyPartsSelfPurchaseDetailItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(warrantyPartsSelfPurchaseDetailItem, null);
            log.info("ReportServicelmpl:getWarrantyPartsSelfPurchaseDetailToExcel:success");
            return restClient.findAll("/report/getWarrantyPartsSelfPurchaseDetailToExcel", requestEntity, new ParameterizedTypeReference<List<WarrantyPartsSelfPurchaseDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getWarrantyPartsSelfPurchaseDetailToExcel:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * @param qualityExpenseReportDetailItem
     * @return
     */

    public List<QualityExpenseReportDetailItem> qualityExpenseReportDetailViewToExcel(QualityExpenseReportDetailItem qualityExpenseReportDetailItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(qualityExpenseReportDetailItem, null);
            log.info("ReportServicelmpl:qualityExpenseReportDetailViewToExcel:success");
            return restClient.findAll("/report/qualityExpenseReportDetailViewToExcel", requestEntity, new ParameterizedTypeReference<List<QualityExpenseReportDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:qualityExpenseReportDetailViewToExcel:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 故障件明细导出
     *
     * @param recycleDetailItem
     * @return
     */

    public List<RecycleDetailItem> recycleDetailViewToExcel(RecycleDetailItem recycleDetailItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(recycleDetailItem, null);
            log.info("ReportServicelmpl:recycleDetailViewToExcel:success");
            return restClient.findAll("/report/recycleDetailViewToExcel", requestEntity, new ParameterizedTypeReference<List<RecycleDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:recycleDetailViewToExcel:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 故障件汇总导出
     *
     * @param recycleSummaryItem
     * @return
     */

    public List<RecycleSummaryItem> recycleSummaryViewToExcel(RecycleSummaryItem recycleSummaryItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(recycleSummaryItem, null);
            log.info("ReportServicelmpl:recycleSummaryViewToExcel:success");
            return restClient.findAll("/report/recycleSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<RecycleSummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:recycleDetailViewToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 服务站结算导出
     *
     * @param settlementMaintainExpenseItem
     * @return
     */

    public List<SettlementMaintainExpenseItem> settlementMaintainExpenseViewToExcel(SettlementMaintainExpenseItem settlementMaintainExpenseItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(settlementMaintainExpenseItem, null);
            log.info("ReportServicelmpl:settlementMaintainExpenseViewToExcel:success");
            return restClient.findAll("/report/settlementMaintainExpenseViewToExcel", requestEntity, new ParameterizedTypeReference<List<SettlementMaintainExpenseItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:settlementMaintainExpenseViewToExcel:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 合作商结算明细导出
     *
     * @param agencySettlementDetailItem
     * @return
     */

    public List<AgencySettlementDetailItem> agencySettlementDetailViewToExcel(AgencySettlementDetailItem agencySettlementDetailItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(agencySettlementDetailItem, null);
            log.info("ReportServicelmpl:agencySettlementDetailViewToExcel:success");
            return restClient.findAll("/report/agencySettlementDetailViewToExcel", requestEntity, new ParameterizedTypeReference<List<AgencySettlementDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:agencySettlementDetailViewToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 合作商供货明细导出
     *
     * @param agencySupplySummaryItem
     * @return
     */

    public List<AgencySupplySummaryItem> agencySupplySummaryViewToExcel(AgencySupplySummaryItem agencySupplySummaryItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(agencySupplySummaryItem, null);
            log.info("ReportServicelmpl:agencySupplySummaryViewToExcel:success");
            return restClient.findAll("/report/agencySupplySummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<AgencySupplySummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:agencySupplySummaryViewToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 合作商配件年费汇总导出
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */

    public List<AgencySettlementYearSummaryItem> agencySettlementYearSummaryViewToExcel(AgencySettlementYearSummaryItem agencySettlementYearSummaryItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(agencySettlementYearSummaryItem, null);
            log.info("ReportServicelmpl:agencySettlementYearSummaryViewToExcel:success");
            return restClient.findAll("/report/agencySettlementYearSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<AgencySettlementYearSummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:agencySettlementYearSummaryViewToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 合作商配件年费汇总导出
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */

    public List<AgencySettlementYearSummaryItem> agencySettlementYearSummaryFreightViewToExcel(AgencySettlementYearSummaryItem agencySettlementYearSummaryItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(agencySettlementYearSummaryItem, null);
            log.info("ReportServicelmpl:agencySettlementYearSummaryFreightViewToExcel:success");
            return restClient.findAll("/report/agencySettlementYearSummaryFreightViewToExcel", requestEntity, new ParameterizedTypeReference<List<AgencySettlementYearSummaryItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:agencySettlementYearSummaryFreightViewToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 工时费用导出
     *
     * @param workHoursReportItem
     * @return
     */

    public List<WorkHoursReportItem> workHoursReportToExcel(WorkHoursReportItem workHoursReportItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(workHoursReportItem, null);
            log.info("ReportServicelmpl:workHoursReportToExcel:success");
            return restClient.findAll("/report/workHoursReportToExcel", requestEntity, new ParameterizedTypeReference<List<WorkHoursReportItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:workHoursReportToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 服务站信息导出
     *
     * @param dealerMessageItem
     * @return
     */

    public List<DealerMessageItem> dealerMessageViewToExcel(DealerMessageItem dealerMessageItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(dealerMessageItem, null);
            log.info("ReportServicelmpl:dealerMessageViewToExcel:success");
            return restClient.findAll("/report/dealerMessageViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerMessageItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:dealerMessageViewToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 三包配件明细导出
     *
     * @param warrantyPartDetailItem
     * @return
     */

    public List<WarrantyPartDetailItem> warrantyPartDetailViewToExcel(WarrantyPartDetailItem warrantyPartDetailItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(warrantyPartDetailItem, null);
            log.info("ReportServicelmpl:warrantyPartDetailViewToExcel:success");
            return restClient.findAll("/report/warrantyPartDetailViewToExcel", requestEntity, new ParameterizedTypeReference<List<WarrantyPartDetailItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:warrantyPartDetailViewToExcel:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 服务站年费明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */

    public List<DealerExpenseSummaryItem> DealerExpenseSummaryItemToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(dealerExpenseSummaryItem, null);

            if ("三包费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:dealerExpenseWarrantySummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpenseWarrantySummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            } else if ("活动费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:dealerExpenseActivitySummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpenseActivitySummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            } else if ("首保费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:dealerExpenseFirstSummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpenseFirstSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            } else if ("故障件运费".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:dealerExpenseFreightSummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpenseFreightSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            } else if ("外出费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:dealerExpenseGoOutSummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpenseGoOutSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            } else if ("配件费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:dealerExpensePartSummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpensePartSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            } else if ("工时费用".equals(dealerExpenseSummaryItem.getType())) {
                log.info("ReportServicelmpl:dealerExpenseWorkHoursSummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpenseWorkHoursSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            } else {
                log.info("ReportServicelmpl:dealerExpenseSummaryViewToExcel:success");
                return restClient.findAll("/report/dealerExpenseSummaryViewToExcel", requestEntity, new ParameterizedTypeReference<List<DealerExpenseSummaryItem>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:warrantyPartDetailViewToExcel:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 调拨通知单
     *
     * @param pageParam
     * @return
     */

    public PageResult<ActivityNoticeReportItem> getActivityNoticeReportViewPageList(PageParam<ActivityNoticeReportItem> pageParam) {
        try {
            log.info("ReportServicelmpl:getActivityNoticeReportViewPageList:success");
            return restClient.getPageList("/report/getActivityNoticeReportViewPageList", pageParam, new ParameterizedTypeReference<PageResult<ActivityNoticeReportItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:getActivityNoticeReportViewPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 活动通知单报表导出
     *
     * @param activityNoticeReportItem
     * @return
     */

    public List<ActivityNoticeReportItem> activityNoticeReportToExcel(ActivityNoticeReportItem activityNoticeReportItem) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityNoticeReportItem, null);
            log.info("ReportServicelmpl:activityNoticeReportToExcel:success");
            return restClient.findAll("/report/activityNoticeReportToExcel", requestEntity, new ParameterizedTypeReference<List<ActivityNoticeReportItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportServicelmpl:activityNoticeReportToExcel:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 打印页面
     *
     * @param map
     * @return
     */

    public byte[] printReport(Map<String, Object> map) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/report/printPage", requestEntity, byte[].class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
