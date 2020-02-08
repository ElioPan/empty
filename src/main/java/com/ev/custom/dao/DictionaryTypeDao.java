package com.ev.custom.dao;

import com.ev.custom.domain.DictionaryTypeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字典类型
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 08:57:07
 */
@Mapper
public interface DictionaryTypeDao {

	DictionaryTypeDO get(Long id);
	
	List<DictionaryTypeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(DictionaryTypeDO dictionaryType);
	
	int update(DictionaryTypeDO dictionaryType);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
