package com.ev.mes.service;

import com.ev.mes.domain.CheckPlanItemDO;

import java.util.List;
import java.util.Map;

/**
 * 检验方案明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:49:08
 */
public interface CheckPlanItemService {
	
	CheckPlanItemDO get(Long id);
	
	List<CheckPlanItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CheckPlanItemDO checkPlanItem);
	
	int update(CheckPlanItemDO checkPlanItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDeletOfProject(Map<String, Object> map);

}
