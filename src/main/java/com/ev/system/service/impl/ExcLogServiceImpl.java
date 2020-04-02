package com.ev.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.system.dao.ExcLogDao;
import com.ev.system.domain.ExcLogDO;
import com.ev.system.service.ExcLogService;



@Service
public class ExcLogServiceImpl implements ExcLogService {
	@Autowired
	private ExcLogDao excLogDao;
	
	@Override
	public ExcLogDO get(String excRequParam){
		return excLogDao.get(excRequParam);
	}
	
	@Override
	public List<ExcLogDO> list(Map<String, Object> map){
		return excLogDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return excLogDao.count(map);
	}
	
	@Override
	public int save(ExcLogDO excLog){
		return excLogDao.save(excLog);
	}
	
	@Override
	public int update(ExcLogDO excLog){
		return excLogDao.update(excLog);
	}

	@Override
	public int updateAll(ExcLogDO excLog){
		return excLogDao.updateAll(excLog);
	}
	
	@Override
	public int remove(String excRequParam){
		return excLogDao.remove(excRequParam);
	}
	
	@Override
	public int batchRemove(String[] excRequParams){
		return excLogDao.batchRemove(excRequParams);
	}
	
}
