package com.ev.system.service;

import com.ev.system.domain.ProviderDO;

import java.util.List;
import java.util.Map;

/**
 * 租户表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-04 13:55:46
 */
public interface ProviderService {
	
	ProviderDO get(Long id);
	
	List<ProviderDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProviderDO provider);
	
	int update(ProviderDO provider);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
