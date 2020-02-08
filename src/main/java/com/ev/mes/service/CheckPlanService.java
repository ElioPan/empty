package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.CheckPlanDO;

import java.util.List;
import java.util.Map;

/**
 * 检验方案
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:58
 */
public interface CheckPlanService {
	
	CheckPlanDO get(Long id);
	
	List<CheckPlanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CheckPlanDO checkPlan);
	
	int update(CheckPlanDO checkPlan);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R saveAndChangePlan(CheckPlanDO checkPlan, String  projectDetail, Long[] ids);

	R auditPlanOfCheck(Long id,Long auditId);

	R opposeAuditCheckPlan(Long id );

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countListForMap(Map<String, Object> map);

	R getPlanOfDetail(Long id);

	R deletOfPlan(Long[] ids);
}
