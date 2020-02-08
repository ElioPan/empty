package com.ev.framework.il8n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class Il8nConfig {
    /** 国际化文件路径 */
    @Value("${spring.messages.basename}")
    public String basename;

    /**
     * 用于解析消息的策略接口，支持这些消息的参数化和国际化。
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        return messageSource;
    }

    /**
     * 自定义LocaleResolver
     * @return
     */
    @Bean
    public CusLocaleResolver localeResolver(){
        return new CusLocaleResolver();
    }
}
