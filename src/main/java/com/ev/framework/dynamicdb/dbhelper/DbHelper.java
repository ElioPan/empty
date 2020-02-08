package com.ev.framework.dynamicdb.dbhelper;

import com.ev.framework.dynamicdb.dbpool.DataSourcePoolFactory;
import com.ev.framework.dynamicdb.dbpool.DataSourcePoolWrapper;
import com.ev.framework.dynamicdb.model.DsModel;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供实现数据库的连接获取以及各种连接释放工作
 *
 */
public class DbHelper {

	private static  Logger logger = LoggerFactory.getLogger("DbHelper");
	/**
	 * 用于暂时的存储数据库信息的Map
	 */

	//com.kt.ev.ds.api.dynamicdb.C3p0DataSourcePool
	private static final String datasourcePool = "com.ev.framework.dynamicdb.dbpool.DruidDataSourcePool";
	public static Map<String,Connection> connMap = new ConcurrentHashMap<String,Connection>();

	public static Map<String, DataSource> dbPoolMap = new ConcurrentHashMap<>();
	/**
	 *  注册数据源
	 * 
	 * @throws Exception
	 */
	public static void register(DsModel dsModel,StringRedisTemplate stringRedisTemplate) throws Exception {
		stringRedisTemplate.opsForValue().set("dsModel:"+dsModel.getDsName(),new Gson().toJson(dsModel));
		initDs(dsModel);
	}

	public static void initDs(DsModel dsModel){
		logger.info("初始化数据源:"+dsModel.getDsName()+"----");
		DataSourcePoolWrapper wrap = DataSourcePoolFactory.create(datasourcePool);
		dbPoolMap.put(dsModel.getDsName(),wrap.wrap(dsModel));
	}
	/**
	 *  注册数据源
	 *
	 * @throws Exception
	 */
	public static void unRegister(String dsName,StringRedisTemplate stringRedisTemplate) throws Exception {
		connMap.remove(dsName);
		stringRedisTemplate.delete("dsModel:"+dsName);
	}

	/**
	 * 单例模式下的数据库连接对象获取方式
	 * 
	 * @return 返回单例模式下的数据库连接对象
	 */
	public static Connection getConn(String dsName,StringRedisTemplate stringRedisTemplate) {
		DataSource ds = dbPoolMap.get(dsName);
		if(ds==null){
			String dsJson = stringRedisTemplate.opsForValue().get("dsModel:"+dsName);
			DsModel dsModel = new Gson().fromJson(dsJson,DsModel.class);
			DataSourcePoolWrapper wrap = DataSourcePoolFactory.create(datasourcePool);
			ds = wrap.wrap(dsModel);
			dbPoolMap.put(dsModel.getDsName(),ds);
		}
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("获取数据库连接时出错！ :\n" + e);
		}
//		try {
//			String dsJson = stringRedisTemplate.opsForValue().get(dsName);
//			if(dsJson == null){
//				throw new RuntimeException("获取不到配置文件！" + dsName);
//			}
//			DsModel map = new Gson().fromJson(dsJson,DsModel.class);
//			Connection conn = null;
//			if((conn = connMap.get(dsName)) == null){
//				// 当map取值为null时再加锁判断
//				synchronized(connMap) {
//					if((conn = connMap.get(dsName)) == null) {
//						String DRIVER = map.getDriver();
//						String URL = map.getUrl();
//						String USER = map.getUser();
//						String PASSWORD = map.getPassword();
//						Class.forName(DRIVER);
//						conn = DriverManager.getConnection(URL, USER, PASSWORD);
//						connMap.put(dsName,conn);
//						return conn;
//					}else{
//						conn = connMap.get(dsName);
//						Statement stmt = null;
//						try {
//							stmt = conn.createStatement();
//							ResultSet rs = stmt.executeQuery("select 1");
//						} catch (Exception e) {
//							String DRIVER = map.getDriver();
//							String URL = map.getUrl();
//							String USER = map.getUser();
//							String PASSWORD = map.getPassword();
//							Class.forName(DRIVER);
//							conn = DriverManager.getConnection(URL, USER, PASSWORD);
//							connMap.put(dsName,conn);
//							return conn;
//						}finally {
//							if (stmt!=null){
//								stmt.close();
//							}
//						}
//						return connMap.get(dsName);
//					}
//				}
//			}else{
//				conn = connMap.get(dsName);
//				Statement stmt = null;
//				try {
//					stmt = conn.createStatement();
//					ResultSet rs = stmt.executeQuery("select 1");
//				} catch (Exception e) {
//					String DRIVER = map.getDriver();
//					String URL = map.getUrl();
//					String USER = map.getUser();
//					String PASSWORD = map.getPassword();
//					Class.forName(DRIVER);
//					conn = DriverManager.getConnection(URL, USER, PASSWORD);
//					connMap.put(dsName,conn);
//					return conn;
//				}finally {
//					if (stmt!=null){
//						stmt.close();
//					}
//				}
//				return connMap.get(dsName);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException("获取数据库连接时出错！ :\n" + e);
//		}
	}

	/**
	 * 释放数据库连接对象
	 * 
	 * @param conn
	 *            给定的数据库连接对象
	 */
	public static void release(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库连接对象时出错！ :\n" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放数据库连接对象以及数据库查询对象
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param stmt
	 *            数据库查询语句
	 */
	public static void release(Connection conn, Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库查询对象Statement时出错！ :\n" + e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库连接对象时出错！ :\n" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void release(Connection conn, Statement stmt, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(" 关闭数据库结果集对象时出错！:\n" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库查询对象Statement时出错！ :\n" + e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库连接对象时出错！ :\n" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
