package com.ev.mes.dao;

import com.ev.mes.domain.ProductionPlanDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 生产计划单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-21 08:26:39
 */
@Mapper
public interface ProductionPlanDao {

	ProductionPlanDO get(Long id);

	List<ProductionPlanDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ProductionPlanDO productionPlan);

	int update(ProductionPlanDO productionPlan);

	int remove(Long id);

	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	Map<String, Object> countForMap(Map<String, Object> params);

    List<Map<String, Object>> listDialogMap(Map<String, Object> params);
}
