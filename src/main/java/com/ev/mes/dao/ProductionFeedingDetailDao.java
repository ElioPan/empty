package com.ev.mes.dao;

import com.ev.mes.domain.ProductionFeedingDetailDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 生产投料单（详情列表）
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 10:10:27
 */
@Mapper
public interface ProductionFeedingDetailDao {

	ProductionFeedingDetailDO get(Long id);
	
	List<ProductionFeedingDetailDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ProductionFeedingDetailDO productionFeedingDetail);
	
	int update(ProductionFeedingDetailDO productionFeedingDetail);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	int countForMap(Map<String, Object> params);

	int removeByHeadId(Long id);

    List<Map<String, Object>> phoneListForMap(Map<String, Object> params);

	int phoneCountForMap(Map<String, Object> params);
}
