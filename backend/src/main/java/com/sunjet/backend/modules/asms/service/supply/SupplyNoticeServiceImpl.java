package com.sunjet.backend.modules.asms.service.supply;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyNoticeView;
import com.sunjet.backend.modules.asms.repository.activity.ActivityDistributionRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityNoticeRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeViewRepository;
import com.sunjet.backend.modules.asms.service.asm.WarrantyMaintenanceService;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.DictionaryService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.supply.*;
import com.sunjet.dto.system.admin.DictionaryInfo;
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
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("supplyNoticeService")
public class SupplyNoticeServiceImpl implements SupplyNoticeService {


    //@Autowired
    //private DocumentNoService documentNoService;

    @Autowired
    private SupplyNoticeRepository supplyNoticeRepository;

    @Autowired
    private SupplyNoticeViewRepository supplyNoticeViewRepository;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;
    @Autowired
    private SupplyItemService supplyItemService;
    @Autowired
    private SupplyService supplyService;

    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private ActivityDistributionRepository activityDistributionRepository;
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;
    @Autowired
    private ActivityNoticeRepository activityNoticeRepository;
    @Autowired
    private WarrantyMaintenanceService warrantyMaintenanceService;  // 三包服务单
    @Autowired
    private DictionaryService dictionaryService;    // 数据字典


