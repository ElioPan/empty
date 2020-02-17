package com.ev.scm.service;

import com.ev.scm.domain.PurchaseExpenseItemDO;

import java.util.List;
import java.util.Map;

/**
 * 采购费用主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-10 14:30:52
 */
public interface PurchaseExpenseItemService {
	
	PurchaseExpenseItemDO get(Long id);
	
	List<PurchaseExpenseItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseExpenseItemDO purchaseExpenseItem);
	
	int update(PurchaseExpenseItemDO purchaseExpenseItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchRemoveByPurcahseId(Long[] ids);

	List<Map<String, Object>> getDetailOfBody(Map<String, Object> map);

	int getTotailTaxAmount(Map<String, Object> map);



}
