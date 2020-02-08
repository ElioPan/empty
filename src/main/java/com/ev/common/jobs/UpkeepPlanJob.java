package com.ev.common.jobs;

import com.ev.framework.config.ApplicationContextRegister;
import com.ev.custom.service.UpkeepPlanService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

/**
 * Created by wangyupeng on 2019-10-16.
 * 定时根据计划的结束时间更新计划单的状态为已完成状态
 */
@Component
public class UpkeepPlanJob {


    @Scheduled(cron="0 0 23/1 2/1 * ? ")
    private void executeInternal() {
        try {
            changePlanStatus();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void changePlanStatus() throws ParseException{


        UpkeepPlanService upkeepPlanService = ApplicationContextRegister.getBean(UpkeepPlanService.class);

        //获取upkeepPlan的所有结束时间和计划id     筛选条件&&计划状态为  129启用 +131 已完成  且delet_flage为未删除状态
        List<Long> planstatusByEndTime = upkeepPlanService.getPlanstatusByEndTime();

        if (planstatusByEndTime.size() > 0) {

            Long[] longs = new Long[planstatusByEndTime.size()];

            for (int i = 0; i < planstatusByEndTime.size(); i++) {

                planstatusByEndTime.get(i);
                longs[i] = planstatusByEndTime.get(i);

            }
            int counts = upkeepPlanService.updateStatus(longs);
//
//            System.out.println("==========planstatusByEndTime.size()===定时任务打印信息：" + planstatusByEndTime.size() + "========================================");
//
//            System.out.println("======================counts===定时任务打印信息：" + counts + "========================================");

        }


    }
}
