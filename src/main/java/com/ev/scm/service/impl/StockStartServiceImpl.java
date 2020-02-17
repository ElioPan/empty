package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.StockStartDao;
import com.ev.scm.domain.StockStartDO;
import com.ev.scm.service.StockStartService;



@Service
public class StockStartServiceImpl implements StockStartService {
	@Autowired
	private StockStartDao stockStartDao;
	
	@Override
	public StockStartDO get(Integer id){
		return stockStartDao.get(id);
	}
	
	@Override
	public List<StockStartDO> list(Map<String, Object> map){
		return stockStartDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return stockStartDao.count(map);
	}
	
	@Override
	public int save(StockStartDO stockStart){
		return stockStartDao.save(stockStart);
	}
	
	@Override
	public int update(StockStartDO stockStart){
		return stockStartDao.update(stockStart);
	}
	
	@Override
	public int remove(Integer id){
		return stockStartDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return stockStartDao.batchRemove(ids);
	}
	
}
