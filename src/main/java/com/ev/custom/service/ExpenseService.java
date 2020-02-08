package com.ev.custom.service;

import com.ev.custom.domain.ExpenseDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 费用
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-13 12:53:38
 */
public interface ExpenseService {
	
	ExpenseDO get(Long id);
	
	List<ExpenseDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ExpenseDO expense);
	
	int update(ExpenseDO expense);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R add(ExpenseDO ExpenseDO);

	R audit(Long id);

	R rollBackAudit(Long id);

	R removePurchase(Long[] ids);

	R getDetail(Long id);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

}