    /**
     * 保存 一个实体
     *
     * @param supplyNoticeInfo
     * @return
     */
    @Override
    public SupplyNoticeInfo save(SupplyNoticeInfo supplyNoticeInfo) {
        try {

            if (supplyNoticeInfo != null && StringUtils.isBlank(supplyNoticeInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(SupplyNoticeEntity.class.getSimpleName());
                supplyNoticeInfo.setDocNo(docNo);
            }
            SupplyNoticeEntity entity = supplyNoticeRepository.save(BeanUtils.copyPropertys(supplyNoticeInfo, new SupplyNoticeEntity()));
            List<SupplyNoticeItemInfo> supplyNoticeItemInfoList = new ArrayList<>();
            //保存调拨通知单子行列表
            if (supplyNoticeInfo.getSupplyNoticeItemInfos() != null) {
                supplyNoticeItemInfoList = saveSupplyNoticeItems(entity.getObjId(), supplyNoticeInfo.getSupplyNoticeItemInfos());
            }

            supplyNoticeInfo = BeanUtils.copyPropertys(entity, supplyNoticeInfo);
            supplyNoticeInfo.setSupplyNoticeItemInfos(supplyNoticeItemInfoList);
            return supplyNoticeInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 supplyNoticeInfo 对象
     *
     * @param supplyNoticeInfo
     * @return
     */
    @Override
    public boolean delete(SupplyNoticeInfo supplyNoticeInfo) {
        try {
            SupplyNoticeEntity entity = BeanUtils.copyPropertys(supplyNoticeInfo, new SupplyNoticeEntity());
            supplyNoticeRepository.delete(entity);
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
            supplyNoticeRepository.delete(objId);

            deleteBySupplyNotices(objId);   //删除调拨通知行

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
    public SupplyNoticeInfo findOne(String objId) {
        try {
            SupplyNoticeEntity entity = supplyNoticeRepository.findOne(objId);

            List<SupplyNoticeItemInfo> supplyNoticeItemInfoList = new ArrayList<>();
            List<SupplyNoticeItemEntity> supplyNoticeItemEntityList = supplyNoticeItemRepository.findAllBySupplyNoticeObjId(objId);
            if (supplyNoticeItemEntityList != null && supplyNoticeItemEntityList.size() > 0) {
                for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntityList) {
                    supplyNoticeItemInfoList.add(BeanUtils.copyPropertys(supplyNoticeItemEntity, new SupplyNoticeItemInfo()));
                }
            }

            SupplyNoticeInfo supplyNoticeInfo = BeanUtils.copyPropertys(entity, new SupplyNoticeInfo());
            supplyNoticeInfo.setSupplyNoticeItemInfos(supplyNoticeItemInfoList);
            return supplyNoticeInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageResult<SupplyNoticeView> getPageList(PageParam<SupplyNoticeItem> pageParam) {

        //1.查询条件
        SupplyNoticeItem supplyNoticeItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SupplyNoticeView> specification = null;
        if (supplyNoticeItem != null) {
            specification = Specifications.<SupplyNoticeView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(supplyNoticeItem.getDealerCode()), "dealerCode", supplyNoticeItem.getDealerCode())
                    .eq(StringUtils.isNotEmpty(supplyNoticeItem.getDealerName()), "dealerName", supplyNoticeItem.getDealerName())
                    .eq(StringUtils.isNotEmpty(supplyNoticeItem.getServiceManager()), "serviceManager", supplyNoticeItem.getServiceManager())
                    .like(StringUtils.isNotBlank(supplyNoticeItem.getDocNo()), "docNo", "%" + supplyNoticeItem.getDocNo() + "%")
                    .like(StringUtils.isNotBlank(supplyNoticeItem.getSrcDocNo()), "srcDocNo", "%" + supplyNoticeItem.getSrcDocNo() + "%")
                    .like(StringUtils.isNotBlank(supplyNoticeItem.getActivityNoticeDocNo()), "activityNoticeDocNo", "%" + supplyNoticeItem.getActivityNoticeDocNo() + "%")
                    .eq(!supplyNoticeItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", supplyNoticeItem.getStatus())//表单状态
                    .ge(supplyNoticeItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((supplyNoticeItem.getStartDate() != null && supplyNoticeItem.getEndDate() != null), "createdTime", new Range<Date>(supplyNoticeItem.getStartDate(), DateHelper.getEndDate(supplyNoticeItem.getEndDate())))
                    //.in((supplyNoticeItem.getObjiIds() != null && supplyNoticeItem.getObjiIds().size() > 0), "objId", supplyNoticeItem.getObjiIds())
                    .build();
        }

        //3.执行查询
        Page<SupplyNoticeView> pages = supplyNoticeViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //4.数据转换
        //List<SupplyNoticeItem> rows = new ArrayList<>();
        //for(SupplyNoticeView view :pages.getContent()){
        //    SupplyNoticeItem item = new SupplyNoticeItem();
        //    item = BeanUtils.copyPropertys(view,item);
        //    rows.add(item);
        //}
        //
        ////5.返回
        //return PageUtil.getPageResult(rows,pages,pageParam);
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 通过调拨通知单id查一个调拨子行
     *
     * @param supplyNoticeId
     * @return
     */
    @Override
    public List<SupplyNoticeItemInfo> findByNoticeId(String supplyNoticeId) {
        try {
            List<SupplyNoticeItemEntity> supplyItemEntities = supplyNoticeItemRepository.findByNoticeId(supplyNoticeId);
            List<SupplyNoticeItemInfo> supplyItemInfos = new ArrayList<>();
            for (SupplyNoticeItemEntity recycleItemEntity : supplyItemEntities) {
                supplyItemInfos.add(BeanUtils.copyPropertys(recycleItemEntity, new SupplyNoticeItemInfo()));
            }
            return supplyItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除调拨通知行
     *
     * @param supplyNoticeId
     * @return
     */
    @Override
    public boolean deleteBySupplyNotices(String supplyNoticeId) {
        try {
            supplyNoticeItemRepository.deleteBySupplyNotices(supplyNoticeId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
            SupplyNoticeInfo supplyNoticeInfo = JsonHelper.map2Bean(variables.get("entity"), SupplyNoticeInfo.class);
            SupplyNoticeEntity entity = BeanUtils.copyPropertys(supplyNoticeInfo, new SupplyNoticeEntity());
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
            if (!supplyNoticeInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                //UserEntity serviceManager = userRepository.findOne(userInfo.getDealer().getServiceManagerId());
                if (userInfo.getDealer() != null) {
                    variables.put("level", userInfo.getDealer().getLevel());
                } else {
                    variables.put("level", null);
                }

                //判断单据类型
                WarrantyMaintenanceInfo warrantyMaintenanceInfo = warrantyMaintenanceService.findOne(supplyNoticeInfo.getSrcDocID());
                if (warrantyMaintenanceInfo != null) {
                    DictionaryInfo dictionaryInfo = dictionaryService.findDictionaryByCode("14002");
                    variables.put("docType", supplyNoticeInfo.getDocType());
                    if (dictionaryInfo.getName().equals(supplyNoticeInfo.getDocType())) {
                        if (StringUtils.isNotBlank(warrantyMaintenanceInfo.getAgencyId())) {
                            List<UserEntity> userEntities = userRepository.findAllByAgencyId(warrantyMaintenanceInfo.getAgencyId());
                            List<String> userIds = new ArrayList<>();
                            for (UserEntity userEntity : userEntities) {
                                userIds.add(userEntity.getLogId());
                            }
                            variables.put("agencyUsers", userIds);
                        }
                    }
                }

                if (userInfo.getDealer() != null) {
                    if (StringUtils.isNotBlank(userInfo.getDealer().getParentId())) {
                        //一级服务站
                        DealerEntity parentDealer = dealerRepository.findOne(userInfo.getDealer().getParentId());

                        List<UserEntity> list = userRepository.findAllByDealerId(parentDealer.getObjId());
                        List<String> users = new ArrayList<>();
                        for (UserEntity userEntity : list) {
                            System.out.println(userEntity.getLogId());
                            users.add(userEntity.getLogId());
                        }
                        variables.put("firstLevelUsers", users);
                    }
                }


                //启动流程

                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    SupplyNoticeInfo supplyNotice = findOne(supplyNoticeInfo.getObjId());
                    supplyNotice.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    supplyNotice.setStatus(DocStatus.AUDITING.getIndex());
                    save(supplyNotice);

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
     * 检查收货状态
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean checkSupplyReceiveState(String objId) {


        //调拨通知单审核通过,获取调拨通知单子行
        List<SupplyNoticeItemEntity> supplyNoticeItemList = supplyNoticeItemRepository.findByNoticeId(objId);
        List<SupplyItemInfo> supplyItemEntityList = new ArrayList<>();
        //取出所有调拨供货单清单配件
        for (SupplyNoticeItemEntity supplyNoticeItem : supplyNoticeItemList) {
            for (SupplyItemInfo supplyItemInfo : supplyItemService.findAllByNoticeItemId(supplyNoticeItem.getObjId())) {
                supplyItemEntityList.add(supplyItemInfo);
            }

        }
        if (supplyItemEntityList != null && supplyItemEntityList.size() > 0) {
            //已发货数量
            Double supplyAmount = 0.0;
            for (SupplyItemInfo supplyItemEntity : supplyItemEntityList) {
                supplyAmount += supplyItemEntity.getAmount();
            }
            //需要发货数量
            Double supplyNoticeAmount = 0.0;
            for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemList) {
                supplyNoticeAmount += supplyNoticeItemEntity.getRequestAmount();
            }

            for (SupplyItemInfo supplyItemEntity : supplyItemEntityList) {
                //通知单清单配件是否已发完
                if (supplyAmount < supplyNoticeAmount) {
                    return false;
                } else {
                    SupplyInfo supplyInfo = supplyService.findOne(supplyItemEntity.getSupplyId());
                    if (supplyInfo != null) {
                        //判断调拨供货单的单据状态
                        if (!supplyInfo.getStatus().equals(DocStatus.CLOSED) && supplyInfo.getRcvDate() == null) {
                            return false;
                        }
                    } else {
                        return false;
                    }

                }
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * 通过来源编号查询调拨通知单子行
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public List<SupplyItemInfo> findSupplyItemIdsBySrcDocNo(String srcDocNo) {
        List<SupplyItemInfo> supplyItemInfoList = new ArrayList<>();
        try {
            List<String> supplyNoticeItemIdList = new ArrayList<>();
            List<SupplyNoticeItemEntity> noticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocNo("%" + srcDocNo + "%");
            if (noticeItemEntities != null && noticeItemEntities.size() > 0) {
                for (SupplyNoticeItemEntity noticeItemEntity : noticeItemEntities) {
                    supplyNoticeItemIdList.add(noticeItemEntity.getObjId());
                }
                supplyItemInfoList = supplyItemService.findAllByNoticeItemId(supplyNoticeItemIdList);
            }
            return supplyItemInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return supplyItemInfoList;
        }
    }

    /**
     * 通过VIN码查询调拨供货单子行
     *
     * @param vin
     * @return
     */
    @Override
    public List<SupplyItemInfo> findSupplyItemIdsByVin(String vin) {
        List<SupplyItemInfo> supplyItemInfoList = new ArrayList<>();
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
        try {
            VehicleEntity vehicleEntity = vehicleRepository.findOneByVin(vin);
            if (vehicleEntity != null) {
                //来源单据id
                List<String> srcDocIds = new ArrayList<>();
                //根据车辆id查询三包单
                List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllByVehicleObjId(vehicleEntity.getObjId());
                if (warrantyMaintenanceEntities != null && warrantyMaintenanceEntities.size() > 0) {
                    for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintenanceEntities) {
                        srcDocIds.add(warrantyMaintenanceEntity.getObjId());
                    }
                }
                //根据车辆id查活动分配单
                List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicleEntity.getObjId());
                if (activityVehicleEntities != null && activityVehicleEntities.size() > 0) {
                    for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntities) {
                        srcDocIds.add(activityVehicleEntity.getActivityDistributionId());
                    }
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
            }

            return supplyItemInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return supplyItemInfoList;
        }
    }

    /**
     * 通过活动通知单查询调拨通知单objId集合
     *
     * @param activityNoticeDocNo
     * @return
     */
    @Override
    public List<String> findAllObjIdByActivityNoticeDocNo(String activityNoticeDocNo) {
        //调拨通知子行
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
        //调拨通知单id
        List<String> objIds = new ArrayList<>();
        try {
            ActivityNoticeEntity activityNoticeEntity = activityNoticeRepository.findoneByDocNo("%" + activityNoticeDocNo + "%");
            if (activityNoticeEntity != null) {
                List<ActivityDistributionEntity> activityDistributionEntities = activityDistributionRepository.findAllByActivityNoticeObjId(activityNoticeEntity.getObjId());
                if (activityDistributionEntities != null && activityDistributionEntities.size() > 0) {
                    List<String> srcObjids = new ArrayList<>();//活动分配单id
                    for (ActivityDistributionEntity activityDistributionEntity : activityDistributionEntities) {
                        srcObjids.add(activityDistributionEntity.getObjId());
                    }
                    if (srcObjids.size() > 0) {
                        supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocIds(srcObjids);
                    }
                    if (supplyNoticeItemEntities != null && srcObjids.size() > 0) {
                        for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                            objIds.add(supplyNoticeItemEntity.getSupplyNoticeId());
                        }

                    }
                }
            }
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }

    }

    /**
     * 通过VIN查询所有调拨通知单objid
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findSupplyNoticeIdsByVin(String vin) {
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
        List<String> objIds = new ArrayList<>();
        try {
            VehicleEntity vehicleEntity = vehicleRepository.findOneByVin(vin);
            if (vehicleEntity != null) {
                //来源单据id
                List<String> srcDocIds = new ArrayList<>();
                //根据车辆id查询三包单
                List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllByVehicleObjId(vehicleEntity.getObjId());
                if (warrantyMaintenanceEntities != null && warrantyMaintenanceEntities.size() > 0) {
                    for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintenanceEntities) {
                        srcDocIds.add(warrantyMaintenanceEntity.getObjId());
                    }
                }
                //根据车辆id查活动分配单
                List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicleEntity.getObjId());
                if (activityVehicleEntities != null && activityVehicleEntities.size() > 0) {
                    for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntities) {
                        srcDocIds.add(activityVehicleEntity.getActivityDistributionId());
                    }
                }
                if (srcDocIds.size() > 0) {
                    supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocIds(srcDocIds);
                }
                if (supplyNoticeItemEntities.size() > 0) {
                    for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                        objIds.add(supplyNoticeItemEntity.getSupplyNoticeId());
                    }
                }
            }

            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    /**
     * 通过单据编号查询调拨通知单
     *
     * @param docNo
     * @return
     */
    @Override
    public SupplyNoticeInfo findOneByDocNo(String docNo) {

        try {
            return BeanUtils.copyPropertys(supplyNoticeRepository.findOneByDocNo("%" + docNo + "%"), new SupplyNoticeInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean desertTask(String objId) {
        //获取调拨通知单实体
        SupplyNoticeEntity entity = supplyNoticeRepository.findOne(objId);
        //获取调拨通知单明细
        List<SupplyNoticeItemEntity> noticeItemEntityList = supplyNoticeItemRepository.findAllBySupplyNoticeObjId(entity.getObjId());
        for (SupplyNoticeItemEntity supplyNoticeItemEntity : noticeItemEntityList) {
            if (supplyNoticeItemEntity.getSentAmount() > 0) {
                return false;
            }
        }

        try {
            // 如果单子已经关闭
            if (entity.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                processService.deleteHistoricProcessInstance(entity.getProcessInstanceId());
            } else {
                processService.deleteProcessInstance(entity.getProcessInstanceId());
                processService.deleteHistoricProcessInstance(entity.getProcessInstanceId());
            }

            entity.setStatus(DocStatus.OBSOLETE.getIndex());
            if ("活动分配单".equals(entity.getSrcDocType())) {
                ActivityDistributionEntity activityDistributionEntity = activityDistributionRepository.findOne(entity.getSrcDocID());
                if (activityDistributionEntity != null) {
                    activityDistributionEntity.setSupplyNoticeId(null);
                    activityDistributionEntity.setCanEditSupply(true);
                    activityDistributionRepository.save(activityDistributionEntity);
                }
            } else {
                WarrantyMaintenanceEntity maintenanceEntity = warrantyMaintenanceRepository.findOne(entity.getSrcDocID());
                if (maintenanceEntity != null) {
                    maintenanceEntity.setSupplyNoticeId(null);
                    maintenanceEntity.setCanEditSupply(true);
                    warrantyMaintenanceRepository.save(maintenanceEntity);
                }
            }

            for (SupplyNoticeItemEntity supplyNoticeItemEntity : noticeItemEntityList) {
                supplyNoticeItemEntity.setSrcDocID(null);
                supplyNoticeItemEntity.setSrcDocNo(null);
                supplyNoticeItemEntity.setCommissionPartId(null);
            }
            supplyNoticeItemRepository.save(noticeItemEntityList);

            entity.setSrcDocID(null);
            entity.setSrcDocType(null);
            entity.setSrcDocNo(null);
            entity.setProcessInstanceId(null);
            supplyNoticeRepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public SupplyNoticeEntity findOneBySrcDocId(String objId) {
        return supplyNoticeRepository.findOneBySrcDocId(objId);
    }


    /**
     * 保存调拨通知单列表
     *
     * @param supplyNoticeItemInfos
     */
    private List<SupplyNoticeItemInfo> saveSupplyNoticeItems(String supplyNoticeId, List<SupplyNoticeItemInfo> supplyNoticeItemInfos) {
        List<SupplyNoticeItemInfo> supplyNoticeItemInfoList = new ArrayList<>();
        try {

            if (StringUtils.isNotBlank(supplyNoticeId)) {
                for (SupplyNoticeItemInfo supplyNoticeItemInfo : supplyNoticeItemInfos) {
                    SupplyNoticeItemEntity supplyNoticeItemEntity = BeanUtils.copyPropertys(supplyNoticeItemInfo, new SupplyNoticeItemEntity());
                    supplyNoticeItemEntity.setSupplyNoticeId(supplyNoticeId);
                    SupplyNoticeItemEntity save = supplyNoticeItemRepository.save(supplyNoticeItemEntity);
                    supplyNoticeItemInfoList.add(BeanUtils.copyPropertys(save, new SupplyNoticeItemInfo()));
                }
            }
            return supplyNoticeItemInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return supplyNoticeItemInfoList;
        }

    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyNoticeRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return supplyNoticeRepository;
    }

//    @Override
//    public List<SupplyNoticeEntity> getSupplyNoticeList(SupplyNoticeEntity supplyNoticeRequest) {
//        return supplyNoticeRepository.getSupplyNoticeList(supplyNoticeRequest.getDocNo());
//    }
//
//
//    @Override
//    public List<SupplyNoticeItemEntity> findItems(SupplyNoticeEntity supplyNoticeRequest) {
//        return supplyNoticeItemRepository.findSupplyNoticeItemsByNoticeId(supplyNoticeRequest.getObjId());
//
//    }

//    @Override
//    public SupplyNoticeEntity getSupplyNoticeByID(String id) {
//        return supplyNoticeRepository.getSupplyNotice(id);
//    }
//
//    @Override
//    public SupplyNoticeEntity save(SupplyNoticeEntity supplyNotice) {
////        String doc = "";
////        if (StringUtils.isBlank(supplyNotice.getDocNo())) {
////            supplyNotice.setDocNo(documentNoService.getDocumentNo(supplyNotice.getClass().getSimpleName()));
////            supplyNotice.setCreatedTime(new Date());
////            supplyNotice.setCreaterName(CommonHelper.getActiveUser().getUsername());
////            supplyNotice.setCreaterId(CommonHelper.getActiveUser().getUserId());
////        }
//
////        supplyNotice.setModifiedTime(new Date());
////        supplyNotice.setModifierName(CommonHelper.getActiveUser().getUsername());
////        supplyNotice.setModifierId(CommonHelper.getActiveUser().getUserId());
//
//        return supplyNoticeRepository.save(supplyNotice);
////        return doc;
//    }
//
//    @Override
//    public boolean audit(SupplyNoticeEntity entity) {
//        try {
//            entity.setStatus(2);
//            //entity.setModifiedTime(new Date());
//            //entity.setModifierId(CommonHelper.getActiveUser().getUserId());
//            //entity.setModifierName(CommonHelper.getActiveUser().getUsername());
//            supplyNoticeRepository.save(entity);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean delete(SupplyNoticeEntity entity) {
//        try {
//            supplyNoticeRepository.delete(entity);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean close(SupplyNoticeEntity entity) {
//        try {
//            entity.setStatus(3);
//            //entity.setModifiedTime(new Date());
//            //entity.setModifierId(CommonHelper.getActiveUser().getUserId());
//            //entity.setModifierName(CommonHelper.getActiveUser().getUsername());
//
//            supplyNoticeRepository.save(entity);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//    }

}
