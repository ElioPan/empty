package com.ev.custom.dao;

import com.ev.custom.domain.ExpenseDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 费用
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-13 12:53:38
 */
@Mapper
public interface ExpenseDao {

	ExpenseDO get(Long id);
	
	List<ExpenseDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ExpenseDO expense);
	
	int update(ExpenseDO expense);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDeletOfCount(Map<String, Object> map);

	int deletOfExpense(Map<String,Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);


}
