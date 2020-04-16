package com.ev.framework.il8n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class MessageSourceHandler {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String messageKey,String[] args) {
        String message = messageSource.getMessage("common.prefix", args, RequestContextUtils.getLocale(request))+messageSource.getMessage(messageKey, args, RequestContextUtils.getLocale(request));
        return message;
    }
}
