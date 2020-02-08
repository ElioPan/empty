package com.ev.scm.dao;

import com.ev.scm.domain.PurchaseExpenseDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 采购费用主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-10 14:30:32
 */
@Mapper
public interface PurchaseExpenseDao {

	PurchaseExpenseDO get(Long id);
	
	List<PurchaseExpenseDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseExpenseDO purchaseExpense);
	
	int update(PurchaseExpenseDO purchaseExpense);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDeletOfCount(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	Map<String, Object> getDetailOfHead (Map<String, Object> map);


}
