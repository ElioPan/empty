package com.ev.custom.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DdApproveListener implements TaskListener {
    private static final Logger logger = LoggerFactory.getLogger(DdApproveListener.class);

    private Expression arg;

    public Expression getArg() {
        return arg;
    }

    public void setArg(Expression arg) {
        this.arg = arg;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        // 实现TaskListener中的方法
        String eventName = delegateTask.getEventName();
        if ("create".endsWith(eventName)) {
            logger.info("approve create=========");
        } else if ("assignment".endsWith(eventName)) {
            logger.info("approve assignment========");
        } else if ("complete".endsWith(eventName)) {
            logger.info("approve complete===========");
        } else if ("delete".endsWith(eventName)) {
            logger.info("approve delete===========");
        }
    }
}
