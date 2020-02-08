package com.ev.mes.service;

import java.util.List;
import java.util.Map;

import com.ev.mes.domain.ProductionPlanAlterationDO;

/**
 * 生产计划单(变更历史)
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-21 08:27:23
 */
public interface ProductionPlanAlterationService {

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
	void removeByPlanId(Long id);

	/**
	 * @param ids
	 */
	void batchRemoveByPlanId(Long[] ids);
}
