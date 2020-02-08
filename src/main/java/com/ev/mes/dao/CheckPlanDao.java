package com.ev.mes.dao;

import com.ev.mes.domain.CheckPlanDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 检验方案
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:58
 */
@Mapper
public interface CheckPlanDao {

	CheckPlanDO get(Long id);
	
	List<CheckPlanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CheckPlanDO checkPlan);
	
	int update(CheckPlanDO checkPlan);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countListForMap(Map<String, Object> map);

	int deletOfPlan(Map<String,Object> map);

	int canDelet(Map<String, Object> map);

}
