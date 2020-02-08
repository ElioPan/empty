package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.PurchaseExpenseDO;

import java.util.List;
import java.util.Map;

/**
 * 采购费用主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-10 14:30:32
 */
public interface PurchaseExpenseService {
	
	PurchaseExpenseDO get(Long id);
	
	List<PurchaseExpenseDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseExpenseDO purchaseExpense);
	
	int update(PurchaseExpenseDO purchaseExpense);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R addPurchase(PurchaseExpenseDO purchaseExpenseDO, String body, Long[] itemIds);

	R audit(Long id);

	R rollBackAudit(Long id);

	R removePurchase(Long[] id);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	R getDetail(Long id );

}
