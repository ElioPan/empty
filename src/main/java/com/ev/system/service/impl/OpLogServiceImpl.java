package com.ev.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.system.dao.OpLogDao;
import com.ev.system.domain.OpLogDO;
import com.ev.system.service.OpLogService;



@Service
public class OpLogServiceImpl implements OpLogService {
	@Autowired
	private OpLogDao opLogDao;
	
	@Override
	public OpLogDO get(String opModule){
		return opLogDao.get(opModule);
	}
	
	@Override
	public List<OpLogDO> list(Map<String, Object> map){
		return opLogDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return opLogDao.count(map);
	}
	
	@Override
	public int save(OpLogDO opLog){
		return opLogDao.save(opLog);
	}
	
	@Override
	public int update(OpLogDO opLog){
		return opLogDao.update(opLog);
	}

	@Override
	public int updateAll(OpLogDO opLog){
		return opLogDao.updateAll(opLog);
	}
	
	@Override
	public int remove(String opModule){
		return opLogDao.remove(opModule);
	}
	
	@Override
	public int batchRemove(String[] opModules){
		return opLogDao.batchRemove(opModules);
	}
	
}
