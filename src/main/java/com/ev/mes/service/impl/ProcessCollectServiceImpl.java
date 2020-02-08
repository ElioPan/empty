package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.ProcessCollectDao;
import com.ev.mes.domain.ProcessCollectDO;
import com.ev.mes.service.ProcessCollectService;



@Service
public class ProcessCollectServiceImpl implements ProcessCollectService {
	@Autowired
	private ProcessCollectDao processCollectDao;
	
	@Override
	public ProcessCollectDO get(Long id){
		return processCollectDao.get(id);
	}
	
	@Override
	public List<ProcessCollectDO> list(Map<String, Object> map){
		return processCollectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processCollectDao.count(map);
	}
	
	@Override
	public int save(ProcessCollectDO processCollect){
		return processCollectDao.save(processCollect);
	}
	
	@Override
	public int update(ProcessCollectDO processCollect){
		return processCollectDao.update(processCollect);
	}
	
	@Override
	public int remove(Long id){
		return processCollectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processCollectDao.batchRemove(ids);
	}
	
}
