package com.sunjet.backend.modules.asms.service.report;

import com.sunjet.backend.modules.asms.entity.report.*;
import com.sunjet.backend.modules.asms.repository.report.*;
import com.sunjet.backend.system.Jpa.Specifications;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.backend.utils.common.ReportUtils;
import com.sunjet.dto.asms.report.*;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

/**
 * Created by SUNJET_WS on 2017/8/17.
 */
@Transactional
@Service("reportService")
public class ReportServicelmpl implements ReportService {

    @Autowired
    private MaintainDetailViewRepository maintainDetailViewRepository;    //服务单明细

    @Autowired
    private WarrantyPartDetailViewRepository warrantyPartDetailViewRepository;   //三包配件明细

    @Autowired
    private AgencySettlementDetailViewRepository agencySettlementDetailViewRepository;   // 合作商结算明细

    @Autowired
    private AgencySettlementYearSummaryViewRepository agencySettlementYearSummaryViewRepository;  //合作商年度汇总

    @Autowired
    private AgencySettlementYearSummaryFreightViewRepository agencySettlementYearSummaryFreightViewRepository;  //合作商年度运费汇总 @Autowired

    @Autowired
    private AgencySupplySummaryViewRepository agencySupplySummaryViewRepository;  //合作商供货明细

    @Autowired
    private QualityExpenseReportDetailViewRepository qualityExpenseReportDetailViewRepository;  //质量费用速报视图@Autowired

    @Autowired
    private RecycleDetailViewRepository recycleDetailViewRepository;  //故障件返回视图

    @Autowired
    private RecycleSummaryViewRepository recycleSummaryViewRepository; //故障件汇总
    @Autowired
    private WorkHoursReportViewRepository workHoursReportViewRepository; //工时统计
    @Autowired
    private DealerMessageViewRepository dealerMessageViewRepository;   // 服务站信息

    @Autowired
    private WarrantyPartsSelfPurchaseDetailViewRepository warrantyPartsSelfPurchaseDetailViewRepository;  //三包自购明细

    @Autowired
    private SettlementMaintainExpenseViewRepository settlementMaintainExpenseViewRepository;    //服务站结算总计

    @Autowired
    private DealerExpenseSummaryViewRepository dealerExpenseSummaryViewRepository; // 服务站费用总计

    @Autowired
    private DealerExpenseWarrantySummaryViewRepository dealerExpenseWarrantySummaryViewRepository; // 服务站三包费用总计

    @Autowired
    private DealerExpenseActivitySummaryViewRepository dealerExpenseActivitySummaryViewRepository; // 服务站活动费用总计

    @Autowired
    private DealerExpenseFirstSummaryViewRepository dealerExpenseFirstSummaryViewRepository;//服务站首保费用

    @Autowired
    private DealerExpenseFreightSummaryViewRepository dealerExpenseFreightSummaryViewRepository;// 服务站故障件返回费用

    @Autowired
    private DealerExpenseGoOutSummaryViewRepository dealerExpenseGoOutSummaryViewRepository; //服务站外出费用

    @Autowired
    private DealerExpensePartSummaryViewRepository dealerExpensePartSummaryViewRepository; //服务站配件费用

    @Autowired
    private DealerExpenseWorkHoursSummaryViewRepository dealerExpenseWorkHoursSummaryViewRepository;// 服务站工时费用
    @Autowired
    private ActivitiNoticeReportViewRepository activitiNoticeReportViewRepository; //活动通知单报表

    @Autowired
    private RecycleLabelViewRepository recycleLabelViewRepository;  // 故障件打印标签

    @Autowired
    private WarrantyRecycleLabelViewRepository WarrantyRecycleLabelViewRepository;


    @Value("${spring.datasource.url}")
    private String dataSourceUrl;//数据库链接
    @Value("${spring.datasource.username}")
    private String userName;  //用户名
    @Value("${spring.datasource.password}")
    private String password;  //用户密码
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;  // 驱动


    /**
     * 查找服务明细
     *
     * @return
     */
    @Override
    public List<MaintainDetailView> maintainDetailToExcel(MaintainDetailItem maintainDetailItem) {

        Specification<MaintainDetailView> specification = Specifications.<MaintainDetailView>and()
                .like(StringUtils.isNotBlank(maintainDetailItem.getDealer_name()), "dealer_name", "%" + maintainDetailItem.getDealer_name() + "%")
                .like(StringUtils.isNotBlank(maintainDetailItem.getDealer_code()), "dealer_code", "%" + maintainDetailItem.getDealer_code() + "%")
                .like(StringUtils.isNotBlank(maintainDetailItem.getService_manager()), "service_manager", "%" + maintainDetailItem.getService_manager() + "%")//服务经理
                .eq(StringUtils.isNotBlank(maintainDetailItem.getDoc_type()), "doc_type", maintainDetailItem.getDoc_type())
                .between("createdTime", new Range<Date>(maintainDetailItem.getStart_date(), maintainDetailItem.getEnd_date()))
                .build();

        return maintainDetailViewRepository.findAll(specification);


    }

