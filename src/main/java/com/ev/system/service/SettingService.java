package com.ev.system.service;

import com.ev.system.domain.SettingDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * 
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-04-15 15:23:52
 */
public interface SettingService {

	/**
	 * 通过key获取
	 * @param key
	 * @return
	 */
	String getSettingValue(String key);

	/**
	 * 通过key获取
	 * @param key
	 * @return
	 */
	SettingDO get(String key);

	/**
	 * 修改
	 * @param setting
	 * @return
	 */
	SettingDO saveOrUpdate(SettingDO setting);
}
