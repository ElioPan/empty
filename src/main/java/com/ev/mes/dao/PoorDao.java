package com.ev.mes.dao;

import com.ev.mes.domain.PoorDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 不良原因
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-26 20:02:31
 */
@Mapper
public interface PoorDao {

	PoorDO get(Long id);
	
	List<PoorDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PoorDO poor);
	
	int update(PoorDO poor);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	int deletOfPoor(Map<String,Object> map);

}
