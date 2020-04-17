package com.ev.system.service;

import com.ev.system.domain.ExcLogDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author zd
 * @email 911435330@qq.com
 * @date 2020-04-09 14:33:09
 */
public interface ExcLogService {
	
	ExcLogDO get(String excRequParam);
	
	List<ExcLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ExcLogDO excLog);
	
	int update(ExcLogDO excLog);

	int updateAll(ExcLogDO excLog);
	
	int remove(String excRequParam);
	
	int batchRemove(String[] excRequParams);
}
