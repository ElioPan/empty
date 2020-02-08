package com.ev.custom.service;

import com.ev.custom.domain.MeasurePointDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:20:41
 */
public interface MeasurePointService {
	
	MeasurePointDO get(Long id);
	
	List<MeasurePointDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MeasurePointDO measurePoint);
	
	int update(MeasurePointDO measurePoint);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int add(MeasurePointDO measurePoint, String childTypeName, String uomName);

	int edit(MeasurePointDO measurePoint, String childTypeName, String uomName);

	int checkSave(MeasurePointDO measurePointDO);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	int countForMap(Map<String, Object> params);

	int batchEdit(String pointArray,Long deviceId);
	
	void formatSortNoJob();

	int logicRemove(Long id);

	int logicBatchRemove(Long[] ids);
}
