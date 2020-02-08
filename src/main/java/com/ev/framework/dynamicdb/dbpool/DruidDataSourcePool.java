package com.ev.framework.dynamicdb.dbpool;

import com.alibaba.druid.pool.DruidDataSource;
import com.ev.framework.dynamicdb.model.DsModel;

import javax.sql.DataSource;

/**
 * Druid数据源连接池包装类
 * <a href="https://github.com/alibaba/druid/wiki>Druid</a>
 *
 * @author tomdeng
 */
public class DruidDataSourcePool implements DataSourcePoolWrapper {
    @Override
    public DataSource wrap(final DsModel rptDs) {
        try {
            final DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName(rptDs.getDriver());
            dataSource.setUrl(rptDs.getUrl());
            dataSource.setUsername(rptDs.getUser());
            dataSource.setPassword(rptDs.getPassword());
            dataSource.setInitialSize(3);
            dataSource.setMaxActive(20);
            dataSource.setMinIdle(1);
            dataSource.setMaxWait(60000);
            dataSource.setTimeBetweenEvictionRunsMillis(60000);
            dataSource.setMinEvictableIdleTimeMillis(300000);
            dataSource.setValidationQuery("select 1");
            dataSource.setTestWhileIdle(true);
            dataSource.setTestOnBorrow(true);
            dataSource.setTestOnReturn(true);
            dataSource.setMaxOpenPreparedStatements(20);
            dataSource.setRemoveAbandoned(true);
            dataSource.setRemoveAbandonedTimeout(30);
            dataSource.setLogAbandoned(true);
            dataSource.setBreakAfterAcquireFailure(true);
            return dataSource;
        } catch (final Exception ex) {
            throw new RuntimeException("DruidDataSourcePool Create Error", ex);
        }
    }
}
