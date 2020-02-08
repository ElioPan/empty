package com.ev.framework.config;

import com.ev.common.jobs.NoticeJob;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

//@Configuration
public class QuartzConfig {

    @Bean
    JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean bean =  new JobDetailFactoryBean();
        bean.setJobClass(NoticeJob.class);
        //可带参数的定时任务
        JobDataMap jobDataMap = new JobDataMap();
        bean.setJobDataMap(jobDataMap);
        bean.setDurability(true);
        return bean;
    }


    @Bean
    CronTriggerFactoryBean cronTriggerFactoryBean(){
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(jobDetailFactoryBean().getObject());
        bean.setCronExpression("0 50 23 * * ?");
        return bean;
    }

    @Bean
    SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        CronTrigger cronTrigger = cronTriggerFactoryBean().getObject();
        bean.setTriggers(cronTrigger);
        return bean;
    }

}
