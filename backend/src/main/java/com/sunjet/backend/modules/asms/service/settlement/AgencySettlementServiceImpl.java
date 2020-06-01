package com.sunjet.backend.modules.asms.service.settlement;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.AgencyEntity;
import com.sunjet.backend.modules.asms.entity.settlement.AgencySettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.settlement.view.AgencySettlementView;
import com.sunjet.backend.modules.asms.entity.supply.SupplyEntity;
import com.sunjet.backend.modules.asms.repository.basic.AgencyRepository;
import com.sunjet.backend.modules.asms.repository.settlement.AgencySettlementRepository;
import com.sunjet.backend.modules.asms.repository.settlement.AgencySettlementViewRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PartExpenseListRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.settlement.AgencySettlementInfo;
import com.sunjet.dto.asms.settlement.AgencySettlementItem;
import com.sunjet.dto.asms.settlement.PartExpenseItemsInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.JsonHelper;
import org.apache.commons.lang.StringUtils;
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
 */
@Transactional
@Service("agencySettlementService")
public class AgencySettlementServiceImpl implements AgencySettlementService {

    @Autowired
    private AgencySettlementRepository agencySettlementRepository;//dao

    @Autowired
    private AgencySettlementViewRepository agencySettlementViewRepository; //view

    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private PartExpenseListService partExpenseListService;
    @Autowired
    private PartExpenseListRepository partExpenseListRepository;
    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AgencyRepository agencyRepository;


