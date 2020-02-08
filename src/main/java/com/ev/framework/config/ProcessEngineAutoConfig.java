package com.ev.framework.config;

import com.ev.system.activiti.factory.CustomGroupEntityManagerFactory;
import com.ev.system.activiti.factory.CustomUserEntityManagerFactory;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ProcessEngineAutoConfig extends AbstractProcessEngineAutoConfiguration {

    @Autowired
    CustomGroupEntityManagerFactory customGroupEntityManagerFactory;

    @Autowired
    CustomUserEntityManagerFactory customUserEntityManagerFactory;

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            @Qualifier("dataSource") DataSource dataSource,
            @Qualifier("transactionManager") PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor) throws IOException {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
        List<SessionFactory> factoryList = new ArrayList<SessionFactory>();
        factoryList.add(customGroupEntityManagerFactory);
        factoryList.add(customUserEntityManagerFactory);
        springProcessEngineConfiguration.setCustomSessionFactories(factoryList);
        return springProcessEngineConfiguration;
    }
}
