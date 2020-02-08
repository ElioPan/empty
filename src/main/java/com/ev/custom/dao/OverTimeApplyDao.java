package com.ev.custom.dao;

import com.ev.custom.domain.OverTimeApplyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 加班申请管理
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 08:55:04
 */
@Mapper
public interface OverTimeApplyDao {

	OverTimeApplyDO get(Long id);
	
	List<OverTimeApplyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);
	
	int save(OverTimeApplyDO overTimeApply);
	
	int update(OverTimeApplyDO overTimeApply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listOfCanDelet(Map<String, Object> map);

	Map<String, Object> getOneOfDetail(Map<String, Object> map);
}
