package com.ev.custom.service;

import com.ev.custom.domain.PatrolDetailDO;

import java.util.List;
import java.util.Map;

/**
 * 巡检明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:37
 */
public interface PatrolDetailService {
	
	PatrolDetailDO get(Long id);
	
	PatrolDetailDO getByProjectId(Long recordId,Long deviceId,Long projectId);
	
	List<PatrolDetailDO> list(Map<String, Object> map);
	
	List<Map<String, Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int countForMap(Map<String, Object> map);
	
	int save(PatrolDetailDO patrolDetail);
	
	int update(PatrolDetailDO patrolDetail);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	List<Map<String, Object>>getRecordByDevice(Map<String, Object>params);

	List<Map<String, Object>> devicePatrolListForMap(Map<String, Object> params);

	int devicePatrolCountForMap(Map<String, Object> params);
	
	
}
