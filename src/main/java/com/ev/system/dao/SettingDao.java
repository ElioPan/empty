package com.ev.system.dao;

import com.ev.system.domain.SettingDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-04-15 15:23:52
 */
@Mapper
public interface SettingDao {

	SettingDO get(String key);
	
	List<SettingDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SettingDO setting);
	
	int update(SettingDO setting);

	int updateAll(SettingDO setting);
	
	int remove(String key);
	
	int batchRemove(String[] keys);
}
