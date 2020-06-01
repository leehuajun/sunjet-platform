package com.sunjet.backend.modules.asms.service.settlement;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.FirstMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.settlement.DealerSettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.settlement.view.DealerSettlementView;
import com.sunjet.backend.modules.asms.repository.activity.ActivityMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.asm.FirstMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.settlement.DealerSettlementRepository;
import com.sunjet.backend.modules.asms.repository.settlement.DealerSettlementViewRepository;
import com.sunjet.backend.modules.asms.repository.settlement.ExpenseListRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.settlement.DealerSettlementInfo;
import com.sunjet.dto.asms.settlement.DealerSettlementItem;
import com.sunjet.dto.asms.settlement.ExpenseItemInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.common.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/10/26.
 * 三包服务站结算
 */
@Transactional
@Service("dealerSettlementService")
public class DealerSettlementServiceImpl implements DealerSettlementService {
    @Autowired
    private DealerSettlementRepository dealerSettlementRepository;//dao
    @Autowired
    private DealerSettlementViewRepository dealerSettlementViewRepository;//view

    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private FirstMaintenanceRepository firstMaintenanceRepository;
    @Autowired
    private ActivityMaintenanceRepository activityMaintenanceRepository;

    @Autowired
    private ExpenseListService expenseListService;  //service
    @Autowired
    private ExpenseListRepository expenseListRepository;
    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DealerRepository dealerRepository;

