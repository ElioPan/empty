package com.ev.custom.dao;

import com.ev.common.vo.PlanVo;
import com.ev.custom.domain.PatrolPlanDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检计划表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
@Mapper
public interface PatrolPlanDao {

	PatrolPlanDO get(Long id);
	
	List<PatrolPlanDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int countForMap(Map<String,Object> map);
	
	int save(PatrolPlanDO patrolPlan);
	
	int update(PatrolPlanDO patrolPlan);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object> getDetailInfo(Long id);

	List<Map<String,Object>> listForMap(Map<String, Object> params);

	List<Map<String,Object>> getNoticeList(Map<String, Object> params);
	
	List<PlanVo> getPlanView(Map<String,Object> map);
	
	int getNoticeListCount(Map<String, Object> params);
}
