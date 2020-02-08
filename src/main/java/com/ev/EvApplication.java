package com.ev;

import com.ev.framework.aspect.WebLogAspect;
import com.ev.framework.socket.Server;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;


@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.ev.*.dao")
@EnableFeignClients(basePackages = "com.ev.**.gateway")
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class EvApplication implements CommandLineRunner {
    @Resource
    private Server socketServer;

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EvApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        socketServer.run(18888);
    }
}
