package com.ev.scm.service;

import com.ev.scm.domain.InventoryPlanFitlossDO;

import java.util.List;
import java.util.Map;

/**
 * 盘点盈亏单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-24 14:50:47
 */
public interface InventoryPlanFitlossService {
	
	InventoryPlanFitlossDO get(Integer id);
	
	List<InventoryPlanFitlossDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(InventoryPlanFitlossDO checkProfitloss);
	
	int update(InventoryPlanFitlossDO checkProfitloss);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	Boolean saveProfitORLoss(List<Map<String, Object>> listMap, Long documentType);

	int countOfOtherByPY(Map<String, Object> map);

	int countOfYKCount(Long planId);

	int countOfOutByPK(Map<String, Object> map);



}
