package com.ev.mes.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ev.mes.domain.WorkingProcedurePlanDO;

/**
 * 工序计划单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 15:44:50
 */
@Mapper
public interface WorkingProcedurePlanDao {

	WorkingProcedurePlanDO get(Long id);

	List<WorkingProcedurePlanDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(WorkingProcedurePlanDO workingProcedurePlan);

	int update(WorkingProcedurePlanDO workingProcedurePlan);

	int remove(Long id);

	int batchRemove(Long[] ids);

	Map<String, Object> getDetail(Long id);

	int countForMap(Map<String, Object> params);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	List<Map<String, Object>> dispatchItemList(Map<String, Object> param);

	int dispatchItemCount(Map<String, Object> param);

}
