package com.ev.mes.service;

import com.ev.mes.domain.WorkingProcedureDetailDO;

import java.util.List;
import java.util.Map;

/**
 * 工序计划单的工序详情
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 15:46:48
 */
public interface WorkingProcedureDetailService {
	
	WorkingProcedureDetailDO get(Long id);
	
	List<WorkingProcedureDetailDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(WorkingProcedureDetailDO workingProcedureDetail);
	
	int update(WorkingProcedureDetailDO workingProcedureDetail);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	Map<String, Object> countForMap(Map<String, Object> params);

	List<Map<String, Object>> listByPlanId(Long id);

	void removeByHeadId(Long id);

	void updateByPlanId(WorkingProcedureDetailDO detailDO);

    List<Map<String, Object>> getDetailByPlanId(Map<String, Object> param);

    List<Map<String, Object>> getDispatchItemlist(Long id);

    Map<String, Object> listByDetailId(Long bodyId);
}
