package com.ev.custom.service;

import com.ev.custom.domain.UomDO;

import java.util.List;
import java.util.Map;

/**
 * 单位
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
public interface UomService {
	
	UomDO get(Integer id);
	
	List<UomDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UomDO uom);
	
	int update(UomDO uom);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	UomDO getByName(String childTypeName);
}
