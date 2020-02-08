package com.ev.framework.dynamicdb.dbpool;


import com.ev.framework.dynamicdb.model.DsModel;

import javax.sql.DataSource;

/**
 * 数据源连接包装器
 *
 * @author tomdeng
 */
public interface DataSourcePoolWrapper {
    DataSource wrap(DsModel rptDs);
}
