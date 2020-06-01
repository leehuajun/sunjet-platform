package com.sunjet.backend.quartz;


import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityDistributionRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityNoticeRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将已经维修和分配的活动车辆更新到主表
 */
@DisallowConcurrentExecution
@Slf4j
public class CheckActivityVehicleDistributeAndRepairStatusHandleJob implements Job {

    @Autowired
    ActivityNoticeRepository activityNoticeRepository;
    @Autowired
    ActivityVehicleRepository activityVehicleRepository;
    @Autowired
    ActivityDistributionRepository activityDistributionRepository;
    @Autowired
    ActivityMaintenanceRepository activityMaintenanceRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            //获取已经关闭的活动通知单
            List<ActivityNoticeEntity> activityNoticeEntities = activityNoticeRepository.findCloseActivityNotices();
            for (ActivityNoticeEntity activityNoticeEntity : activityNoticeEntities) {
                //查询为分配的
                List<ActivityVehicleEntity> undistributedActivityVehicles = activityVehicleRepository.findUndistributedActivityVehiclesByNoticeId(activityNoticeEntity.getObjId());
                //更新活动通知单的分配状态
                if (undistributedActivityVehicles.size() == 0) {
                    activityNoticeEntity.setDistribute(true);
                    activityNoticeRepository.save(activityNoticeEntity);
                }
                List<ActivityVehicleEntity> unRepairActivityVehicles = activityVehicleRepository.findUnRepairActivityVehiclesByNoticeId(activityNoticeEntity.getObjId());
                //更新活动通知单的维修状态
                if (unRepairActivityVehicles.size() == 0) {
                    activityNoticeEntity.setRepair(true);
                    activityNoticeRepository.save(activityNoticeEntity);
                }

            }
            //更新已关闭的活动分配单的维修状态
            List<ActivityDistributionEntity> activityDistributionEntities = activityDistributionRepository.findCloseActivityDistribution();

            for (ActivityDistributionEntity activityDistributionEntity : activityDistributionEntities) {
                //查询活动分配单,未维修的车辆
                List<ActivityVehicleEntity> undistributedActivityVehicles = activityVehicleRepository.findUnrepairActivityVehiclesByDistributionId(activityDistributionEntity.getObjId());
                //没有剩余未维修的车辆,将活动分配单维修状态改为true
                if (undistributedActivityVehicles.size() == 0) {
                    activityDistributionEntity.setRepair(true);
                    activityDistributionRepository.save(activityDistributionEntity);
                } else {
                    activityDistributionEntity.setRepair(false);
                    activityDistributionRepository.save(activityDistributionEntity);
                }

                //查询活动分配单,已维修的车辆
                List<ActivityVehicleEntity> distributedActivityVehicles = activityVehicleRepository.findrepairActivityVehiclesByDistributionId(activityDistributionEntity.getObjId());
                for (ActivityVehicleEntity distributedActivityVehicle : distributedActivityVehicles) {
                    List<ActivityMaintenanceEntity> activityMaintenanceEntities = activityMaintenanceRepository.findAllByActivityVehicleId(distributedActivityVehicle.getObjId());
                    //如果没有关联活动服务单 取消维修状态
                    if (activityMaintenanceEntities == null && activityMaintenanceEntities.size() == 0) {
                        distributedActivityVehicle.setRepair(false);
                        activityVehicleRepository.save(distributedActivityVehicle);
                    }
                }

            }
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }


}

