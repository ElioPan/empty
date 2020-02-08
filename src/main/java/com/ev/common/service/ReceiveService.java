package com.ev.common.service;

import org.springframework.stereotype.Service;

/**
 * @author AirOrangeWorkSpace
 *
 */
@Service
public interface ReceiveService {

	/**
	 * 保存服务码进入Redis数据库
	 */
	void saveServiceCodeForRedis(String serviceCode);

	/**
	 * 保存服务码进入mysql数据库
	 */
	void saveServiceCodeForMysql(String serviceCode);

	/**
	 * 获取服务码通过Redis
	 */
	String getServiceCodeByRedis();

	/**
	 * 获取服务码通过mysql
	 */
	String getServiceCodeByMysql();

	/**
	  *  判断服务码能否使用
	 */
	boolean is(String serviceCode) throws Exception;

}
