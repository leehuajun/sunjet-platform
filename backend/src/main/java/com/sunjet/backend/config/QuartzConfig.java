package com.sunjet.backend.config;

import com.sunjet.backend.SpringJobFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author: lhj
 * @create: 2017-07-18 10:27
 * @description: Quartz 的配置类，要求把 Job 执行类放入 Spring 容器管理
 */
@Configuration
public class QuartzConfig {

    @Autowired
    private SpringJobFactory springJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // 把Job交给Spring来管理，这样Job就能使用Spring产生的bean
        schedulerFactoryBean.setJobFactory(springJobFactory);
        // 延迟启动
//        schedulerFactoryBean.setStartupDelay(10);

        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
}
