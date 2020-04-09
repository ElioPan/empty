package com.ev.scm.dao;

import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockPeriodDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 库存表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:36:43
 */
@Mapper
public interface StockDao {

	StockDO get(Long id);
	
	List<StockDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockDO stock);
	
	int update(StockDO stock);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
    int batchSave(List<StockDO> stockDOs);

    List<Map<String, Object>> listForMap(Map<String, Object> results);

	Map<String, Object> countForMap(Map<String, Object> results);

	int batchUpdate(List<StockDO> batchUpdate);

	int batchSaveForCopy(List<StockDO> stockDOList);

	List<Map<String, Object>> listForMapForCopy(Map<String, Object> param);

	Map<String, Object> countForMapForCopy(Map<String, Object> param);

    StockPeriodDO getPeriodTimeForMysql();

	void savePeriodTime(StockPeriodDO stockPeriodDO);

	void updatePeriodTime(StockPeriodDO periodTimeForMysql);
}
