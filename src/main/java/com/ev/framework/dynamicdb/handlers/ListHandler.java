package com.ev.framework.dynamicdb.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ListHandler<T> {

	/**
	 *
	 * @param rs
	 *            数据库结果集对象
	 * @return 返回一个泛型的封装了查询结果的集合
	 * @throws SQLException
	 */
	List<T> handle(ResultSet rs) throws SQLException;

	//有序
	List<T> handleLinked(ResultSet rs) throws SQLException;
}
