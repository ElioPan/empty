package com.ev.custom.dao;

import com.ev.custom.domain.UpkeepPlanProjectDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 保养计划与项目中间表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
@Mapper
public interface UpkeepPlanProjectDao {

	UpkeepPlanProjectDO get(Long id);
	
	List<UpkeepPlanProjectDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UpkeepPlanProjectDO upkeepPlanProject);
	
	int update(UpkeepPlanProjectDO upkeepPlanProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByPlanId(Long planId);
}