    /**
     * 通过info保存一个实体
     *
     * @param agencySettlementInfo
     * @return
     */
    @Override
    public AgencySettlementInfo save(AgencySettlementInfo agencySettlementInfo) {
        try {

            if (agencySettlementInfo != null && org.apache.commons.lang.StringUtils.isBlank(agencySettlementInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(AgencySettlementEntity.class.getSimpleName());
                agencySettlementInfo.setDocNo(docNo);

            }

            AgencySettlementEntity entity = agencySettlementRepository.save(BeanUtils.copyPropertys(agencySettlementInfo, new AgencySettlementEntity()));

            //保存配件费用子行
            List<PartExpenseItemsInfo> expenseItemsInfos = savePartExpenseItemsInfos(entity.getObjId(), agencySettlementInfo.getPartExpenseItemsInfos());

            agencySettlementInfo = BeanUtils.copyPropertys(entity, agencySettlementInfo);
            agencySettlementInfo.setPartExpenseItemsInfos(expenseItemsInfos);
            return agencySettlementInfo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存配件费用列表子行
     *
     * @param agencySettlementObjId
     * @param partExpenseItemsInfos
     */
    private List<PartExpenseItemsInfo> savePartExpenseItemsInfos(String agencySettlementObjId, List<PartExpenseItemsInfo> partExpenseItemsInfos) {
        List<PartExpenseItemsInfo> infos = new ArrayList<>();
        try {
            partExpenseListRepository.deleteByAgencySettlementId(agencySettlementObjId);
            if (partExpenseItemsInfos != null) {
                for (PartExpenseItemsInfo partExpenseItemsInfo : partExpenseItemsInfos) {
                    partExpenseItemsInfo.setAgencySettlementId(agencySettlementObjId);
                    PartExpenseItemsInfo itemsInfo = partExpenseListService.save(partExpenseItemsInfo);
                    infos.add(itemsInfo);
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return infos;
        }
    }


    /**
     * 通过objId
     *
     * @param objId
     * @return
     */
    @Override
    public AgencySettlementInfo findOne(String objId) {

        try {
            AgencySettlementEntity entity = agencySettlementRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new AgencySettlementInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info 删除一个实体
     *
     * @param agencySettlementInfo
     * @return
     */
    @Override
    public boolean delete(AgencySettlementInfo agencySettlementInfo) {
        try {
            agencySettlementRepository.delete(agencySettlementInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            agencySettlementRepository.delete(objId);
            partExpenseListService.deleteByAgencySettlementId(objId);   //删除配件行
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
    public PageResult<AgencySettlementView> getPageList(PageParam<AgencySettlementItem> pageParam) {
        //1.查询条件
        AgencySettlementItem agencySettlementItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencySettlementView> specification = null;

        if (agencySettlementItem != null) {
            List<String> objIds = new ArrayList<>();
            //来源单据
            if (StringUtils.isNotBlank(agencySettlementItem.getSrcDocNo())) {
                objIds.addAll(partExpenseListService.findAllAgencySettlementObjIdBySrcDocNo(agencySettlementItem.getSrcDocNo()));
            }
            if (StringUtils.isNotBlank(agencySettlementItem.getVin())) {
                objIds.addAll(partExpenseListService.findAllAgencySettlementObjIdByVin(agencySettlementItem.getVin()));
            }
            if ((StringUtils.isNotBlank(agencySettlementItem.getSrcDocNo()) || StringUtils.isNotBlank(agencySettlementItem.getVin())) && objIds.size() == 0) {
                objIds.add("null");
            }

            specification = Specifications.<AgencySettlementView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(agencySettlementItem.getAgencyCode()), "agencyCode", agencySettlementItem.getAgencyCode())
                    .eq(StringUtils.isNotEmpty(agencySettlementItem.getAgencyName()), "agencyName", agencySettlementItem.getAgencyName())
                    .eq(StringUtils.isNotEmpty(agencySettlementItem.getServiceManager()), "serviceManager", agencySettlementItem.getServiceManager())
                    .like(StringUtils.isNotBlank(agencySettlementItem.getDocNo()), "docNo", "%" + agencySettlementItem.getDocNo() + "%")
                    .eq(!agencySettlementItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", agencySettlementItem.getStatus())//表单状态
                    .ge(agencySettlementItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((agencySettlementItem.getStartDate() != null && agencySettlementItem.getEndDate() != null), "createdTime", new Range<Date>(agencySettlementItem.getStartDate(), agencySettlementItem.getEndDate()))
                    .in(objIds.size() > 0, "objId", objIds)
                    .build();
        }

        //3.执行查询
        Page<AgencySettlementView> pages = agencySettlementViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //4.数据转换
        //List<AgencySettlementItem> rows = new ArrayList<>();
        //for(AgencySettlementView view :pages.getContent()){
        //    AgencySettlementItem item = new AgencySettlementItem();
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
            AgencySettlementInfo agencySettlementInfo = JsonHelper.map2Bean(variables.get("entity"), AgencySettlementInfo.class);
            AgencySettlementEntity entity = BeanUtils.copyPropertys(agencySettlementInfo, new AgencySettlementEntity());
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);

            if (!agencySettlementInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                //选择的合作商
                AgencyEntity agencyEntity = agencyRepository.findOneByCode(agencySettlementInfo.getAgencyCode());
                List<UserEntity> list = userRepository.findAllByAgencyId(agencyEntity.getObjId());
                if (list.size() == 0) {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }
                List<String> users = new ArrayList<>();
                for (UserEntity userEntity : list) {
                    users.add(userEntity.getLogId());
                }
                variables.put("agencyUsers", users);
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    AgencySettlementEntity agencySettlement = agencySettlementRepository.findOne(agencySettlementInfo.getObjId());
                    //AgencySettlementInfo agencySettlement = findOne(agencySettlementInfo.getObjId());
                    agencySettlement.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    agencySettlement.setStatus(DocStatus.AUDITING.getIndex());
                    //save(agencySettlement);
                    agencySettlementRepository.save(agencySettlement);
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
        AgencySettlementEntity entity = agencySettlementRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                List<PendingSettlementDetailsEntity> pendingsBySettlementID = pendingSettlementDetailsRepository.getPendingsBySettlementID(entity.getObjId());
                if (pendingsBySettlementID != null) {
                    for (PendingSettlementDetailsEntity detail : pendingsBySettlementID) {
                        detail.setSettlementDocType("配件结算单");
                        detail.setSettlementDocID(null);
                        detail.setSettlementDocNo(null);
                        detail.setOperator(null);
                        detail.setOperatorPhone(null);
                        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                        detail.setModifierId(entity.getSubmitter());
                        detail.setModifierName(entity.getModifierName());
                        detail.setModifiedTime(new Date());
                        pendingSettlementDetailsRepository.save(detail);
                        SupplyEntity supplyEntity = supplyRepository.findOne(detail.getSrcDocID());
                        if (supplyEntity != null) {
                            supplyEntity.setStatus(detail.getStatus());
                            supplyRepository.save(supplyEntity);
                        }
                    }
                }
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                agencySettlementRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencySettlementRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return agencySettlementRepository;
    }

    //@Override
    //public AgencySettlementEntity findOneById(String objId) {
    //    return agencySettlementRepository.findOneById(objId);
    //}
    //
    //@Override
    //public List<PartExpenseListEntity> findPartExpenseListById(String objId) {
    //    return agencySettlementRepository.findPartExpenseListById(objId);
    //}
}
