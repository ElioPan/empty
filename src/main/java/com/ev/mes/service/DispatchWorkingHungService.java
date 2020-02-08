package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.DispatchWorkingHungDO;

import java.util.List;
import java.util.Map;

/**
 * 开工/挂起记录
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-27 20:21:50
 */
public interface DispatchWorkingHungService {
	
	DispatchWorkingHungDO get(Long id);
	
	List<DispatchWorkingHungDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DispatchWorkingHungDO dispatchWorkingHung);
	
	int update(DispatchWorkingHungDO dispatchWorkingHung);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> getWHungTime(int sign,Long dispatchId);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int	countForMap(Map<String, Object> map);

	R deletOfHungOrWorking(Long[] id );

}
