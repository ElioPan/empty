package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.MaterialsScrapItemDao;
import com.ev.mes.domain.MaterialsScrapItemDO;
import com.ev.mes.service.MaterialsScrapItemService;



@Service
public class MaterialsScrapItemServiceImpl implements MaterialsScrapItemService {
	@Autowired
	private MaterialsScrapItemDao materialsScrapItemDao;
	
	@Override
	public MaterialsScrapItemDO get(Long id){
		return materialsScrapItemDao.get(id);
	}
	
	@Override
	public List<MaterialsScrapItemDO> list(Map<String, Object> map){
		return materialsScrapItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return materialsScrapItemDao.count(map);
	}
	
	@Override
	public int save(MaterialsScrapItemDO materialsScrapItem){
		return materialsScrapItemDao.save(materialsScrapItem);
	}
	
	@Override
	public int update(MaterialsScrapItemDO materialsScrapItem){
		return materialsScrapItemDao.update(materialsScrapItem);
	}
	
	@Override
	public int remove(Long id){
		return materialsScrapItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return materialsScrapItemDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> getItemDetails(Map<String, Object> map) {
		return materialsScrapItemDao.getItemDetails(map);
	}

	@Override
	public int batchRemoveByScrapId(Long[] ids) {
		return materialsScrapItemDao.batchRemoveByScrapId(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return materialsScrapItemDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return materialsScrapItemDao.countForMap(map);
	}



}
