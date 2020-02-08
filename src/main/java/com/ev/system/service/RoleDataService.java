package com.ev.system.service;

import com.ev.system.domain.RoleDataDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-13 15:02:53
 */
public interface RoleDataService {
	
	RoleDataDO get(Integer id);
	
	List<RoleDataDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(RoleDataDO roleData);
	
	int update(RoleDataDO roleData);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
