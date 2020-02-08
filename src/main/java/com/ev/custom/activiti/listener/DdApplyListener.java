package com.ev.custom.activiti.listener;

import com.ev.custom.service.LeaveApplyService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DdApplyListener implements TaskListener {
    private static final Logger logger = LoggerFactory.getLogger(DdApplyListener.class);

    @Autowired
    LeaveApplyService leaveApplyService;

    @Override
    public void notify(DelegateTask delegateTask) {
        // 实现TaskListener中的方法
        String eventName = delegateTask.getEventName();
        if ("create".endsWith(eventName)) {
            logger.info("apply create=========");
        } else if ("assignment".endsWith(eventName)) {
            logger.info("apply assignment========");
        } else if ("complete".endsWith(eventName)) {
            logger.info("apply complete===========");
            delegateTask.setVariable("up",0);
            delegateTask.setVariable("down",0);
        }
    }
}
