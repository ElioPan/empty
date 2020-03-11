package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.StationDao;
import com.ev.mes.domain.StationDO;
import com.ev.mes.service.StationService;



@Service
public class StationServiceImpl implements StationService {
	@Autowired
	private StationDao stationDao;
	
	@Override
	public StationDO get(Long id){
		return stationDao.get(id);
	}
	
	@Override
	public List<StationDO> list(Map<String, Object> map){
		return stationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return stationDao.count(map);
	}
	
	@Override
	public int save(StationDO station){
		return stationDao.save(station);
	}
	
	@Override
	public int update(StationDO station){
		return stationDao.update(station);
	}

	@Override
	public int updateAll(StationDO station){
		return stationDao.updateAll(station);
	}
	
	@Override
	public int remove(Long id){
		return stationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return stationDao.batchRemove(ids);
	}
	
}
