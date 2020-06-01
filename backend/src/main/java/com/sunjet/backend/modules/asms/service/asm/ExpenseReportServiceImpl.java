package com.sunjet.backend.modules.asms.service.asm;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.ExpenseReportEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.ExpenseReportView;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.repository.asm.ExpenseReportRepository;
import com.sunjet.backend.modules.asms.repository.asm.ExpenseReportViewRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.asm.ExpenseReportInfo;
import com.sunjet.dto.asms.asm.ExpenseReprotItem;
import com.sunjet.dto.asms.asm.ReportVehicleInfo;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.common.JsonHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;


/**
 * 售后费用速报
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("expenseReportService")
public class ExpenseReportServiceImpl implements ExpenseReportService {

    @Autowired
    private ExpenseReportRepository expenseReportRepository;
    @Autowired
    private ReportVehicleService reportVehicleService;
    @Autowired
    private ReportPartService reportPartService;
    @Autowired
    private DocumentNoService documentNoService;

    @Autowired
    private ExpenseReportViewRepository expenseReportViewRepository;
    @Autowired
    private ProcessService processService;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;
    ReportVehicleInfo reportVehicleInfo = new ReportVehicleInfo();

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<ExpenseReportView> getPageList(PageParam<ExpenseReprotItem> pageParam) {
        //1.查询条件
        ExpenseReprotItem expenseReprotItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ExpenseReportView> specification = null;
        if (expenseReprotItem != null) {
            specification = Specifications.<ExpenseReportView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(expenseReprotItem.getDocNo()), "docNo", "%" + expenseReprotItem.getDocNo() + "%")//单据编号
                    .like(StringUtils.isNotEmpty(expenseReprotItem.getVehicleType()), "vehicleType", "%" + expenseReprotItem.getVehicleType() + "%")//车辆类型
                    .eq(StringUtils.isNotEmpty(expenseReprotItem.getDealerCode()), "dealerCode", expenseReprotItem.getDealerCode())
                    .eq(StringUtils.isNotEmpty(expenseReprotItem.getDealerName()), "dealerName", expenseReprotItem.getDealerName())
                    .eq(StringUtils.isNotEmpty(expenseReprotItem.getServiceManager()), "serviceManager", expenseReprotItem.getServiceManager())
                    .eq(!expenseReprotItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", expenseReprotItem.getStatus())//表单状态
                    .ge(expenseReprotItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .like((StringUtils.isNotBlank(expenseReprotItem.getVin())), "vin", "%" + expenseReprotItem.getVin() + "%")
                    .between((expenseReprotItem.getStartDate() != null && expenseReprotItem.getEndDate() != null),
                            "createdTime", new Range<Date>(expenseReprotItem.getStartDate(), DateHelper.getEndDate(expenseReprotItem.getEndDate())))
                    .build();
        }

        //3.执行查询
        Page<ExpenseReportView> pages = expenseReportViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<ExpenseReprotItem> rows = new ArrayList<>();
        //for (ExpenseReportView entity : pages.getContent()) {
        //    ExpenseReprotItem info = entityToInfo(entity);
        //    rows.add(info);
        //}

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public ExpenseReportInfo findOneById(String objId) {
        try {
            ExpenseReportEntity expenseReportEntity = expenseReportRepository.findOne(objId);
            return BeanUtils.copyPropertys(expenseReportEntity, new ExpenseReportInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增/ 保存
     *
     * @param expenseReportEntity
     * @return
     */
    @Override
    public ExpenseReportEntity save(ExpenseReportEntity expenseReportEntity) {
        try {
            //ReportVehicleEntity reportVehicleEntity = new ReportVehicleEntity();
            if (expenseReportEntity != null && StringUtils.isBlank(expenseReportEntity.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(ExpenseReportEntity.class.getSimpleName());
                expenseReportEntity.setDocNo(docNo);

            }
            return expenseReportRepository.save(expenseReportEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 by objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {

        try {
            expenseReportRepository.delete(objId);
            reportVehicleService.deleteByExpenseReprotObId(objId);
            reportPartService.deleteByExpenseReportObjId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 关键字搜索费用速报
     *
     * @param keyword
     * @param dealerCode
     * @return
     */
    @Override
    public List<ExpenseReportInfo> findAllByKeywordAndDealerCode(String keyword, String dealerCode) {

        try {
            List<ExpenseReportEntity> expenseReportEntityList = expenseReportRepository.findAllByKeywordAndDealerCode("%" + keyword + "%", dealerCode);
            List<ExpenseReportInfo> expenseReportInfoList = new ArrayList<>();
            for (ExpenseReportEntity expenseReportEntity : expenseReportEntityList) {
                expenseReportInfoList.add(BeanUtils.copyPropertys(expenseReportEntity, new ExpenseReportInfo()));
            }
            return expenseReportInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Map<String, String> startProcess(Map<String, Object> variables) {
        //提交流程返回信息
        Map<String, String> message = new HashMap<>();
        try {
            ExpenseReportInfo expenseReportInfo = JsonHelper.map2Bean(variables.get("entity"), ExpenseReportInfo.class);
            if (!expenseReportInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                ExpenseReportEntity entity = BeanUtils.copyPropertys(expenseReportInfo, new ExpenseReportEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);

                DealerEntity dealerEntity = dealerRepository.findOneByCode(expenseReportInfo.getDealerCode());
                UserEntity userEntity = userRepository.findOne(dealerEntity.getServiceManagerId());
                if (userEntity == null) {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }
                variables.put("serviceManager", userEntity.getLogId());
                variables.put("amount", expenseReportInfo.getEstimatedCost());
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    //ExpenseReportInfo expenseReport = findOneById(expenseReportInfo.getObjId());
                    ExpenseReportEntity expenseReportEntity = expenseReportRepository.findOne(expenseReportInfo.getObjId());
                    expenseReportEntity.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态
                    expenseReportEntity.setStatus(DocStatus.AUDITING.getIndex());
                    expenseReportRepository.save(expenseReportEntity);
                    //save(expenseReport);

                    message.put("result", "提示");
                    message.put("message", "提交成功");
                    return message;
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }
            } else {
                message.put("result", "提示");
                message.put("message", "提交失败");
            }

            return message;
        } catch (IOException e) {
            e.printStackTrace();
            message.put("result", "提示");
            message.put("message", "提交失败");
            return message;
        }

    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean desertTask(String objId) {
        ExpenseReportEntity entity = expenseReportRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                expenseReportRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return expenseReportRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return expenseReportRepository;
    }
}
