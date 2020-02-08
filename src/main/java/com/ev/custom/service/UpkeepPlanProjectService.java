package com.ev.custom.service;

import com.ev.custom.domain.UpkeepPlanProjectDO;

import java.util.List;
import java.util.Map;

/**
 * 保养计划与项目中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
public interface UpkeepPlanProjectService {
	
	UpkeepPlanProjectDO get(Long id);
	
	List<UpkeepPlanProjectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepPlanProjectDO upkeepPlanProject);
	
	int update(UpkeepPlanProjectDO upkeepPlanProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByPlanId(Long planId);
}
