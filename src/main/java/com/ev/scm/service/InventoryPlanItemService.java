package com.ev.scm.service;

import com.ev.scm.domain.InventoryPlanItemDO;

import java.util.List;
import java.util.Map;

/**
 * 盘点子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-19 14:21:57
 */
public interface InventoryPlanItemService {
	
	InventoryPlanItemDO get(Long id);
	
	List<InventoryPlanItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(InventoryPlanItemDO checkBody);
	
	int update(InventoryPlanItemDO checkBody);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByPlanId(Long id);

	List<Map<String,Object>> getProfitLossMsg(Map<String, Object> map);

	List<Map<String,Object>> getProMsgCount(Map<String, Object> map);

	int countOfWinLoss(Map<String, Object> map);

	List<Map<String, Object>> getMaterialAll(Map<String, Object> map);


}
