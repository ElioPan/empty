package com.ev.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.system.dao.RoleDataDao;
import com.ev.system.domain.RoleDataDO;
import com.ev.system.service.RoleDataService;



@Service
public class RoleDataServiceImpl implements RoleDataService {
	@Autowired
	private RoleDataDao roleDataDao;
	
	@Override
	public RoleDataDO get(Integer id){
		return roleDataDao.get(id);
	}
	
	@Override
	public List<RoleDataDO> list(Map<String, Object> map){
		return roleDataDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return roleDataDao.count(map);
	}
	
	@Override
	public int save(RoleDataDO roleData){
		return roleDataDao.save(roleData);
	}
	
	@Override
	public int update(RoleDataDO roleData){
		return roleDataDao.update(roleData);
	}
	
	@Override
	public int remove(Integer id){
		return roleDataDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return roleDataDao.batchRemove(ids);
	}
	
}
