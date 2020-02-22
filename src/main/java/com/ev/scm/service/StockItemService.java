package com.ev.scm.service;

import com.ev.scm.domain.StockItemDO;

import java.util.List;
import java.util.Map;

/**
 * 库存变化明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:36:48
 */
public interface StockItemService {
	
	StockItemDO get(Long id);
	
	List<StockItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockItemDO stockDetail);
	
	int update(StockItemDO stockDetail);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	//int getStockIdByHeadId(Long id);

	int getStockIdByHeadId(Long exitentryType, Long headid);


	int countOfStockId(Long id);

	int dealDateOveraAudite(Long exitentryType, Long id);
	
	int removeByInheadId(Long exitentryType, Long id);

	int getStockIdByHeadIds(Long exitentryType,Long inheadId);

	int batchSave(List<StockItemDO> stockItemDOs);



}
