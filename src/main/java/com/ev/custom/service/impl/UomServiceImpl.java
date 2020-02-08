package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.UomDao;
import com.ev.custom.domain.UomDO;
import com.ev.custom.service.UomService;



@Service
public class UomServiceImpl implements UomService {
	@Autowired
	private UomDao uomDao;
	
	@Override
	public UomDO get(Integer id){
		return uomDao.get(id);
	}
	
	@Override
	public List<UomDO> list(Map<String, Object> map){
		return uomDao.list(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return uomDao.listForMap(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return uomDao.count(map);
	}
	
	@Override
	public int save(UomDO uom){
		return uomDao.save(uom);
	}
	
	@Override
	public int update(UomDO uom){
		return uomDao.update(uom);
	}
	
	@Override
	public int remove(Integer id){
		return uomDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return uomDao.batchRemove(ids);
	}

	@Override
	public UomDO getByName(String childTypeName) {
		return uomDao.getByName(childTypeName);
	}
	
}
