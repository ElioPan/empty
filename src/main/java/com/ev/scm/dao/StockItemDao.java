package com.ev.scm.dao;

import com.ev.scm.domain.StockItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 库存变化明细表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:36:48
 */
@Mapper
public interface StockItemDao {

	StockItemDO get(Long id);
	
	List<StockItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockItemDO stockDetail);
	
	int update(StockItemDO stockDetail);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	// List<Long> getStockIdByHeadId(Long id);

	List<Long> getStockIdByHeadId(Map<String, Object> map);

	int countOfStockId(Long id);

	int countOfStockIds(Map<String, Object> map);

	//int removeByInheadId(Long id);
	int removeByInheadId(Map<String, Object> map);

//	int getStoIdByHeIdForOther(Map<String, Object> map);
//
//
//	int removeOutByInheadId(Long id);


	int batchSave(List<StockItemDO> stockItemDOs);

}
