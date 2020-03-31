package com.ev.scm.dao;

import com.ev.scm.domain.StockAnalysisDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 存货报表分析
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-15 09:25:43
 */
@Mapper
public interface StockAnalysisDao {

	StockAnalysisDO get(Long id);
	
	List<StockAnalysisDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockAnalysisDO stockAnalysis);
	
	int update(StockAnalysisDO stockAnalysis);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchInsert(List<StockAnalysisDO> stockAnalysisDOS);

	int batchUpdate(List<StockAnalysisDO> stockAnalysisBatchEmptyDOS);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	int countForMap(Map<String, Object> params);

	void batchRemoveById(Long id);

	Map<String, Object> countForTotal(Map<String, Object> params);
}
