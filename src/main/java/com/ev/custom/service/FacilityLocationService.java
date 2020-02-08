package com.ev.custom.service;

import com.ev.custom.domain.FacilityLocationDO;

import java.util.List;
import java.util.Map;

/**
 * 库位
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
public interface FacilityLocationService {
	
	FacilityLocationDO get(Integer id);
	
	List<FacilityLocationDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FacilityLocationDO facilityLocation);
	
	int update(FacilityLocationDO facilityLocation);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	int logicRemove(Integer id);

	int logicBatchRemove(Integer[] ids);

    int audit(Integer id);

    int reverseAudit(Integer id);

}
