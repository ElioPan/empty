package com.ev.mes.dao;

import com.ev.mes.domain.ProductionFeedingDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 生产投料单
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 10:08:55
 */
@Mapper
public interface ProductionFeedingDao {

	ProductionFeedingDO get(Long id);
	
	List<ProductionFeedingDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ProductionFeedingDO productionFeeding);
	
	int update(ProductionFeedingDO productionFeeding);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	/**
	 * 获取生产投料计划列表
	 */
	List<Map<String, Object>> listForMap(Map<String, Object> params);

	/**
	 * 获取生产投料计划总数
	 */
	int countForMap(Map<String, Object> params);

	List<ProductionFeedingDO> getByOutsourcingContractItemId(Long id);

    List<Map<String, Object>> listForMapToOutsourcingContract(Map<String, Object> map);

	int countForMapToOutsourcingContract(Map<String, Object> map);

	int countBySource(Map<String, Object> map);

    int childCount(Map<String, Object> map);
}
