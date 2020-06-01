package com.sunjet.backend.modules.asms.service.supply;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyWaitingItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyView;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.supply.*;
import com.sunjet.backend.modules.asms.service.activity.ActivityDistributionService;
import com.sunjet.backend.modules.asms.service.asm.WarrantyMaintenanceService;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.asms.supply.SupplyItem;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
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

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * 供货单/发货单
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("supplyService")
public class SupplyServiceImpl implements SupplyService {


    @Autowired
    private SupplyRepository supplyRepository;  //dao

    @Autowired
    private SupplyViewRepository supplyViewRepository;  //view

    @Autowired
    private SupplyItemService supplyItemService;    //service

    @Autowired
    private SupplyItemRepository supplyItemRepository;

    @Autowired
    private SupplyWaitingItemRepository supplyWaitingItemRepository;

    @Autowired
    private DocumentNoService documentNoService;

    @Autowired
    private ProcessService processService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DealerRepository dealerRepository;
    @Autowired
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @Autowired
    private ActivityDistributionService activityDistributionService;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;

    /**
     * 保存 一个实体
     *
     * @param supplyInfo
     * @return
     */
    @Override
    public SupplyInfo save(SupplyInfo supplyInfo) {
        try {
            if (StringUtils.isBlank(supplyInfo.getDocNo())) {
                String docNo = documentNoService.getDocumentNo(SupplyEntity.class.getSimpleName());
                supplyInfo.setDocNo(docNo);
            }
            SupplyEntity entity = supplyRepository.save(BeanUtils.copyPropertys(supplyInfo, new SupplyEntity()));
            //保存子行列表
            List<SupplyItemInfo> list = saveSupplyItemList(supplyInfo.getItems(), entity.getObjId());
            supplyInfo = BeanUtils.copyPropertys(entity, supplyInfo);
            supplyInfo.setItems(list);
            return supplyInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存子行列表
     *
     * @param items
     * @param objId
     */
    private List<SupplyItemInfo> saveSupplyItemList(List<SupplyItemInfo> items, String objId) {
        List<SupplyItemInfo> list = new ArrayList<>();

        try {
            for (SupplyItemInfo item : items) {
                item.setSupplyId(objId);
                SupplyItemInfo save = supplyItemService.save(BeanUtils.copyPropertys(item, new SupplyItemInfo()));
                list.add(save);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    /**
     * 删除 --> 通过 supplyInfo 对象
     *
     * @param supplyInfo
     * @return
     */
    @Override
    public boolean delete(SupplyInfo supplyInfo) {
        try {
            this.delete(supplyInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 --> 通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            supplyRepository.delete(objId);

            supplyItemService.deleteBySupplyNoticeItems(objId); //删除调拨供货行

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public SupplyInfo findOne(String objId) {
        try {
            SupplyEntity entity = supplyRepository.findOne(objId);
            SupplyInfo supplyInfo = BeanUtils.copyPropertys(entity, new SupplyInfo());
            List<SupplyItemInfo> supplyitem = supplyItemService.findBySupplyId(objId);
            supplyInfo.setItems(supplyitem);
            return supplyInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageResult<SupplyView> getPageList(PageParam<SupplyItem> pageParam) {
        //1.查询条件
        Page<SupplyView> pages = null;
        try {
            SupplyItem supplyItem = pageParam.getInfoWhere();

            //2.设置查询参数
            Specification<SupplyView> specification = null;

            if (supplyItem != null) {
                specification = Specifications.<SupplyView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotBlank(supplyItem.getDealerCode()), "dealerCode", "%" + supplyItem.getDealerCode() + "%")
                        .like(StringUtils.isNotBlank(supplyItem.getAgencyCode()), "agencyCode", "%" + supplyItem.getAgencyCode() + "%")
                        .like(StringUtils.isNotBlank(supplyItem.getDealerName()), "dealerName", "%" + supplyItem.getDealerName() + "%")
                        .like(StringUtils.isNotBlank(supplyItem.getLogisticsNum()), "logisticsNum", "%" + supplyItem.getLogisticsNum() + "%")
                        .like(StringUtils.isNotBlank(supplyItem.getDocNo()), "docNo", "%" + supplyItem.getDocNo() + "%")
                        //.like(StringUtils.isNotBlank(supplyItem.getSrcDocNo()), "srcDocNo", "%" + supplyItem.getSrcDocNo() + "%")
                        //.like(StringUtils.isNotBlank(supplyItem.getActivityNoticeDocNo()), "activityNoticeDocNo", "%" + supplyItem.getActivityNoticeDocNo() + "%")
                        .eq(!supplyItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", supplyItem.getStatus())//表单状态
                        .ge(supplyItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                        .between((supplyItem.getStartDate() != null && supplyItem.getEndDate() != null), "createdTime", new Range<Date>(supplyItem.getStartDate(), DateHelper.getEndDate(supplyItem.getEndDate())))
                        .build();
            }

            //3.执行查询
            pages = supplyViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //4.数据转换
        //List<SupplyItem> rows = new ArrayList<>();
        //for(SupplyView view :pages.getContent()){
        //    SupplyItem item = new SupplyItem();
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
            SupplyInfo supplyInfo = JsonHelper.map2Bean(variables.get("entity"), SupplyInfo.class);
            SupplyEntity entity = BeanUtils.copyPropertys(supplyInfo, new SupplyEntity());
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
            if (!supplyInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                //选择的服务站
                DealerEntity dealerEntity = dealerRepository.findOneByCode(supplyInfo.getDealerCode());
                List<UserEntity> list = userRepository.findAllByDealerId(dealerEntity.getObjId());

                List<String> users = new ArrayList<>();
                for (UserEntity userEntity : list) {
                    users.add(userEntity.getLogId());
                }
                variables.put("dealerUsers", users);

                //启动流程

                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    SupplyInfo supply = findOne(supplyInfo.getObjId());
                    supply.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    supply.setStatus(DocStatus.AUDITING.getIndex());
                    save(supply);

                    message.put("result", "提示");
                    message.put("message", "提交成功");
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
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
     * 通过车辆id查询所有的
     *
     * @param vehicleObjIds
     * @return
     */
    @Override
    public List<String> findAllIdByVehicleIds(List<String> vehicleObjIds) {
        List<String> objIds = new ArrayList<>();
        List<SupplyItemInfo> supplyItemInfoList = new ArrayList<>();
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
        //来源单据id
        List<String> srcDocIds = new ArrayList<>();
        try {
            if (vehicleObjIds.size() > 0) {
                //三包单
                List<String> warrantyIds = warrantyMaintenanceService.findAllObjIdsByVehicleId(vehicleObjIds);
                if (warrantyIds != null && warrantyIds.size() > 0) {
                    srcDocIds.addAll(warrantyIds);
                }
                //活动分配单
                srcDocIds.addAll(activityDistributionService.findAllObjIdsByVehicleId(vehicleObjIds));
            }
            if (srcDocIds.size() > 0) {
                supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocIds(srcDocIds);
            }
            if (supplyNoticeItemEntities.size() > 0) {
                List<String> supplyNoticeItemIds = new ArrayList<>();
                for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                    supplyNoticeItemIds.add(supplyNoticeItemEntity.getObjId());
                }
                supplyItemInfoList = supplyItemService.findAllByNoticeItemId(supplyNoticeItemIds);
            }
            for (SupplyItemInfo supplyItemInfo : supplyItemInfoList) {
                if (StringUtils.isNotBlank(supplyItemInfo.getSupplyId())) {
                    objIds.add(supplyItemInfo.getSupplyId());
                }
            }
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
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
        SupplyEntity entity = supplyRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                List<SupplyItemEntity> supplyItemEntityList = supplyItemRepository.findBySupplyId(entity.getObjId());
                for (SupplyItemEntity item : supplyItemEntityList) {

                    SupplyWaitingItemEntity supplyWaitingItem = supplyWaitingItemRepository.findOne(item.getSupplyWaitingItemId());
                    if (supplyWaitingItem != null) {
                        //待发货单需求数量 =  待发货可分配数量 + 供货单发送数量
                        supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() + item.getAmount());
                        //待发货单发货数量 = 待发货单的已发数量 - 供货单发送数量
                        supplyWaitingItem.setSentAmount(supplyWaitingItem.getSentAmount() - item.getAmount());
                        supplyWaitingItem.setModifiedTime(new Date());
                        supplyWaitingItem.setModifierName(entity.getSubmitterName());
                        supplyWaitingItem.setModifierId(entity.getSubmitter());
                        supplyWaitingItemRepository.save(supplyWaitingItem);
                    }

                    item.setSupplyWaitingItemId(null);
                    item.setSupplyNoticeItemId(null);
                    supplyItemRepository.save(item);
                }
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                supplyRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return supplyRepository;
    }


    //@Override
    //public void save(SupplyEntity supplyRequest) {
    //    supplyRepository.save(supplyRequest);
    //}
    //
    //@Override
    //public SupplyEntity getSupplyByID(String id) {
    //    return supplyRepository.getOne(id);
    //}
    //
    //@Override
    //public SupplyEntity findSupplyWithPartsById(String objId) {
    //    return this.supplyRepository.findOneWithPartsById(objId);
    //}
    //
    //@Override
    //public boolean deleteEntity(SupplyEntity supplyRequest) {
    //    try {
    //        supplyRepository.delete(supplyRequest);
    //        return true;
    //    } catch (Exception ex) {
    //        return false;
    //    }
    //}
}
