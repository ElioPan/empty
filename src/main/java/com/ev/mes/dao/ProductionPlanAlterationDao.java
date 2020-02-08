package com.ev.mes.dao;

import com.ev.mes.domain.ProductionPlanAlterationDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 生产计划单(变更历史)
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-21 08:27:23
 */
@Mapper
public interface ProductionPlanAlterationDao {

	ProductionPlanAlterationDO get(Long id);

	List<ProductionPlanAlterationDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ProductionPlanAlterationDO productionPlanAlteration);

	int update(ProductionPlanAlterationDO productionPlanAlteration);

	int remove(Long id);

	int batchRemove(Long[] ids);

	/**
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> listForMap(Map<String, Object> params);

	/**
	 * @param params
	 * @return
	 */
	int countForMap(Map<String, Object> params);

	/**
	 * @param id
	 */
	int removeByPlanId(Long id);

	/**
	 * @param ids
	 */
	int batchRemoveByPlanId(Long[] ids);
}
