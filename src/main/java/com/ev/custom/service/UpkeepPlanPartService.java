package com.ev.custom.service;

import com.ev.custom.domain.UpkeepPlanPartDO;

import java.util.List;
import java.util.Map;

/**
 * 保养计划与备件中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
public interface UpkeepPlanPartService {
	
	UpkeepPlanPartDO get(Long id);
	
	List<UpkeepPlanPartDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepPlanPartDO upkeepPlanPart);
	
	int update(UpkeepPlanPartDO upkeepPlanPart);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removePartByPlanId(Long id);
}
