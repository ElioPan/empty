package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.SparePartDao;
import com.ev.custom.domain.SparePartDO;
import com.ev.custom.service.SparePartService;



@Service
public class SparePartServiceImpl implements SparePartService {
	@Autowired
	private SparePartDao sparePartDao;
	
	@Override
	public SparePartDO get(Long id){
		return sparePartDao.get(id);
	}
	
	@Override
	public List<SparePartDO> list(Map<String, Object> map){
		return sparePartDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return sparePartDao.count(map);
	}
	
	@Override
	public int save(SparePartDO sparePart){
		return sparePartDao.save(sparePart);
	}
	
	@Override
	public int update(SparePartDO sparePart){
		return sparePartDao.update(sparePart);
	}
	
	@Override
	public int remove(Long id){
		return sparePartDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return sparePartDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> spareIdName() {

		return sparePartDao.getSpareIdName();
	}

	@Override
	public List<Map<String, Object>> warehouseNameId() {
		return  sparePartDao.spareWarehouseNameId();
	}

	@Override
	public List<Map<String, Object>> notAssociatedSparePart(Map<String, Object> map) {
		return sparePartDao.spareNotAssociated(map);
	}

}
