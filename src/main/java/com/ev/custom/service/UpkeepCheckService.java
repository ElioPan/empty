package com.ev.custom.service;

import com.ev.custom.domain.UpkeepCheckDO;

import java.util.List;
import java.util.Map;

/**
 * 养保验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 17:06:42
 */
public interface UpkeepCheckService {
	
	UpkeepCheckDO get(Long id);
	
	List<UpkeepCheckDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepCheckDO upkeepCheck);
	
	int update(UpkeepCheckDO upkeepCheck);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object> getDetailByRecordId(Long id);

	int updateByRecordId(UpkeepCheckDO upkeepCheck);

}
