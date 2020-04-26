package com.ev.mes.service;

import com.ev.mes.domain.ProductionFeedingAlterationDO;
import com.ev.mes.vo.FeedingDetailVO;

import java.util.List;
import java.util.Map;

/**
 * 投料单（变更列表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-26 10:22:50
 */
public interface ProductionFeedingAlterationService {
	
	ProductionFeedingAlterationDO get(Long id);
	
	List<ProductionFeedingAlterationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProductionFeedingAlterationDO productionFeedingAlteration);
	
	int update(ProductionFeedingAlterationDO productionFeedingAlteration);

	int updateAll(ProductionFeedingAlterationDO productionFeedingAlteration);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<FeedingDetailVO> listForFeedingItem(Map<String, Object> params);

    List<Map<String, Object>> listForMap(Map<String, Object> params);

	int countForMap(Map<String, Object> params);

}
