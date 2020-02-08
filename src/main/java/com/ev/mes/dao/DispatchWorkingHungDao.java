package com.ev.mes.dao;

import com.ev.mes.domain.DispatchWorkingHungDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 开工/挂起记录
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-27 20:21:50
 */
@Mapper
public interface DispatchWorkingHungDao {

	DispatchWorkingHungDO get(Long id);
	
	List<DispatchWorkingHungDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DispatchWorkingHungDO dispatchWorkingHung);
	
	int update(DispatchWorkingHungDO dispatchWorkingHung);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> getWHungTime(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int	countForMap(Map<String, Object> map);

	int canDelet(Map<String, Object> map);

}
