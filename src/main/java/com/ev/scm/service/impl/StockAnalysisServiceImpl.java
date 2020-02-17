package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.StockAnalysisDao;
import com.ev.scm.domain.StockAnalysisDO;
import com.ev.scm.service.StockAnalysisService;



@Service
public class StockAnalysisServiceImpl implements StockAnalysisService {
	@Autowired
	private StockAnalysisDao stockAnalysisDao;
	
	@Override
	public StockAnalysisDO get(Long id){
		return stockAnalysisDao.get(id);
	}
	
	@Override
	public List<StockAnalysisDO> list(Map<String, Object> map){
		return stockAnalysisDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return stockAnalysisDao.count(map);
	}
	
	@Override
	public int save(StockAnalysisDO stockAnalysis){
		return stockAnalysisDao.save(stockAnalysis);
	}
	
	@Override
	public int update(StockAnalysisDO stockAnalysis){
		return stockAnalysisDao.update(stockAnalysis);
	}
	
	@Override
	public int remove(Long id){
		return stockAnalysisDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return stockAnalysisDao.batchRemove(ids);
	}

	@Override
	public int batchInsert(List<StockAnalysisDO> stockAnalysisDOS){
		return stockAnalysisDao.batchInsert(stockAnalysisDOS);
	}

	@Override
	public int batchUpdate(List<StockAnalysisDO> stockAnalysisBatchEmptyDOS) {
		return stockAnalysisDao.batchUpdate(stockAnalysisBatchEmptyDOS);
	}

}
