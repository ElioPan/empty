package com.ev.scm.service.impl;

import com.ev.scm.dao.StockDao;
import com.ev.scm.domain.StockDO;
import com.ev.scm.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class StockServiceImpl implements StockService {
	@Autowired
	private StockDao stockDao;
	
	@Override
	public StockDO get(Long id){
		return stockDao.get(id);
	}
	
	@Override
	public List<StockDO> list(Map<String, Object> map){
		return stockDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return stockDao.count(map);
	}
	
	@Override
	public int save(StockDO stock){
		return stockDao.save(stock);
	}
	
	@Override
	public int update(StockDO stock){
		return stockDao.update(stock);
	}
	
	@Override
	public int remove(Long id){
		return stockDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return stockDao.batchRemove(ids);
	}

	@Override
	public List<StockDO> getProCountForMap(Map<String, Object> map) {
		return stockDao.getProCountForMap(map);
	}

    @Override
    public int batchSave(List<StockDO> stockDOs) {
        return  stockDao.batchSave(stockDOs);
    }

}
