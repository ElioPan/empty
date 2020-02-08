package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.PoorDO;

import java.util.List;
import java.util.Map;

/**
 * 不良原因
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-26 20:02:31
 */
public interface PoorService {
	
	PoorDO get(Long id);
	
	List<PoorDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PoorDO poor);
	
	int update(PoorDO poor);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R savePoor(PoorDO poorDO);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	R batchPoorDetail(Long[] ids );
}