    /**
     * 查询服务单明细分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<MaintainDetailView> getMaintainDetailViewPageList(PageParam<MaintainDetailItem> pageParam) {
        //1.查询条件
        MaintainDetailItem maintainDetailItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<MaintainDetailView> specification = null;
        if (maintainDetailItem != null) {
            specification = Specifications.<MaintainDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(maintainDetailItem.getDealer_name()), "dealer_name", "%" + maintainDetailItem.getDealer_name() + "%")
                    .like(StringUtils.isNotBlank(maintainDetailItem.getDealer_code()), "dealer_code", "%" + maintainDetailItem.getDealer_code() + "%")
                    .like(StringUtils.isNotBlank(maintainDetailItem.getService_manager()), "service_manager", "%" + maintainDetailItem.getService_manager() + "%")//服务经理
                    .eq(StringUtils.isNotBlank(maintainDetailItem.getDoc_type()), "doc_type", maintainDetailItem.getDoc_type())
                    .between("createdTime", new Range<Date>(maintainDetailItem.getStart_date(), maintainDetailItem.getEnd_date()))
                    .build();

        }


        //3.执行查询
        Page<MaintainDetailView> pages = maintainDetailViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 三包配件列表分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<WarrantyPartDetailView> getWarrantyPartDetailViewPageList(PageParam<WarrantyPartDetailItem> pageParam) {
        //1.查询条件
        WarrantyPartDetailItem warrantyPartDetailItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<WarrantyPartDetailView> specification = null;
        if (warrantyPartDetailItem != null) {
            specification = Specifications.<WarrantyPartDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getDealerName()), "dealerName", "%" + warrantyPartDetailItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getDealerName()), "dealerCode", "%" + warrantyPartDetailItem.getDealerCode() + "%")
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getServiceManager()), "serviceManager", "%" + warrantyPartDetailItem.getServiceManager() + "%")//服务经理
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getPartCode()), "partCode", "%" + warrantyPartDetailItem.getPartCode() + "%")
                    .between("asnCreatedTime", new Range<Date>(warrantyPartDetailItem.getStartDate(), warrantyPartDetailItem.getEndDate()))
                    .build();

        }


        //3.执行查询
        Page<WarrantyPartDetailView> pages = warrantyPartDetailViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<AgencySettlementDetailView> getAgencySettlementDetailViewPageList(PageParam<AgencySettlementDetailItem> pageParam) {
        //1.查询条件
        AgencySettlementDetailItem agencySettlementDetailItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencySettlementDetailView> specification = null;
        if (agencySettlementDetailItem != null) {
            specification = Specifications.<AgencySettlementDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(agencySettlementDetailItem.getAgencyName()), "agencyName", "%" + agencySettlementDetailItem.getAgencyCode() + "%")
                    .ne(StringUtils.isNotBlank(agencySettlementDetailItem.getStatus()), "status", agencySettlementDetailItem.getStatus())
                    .between("createdTime", new Range<Date>(agencySettlementDetailItem.getStartDate(), agencySettlementDetailItem.getEndDate()))
                    .build();

        }


        //3.执行查询
        Page<AgencySettlementDetailView> pages = agencySettlementDetailViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 获取合作商结算年度汇总分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<AgencySettlementYearSummaryView> getAgencySettlementYearSummaryViewPageList(PageParam<AgencySettlementYearSummaryItem> pageParam) {
        //1.查询条件
        AgencySettlementYearSummaryItem agencySettlementYearSummaryItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencySettlementYearSummaryView> specification = null;
        if (agencySettlementYearSummaryItem != null) {
            specification = Specifications.<AgencySettlementYearSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(agencySettlementYearSummaryItem.getStartDate(), agencySettlementYearSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(agencySettlementYearSummaryItem.getAgencyName()), "agencyName", "%" + agencySettlementYearSummaryItem.getAgencyName() + "%")
                    .build();

        }
        //3.执行查询
        Page<AgencySettlementYearSummaryView> pages = agencySettlementYearSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 获取合作商结算年度汇总分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<AgencySettlementYearSummaryFreightView> getAgencySettlementYearSummaryFreightViewPageList(PageParam<AgencySettlementYearSummaryItem> pageParam) {
        //1.查询条件
        AgencySettlementYearSummaryItem agencySettlementYearSummaryItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencySettlementYearSummaryFreightView> specification = null;
        if (agencySettlementYearSummaryItem != null) {
            specification = Specifications.<AgencySettlementYearSummaryFreightView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(agencySettlementYearSummaryItem.getStartDate(), agencySettlementYearSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(agencySettlementYearSummaryItem.getAgencyName()), "dealer_name", "%" + agencySettlementYearSummaryItem.getAgencyName() + "%")
                    .build();

        }

        //3.执行查询
        Page<AgencySettlementYearSummaryFreightView> pages = agencySettlementYearSummaryFreightViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }


    /**
     * 获取合作商供货明细分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<AgencySupplySummaryView> getAgencySupplySummaryViewPageList(PageParam<AgencySupplySummaryItem> pageParam) {
        //1.查询条件
        AgencySupplySummaryItem agencySupplySummaryItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencySupplySummaryView> specification = null;
        if (agencySupplySummaryItem != null) {
            specification = Specifications.<AgencySupplySummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(agencySupplySummaryItem.getAgencyName()), "agencyName", "%" + agencySupplySummaryItem.getAgencyName() + "%")
                    .between("createdTime", new Range<Date>(agencySupplySummaryItem.getStartDate(), agencySupplySummaryItem.getEndDate()))
                    .build();

        }
        //3.执行查询
        Page<AgencySupplySummaryView> pages = agencySupplySummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 获取质量费用速报明细分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<QualityExpenseReportDetailView> getQualityExpenseReportDetailViewPageList(PageParam<QualityExpenseReportDetailItem> pageParam) {
        //1.查询条件
        QualityExpenseReportDetailItem qualityExpenseReportDetailItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<QualityExpenseReportDetailView> specification = null;
        if (qualityExpenseReportDetailItem != null) {
            specification = Specifications.<QualityExpenseReportDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getDealerName()), "dealerName", "%" + qualityExpenseReportDetailItem.getDealerName() + "%")
                    .eq(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getCostType()), "costType", qualityExpenseReportDetailItem.getCostType())
                    .eq(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getReportType()), "reportType", qualityExpenseReportDetailItem.getReportType())
                    .eq(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getType()), "type", qualityExpenseReportDetailItem.getType())
                    .between("createdTime", new Range<Date>(qualityExpenseReportDetailItem.getStartDate(), qualityExpenseReportDetailItem.getEndDate()))
                    .build();

        }
        //3.执行查询
        Page<QualityExpenseReportDetailView> pages = qualityExpenseReportDetailViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 获取故障件返回明细分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<RecycleDetailView> getRecycleDetailViewPageList(PageParam<RecycleDetailItem> pageParam) {
        //1.查询条件
        RecycleDetailItem recycleDetailItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RecycleDetailView> specification = null;
        if (recycleDetailItem != null) {
            specification = Specifications.<RecycleDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(recycleDetailItem.getDealerName()), "dealerName", "%" + recycleDetailItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(recycleDetailItem.getPartCode()), "partCode", "%" + recycleDetailItem.getPartCode() + "%")
                    .like(StringUtils.isNotBlank(recycleDetailItem.getPartName()), "partName", "%" + recycleDetailItem.getPartName() + "%")
                    .between("createdTime", new Range<Date>(recycleDetailItem.getStartDate(), recycleDetailItem.getEndDate()))
                    .build();

        }
        //3.执行查询
        Page<RecycleDetailView> pages = recycleDetailViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 获取故障件汇总分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<RecycleSummaryView> getRecycleSummaryViewPageList(PageParam<RecycleSummaryItem> pageParam) {
        //1.查询条件
        RecycleSummaryItem recycleSummaryItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RecycleSummaryView> specification = null;
        if (recycleSummaryItem != null) {
            specification = Specifications.<RecycleSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(recycleSummaryItem.getDealerName()), "dealer_name", "%" + recycleSummaryItem.getDealerName() + "%")
                    .between("createdTime", new Range<Date>(recycleSummaryItem.getStartDate(), recycleSummaryItem.getEndDate()))
                    .build();

        }
        //3.执行查询
        Page<RecycleSummaryView> pages = recycleSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 工时统计分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<WorkHoursReportView> getWorkHoursReportViewPageList(PageParam<WorkHoursReportItem> pageParam) {
        //1.查询条件
        WorkHoursReportItem workHoursReportItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<WorkHoursReportView> specification = null;
        if (workHoursReportItem != null) {
            specification = Specifications.<WorkHoursReportView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(workHoursReportItem.getDealerName()), "dealerName", "%" + workHoursReportItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(workHoursReportItem.getDealerName()), "dealerCode", "%" + workHoursReportItem.getDealerCode() + "%")
                    .between("createdTime", new Range<Date>(workHoursReportItem.getStartDate(), workHoursReportItem.getEndDate()))
                    .build();

        }
        //3.执行查询
        Page<WorkHoursReportView> pages = workHoursReportViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 获取服务站信息分页信息
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerMessageView> getDealerMessageViewPageList(PageParam<DealerMessageItem> pageParam) {
        //1.查询条件
        DealerMessageItem dealerMessageItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DealerMessageView> specification = null;
        if (dealerMessageItem != null) {
            specification = Specifications.<DealerMessageView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(dealerMessageItem.getName()), "name", "%" + dealerMessageItem.getName() + "%")
                    .like(StringUtils.isNotBlank(dealerMessageItem.getCode()), "code", "%" + dealerMessageItem.getCode() + "%")
                    .eq(StringUtils.isNotBlank(dealerMessageItem.getStar()), "star", dealerMessageItem.getStar())
                    .eq(StringUtils.isNotBlank(dealerMessageItem.getLevel()), "level", dealerMessageItem.getLevel())
                    .eq(StringUtils.isNotBlank(dealerMessageItem.getProvinceId()), "provinceId", dealerMessageItem.getProvinceId())
                    .eq(StringUtils.isNotBlank(dealerMessageItem.getCityId()), "cityId", dealerMessageItem.getCityId())
                    .build();

        }
        //3.执行查询
        Page<DealerMessageView> pages = dealerMessageViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 三包自购明细
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<WarrantyPartsSelfPurchaseDetailView> getWarrantyPartsSelfPurchaseDetailViewPageList(PageParam<WarrantyPartsSelfPurchaseDetailItem> pageParam) {
        //1.查询条件
        WarrantyPartsSelfPurchaseDetailItem warrantyPartsSelfPurchaseDetailItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<WarrantyPartsSelfPurchaseDetailView> specification = null;
        if (warrantyPartsSelfPurchaseDetailItem != null) {
            specification = Specifications.<WarrantyPartsSelfPurchaseDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(warrantyPartsSelfPurchaseDetailItem.getDocNo()), "docNo", warrantyPartsSelfPurchaseDetailItem.getDocNo())
                    .like(StringUtils.isNotBlank(warrantyPartsSelfPurchaseDetailItem.getDealerName()), "dealerName", "%" + warrantyPartsSelfPurchaseDetailItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(warrantyPartsSelfPurchaseDetailItem.getDealerName()), "dealerCode", "%" + warrantyPartsSelfPurchaseDetailItem.getDealerCode() + "%")
                    .between("createdTime", new Range<Date>(warrantyPartsSelfPurchaseDetailItem.getStartDate(), warrantyPartsSelfPurchaseDetailItem.getEndDate()))
                    .build();

        }
        //3.执行查询
        Page<WarrantyPartsSelfPurchaseDetailView> pages = warrantyPartsSelfPurchaseDetailViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 服务站结算费用总计分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<SettlementMaintainExpenseView> getSettlementMaintainExpenseViewPageList(PageParam<SettlementMaintainExpenseItem> pageParam) {
        //1.查询条件
        SettlementMaintainExpenseItem settlementMaintainExpenseItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SettlementMaintainExpenseView> specification = null;
        if (settlementMaintainExpenseItem != null) {
            specification = Specifications.<SettlementMaintainExpenseView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(settlementMaintainExpenseItem.getDealerName()), "dealerName", "%" + settlementMaintainExpenseItem.getDealerName() + "%")
                    .between("createdTime", new Range<Date>(settlementMaintainExpenseItem.getStartDate(), settlementMaintainExpenseItem.getEndDate()))
                    .build();

        }
        //3.执行查询
        Page<SettlementMaintainExpenseView> pages = settlementMaintainExpenseViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 服务站年度总费用分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerExpenseSummaryView> getDealerExpenseSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpenseSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .build();
        }
        //3.执行查询
        Page<DealerExpenseSummaryView> pages = dealerExpenseSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 服务站年度三包费用用分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerExpenseWarrantySummaryView> getDealerExpenseWarrantySummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpenseWarrantySummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseWarrantySummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        //3.执行查询
        Page<DealerExpenseWarrantySummaryView> pages = dealerExpenseWarrantySummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 服务站活动费用
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerExpenseActivitySummaryView> getDealerExpenseActivitySummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpenseActivitySummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseActivitySummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        //3.执行查询
        Page<DealerExpenseActivitySummaryView> pages = dealerExpenseActivitySummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 服务站首保费用
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerExpenseFirstSummaryView> getDealerExpenseFirstSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpenseFirstSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseFirstSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .build();
        }
        //3.执行查询
        Page<DealerExpenseFirstSummaryView> pages = dealerExpenseFirstSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 服务站故障件返回运费
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerExpenseFreightSummaryView> getDealerExpenseFreightSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpenseFreightSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseFreightSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        //3.执行查询
        Page<DealerExpenseFreightSummaryView> pages = dealerExpenseFreightSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }


    /**
     * 服务站外出费用
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerExpenseGoOutSummaryView> getDealerExpenseGoOutSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpenseGoOutSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseGoOutSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        //3.执行查询
        Page<DealerExpenseGoOutSummaryView> pages = dealerExpenseGoOutSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }


    /**
     * 服务站年度工时费用
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerExpenseWorkHoursSummaryView> getDealerExpenseWorkHoursSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpenseWorkHoursSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseWorkHoursSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        //3.执行查询
        Page<DealerExpenseWorkHoursSummaryView> pages = dealerExpenseWorkHoursSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 服务站年度配件费用
     */
    @Override
    public PageResult<DealerExpensePartSummaryView> getDealerExpensePartSummaryViewPageList(PageParam<DealerExpenseSummaryItem> pageParam) {
        //1.查询条件
        DealerExpenseSummaryItem dealerExpenseSummaryItem = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<DealerExpensePartSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpensePartSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        //3.执行查询
        Page<DealerExpensePartSummaryView> pages = dealerExpensePartSummaryViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 三包自购明细导出
     *
     * @param warrantyPartsSelfPurchaseDetailItem
     * @return
     */
    @Override
    public List<WarrantyPartsSelfPurchaseDetailView> warrantyPartsSelfPurchaseDetailViewToExcel(WarrantyPartsSelfPurchaseDetailItem warrantyPartsSelfPurchaseDetailItem) {
        Specification<WarrantyPartsSelfPurchaseDetailView> specification = null;
        if (warrantyPartsSelfPurchaseDetailItem != null) {
            specification = Specifications.<WarrantyPartsSelfPurchaseDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(warrantyPartsSelfPurchaseDetailItem.getDocNo()), "docNo", warrantyPartsSelfPurchaseDetailItem.getDocNo())
                    .like(StringUtils.isNotBlank(warrantyPartsSelfPurchaseDetailItem.getDealerName()), "dealerName", "%" + warrantyPartsSelfPurchaseDetailItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(warrantyPartsSelfPurchaseDetailItem.getDealerName()), "dealerCode", "%" + warrantyPartsSelfPurchaseDetailItem.getDealerCode() + "%")
                    .between("createdTime", new Range<Date>(warrantyPartsSelfPurchaseDetailItem.getStartDate(), warrantyPartsSelfPurchaseDetailItem.getEndDate()))
                    .build();

        }

        return warrantyPartsSelfPurchaseDetailViewRepository.findAll(specification);
    }


