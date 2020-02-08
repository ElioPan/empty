package com.ev.common.jobs;

import com.ev.framework.config.ApplicationContextRegister;
import com.ev.custom.service.MeasurePointService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Created by gumingjie on 2019-10-16.
 */
//@Component
public class UpdateSortNoJob {


//    @Scheduled(cron="0 0 0 15 1/1 ? ")
    private void executeInternal() {
    	MeasurePointService measurePointService = ApplicationContextRegister.getBean(MeasurePointService.class);
        measurePointService.formatSortNoJob();
    }
    
}
