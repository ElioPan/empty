package com.ev.mes.dao;

import com.ev.mes.domain.DispatchItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工单明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-27 20:21:38
 */
@Mapper
public interface DispatchItemDao {

	DispatchItemDO get(Long id);
	
	List<DispatchItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DispatchItemDO dispatchItem);
	
	int update(DispatchItemDO dispatchItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMapOfPlan(Map<String, Object> map);

	int countForListMapOfPlan(Map<String, Object> map);

	Map<String, Object> sendOfButton(Long planItemId);

	int canDeletOfDispatch(Map<String, Object> map);

	List<Map<String, Object>> listForMapOfDispatch(Map<String, Object> map);

	int countForMapOfDispatch(Map<String, Object> map);

	Map<String, Object> rollBackOfDetail(Map<String, Object> map);

	int changeOfDiapatchStatus(Map<String, Object> map);

	Map<String, Object> getStatusOfPlanAndDisp(Long id);

	int canDelet(Map<String, Object> map);

	Map<String, Object> getPDStartEndTime(Long id);

	Map<String, Object> getWHungTime(Map<String, Object> map);

	int countOfStartWorking(Long dispatchId);

	List<Map<String, Object>> getStartWorkByOperator(Long id);


	List<DispatchItemDO> countOfStatus(Map<String, Object> map);

}
