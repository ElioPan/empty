package com.ev.framework.dynamicdb.dbpool;

import com.ev.framework.dynamicdb.model.DsModel;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;

/**
 * c3p0数据源连接池包装类
 * <a href="http://www.mchange.com/projects/c3p0/#quickstart>c3po</a>
 *
 * @author tomdeng
 */
public class C3p0DataSourcePool implements DataSourcePoolWrapper {
    @Override
    public DataSource wrap(final DsModel rptDs) {
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(rptDs.getDriver());
            dataSource.setJdbcUrl(rptDs.getUrl());
            dataSource.setUser(rptDs.getUser());
            dataSource.setPassword(rptDs.getPassword());
            dataSource.setInitialPoolSize( 3);
            dataSource.setMinPoolSize(1);
            dataSource.setMaxPoolSize(20);
            dataSource.setMaxStatements(50);
            dataSource.setMaxIdleTime(1800);
            dataSource.setAcquireIncrement(3);
            dataSource.setAcquireRetryAttempts(30);
            dataSource.setIdleConnectionTestPeriod(60);
            dataSource.setBreakAfterAcquireFailure(false);
            dataSource.setTestConnectionOnCheckout(true);
            return dataSource;
        } catch (Exception ex) {
            throw new RuntimeException("C3p0DataSourcePool Create Error", ex);
        }
    }
}
