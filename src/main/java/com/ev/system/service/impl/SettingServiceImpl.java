package com.ev.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.system.dao.SettingDao;
import com.ev.system.domain.SettingDO;
import com.ev.system.service.SettingService;



@Service
@CacheConfig(cacheNames ="setting")
public class SettingServiceImpl implements SettingService {

	@Autowired
	SettingDao settingDao;

	/**
	 * 通过key获取
	 *
	 * @param key
	 * @return
	 */
	@Override
	@Cacheable(key = "#key")
	public String getSettingValue(String key) {
		return settingDao.get(key)==null?null:settingDao.get(key).getValue();
	}

	/**
	 * 通过key获取
	 *
	 * @param key
	 * @return
	 */
	@Override
	public SettingDO get(String key) {
		return settingDao.get(key);
	}

	/**
	 * 修改
	 *
	 * @param setting
	 * @return
	 */
	@Override
	@CacheEvict(key = "#setting.key")
	public SettingDO saveOrUpdate(SettingDO setting) {
		if(settingDao.get(setting.getKey()) ==null){
			settingDao.save(setting);
		}else{
			settingDao.update(setting);
		}
		return setting;

	}
}
