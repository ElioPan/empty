package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PatrolCheckDao;
import com.ev.custom.domain.PatrolCheckDO;
import com.ev.custom.service.PatrolCheckService;



@Service
public class PatrolCheckServiceImpl implements PatrolCheckService {
	@Autowired
	private PatrolCheckDao patrolCheckDao;
	
	@Override
	public PatrolCheckDO get(Long id){
		return patrolCheckDao.get(id);
	}
	
	@Override
	public List<PatrolCheckDO> list(Map<String, Object> map){
		return patrolCheckDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return patrolCheckDao.count(map);
	}
	
	@Override
	public int save(PatrolCheckDO patrolCheck){
		return patrolCheckDao.save(patrolCheck);
	}
	
	@Override
	public int update(PatrolCheckDO patrolCheck){
		return patrolCheckDao.update(patrolCheck);
	}
	
	@Override
	public int remove(Long id){
		return patrolCheckDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return patrolCheckDao.batchRemove(ids);
	}
	
}
