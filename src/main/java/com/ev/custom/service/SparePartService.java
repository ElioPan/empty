package com.ev.custom.service;

import com.ev.custom.domain.SparePartDO;

import java.util.List;
import java.util.Map;

/**
 * 备品备件分类表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 15:43:45
 */
public interface SparePartService {
	
	SparePartDO get(Long id);
	
	List<SparePartDO> list(Map<String, Object> map);

	List<Map<String,Object>> spareIdName();

	List<Map<String,Object>> warehouseNameId();

	List<Map<String,Object>> notAssociatedSparePart(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SparePartDO sparePart);
	
	int update(SparePartDO sparePart);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
