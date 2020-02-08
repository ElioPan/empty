package com.ev.custom.dao;

import com.ev.custom.domain.PatrolPlanDetailDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检计划与设备及子项目关联表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-23 15:55:42
 */
@Mapper
public interface PatrolPlanDetailDao {

	PatrolPlanDetailDO get(Long id);
	
	List<PatrolPlanDetailDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PatrolPlanDetailDO patrolPlanDetail);
	
	int update(PatrolPlanDetailDO patrolPlanDetail);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);
}
