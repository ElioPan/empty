package com.ev.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.system.dao.SettingDao;
import com.ev.system.domain.SettingDO;
import com.ev.system.service.SettingService;



@Service
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
	public SettingDO saveOrUpdate(SettingDO setting) {
		if(settingDao.get(setting.getKey()) ==null){
			settingDao.save(setting);
		}else{
			settingDao.update(setting);
		}
		return setting;

	}
}
