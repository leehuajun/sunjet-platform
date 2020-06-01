package com.sunjet.backend.modules.asms.service.recycle;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticeView;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeViewRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItem;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
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
 * 故障件返回通知
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("recycleNoticeService")
public class RecycleNoticeServiceImpl implements RecycleNoticeService {
    @Autowired
    private RecycleNoticeRepository recycleNoticeRepository;
    @Autowired
    private RecycleNoticeViewRepository recycleNoticeViewRepository;
    @Autowired
    private RecycleNoticeItemService recycleNoticeItemService;
    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<RecycleNoticeView> getPageList(PageParam<RecycleNoticeItem> pageParam) {
        //1.查询条件
        RecycleNoticeItem recycleNoticeItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RecycleNoticeView> specification = null;
        if (recycleNoticeItem != null) {
            specification = Specifications.<RecycleNoticeView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(recycleNoticeItem.getDealerCode()), "dealerCode", recycleNoticeItem.getDealerCode())
                    .eq(StringUtils.isNotBlank(recycleNoticeItem.getDealerName()), "dealerName", recycleNoticeItem.getDealerName())
                    .like(StringUtils.isNotBlank(recycleNoticeItem.getDocNo()), "docNo", "%" + recycleNoticeItem.getDocNo() + "%")
                    .like(StringUtils.isNotBlank(recycleNoticeItem.getSrcDocNo()), "srcDocNo", "%" + recycleNoticeItem.getSrcDocNo() + "%")
                    .like(StringUtils.isNotBlank(recycleNoticeItem.getSrcDocType()), "srcDocType", "%" + recycleNoticeItem.getSrcDocType() + "%")
                    .eq(!recycleNoticeItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", recycleNoticeItem.getStatus())//表单状态
                    .ge(recycleNoticeItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((recycleNoticeItem.getStartDate() != null && recycleNoticeItem.getEndDate() != null), "createdTime", new Range<Date>(recycleNoticeItem.getStartDate(), DateHelper.getEndDate(recycleNoticeItem.getEndDate())))
                    //.in((recycleNoticeItem.getObjIds() != null && recycleNoticeItem.getObjIds().size() > 0), "objId", recycleNoticeItem.getObjIds())
                    .build();
        }

        //3.执行查询
        Page<RecycleNoticeView> pages = recycleNoticeViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

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
     * 通过info 保存一个实体
     *
     * @param recycleNoticeInfo
     * @return
     */
    @Override
    public RecycleNoticeInfo save(RecycleNoticeInfo recycleNoticeInfo) {
        try {
            if (recycleNoticeInfo != null && StringUtils.isBlank(recycleNoticeInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(RecycleNoticeEntity.class.getSimpleName());
                recycleNoticeInfo.setDocNo(docNo);
            }
            RecycleNoticeEntity entity = recycleNoticeRepository.save(BeanUtils.copyPropertys(recycleNoticeInfo, new RecycleNoticeEntity()));
            //保存故障件通知单明细
            List<RecycleNoticeItemInfo> recycleNoticeItemInfos = saveRecycleNoticeitem(entity, recycleNoticeInfo.getRecycleNoticeItemInfoList());
            //返回保存后的值
            RecycleNoticeInfo recycleNotice = BeanUtils.copyPropertys(entity, recycleNoticeInfo);
            recycleNotice.setRecycleNoticeItemInfoList(recycleNoticeItemInfos);
            return recycleNotice;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 保存故障件明细
     *
     * @param entity
     * @param recycleNoticeItemInfoList
     */
    private List<RecycleNoticeItemInfo> saveRecycleNoticeitem(RecycleNoticeEntity entity, List<RecycleNoticeItemInfo> recycleNoticeItemInfoList) {
        List<RecycleNoticeItemInfo> recycleNoticeItemInfos = new ArrayList<>();
        if (recycleNoticeItemInfoList != null) {
            for (RecycleNoticeItemInfo recycleNoticeItemInfo : recycleNoticeItemInfoList) {
                recycleNoticeItemInfo.setRecycleNoticeId(entity.getObjId());
                recycleNoticeItemInfos.add(recycleNoticeItemService.save(recycleNoticeItemInfo));
            }
        }

        return recycleNoticeItemInfos;

    }

    /**
     * 通过objId 查找一个entity
     *
     * @param objId
     * @return info
     */
    @Override
    public RecycleNoticeInfo findOne(String objId) {
        try {
            RecycleNoticeEntity entity = recycleNoticeRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new RecycleNoticeInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info 删除 entity
     *
     * @param recycleNoticeInfo
     * @return
     */
    @Override
    public boolean delete(RecycleNoticeInfo recycleNoticeInfo) {
        try {
            recycleNoticeRepository.delete(recycleNoticeInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过 objId 删除entity
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            recycleNoticeRepository.delete(objId);
            recycleNoticeItemService.deleteByRecycleNoticeId(objId);    //删除与返回通知单子行相关联的数据
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
            RecycleNoticeInfo recycleNoticeInfo = JsonHelper.map2Bean(variables.get("entity"), RecycleNoticeInfo.class);
            if (!recycleNoticeInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                RecycleNoticeEntity entity = BeanUtils.copyPropertys(recycleNoticeInfo, new RecycleNoticeEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);

                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程

                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    RecycleNoticeInfo recycleNotice = findOne(recycleNoticeInfo.getObjId());
                    recycleNotice.setProcessInstanceId(processInstanceInfo.getId());

                    //变更状态

                    recycleNotice.setStatus(DocStatus.CLOSED.getIndex());
                    save(recycleNotice);
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
     * 通过车辆vin码查询故障件返回单objIds
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllRecycleNoticeObjIdsByVin(String vin) {
        //故障件返回通知单objid集合;
        List<String> objIds = new ArrayList<>();
        //故障件发回单通知单
        List<RecycleNoticeEntity> recycleNoticeEntities = new ArrayList<>();

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
                //List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicleEntity.getObjId());
                //if (activityVehicleEntities != null && activityVehicleEntities.size() > 0) {
                //    for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntities) {
                //        srcDocIds.add(activityVehicleEntity.getActivityDistributionId());
                //    }
                //}
                if (srcDocIds.size() > 0) {
                    recycleNoticeEntities = recycleNoticeRepository.findAllBySrcDocIds(srcDocIds);
                }
                if (recycleNoticeEntities.size() > 0) {
                    for (RecycleNoticeEntity recycleNoticeEntity : recycleNoticeEntities) {
                        objIds.add(recycleNoticeEntity.getObjId());
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
        RecycleNoticeEntity entity = recycleNoticeRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setSrcDocID(null);
                entity.setSrcDocType(null);
                entity.setSrcDocNo(null);
                entity.setProcessInstanceId(null);
                recycleNoticeRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return recycleNoticeRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return recycleNoticeRepository;
    }
}
