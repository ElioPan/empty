package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.CommutationInitializationDO;

import java.util.List;
import java.util.Map;

/**
 * 往来初始化
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 10:48:36
 */
public interface CommutationInitializationService {
	
	CommutationInitializationDO get(Long id);
	
	List<CommutationInitializationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CommutationInitializationDO commutationInitialization);
	
	int update(CommutationInitializationDO commutationInitialization);

	int updateAll(CommutationInitializationDO commutationInitialization);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R disposeAddAndChage(String body);

	List<Map<String, Object>> getListForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	R getClientAccountMessage(Map<String, Object> map);

	List<Map<String, Object>> getDetail(Map<String, Object> map);

	R getSupplierAccountMessage(Map<String, Object> parameter );

	Map<String, Object> getDetailByClientOrSupplierId(Map<String, Object> map);

}
