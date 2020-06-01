package com.sunjet.backend.modules.asms.service.recycle;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleView;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleItemRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleViewRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.recycle.RecycleInfo;
import com.sunjet.dto.asms.recycle.RecycleItem;
import com.sunjet.dto.asms.recycle.RecycleItemInfo;
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
 * 故障件返回
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("recycleService")
public class RecycleServiceImpl implements RecycleService {
    @Autowired
    private RecycleRepository recycleRepository;
    @Autowired
    private RecycleViewRepository recycleViewRepository;

    @Autowired
    private DocumentNoService documentNoService;

    @Autowired
    private RecycleItemService recycleItemService;

    @Autowired
    private RecycleItemRepository recycleItemRepository;

    @Autowired
    private ProcessService processService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private RecycleNoticeItemRepository recycleNoticeItemRepository;


    /**
     * 通过info保存一个实体
     *
     * @param recycleInfo
     * @return
     */
    @Override
    public RecycleInfo save(RecycleInfo recycleInfo) {
        try {
            if (recycleInfo != null && StringUtils.isBlank(recycleInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(RecycleEntity.class.getSimpleName());
                recycleInfo.setDocNo(docNo);
            }
            RecycleEntity entity = recycleRepository.save(BeanUtils.copyPropertys(recycleInfo, new RecycleEntity()));
            //保存故障件返回单子行
            List<RecycleItemInfo> list = saveRecycleItemList(recycleInfo.getRecycleItemInfoList(), entity.getObjId());

            recycleInfo = BeanUtils.copyPropertys(entity, recycleInfo);
            recycleInfo.setRecycleItemInfoList(list);

            return recycleInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 保存故障件返回单子行
     *
     * @param recycleItemInfoList
     * @param recycleObjId
     */
    private List<RecycleItemInfo> saveRecycleItemList(List<RecycleItemInfo> recycleItemInfoList, String recycleObjId) {
        List<RecycleItemInfo> list = new ArrayList<>();
        try {

            if (recycleItemInfoList != null) {
                for (RecycleItemInfo recycleItemInfo : recycleItemInfoList) {
                    recycleItemInfo.setRecycle(recycleObjId);
                    RecycleItemInfo save = recycleItemService.save(recycleItemInfo);
                    list.add(save);
                }
            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }

    }

    /**
     * 通过objId查找一个info
     *
     * @param objId
     * @return RecycleInfo
     */
    @Override
    public RecycleEntity findOne(String objId) {
        try {
            RecycleEntity entity = recycleRepository.findOne(objId);
            //List<RecycleItemInfo> recycleItemInfos = recycleItemService.findByRecycle(objId);
            //RecycleInfo recycleInfo = BeanUtils.copyPropertys(entity, new RecycleInfo());
            //recycleInfo.setRecycleItemInfoList(recycleItemInfos);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info删除实体
     *
     * @param recycleInfo
     * @return 是否删除
     */
    @Override
    public boolean delete(RecycleInfo recycleInfo) {
        try {
            recycleRepository.delete(recycleInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 通过obji 删除一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            //删除父表
            recycleRepository.delete(objId);
            //删除子表
            recycleItemService.deleteByRecycleObjId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PageResult<RecycleView> getPageList(PageParam<RecycleItem> pageParam) {
        //1.查询条件
        RecycleItem recycleItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RecycleView> specification = null;
        if (recycleItem != null) {
            specification = Specifications.<RecycleView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(recycleItem.getDocNo()), "docNo", "%" + recycleItem.getDocNo() + "%")
                    .like(StringUtils.isNotBlank(recycleItem.getLogisticsNum()), "logisticsNum", "%" + recycleItem.getLogisticsNum() + "%")
                    .like(StringUtils.isNotBlank(recycleItem.getSrcDocNo()), "srcDocNo", "%" + recycleItem.getSrcDocNo() + "%")
                    .between((recycleItem.getStartDate() != null && recycleItem.getEndDate() != null), "createdTime", new Range<Date>(recycleItem.getStartDate(), DateHelper.getEndDate(recycleItem.getEndDate())))
                    .eq(StringUtils.isNotBlank(recycleItem.getDealerCode()), "dealerCode", recycleItem.getDealerCode())
                    .eq(StringUtils.isNotBlank(recycleItem.getDealerName()), "dealerName", recycleItem.getDealerName())
                    .eq(StringUtils.isNotBlank(recycleItem.getServiceManager()), "serviceManager", recycleItem.getServiceManager())
                    .eq(!recycleItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", recycleItem.getStatus())//表单状态
                    .ge(recycleItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .in((recycleItem.getObjIds() != null && recycleItem.getObjIds().size() > 0), "objId", recycleItem.getObjIds())
                    .build();
        }

        //3.执行查询
        Page<RecycleView> pages = recycleViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<WarrantyMaintenanceInfo> rows = new ArrayList<>();
        //for (WarrantyMaintenanceEntity entity : pages.getContent()) {
        //    WarrantyMaintenanceInfo info = entityToInfo(entity);
        //    rows.add(info);
        //}


        //5.组装分页信息及集合信息
        //PageResult<DealerInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
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
            RecycleInfo recycleInfo = JsonHelper.map2Bean(variables.get("entity"), RecycleInfo.class);
            RecycleEntity entity = BeanUtils.copyPropertys(recycleInfo, new RecycleEntity());
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);

            //去掉没有用的流程变量
            variables.remove("entity");
            variables.remove("userInfo");
            //启动流程
            if (!recycleInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    //RecycleInfo recycle = findOne(recycleInfo.getObjId());
                    RecycleEntity recycle = recycleRepository.findOne(recycleInfo.getObjId());

                    recycle.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    recycle.setStatus(DocStatus.AUDITING.getIndex());
                    recycleRepository.save(recycle);
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
     * 通过来源单号查询故障件objId集合
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public List<String> findAllRecycleObjIdsBySrcDocNo(String srcDocNo) {
        List<String> objIds = new ArrayList<>();
        try {
            List<RecycleItemEntity> recycleItemEntities = recycleItemService.findAllRecycleItemBySrcDocNo("%" + srcDocNo + "%");
            if (recycleItemEntities != null && recycleItemEntities.size() > 0) {
                for (RecycleItemEntity recycleItemEntity : recycleItemEntities) {
                    objIds.add(recycleItemEntity.getRecycle());
                }
            }
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    /**
     * 通过vin 查询故障件objId集合
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllRecycleObjIdsByVin(String vin) {
        //故障件返回通知单objid集合;
        List<String> objIds = new ArrayList<>();
        //故障件发回单通知单
        List<RecycleItemEntity> recycleItemEntities = new ArrayList<>();

        try {
            VehicleEntity vehicleEntity = vehicleRepository.findOneByVin(vin);
            if (vehicleEntity != null) {
                //来源单据id
                List<String> srcDocNo = new ArrayList<>();
                //根据车辆id查询三包单
                List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllByVehicleObjId(vehicleEntity.getObjId());
                if (warrantyMaintenanceEntities != null && warrantyMaintenanceEntities.size() > 0) {
                    for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintenanceEntities) {
                        srcDocNo.add(warrantyMaintenanceEntity.getDocNo());
                    }
                }
                //根据车辆id查活动分配单
                //List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicleEntity.getObjId());
                //if (activityVehicleEntities != null && activityVehicleEntities.size() > 0) {
                //    for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntities) {
                //        srcDocIds.add(activityVehicleEntity.getActivityDistributionId());
                //    }
                //}
                if (srcDocNo.size() > 0) {
                    recycleItemEntities = recycleItemService.findAllRecycleItemBySrcDocNos(srcDocNo);
                }
                if (recycleItemEntities.size() > 0) {
                    for (RecycleItemEntity recycleItemEntity : recycleItemEntities) {
                        objIds.add(recycleItemEntity.getRecycle());
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
     * 作废单据
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean desertTask(String objId) {
        RecycleEntity entity = recycleRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                List<RecycleItemEntity> recycleItemEntityList = recycleItemService.findByRecycle(objId);

                if (recycleItemEntityList != null && recycleItemEntityList.size() > 0) {
                    for (RecycleItemEntity recycleItemEntity : recycleItemEntityList) {
                        RecycleNoticeItemEntity recycleNoticeItem = recycleNoticeItemRepository.findOne(recycleItemEntity.getNoticeItemId());
                        //返回单已发数量回写    已发数量 = 返回单已发数量 - 故障件本次已返回数量
                        recycleNoticeItem.setBackAmount(recycleNoticeItem.getBackAmount() - recycleItemEntity.getBackAmount());
                        //返回单未返回数量回写   未返回数量 = 未返回数量 + 故障件本次已返回数量
                        recycleNoticeItem.setCurrentAmount(recycleNoticeItem.getCurrentAmount() + recycleItemEntity.getBackAmount());
                        recycleNoticeItemRepository.save(recycleNoticeItem);
                        //解除故障件返回单明细和其他单据的关系
                        recycleItemEntity.setNoticeItemId(null);
                        recycleItemEntity.setSrcDocNo(null);
                        recycleItemEntity.setSrcDocType(null);
                        recycleItemRepository.save(recycleItemEntity);

                    }
                }

                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                recycleRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return recycleRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return recycleRepository;
    }
}