    /**
     * 通过info保存
     *
     * @param dealerSettlementInfo
     * @return
     */
    @Override
    public DealerSettlementInfo save(DealerSettlementInfo dealerSettlementInfo) {
        try {

            if (dealerSettlementInfo != null && org.apache.commons.lang.StringUtils.isBlank(dealerSettlementInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(DealerSettlementEntity.class.getSimpleName());
                dealerSettlementInfo.setDocNo(docNo);

            }

            DealerSettlementEntity entity = dealerSettlementRepository.save(BeanUtils.copyPropertys(dealerSettlementInfo, new DealerSettlementEntity()));

            //保存服务结算子行
            List<ExpenseItemInfo> expenseItemInfos = saveExpenseItems(entity.getObjId(), dealerSettlementInfo.getItems());

            dealerSettlementInfo = BeanUtils.copyPropertys(entity, dealerSettlementInfo);
            dealerSettlementInfo.setItems(expenseItemInfos);

            return dealerSettlementInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 保存服务结算列表子行
     *
     * @param dealerSettlementObjId
     * @param items
     */
    private List<ExpenseItemInfo> saveExpenseItems(String dealerSettlementObjId, List<ExpenseItemInfo> items) {
        List<ExpenseItemInfo> expenseItemInfos = new ArrayList<>();
        try {
            expenseListRepository.deleteByDealerSettlementId(dealerSettlementObjId);
            if (items != null) {
                for (ExpenseItemInfo expenseItemInfo : items) {
                    expenseItemInfo.setDealerSettlementId(dealerSettlementObjId);
                    ExpenseItemInfo info = expenseListService.save(expenseItemInfo);
                    expenseItemInfos.add(info);
                }
            }
            return expenseItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return expenseItemInfos;
        }
    }


    /**
     * 通过objid 查找一个info
     *
     * @param objId
     * @return
     */
    @Override
    public DealerSettlementInfo findOne(String objId) {
        try {
            DealerSettlementEntity entity = dealerSettlementRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new DealerSettlementInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info删除一个实体
     *
     * @param dealerSettlementInfo
     * @return
     */
//    @Override
//    public boolean delete(DealerSettlementInfo dealerSettlementInfo) {
//        try {
//            dealerSettlementRepository.delete(dealerSettlementInfo.getObjId());
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * 通过objId删除一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            dealerSettlementRepository.delete(objId);
            expenseListService.deleteByDealerSettlementId(objId);   //删除结算费用列表
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DealerSettlementView> getPageList(PageParam<DealerSettlementItem> pageParam) {
        //1.查询条件
        DealerSettlementItem dealerSettlementItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DealerSettlementView> specification = null;

        if (dealerSettlementItem != null) {
            List<String> objIds = new ArrayList<>();
            if (StringUtils.isNotBlank(dealerSettlementItem.getSrcDocNo())) {
                objIds.addAll(expenseListService.findAllDealerSettlementObjIdBySrcDocNo(dealerSettlementItem.getSrcDocNo()));
            }
            if (StringUtils.isNotBlank(dealerSettlementItem.getVin())) {
                objIds.addAll(expenseListService.findAllDealerSettlementObjIdByVin(dealerSettlementItem.getVin()));
            }
            if ((StringUtils.isNotBlank(dealerSettlementItem.getSrcDocNo()) || StringUtils.isNotBlank(dealerSettlementItem.getVin())) && objIds.size() == 0) {
                objIds.add("null");
            }
            specification = Specifications.<DealerSettlementView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(dealerSettlementItem.getDealerCode()), "dealerCode", dealerSettlementItem.getDealerCode())
                    .eq(StringUtils.isNotEmpty(dealerSettlementItem.getDealerName()), "dealerName", dealerSettlementItem.getDealerName())
                    .eq(StringUtils.isNotEmpty(dealerSettlementItem.getServiceManager()), "serviceManager", dealerSettlementItem.getServiceManager())
                    .like(StringUtils.isNotBlank(dealerSettlementItem.getDocNo()), "docNo", "%" + dealerSettlementItem.getDocNo() + "%")
                    .eq(!dealerSettlementItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", dealerSettlementItem.getStatus())//表单状态
                    .ge(dealerSettlementItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((dealerSettlementItem.getStartDate() != null && dealerSettlementItem.getEndDate() != null), "createdTime", new Range<Date>(dealerSettlementItem.getStartDate(), DateHelper.getEndDate(dealerSettlementItem.getEndDate())))
                    .in(objIds.size() > 0, "objId", objIds)
                    .build();
        }

        //3.执行查询
        Page<DealerSettlementView> pages = dealerSettlementViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //4.数据转换
        //List<DealerSettlementItem> rows = new ArrayList<>();
        //for(DealerSettlementView view :pages.getContent()){
        //    DealerSettlementItem item = new DealerSettlementItem();
        //    item = BeanUtils.copyPropertys(view,item);
        //    rows.add(item);
        //}
        //
        ////5.返回
        //return PageUtil.getPageResult(rows,pages,pageParam);
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 启动流程
     *
     * @param variables
     * @return
     */
    @Override
    public Map<String, String> startProcess(Map<String, Object> variables) {
        //提交流程返回信息
        Map<String, String> message = new HashMap<>();
        try {
            DealerSettlementInfo dealerSettlementInfo = JsonHelper.map2Bean(variables.get("entity"), DealerSettlementInfo.class);
            DealerSettlementEntity entity = BeanUtils.copyPropertys(dealerSettlementInfo, new DealerSettlementEntity());
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
            if (!dealerSettlementInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                DealerEntity dealerEntity = dealerRepository.findOneByCode(dealerSettlementInfo.getDealerCode());
                //选择服务站
                List<UserEntity> list = userRepository.findAllByDealerId(dealerEntity.getObjId());
                if (list.size() == 0) {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }
                List<String> users = new ArrayList<>();
                for (UserEntity userEntity : list) {
                    System.out.println(userEntity.getLogId());
                    users.add(userEntity.getLogId());
                }

                variables.put("dealerUsers", users);

                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    DealerSettlementEntity dealerSettlementEntity = dealerSettlementRepository.findOne(dealerSettlementInfo.getObjId());
                    //DealerSettlementInfo dealerSettlement = findOne(dealerSettlementInfo.getObjId());
                    dealerSettlementEntity.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态
                    dealerSettlementEntity.setStatus(DocStatus.AUDITING.getIndex());
                    dealerSettlementRepository.save(dealerSettlementEntity);
                    //save(dealerSettlement);
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
            message.put("message", "提交失败,请联系管理员");
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
        DealerSettlementEntity entity = dealerSettlementRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                List<PendingSettlementDetailsEntity> pendingsBySettlementID = pendingSettlementDetailsRepository.getPendingsBySettlementID(entity.getObjId());
                if (pendingsBySettlementID != null) {
                    for (PendingSettlementDetailsEntity detail : pendingsBySettlementID) {
                        detail.setSettlementDocType("服务结算单");
                        detail.setSettlementDocID(null);
                        detail.setSettlementDocNo(null);
                        detail.setOperator(null);
                        detail.setOperatorPhone(null);
                        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                        detail.setModifierId(entity.getSubmitter());
                        detail.setModifierName(entity.getModifierName());
                        detail.setModifiedTime(new Date());
                        pendingSettlementDetailsRepository.save(detail);
                        if ("三包服务单".equals(detail.getSrcDocType())) {
                            WarrantyMaintenanceEntity warrantyMaintenance = warrantyMaintenanceRepository.findOne(detail.getSrcDocID());
                            if (warrantyMaintenance != null) {
                                warrantyMaintenance.setStatus(detail.getStatus());
                                warrantyMaintenanceRepository.save(warrantyMaintenance);
                            }

                        } else if ("首保服务单".equals(detail.getSrcDocType())) {
                            FirstMaintenanceEntity firstMaintenance = firstMaintenanceRepository.findOne(detail.getSrcDocID());
                            if (firstMaintenance != null) {
                                firstMaintenance.setStatus(detail.getStatus());
                                firstMaintenanceRepository.save(firstMaintenance);
                            }
                        } else {
                            ActivityMaintenanceEntity activityMaintenance = activityMaintenanceRepository.findOne(detail.getSrcDocID());
                            if (activityMaintenance != null) {
                                activityMaintenance.setStatus(detail.getStatus());
                                activityMaintenanceRepository.save(activityMaintenance);
                            }

                        }
                    }
                }
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                dealerSettlementRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerSettlementRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerSettlementRepository;
    }


    //@Override
    //public DealerSettlementEntity findOneById(String objId) {
    //    return dealerSettlementRepository.findOneById(objId);
    //}
    //
    //@Override
    //public List<ExpenseListEntity> findExpenseListById(String objId) {
    //    return dealerSettlementRepository.findExpenseListById(objId);
    //}
}
