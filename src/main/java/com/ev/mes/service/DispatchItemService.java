package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.DispatchItemDO;

import java.util.List;
import java.util.Map;

/**
 * 工单明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-27 20:21:38
 */
public interface DispatchItemService {
	
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

	R disposeSbmint(Long id, String dispatchDetal);

	R  deletByIds(Long[] ids);

	List<Map<String, Object>> listForMapOfDispatch(Map<String, Object> map);

	int countForMapOfDispatch(Map<String, Object> map);

	Map<String, Object> rollBackOfDetail(Map<String, Object> map);

	R saveRollBackOfCount(Long dispatchid,String countOfRollBack);


	/*
	    根据外键将工单状态批量挂起
	 */
	int changeOfDiapatchStatus(Long[] foriegnIds);

	R workStartDispatchItem(Long dispatchId);

	R closeDispatch(Long[] dispatchId);

	Map<String, Object> getPDStartEndTime(Long id);

	R hangUpDispatchItem(Long dispatchId) ;

	R oneStartWorkDetail();


}
