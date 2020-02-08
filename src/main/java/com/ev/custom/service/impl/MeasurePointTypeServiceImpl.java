package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.MeasurePointTypeDao;
import com.ev.custom.domain.MeasurePointTypeDO;
import com.ev.custom.service.MeasurePointTypeService;



@Service
public class MeasurePointTypeServiceImpl implements MeasurePointTypeService {
	@Autowired
	private MeasurePointTypeDao measurePointTypeDao;
	
	@Override
	public MeasurePointTypeDO get(Integer id){
		return measurePointTypeDao.get(id);
	}
	
	@Override
	public List<MeasurePointTypeDO> list(Map<String, Object> map){
		return measurePointTypeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return measurePointTypeDao.count(map);
	}
	
	@Override
	public int save(MeasurePointTypeDO measurePointType){
		return measurePointTypeDao.save(measurePointType);
	}
	
	@Override
	public int update(MeasurePointTypeDO measurePointType){
		return measurePointTypeDao.update(measurePointType);
	}
	
	@Override
	public int remove(Integer id){
		return measurePointTypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return measurePointTypeDao.batchRemove(ids);
	}

	@Override
	public MeasurePointTypeDO getByName(String childTypeName) {
		return measurePointTypeDao.getByName(childTypeName);
	}

	@Override
	public List<MeasurePointTypeDO> listByDeviceId(Map<String, Object> param) {
		return measurePointTypeDao.listByDeviceId(param);
	}
	
}
