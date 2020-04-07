package com.ev.custom.service;

import com.ev.common.vo.PlanVo;
import com.ev.custom.domain.PatrolPlanDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 保养计划表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
public interface PatrolPlanService {
	
	PatrolPlanDO get(Long id);
	
	List<PatrolPlanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PatrolPlanDO patrolPlan);
	
	int update(PatrolPlanDO patrolPlan);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
		
	int countForMap(Map<String, Object> map);

	Map<String,Object> addPlan(PatrolPlanDO plan, String detailArray);

	List<Map<String,Object>> planList(Map<String, Object> params);
	
	List<Map<String,Object>> getNoticeList(Long deptId,Long userId,Integer checkType);

    Map<String,Object> planDetail(Long id);
    
    List<PlanVo> getPlanView(Map<String, Object> map);
    
    Map<String,Object> params(Map<String,Object> params);
    
    int getNoticeListCount(Map<String, Object> params);

	R disposeForbidden(Long[]ids);

	R disposeStartUsing(Long[]ids);

	int canChangeStatus(Map<String, Object> map);


}