    /**
     * 质量费用速报导出
     *
     * @param qualityExpenseReportDetailItem
     * @return
     */
    @Override
    public List<QualityExpenseReportDetailView> qualityExpenseReportDetailViewToExcel(QualityExpenseReportDetailItem qualityExpenseReportDetailItem) {
        Specification<QualityExpenseReportDetailView> specification = null;
        if (qualityExpenseReportDetailItem != null) {
            specification = Specifications.<QualityExpenseReportDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getDealerName()), "dealerName", "%" + qualityExpenseReportDetailItem.getDealerName() + "%")
                    .eq(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getCostType()), "costType", qualityExpenseReportDetailItem.getCostType())
                    .eq(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getReportType()), "reportType", qualityExpenseReportDetailItem.getReportType())
                    .eq(StringUtils.isNotBlank(qualityExpenseReportDetailItem.getType()), "type", qualityExpenseReportDetailItem.getType())
                    .between("createdTime", new Range<Date>(qualityExpenseReportDetailItem.getStartDate(), qualityExpenseReportDetailItem.getEndDate()))
                    .build();

        }

        return qualityExpenseReportDetailViewRepository.findAll(specification);
    }


    /**
     * 故障件返回单导出
     *
     * @param recycleDetailItem
     * @return
     */
    @Override
    public List<RecycleDetailView> recycleDetailViewToExcel(RecycleDetailItem recycleDetailItem) {
        Specification<RecycleDetailView> specification = null;
        if (recycleDetailItem != null) {
            specification = Specifications.<RecycleDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(recycleDetailItem.getDealerName()), "dealer_name", "%" + recycleDetailItem.getDealerName() + "%")
                    .between("createdTime", new Range<Date>(recycleDetailItem.getStartDate(), recycleDetailItem.getEndDate()))
                    .build();

        }
        return recycleDetailViewRepository.findAll(specification);
    }

    /**
     * 故障件汇总导出
     *
     * @param recycleSummaryItem
     * @return
     */
    @Override
    public List<RecycleSummaryView> recycleSummaryViewToExcel(RecycleSummaryItem recycleSummaryItem) {
        Specification<RecycleSummaryView> specification = null;
        if (recycleSummaryItem != null) {
            specification = Specifications.<RecycleSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(recycleSummaryItem.getDealerName()), "dealer_name", "%" + recycleSummaryItem.getDealerName() + "%")
                    .between("createdTime", new Range<Date>(recycleSummaryItem.getStartDate(), recycleSummaryItem.getEndDate()))
                    .build();

        }
        return recycleSummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站结算导出
     *
     * @param settlementMaintainExpenseItem
     * @return
     */
    @Override
    public List<SettlementMaintainExpenseView> settlementMaintainExpenseViewToExcel(SettlementMaintainExpenseItem settlementMaintainExpenseItem) {
        Specification<SettlementMaintainExpenseView> specification = null;
        if (settlementMaintainExpenseItem != null) {
            specification = Specifications.<SettlementMaintainExpenseView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(settlementMaintainExpenseItem.getDealerName()), "dealerName", "%" + settlementMaintainExpenseItem.getDealerName() + "%")
                    .between("createdTime", new Range<Date>(settlementMaintainExpenseItem.getStartDate(), settlementMaintainExpenseItem.getEndDate()))
                    .build();

        }
        return settlementMaintainExpenseViewRepository.findAll(specification);
    }


    /**
     * 合作商结算导出
     *
     * @param agencySettlementDetailItem
     * @return
     */
    @Override
    public List<AgencySettlementDetailView> agencySettlementDetailViewToExcel(AgencySettlementDetailItem agencySettlementDetailItem) {
        Specification<AgencySettlementDetailView> specification = null;
        if (agencySettlementDetailItem != null) {
            specification = Specifications.<AgencySettlementDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(agencySettlementDetailItem.getAgencyName()), "agencyName", "%" + agencySettlementDetailItem.getAgencyName() + "%")
                    .between("createdTime", new Range<Date>(agencySettlementDetailItem.getStartDate(), agencySettlementDetailItem.getEndDate()))
                    .build();

        }
        return agencySettlementDetailViewRepository.findAll(specification);
    }


    /**
     * 合作商供货汇总
     *
     * @param agencySupplySummaryItem
     * @return
     */
    @Override
    public List<AgencySupplySummaryView> agencySupplySummaryViewToExcel(AgencySupplySummaryItem agencySupplySummaryItem) {
        Specification<AgencySupplySummaryView> specification = null;
        if (agencySupplySummaryItem != null) {
            specification = Specifications.<AgencySupplySummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(agencySupplySummaryItem.getAgencyName()), "agencyName", "%" + agencySupplySummaryItem.getAgencyName() + "%")
                    .between("createdTime", new Range<Date>(agencySupplySummaryItem.getStartDate(), agencySupplySummaryItem.getEndDate()))
                    .build();

        }
        return agencySupplySummaryViewRepository.findAll(specification);
    }


    /**
     * 合作商配件年费汇总导出
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */
    @Override
    public List<AgencySettlementYearSummaryView> agencySettlementYearSummaryViewToExcel(AgencySettlementYearSummaryItem agencySettlementYearSummaryItem) {
        Specification<AgencySettlementYearSummaryView> specification = null;
        if (agencySettlementYearSummaryItem != null) {
            specification = Specifications.<AgencySettlementYearSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(agencySettlementYearSummaryItem.getStartDate(), agencySettlementYearSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(agencySettlementYearSummaryItem.getAgencyName()), "agencyName", "%" + agencySettlementYearSummaryItem.getAgencyName() + "%")
                    .build();
        }
        return agencySettlementYearSummaryViewRepository.findAll(specification);
    }


    /**
     * 合作商运费年费汇总导出
     *
     * @param agencySettlementYearSummaryItem
     * @return
     */
    @Override
    public List<AgencySettlementYearSummaryFreightView> agencySettlementYearSummaryFreightViewToExcel(AgencySettlementYearSummaryItem agencySettlementYearSummaryItem) {
        Specification<AgencySettlementYearSummaryFreightView> specification = null;
        if (agencySettlementYearSummaryItem != null) {
            specification = Specifications.<AgencySettlementYearSummaryFreightView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(agencySettlementYearSummaryItem.getStartDate(), agencySettlementYearSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(agencySettlementYearSummaryItem.getAgencyName()), "agencyName", "%" + agencySettlementYearSummaryItem.getAgencyName() + "%")
                    .build();
        }
        return agencySettlementYearSummaryFreightViewRepository.findAll(specification);
    }


    /**
     * 工时明细导出
     *
     * @param workHoursReportItem
     * @return
     */
    @Override
    public List<WorkHoursReportView> workHoursReportToExcel(WorkHoursReportItem workHoursReportItem) {
        Specification<WorkHoursReportView> specification = null;
        if (workHoursReportItem != null) {
            specification = Specifications.<WorkHoursReportView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(workHoursReportItem.getDealerName()), "dealerName", "%" + workHoursReportItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(workHoursReportItem.getDealerName()), "dealerCode", "%" + workHoursReportItem.getDealerCode() + "%")
                    .eq(StringUtils.isNotBlank(workHoursReportItem.getNightExpense()), "nightWork", workHoursReportItem.getNightExpense())
                    .between("createdTime", new Range<Date>(workHoursReportItem.getStartDate(), workHoursReportItem.getEndDate()))
                    .build();
        }
        return workHoursReportViewRepository.findAll(specification);
    }


    /**
     * 服务站信息导出
     *
     * @param dealerMessageItem
     * @return
     */
    @Override
    public List<DealerMessageView> dealerMessageViewToExcel(DealerMessageItem dealerMessageItem) {
        Specification<DealerMessageView> specification = null;
        if (dealerMessageItem != null) {
            specification = Specifications.<DealerMessageView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(dealerMessageItem.getName()), "name", "%" + dealerMessageItem.getName() + "%")
                    .build();
        }
        return dealerMessageViewRepository.findAll(specification);
    }


    /**
     * 三包配件明细导出
     *
     * @param warrantyPartDetailItem
     * @return
     */
    @Override
    public List<WarrantyPartDetailView> warrantyPartDetailViewToExcel(WarrantyPartDetailItem warrantyPartDetailItem) {
        Specification<WarrantyPartDetailView> specification = null;
        if (warrantyPartDetailItem != null) {
            specification = Specifications.<WarrantyPartDetailView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getPartName()), "partName", "%" + warrantyPartDetailItem.getPartName() + "%")
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getDealerName()), "dealerName", "%" + warrantyPartDetailItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getDealerName()), "dealerCode", "%" + warrantyPartDetailItem.getDealerCode() + "%")
                    .like(StringUtils.isNotBlank(warrantyPartDetailItem.getServiceManager()), "serviceManager", "%" + warrantyPartDetailItem.getServiceManager() + "%")//服务经理

                    .between("asnCreatedTime", new Range<Date>(warrantyPartDetailItem.getStartDate(), warrantyPartDetailItem.getEndDate()))
                    .build();
        }
        return warrantyPartDetailViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpenseSummaryView> dealerExpenseSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpenseSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpenseSummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度三包费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpenseWarrantySummaryView> dealerExpenseWarrantySummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpenseWarrantySummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseWarrantySummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpenseWarrantySummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度活动费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpenseActivitySummaryView> dealerExpenseActivitySummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpenseActivitySummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseActivitySummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpenseActivitySummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度首保费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpenseFirstSummaryView> dealerExpenseFirstSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpenseFirstSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseFirstSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpenseFirstSummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度故障件运费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpenseFreightSummaryView> dealerExpenseFreightSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpenseFreightSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseFreightSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpenseFreightSummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度外出费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpenseGoOutSummaryView> dealerExpenseGoOutSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpenseGoOutSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseGoOutSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpenseGoOutSummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度配件费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpensePartSummaryView> dealerExpensePartSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpensePartSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpensePartSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))
                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpensePartSummaryViewRepository.findAll(specification);
    }


    /**
     * 服务站费年度配件费用明细导出
     *
     * @param dealerExpenseSummaryItem
     * @return
     */
    @Override
    public List<DealerExpenseWorkHoursSummaryView> dealerExpenseWorkHoursSummaryViewToExcel(DealerExpenseSummaryItem dealerExpenseSummaryItem) {
        Specification<DealerExpenseWorkHoursSummaryView> specification = null;
        if (dealerExpenseSummaryItem != null) {
            specification = Specifications.<DealerExpenseWorkHoursSummaryView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .between("createdTime", new Range<Date>(dealerExpenseSummaryItem.getStartDate(), dealerExpenseSummaryItem.getEndDate()))

                    .like(StringUtils.isNotBlank(dealerExpenseSummaryItem.getDealerName()), "dealerName", "%" + dealerExpenseSummaryItem.getDealerName() + "%")
                    .build();
        }
        return dealerExpenseWorkHoursSummaryViewRepository.findAll(specification);
    }

    /**
     * 获取活动通知单报表分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<ActivitiNoticeReportView> getActivityNoticeReportViewPageList(PageParam<ActivityNoticeReportItem> pageParam) {
        //1.查询条件
        ActivityNoticeReportItem activityNoticeReportItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ActivitiNoticeReportView> specification = null;
        if (activityNoticeReportItem != null) {
            specification = Specifications.<ActivitiNoticeReportView>and()
                    .eq(StringUtils.isNotBlank(activityNoticeReportItem.getDocNo()), "docNo", activityNoticeReportItem.getDocNo())
                    .between("createdTime", new Range<Date>(activityNoticeReportItem.getStartDate(), activityNoticeReportItem.getEndDate()))
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .build();
        }
        //3.执行查询
        Page<ActivitiNoticeReportView> pages = activitiNoticeReportViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 活动通知单报表导出excel
     *
     * @param activityNoticeReportItem
     * @return
     */
    @Override
    public List<ActivitiNoticeReportView> activityNoticeReportToExcel(ActivityNoticeReportItem activityNoticeReportItem) {
        Specification<ActivitiNoticeReportView> specification = null;
        if (activityNoticeReportItem != null) {
            specification = Specifications.<ActivitiNoticeReportView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(activityNoticeReportItem.getDocNo()), "docNo", activityNoticeReportItem.getDocNo())
                    .between("createdTime", new Range<Date>(activityNoticeReportItem.getStartDate(), activityNoticeReportItem.getEndDate()))
                    .build();
        }
        return activitiNoticeReportViewRepository.findAll(specification);
    }

    /**
     * 打印页面
     *
     * @param variable
     * @return
     */
    @Override
    public byte[] printPage(Map<String, Object> variable) {

        variable.put("dataSourceUrl", dataSourceUrl);
        variable.put("userName", userName);
        variable.put("password", password);
        variable.put("driverClassName", driverClassName);

        byte[] bytes = ReportUtils.reportToPDF(variable);
        return bytes;
    }

    /**
     * 打印故障件标签
     *
     * @param variable
     * @return
     */
    @Override
    public byte[] printRecycleLabel(Map<String, String> variable) {
        try {
            Map map = new HashMap();
            Specification<RecycleLabel> specification = null;
            if (StringUtils.isNotBlank(variable.get("recycle"))) {
                specification = Specifications.<RecycleLabel>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(StringUtils.isNotBlank(variable.get("recycle")), "recycle", variable.get("recycle"))
                        .build();
            }
            List<RecycleLabel> recycleLabelList = recycleLabelViewRepository.findAll(specification);
            List<RecycleLabel> dataList = new ArrayList<>();
            //拆分故障件打印标签数量
            if (recycleLabelList.size() > 0) {
                for (RecycleLabel recycleLabel : recycleLabelList) {
                    for (int i = 0; i < recycleLabel.getBackAmount(); i++) {
                        dataList.add(recycleLabel);
                    }
                }
            }


            //传入javabean
            JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataList);
            //报表文件路径
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "report/";
            //编译后报表文件
            JasperReport report = (JasperReport) JRLoader.loadObject(new File(path + variable.get("printType")));

            //JasperPrint jasperPrint= JasperFillManager.fillReport(report, map,beanDataSource);
            byte[] bytes = JasperRunManager.runReportToPdf(report, map, beanDataSource);
            System.out.println("Connection1 Successful!");
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 三包单打印故障件标签
     *
     * @param variable
     * @return
     */
    @Override
    public byte[] warrantyPrintRecycleLabel(Map<String, String> variable) {
        try {
            Map map = new HashMap();
            String objId = variable.get("objId");
            List<RecycleLabel> dataList = new ArrayList<>();
            List<WarrantyRecycleLabelView> recycleLabelList = new ArrayList<>();
            //拆分故障件打印标签数量
            Specification<WarrantyRecycleLabelView> specification = null;
            if (StringUtils.isNotBlank(objId)) {
                specification = Specifications.<WarrantyRecycleLabelView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq("awmObjId", objId)
                        .build();
                recycleLabelList = WarrantyRecycleLabelViewRepository.findAll(specification);
            }

            //拆分故障件打印标签数量
            if (recycleLabelList.size() > 0) {
                for (WarrantyRecycleLabelView recycleLabel : recycleLabelList) {
                    for (int i = 0; i < recycleLabel.getWaitAmount(); i++) {
                        dataList.add(BeanUtils.copyPropertys(recycleLabel, new RecycleLabel()));
                    }
                }
            }


            //传入javabean
            JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataList);
            //报表文件路径
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "report/";
            //编译后报表文件
            JasperReport report = (JasperReport) JRLoader.loadObject(new File(path + variable.get("printType")));

            //JasperPrint jasperPrint= JasperFillManager.fillReport(report, map,beanDataSource);
            byte[] bytes = JasperRunManager.runReportToPdf(report, map, beanDataSource);
            System.out.println("Connection1 Successful!");
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
