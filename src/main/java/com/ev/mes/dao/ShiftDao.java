package com.ev.mes.dao;

import com.ev.mes.domain.ShiftDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 班制
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-26 19:58:39
 */
@Mapper
public interface ShiftDao {

	ShiftDO get(Long id);
	
	List<ShiftDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ShiftDO shift);
	
	int update(ShiftDO shift);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int deletOfShift(Map<String, Object> map);

	int countOfCode(Map<String, Object> map);

}
