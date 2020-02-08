package com.ev.custom.dao;

import com.ev.custom.domain.UpkeepPlanPartDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 保养计划与备件中间表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
@Mapper
public interface UpkeepPlanPartDao {

	UpkeepPlanPartDO get(Long id);
	
	List<UpkeepPlanPartDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UpkeepPlanPartDO upkeepPlanPart);
	
	int update(UpkeepPlanPartDO upkeepPlanPart);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removePartByPlanId(Long id);
}
