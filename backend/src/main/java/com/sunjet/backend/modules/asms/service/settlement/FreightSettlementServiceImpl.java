package com.sunjet.backend.modules.asms.service.settlement;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.settlement.FreightSettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.settlement.view.FreightSettlementView;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleRepository;
import com.sunjet.backend.modules.asms.repository.settlement.FreightExpenseRepository;
import com.sunjet.backend.modules.asms.repository.settlement.FreightSettlementRepository;
import com.sunjet.backend.modules.asms.repository.settlement.FreightSettlementViewRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.settlement.FreightExpenseInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
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
 */
@Transactional
@Service("freightSettlementService")
public class FreightSettlementServiceImpl implements FreightSettlementService {
    @Autowired
    private FreightSettlementRepository freightSettlementRepository;//dao

    @Autowired
    private FreightSettlementViewRepository freightSettlementViewRepository;//view

    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private RecycleRepository recycleRepository;

    @Autowired
    private FreightExpenseListService freightExpenseListService;    //service
    @Autowired
    private FreightExpenseRepository freightExpenseRepository;
    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DealerRepository dealerRepository;

    /**
     * 通过info 保存一个entity
     *
     * @param freightSettlementInfo
     * @return
     */
    @Override
    public FreightSettlementInfo save(FreightSettlementInfo freightSettlementInfo) {
        try {

            if (freightSettlementInfo != null && org.apache.commons.lang.StringUtils.isBlank(freightSettlementInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(FreightSettlementEntity.class.getSimpleName());
                freightSettlementInfo.setDocNo(docNo);

            }

            FreightSettlementEntity entity = freightSettlementRepository.save(BeanUtils.copyPropertys(freightSettlementInfo, new FreightSettlementEntity()));

            //保存运费结算单子行
            List<FreightExpenseInfo> freightExpenseInfos = saveFreightExpenseInfos(entity.getObjId(), freightSettlementInfo.getFreightExpenseInfos());

            freightSettlementInfo = BeanUtils.copyPropertys(entity, freightSettlementInfo);
            freightSettlementInfo.setFreightExpenseInfos(freightExpenseInfos);

            return freightSettlementInfo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存运费结算列表行
     *
     * @param freightSettlementObjId
     * @param freightExpenseInfos
     */
    private List<FreightExpenseInfo> saveFreightExpenseInfos(String freightSettlementObjId, List<FreightExpenseInfo> freightExpenseInfos) {
        List<FreightExpenseInfo> infoList = new ArrayList<>();
        try {
            freightExpenseRepository.deleteByFreightSettlementId(freightSettlementObjId);
            if (freightExpenseInfos != null) {
                for (FreightExpenseInfo freightExpenseInfo : freightExpenseInfos) {
                    freightExpenseInfo.setFreightSettlementId(freightSettlementObjId);
                    FreightExpenseInfo info = freightExpenseListService.save(freightExpenseInfo);
                    infoList.add(info);
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return infoList;
        }
    }


    /**
     * 通过objId 查找一个entity
     *
     * @param objId
     * @return
     */
    @Override
    public FreightSettlementInfo findOne(String objId) {
        try {
            FreightSettlementEntity entity = freightSettlementRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new FreightSettlementInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info删除一个entity
     *
     * @param freightSettlementInfo
     * @return
     */
//    @Override
//    public boolean delete(FreightSettlementInfo freightSettlementInfo) {
//        try {
//            freightSettlementRepository.delete(freightSettlementInfo.getObjId());
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * 通过一个objId 删除一个entity
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            freightSettlementRepository.delete(objId);
            freightExpenseListService.deleteByFreightSettlementId(objId);       //删除运费单行
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
    public PageResult<FreightSettlementView> getPageList(PageParam<FreightSettlementItem> pageParam) {
        //1.查询条件
        FreightSettlementItem freightSettlementItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<FreightSettlementView> specification = null;

        if (freightSettlementItem != null) {
            specification = Specifications.<FreightSettlementView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(freightSettlementItem.getDealerCode()), "dealerCode", freightSettlementItem.getDealerCode())
                    .eq(StringUtils.isNotEmpty(freightSettlementItem.getDealerName()), "dealerName", freightSettlementItem.getDealerName())
                    .eq(StringUtils.isNotEmpty(freightSettlementItem.getServiceManager()), "serviceManager", freightSettlementItem.getServiceManager())
                    .like(StringUtils.isNotBlank(freightSettlementItem.getDocNo()), "docNo", "%" + freightSettlementItem.getDocNo() + "%")
                    .eq(!freightSettlementItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", freightSettlementItem.getStatus())//表单状态
                    .ge(freightSettlementItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((freightSettlementItem.getStartDate() != null && freightSettlementItem.getEndDate() != null), "createdTime", new Range<Date>(freightSettlementItem.getStartDate(), freightSettlementItem.getEndDate()))
                    .build();
        }

        //3.执行查询
        Page<FreightSettlementView> pages = freightSettlementViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //4.数据转换
        //List<FreightSettlementItem> rows = new ArrayList<>();
        //for(FreightSettlementView view :pages.getContent()){
        //    FreightSettlementItem item = new FreightSettlementItem();
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
            FreightSettlementInfo freightSettlementInfo = JsonHelper.map2Bean(variables.get("entity"), FreightSettlementInfo.class);
            FreightSettlementEntity entity = BeanUtils.copyPropertys(freightSettlementInfo, new FreightSettlementEntity());
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
            if (!freightSettlementInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {

                //选择的服务站
                DealerEntity dealerEntity = dealerRepository.findOneByCode(freightSettlementInfo.getDealerCode());
                List<UserEntity> list = userRepository.findAllByDealerId(dealerEntity.getObjId());
                if (list.size() == 0) {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }

                List<String> users = new ArrayList<>();
                for (UserEntity userEntity : list) {
                    users.add(userEntity.getLogId());
                }
                variables.put("dealerUsers", users);
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {

                    FreightSettlementEntity freightSettlementEntity = freightSettlementRepository.findOne(freightSettlementInfo.getObjId());
                    //FreightSettlementInfo freightSettlement = findOne(freightSettlementInfo.getObjId());
                    freightSettlementEntity.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    freightSettlementEntity.setStatus(DocStatus.AUDITING.getIndex());
                    //save(freightSettlement);
                    freightSettlementRepository.save(freightSettlementEntity);
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
        FreightSettlementEntity entity = freightSettlementRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                List<PendingSettlementDetailsEntity> pendingsBySettlementID = pendingSettlementDetailsRepository.getPendingsBySettlementID(entity.getObjId());
                if (pendingsBySettlementID != null) {
                    for (PendingSettlementDetailsEntity detail : pendingsBySettlementID) {
                        detail.setSettlementDocType("运费结算单");
                        detail.setSettlementDocID(null);
                        detail.setSettlementDocNo(null);
                        detail.setOperator(null);
                        detail.setOperatorPhone(null);
                        detail.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                        detail.setModifierId(entity.getSubmitter());
                        detail.setModifierName(entity.getSubmitterName());
                        detail.setModifiedTime(new Date());
                        pendingSettlementDetailsRepository.save(detail);
                        RecycleEntity recycleEntity = recycleRepository.findOne(detail.getSrcDocID());
                        if (recycleEntity != null) {
                            recycleEntity.setStatus(detail.getStatus());
                            recycleRepository.save(recycleEntity);
                        }

                    }
                }
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                freightSettlementRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return freightSettlementRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return freightSettlementRepository;
    }

    //@Override
    //public FreightSettlementEntity findOneById(String objId) {
    //    return freightSettlementRepository.findOneById(objId);
    //}
    //
    //@Override
    //public List<FreightExpenseEntity> findFreightExpenseById(String objId) {
    //    return freightSettlementRepository.findFreightExpenseById(objId);
    //}
}
