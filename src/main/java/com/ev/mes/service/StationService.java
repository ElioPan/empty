package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.StationDO;

import java.util.List;
import java.util.Map;

/**
 * 工位
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-11 09:25:14
 */
public interface StationService {
	
	StationDO get(Long id);
	
	List<StationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StationDO station);
	
	int update(StationDO station);

	int updateAll(StationDO station);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> params);

	R addOrUpdate(StationDO stationDO);

	R batchRemoveByIds(Long[] ids);

	R audit(Long id);

	R reverseAudit(Long id);

	String setCode();

	int checkSave(StationDO stationDO);
}
