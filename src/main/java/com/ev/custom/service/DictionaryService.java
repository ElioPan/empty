package com.ev.custom.service;

import com.ev.custom.domain.DictionaryDO;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
public interface DictionaryService {
	
	DictionaryDO get(Long id);

	DictionaryDO getByValue(String value,String type);
	
	List<DictionaryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(DictionaryDO dictionary);
	
	int update(DictionaryDO dictionary);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<DictionaryDO> listByType(String type);

    Map<String,Object> getDictsByType(String type);

	Map<String,Object> getDictMap();
}
