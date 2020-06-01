package com.sunjet.backend.modules.asms.service.quartz;

import com.sunjet.backend.system.entity.ScheduleJobEntity;
import com.sunjet.backend.system.repository.ScheduleJobRepository;
import com.sunjet.backend.utils.BeanUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author: lhj
 * @create: 2017-07-18 12:49
 * @description: Schecule 计划任务 服务类
 * <p>
 * com.sunjet.backend.quartz.ActivityMaintenanceSettlementHandleJob
 */
@Service
public class SchedulerService {

    @Autowired
    ScheduleJobRepository scheduleJobRepository;
    @Autowired
    private Scheduler scheduler;

    @PostConstruct    // 等同于 init-method
    public void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        try {
            //获取已经启用的定时任务
            for (ScheduleJobEntity job : scheduleJobRepository.findAllByEnabled()) {

                // 定义JobDetail
                JobDetail jobDetail = JobBuilder
//                        .newJob(HelloJob.class)
                        .newJob(((Job) Class.forName(BeanUtils.getClassTypeByClassName(job.getJobClass())).newInstance()).getClass())
                        .withIdentity(job.getJobName(), job.getJobGroup())
                        .build();

                // 定义Trigger
                CronScheduleBuilder builder = CronScheduleBuilder
                        .cronSchedule(job.getCronExpression());

                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(job.getJobName(), job.getJobGroup())
                        .startNow()
                        .withSchedule(builder)
                        .build();


                scheduler.scheduleJob(jobDetail, trigger);
            }


        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
