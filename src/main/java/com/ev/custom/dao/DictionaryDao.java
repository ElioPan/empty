package com.ev.custom.dao;

import com.ev.custom.domain.DictionaryDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
@Mapper
public interface DictionaryDao {

	DictionaryDO get(Integer id);
	
	List<DictionaryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(DictionaryDO dictionary);
	
	int update(DictionaryDO dictionary);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	List<DictionaryDO> listByType(String type);
}
