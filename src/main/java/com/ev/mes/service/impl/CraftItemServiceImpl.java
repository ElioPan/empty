package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.CraftItemDao;
import com.ev.mes.domain.CraftItemDO;
import com.ev.mes.service.CraftItemService;



@Service
public class CraftItemServiceImpl implements CraftItemService {
	@Autowired
	private CraftItemDao craftItemDao;
	
	@Override
	public CraftItemDO get(Long id){
		return craftItemDao.get(id);
	}
	
	@Override
	public List<CraftItemDO> list(Map<String, Object> map){
		return craftItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return craftItemDao.count(map);
	}
	
	@Override
	public int save(CraftItemDO craftItem){
		return craftItemDao.save(craftItem);
	}
	
	@Override
	public int update(CraftItemDO craftItem){
		return craftItemDao.update(craftItem);
	}
	
	@Override
	public int remove(Long id){
		return craftItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return craftItemDao.batchRemove(ids);
	}

	@Override
	public int canDeletaByProcessId(Map<String, Object> map) {
		return craftItemDao.canDeletaByProcessId(map);
	}


}
