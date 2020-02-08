package com.ev.framework.config;

import com.ev.common.domain.FastDfsDO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StaticConfig {
    @Value("${upload.config.path}")
    private String uploadConfigPath;

    @Bean
    public int initStatic() {
        FastDfsDO.setUploadConfigPath(uploadConfigPath);
        return 0;
    }

}
