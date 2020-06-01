package com.sunjet.backend.modules.asms.service.activity;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityMaintenanceView;
import com.sunjet.backend.modules.asms.entity.asm.CommissionPartEntity;
import com.sunjet.backend.modules.asms.entity.asm.GoOutEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityMaintenanceViewRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import com.sunjet.backend.modules.asms.repository.asm.CommissionPartRepository;
import com.sunjet.backend.modules.asms.repository.asm.GoOutRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.activity.ActivityMaintenanceInfo;
import com.sunjet.dto.asms.activity.ActivityMaintenanceItem;
import com.sunjet.dto.asms.asm.CommissionPartInfo;
import com.sunjet.dto.asms.asm.GoOutInfo;
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

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * 服务活动维修
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("activityMaintenanceService")
public class ActivityMaintenanceServiceImpl implements ActivityMaintenanceService {
    @Autowired
    private ActivityMaintenanceRepository activityMaintenanceRepository;

    @Autowired
    private ActivityMaintenanceViewRepository activityMaintenanceViewRepository;

    @Autowired
    private CommissionPartRepository commissionPartRepository;

    @Autowired
    private GoOutRepository goOutRepository;

    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;   //活动车辆

    @Autowired
    private ProcessService processService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DocumentNoService documentNoService;

    @Autowired
    private ActivityDistributionService activityDistributionService;


