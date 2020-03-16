package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.FundInitializationDO;

import java.util.List;
import java.util.Map;

/**
 * 资金初始子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 09:55:59
 */
public interface FundInitializationService {
	
	FundInitializationDO get(Integer id);
	
	List<FundInitializationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FundInitializationDO fundInitialization);
	
	int update(FundInitializationDO fundInitialization);

	int updateAll(FundInitializationDO fundInitialization);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	R disposeAddAndChage(String body);

	R disposeStartUsing(Long[] ids);

	R disposeForbidden(Long[] ids);

	Map<String, Object> countOfList(Map<String, Object> map);

	List<Map<String, Object>>getlist(Map<String, Object> map);
}
