package com.ev.framework.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 文件名称： com.ev.framework.config.MybatisPlusConfig.java</br>
 * 初始作者： Mark.Yao</br>
 * 创建日期： 2019年9月6日</br>
 * 功能说明： mybatis插件配置类 <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者 日期 修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2019-2020 .All rights reserved.<br/>
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 方法描述: [分页拦截器.]</br>
     * 初始作者: Mark.Yao<br/>
     * 创建日期: 2019年9月6日-上午11:20:03<br/>
     * 开始版本: 1.0.0<br/>
     * =================================================<br/>
     * 修改记录：<br/>
     * 修改作者 日期 修改内容<br/>
     * ================================================<br/>
     *
     * @return PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

    /**
     * 方法描述: [sql注入器 逻辑删除插件.]</br>
     * 初始作者: Mark.Yao<br/>
     * 创建日期: 2019年9月6日-上午11:19:24<br/>
     * 开始版本: 1.0.0<br/>
     * =================================================<br/>
     * 修改记录：<br/>
     * 修改作者 日期 修改内容<br/>
     * ================================================<br/>
     *
     * @return ISqlInjector
     */
    @Bean
    public ISqlInjector iSqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 方法描述: [sql性能分析插件，输出sql语句及所需时间.]</br>
     * [设置 dev test 环境开启.]<br/>
     * 初始作者: Mark.Yao<br/>
     * 创建日期: 2019年9月6日-上午11:18:35<br/>
     * 开始版本: 1.0.0<br/>
     * =================================================<br/>
     * 修改记录：<br/>
     * 修改作者 日期 修改内容<br/>
     * ================================================<br/>
     *
     * @return PerformanceInterceptor
     */
    @Bean
    @Profile({"dev"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 方法描述: [乐观锁插件.]</br>
     * 初始作者: Mark.Yao<br/>
     * 创建日期: 2019年9月6日-上午11:19:07<br/>
     * 开始版本: 1.0.0<br/>
     * =================================================<br/>
     * 修改记录：<br/>
     * 修改作者 日期 修改内容<br/>
     * ================================================<br/>
     *
     * @return OptimisticLockerInterceptor
     */
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}