    /**
     * 保存 实体
     *
     * @param activityMaintenanceInfo
     * @return
     */
    @Override
    public ActivityMaintenanceInfo save(ActivityMaintenanceInfo activityMaintenanceInfo) {
        if (activityMaintenanceInfo != null && StringUtils.isBlank(activityMaintenanceInfo.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(ActivityMaintenanceEntity.class.getSimpleName());
            activityMaintenanceInfo.setDocNo(docNo);
        }
        try {
            ActivityMaintenanceEntity entity = activityMaintenanceRepository.save(BeanUtils.copyPropertys(activityMaintenanceInfo, new ActivityMaintenanceEntity()));
            //保存维修配件
            List<CommissionPartInfo> commissionPartInfoList = saveCommissionParts(entity, activityMaintenanceInfo.getCommissionParts());
            //保存外出费用
            List<GoOutInfo> goOutInfos = saveGoOuts(entity, activityMaintenanceInfo.getGoOuts());
            ActivityMaintenanceInfo activityMaintenance = BeanUtils.copyPropertys(entity, new ActivityMaintenanceInfo());

            activityMaintenance.setCommissionParts(commissionPartInfoList);
            activityMaintenance.setGoOuts(goOutInfos);
            return activityMaintenance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除 --> 通过 activityMaintenanceInfo 对象
     *
     * @param activityMaintenanceInfo
     * @return
     */
    @Override
    public boolean delete(ActivityMaintenanceInfo activityMaintenanceInfo) {
        try {
            activityMaintenanceRepository.delete(activityMaintenanceInfo.getObjId());

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
            if (StringUtils.isNotBlank(objId)) {
                ActivityMaintenanceEntity activityMaintenance = activityMaintenanceRepository.findOne(objId);
                if (StringUtils.isNotBlank(activityMaintenance.getActivityVehicleId())) {
                    //解除活动车辆跟活动服务单的关系
                    ActivityVehicleEntity activityVehicleEntity = activityVehicleRepository.findOne(activityMaintenance.getActivityVehicleId());
                    if (activityVehicleEntity != null) {
                        activityVehicleEntity.setActivityMaintenanceId(null);
                        activityVehicleEntity.setRepair(false);
                    }
                    activityVehicleRepository.save(activityVehicleEntity);

                }
            }
            activityMaintenanceRepository.delete(objId);
            commissionPartRepository.deleteByActivityMaintenanceId(objId);
            goOutRepository.deleteByActivityMaintenanceId(objId);

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
    public ActivityMaintenanceInfo findAll(String objId) {
        try {
            ActivityMaintenanceEntity Entity = activityMaintenanceRepository.findOne(objId);
            return BeanUtils.copyPropertys(Entity, new ActivityMaintenanceInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查询分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<ActivityMaintenanceView> getPageList(PageParam<ActivityMaintenanceItem> pageParam) {
        //1.查询条件
        ActivityMaintenanceItem activityMaintenanceItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ActivityMaintenanceView> specification = null;
        if (activityMaintenanceItem != null) {
            specification = Specifications.<ActivityMaintenanceView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    //.eq(activityMaintenanceItem.getStatus() != null, "status", activityMaintenanceItem.getStatus())
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getDocNo()), "docNo", "%" + activityMaintenanceItem.getDocNo() + "%")
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getDealerName()), "dealerName", "%" + activityMaintenanceItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getDealerCode()), "dealerCode", "%" + activityMaintenanceItem.getDealerCode() + "%")
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getAadDocNo()), "aadDocNo", "%" + activityMaintenanceItem.getAadDocNo() + "%")
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getAanDocNo()), "aanDocNo", "%" + activityMaintenanceItem.getAanDocNo() + "%")
                    .eq(StringUtils.isNotBlank(activityMaintenanceItem.getServiceManager()), "serviceManager", activityMaintenanceItem.getServiceManager())
                    .eq(!activityMaintenanceItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", activityMaintenanceItem.getStatus())//表单状态
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getVin()), "vin", "%" + activityMaintenanceItem.getVin() + "%")
                    .ge(activityMaintenanceItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getPlate()), "plate", "%" + activityMaintenanceItem.getPlate() + "%")
                    .like(StringUtils.isNotBlank(activityMaintenanceItem.getSender()), "sender", "%" + activityMaintenanceItem.getSender() + "%")
                    .between((activityMaintenanceItem.getStartDate() != null && activityMaintenanceItem.getEndDate() != null), "createdTime", new Range<Date>(activityMaintenanceItem.getStartDate(), DateHelper.getEndDate(activityMaintenanceItem.getEndDate())))
                    //.in((activityMaintenanceItem.getVehicleObjIds() != null && activityMaintenanceItem.getVehicleObjIds().size() > 0), "activityVehicleId", activityMaintenanceItem.getVehicleObjIds())
                    .build();
        }


        //3.执行查询
        Page<ActivityMaintenanceView> pages = activityMaintenanceViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 通过objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public ActivityMaintenanceEntity findOneById(String objId) {
        return activityMaintenanceRepository.findOne(objId);
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
            ActivityMaintenanceInfo activityMaintenanceInfo = JsonHelper.map2Bean(variables.get("entity"), ActivityMaintenanceInfo.class);
            //校验单据状态
            if (!activityMaintenanceInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                ActivityMaintenanceEntity entity = BeanUtils.copyPropertys(activityMaintenanceInfo, new ActivityMaintenanceEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                UserEntity userEntity = userRepository.findOne(userInfo.getDealer().getServiceManagerId());
                variables.put("level", userInfo.getDealer().getLevel());
                variables.put("serviceManager", userEntity.getLogId());
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    activityMaintenanceInfo.setProcessInstanceId(processInstanceInfo.getId());
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                }

                //变更状态

                activityMaintenanceInfo.setStatus(DocStatus.AUDITING.getIndex());
                save(activityMaintenanceInfo);

                message.put("result", "提示");
                message.put("message", "提交成功");
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
     * 通过车辆id 获取活动服务车辆id
     *
     * @param vehicleObjIds
     * @return
     */
    @Override
    public List<String> findAllIdByVehcicleIds(List<String> vehicleObjIds) {
        List<String> objIds = new ArrayList<>();
        List<ActivityMaintenanceEntity> activityMaintenanceEntities = new ArrayList<>();
        try {
            List<ActivityVehicleEntity> activityVehicleEntityList = activityVehicleRepository.findAllByVehicelIds(vehicleObjIds);
            if (activityVehicleEntityList != null && activityVehicleEntityList.size() > 0) {
                List<String> activityVehicleids = new ArrayList<>();
                for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntityList) {
                    activityVehicleids.add(activityVehicleEntity.getObjId());
                }
                if (activityVehicleids.size() > 0) {
                    activityMaintenanceEntities = activityMaintenanceRepository.findAllByActivityVehicleids(activityVehicleids);
                }
                if (activityMaintenanceEntities.size() > 0) {
                    for (ActivityMaintenanceEntity activityMaintenance : activityMaintenanceEntities) {
                        objIds.add(activityMaintenance.getObjId());
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
     * 保存维修配件
     *
     * @param entity
     * @param commissionParts
     */
    private List<CommissionPartInfo> saveCommissionParts(ActivityMaintenanceEntity entity, List<CommissionPartInfo> commissionParts) {
        List<CommissionPartInfo> commissionPartInfoList = new ArrayList<>();
        for (CommissionPartInfo commissionPart : commissionParts) {
            CommissionPartEntity commissionPartEntity = BeanUtils.copyPropertys(commissionPart, new CommissionPartEntity());
            commissionPartEntity.setActivityMaintenanceId(entity.getObjId());
            CommissionPartEntity save = commissionPartRepository.save(commissionPartEntity);
            commissionPartInfoList.add(BeanUtils.copyPropertys(save, new CommissionPartInfo()));

        }
        return commissionPartInfoList;

    }


    /**
     * 保存外出信息
     *
     * @param entity
     * @param goOuts
     */
    private List<GoOutInfo> saveGoOuts(ActivityMaintenanceEntity entity, List<GoOutInfo> goOuts) {
        List<GoOutInfo> goOutInfos = new ArrayList<>();
        for (GoOutInfo goOut : goOuts) {
            GoOutEntity goOutEntity = BeanUtils.copyPropertys(goOut, new GoOutEntity());
            goOutEntity.setActivityMaintenanceId(entity.getObjId());
            GoOutEntity go = goOutRepository.save(goOutEntity);
            goOutInfos.add(BeanUtils.copyPropertys(go, new GoOutInfo()));
        }
        return goOutInfos;

    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityMaintenanceRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return activityMaintenanceRepository;
    }

    /**
     * 通过车辆ID查所有服务单
     *
     * @param vehicleIds
     * @return
     */
    @Override
    public List<ActivityMaintenanceInfo> findAllByVehicleIds(List<String> vehicleIds) {
        List<ActivityMaintenanceInfo> activityMaintenanceInfoList = new ArrayList<>();
        try {

            List<ActivityMaintenanceEntity> activityMaintenanceEntities = activityMaintenanceRepository.findAllByVehicleId(vehicleIds);
            if (activityMaintenanceEntities != null && activityMaintenanceEntities.size() > 0) {
                for (ActivityMaintenanceEntity activityMaintenanceEntity : activityMaintenanceEntities) {
                    activityMaintenanceInfoList.add(BeanUtils.copyPropertys(activityMaintenanceEntity, new ActivityMaintenanceInfo()));
                }
            }

            return activityMaintenanceInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return activityMaintenanceInfoList;
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
        ActivityMaintenanceEntity entity = activityMaintenanceRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                ActivityVehicleEntity activityVehicleEntity = activityVehicleRepository.findOne(entity.getActivityVehicleId());
                activityVehicleEntity.setRepair(false);
                activityVehicleEntity.setActivityMaintenanceId(null);
                activityVehicleRepository.save(activityVehicleEntity);
                //清空活动单车辆绑定
                entity.setActivityVehicleId(null);
                activityMaintenanceRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
