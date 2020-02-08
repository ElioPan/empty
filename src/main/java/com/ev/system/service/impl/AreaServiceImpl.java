package com.ev.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.system.dao.AreaDao;
import com.ev.system.domain.AreaDO;
import com.ev.system.service.AreaService;



@Service
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao areaDao;
	
	@Override
	public AreaDO get(Integer cityId){
		return areaDao.get(cityId);
	}
	
	@Override
	public List<AreaDO> list(Map<String, Object> map){
		return areaDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return areaDao.count(map);
	}
	
	@Override
	public int save(AreaDO area){
		return areaDao.save(area);
	}
	
	@Override
	public int update(AreaDO area){
		return areaDao.update(area);
	}
	
	@Override
	public int remove(Integer cityId){
		return areaDao.remove(cityId);
	}
	
	@Override
	public int batchRemove(Integer[] cityIds){
		return areaDao.batchRemove(cityIds);
	}

	@Override
	public List<AreaDO> listByParentId(Integer cityId) {
		return areaDao.listByParentId(cityId);
	}
	
}
