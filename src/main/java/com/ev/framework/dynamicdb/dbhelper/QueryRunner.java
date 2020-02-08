package com.ev.framework.dynamicdb.dbhelper;


import com.ev.framework.dynamicdb.handlers.BeanListHandler;
import com.ev.framework.dynamicdb.handlers.Handler;
import com.ev.framework.dynamicdb.handlers.MapListHandler;
import com.ev.framework.dynamicdb.handlers.TotalRecordsHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务核心类，实现自动化处理
 * 
 * 包括自动化的增删改查
 *
 */
public class QueryRunner {

	/**
	 * 数据库查询,根据给定的查询条件 返回实例化之后的bean对象
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            进行查询的sql语句
	 * @param handler
	 *            处理结果集的接口实现
	 * @param params
	 *            对应于SQL语句的参数描述
	 * @return 返回实例化Bean对象
	 * @throws Exception
	 */
	public <T> T query(Connection conn, String sql, Handler<T> handler, Object... params) throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject((i + 1), params[i]);
			}
		}
		ResultSet rs = ps.executeQuery();
		return handler.handle(rs);
	}

	/**
	 * 数据库查询,根据给定的查询条件 返回实例化之后的bean对象
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            进行查询的sql语句
	 * @param handler
	 *            处理结果集的接口实现
	 * @return 返回实例化Bean对象
	 * @throws Exception
	 */
	public <T> T query(Connection conn, String sql, Handler<T> handler) throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return handler.handle(rs);
	}

	/**
	 * 数据库查询,根据给定的查询条件 返回实例化之后的bean对象。此处对应于无参的处理方式
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            进行查询的sql语句
	 * @param beanListHandler
	 *            处理结果集的接口实现
	 * @return 返回实例化Bean对象
	 * @throws Exception
	 */
	public <T> List<T> query(Connection conn, String sql, BeanListHandler<T> beanListHandler) throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return beanListHandler.handle(rs);
	}

	/**
	 * 数据库查询,根据给定的查询条件 返回封装了实例化的Bean对象的集合
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            进行查询的sql语句
	 * @param beanListHandler
	 *            处理结果集的接口实现
	 * @param params
	 *            对应于SQL语句的参数描述
	 * @return 返回封装了实例化之后的bean集合
	 * @throws Exception
	 */
	public <T> List<T> query(Connection conn, String sql, BeanListHandler<T> beanListHandler, Object... params)
			throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject((i + 1), params[i]);
			}
		}
		ResultSet rs = ps.executeQuery();
		return beanListHandler.handle(rs);
	}

	/**
	 * 数据库查询,根据给定的查询条件 返回通用Map
	 *
			 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            进行查询的sql语句
	 * @param mapListHandler
	 *            处理结果集的接口实现
	 * @return 返回实例化Bean对象
	 * @throws Exception
	 */
	public List<Map> query(Connection conn, String sql, MapListHandler mapListHandler) throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return mapListHandler.handle(rs);
	}

	public List<LinkedHashMap> queryLinked(Connection conn, String sql, MapListHandler mapListHandler) throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return mapListHandler.handleLinked(rs);
	}

	/**
			* 数据库查询,根据给定的查询条件 返回count(*)总数
	 *
			 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            进行查询的sql语句
	 * @param totalRecordsHandler
	 *            处理结果集的接口实现
	 * @return 返回实例化Bean对象
	 * @throws Exception
	 */
	public Long query4Long(Connection conn, String sql, TotalRecordsHandler totalRecordsHandler) throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return totalRecordsHandler.handle(rs);
	}

	/**
	 * 数据库查询,根据给定的查询条件 返回通用Map
	 *
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            进行查询的sql语句
	 * @param mapListHandler
	 *            处理结果集的接口实现
	 * @param params
	 *            对应于SQL语句的参数描述
	 * @return 返回封装了实例化之后的bean集合
	 * @throws Exception
	 */
	public  List<Map> query(Connection conn, String sql, MapListHandler mapListHandler, Object... params)
			throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject((i + 1), params[i]);
			}
		}
		ResultSet rs = ps.executeQuery();
		return mapListHandler.handle(rs);
	}

	/**
	 * 根据给定的参数实现向数据库中给定SQL语句的update,delete,insert 操作
	 * 
	 * @param conn
	 *            数据库连接对象，用户不必关心其释放问题，这里自动将其释放
	 * @param sql
	 *            数据库查询语句
	 * @param params
	 *            对应于SQL语句占位符的参数列表
	 * @throws Exception
	 */
	public void update(Connection conn, String sql, Object... params) throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i = 0; i < params.length; i++) {
			ps.setObject((i + 1), params[i]);
		}
		ps.executeUpdate();
		DbHelper.release(conn, ps);
	}

}
