package com.ev.custom.service;

import com.ev.custom.domain.FacilityDO;

import java.util.List;
import java.util.Map;

/**
 * 仓库
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
public interface FacilityService {
	
	FacilityDO get(Long id);
	
	List<FacilityDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FacilityDO facility);
	
	int update(FacilityDO facility);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int logicRemove(Long id);

	int logicBatchRemove(Long[] ids);

    int countForMap(Map<String, Object> params);

    int audit(Long id);

    int reverseAudit(Long id);

}
