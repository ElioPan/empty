package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.ProcessCheckDao;
import com.ev.mes.domain.ProcessCheckDO;
import com.ev.mes.service.ProcessCheckService;



@Service
public class ProcessCheckServiceImpl implements ProcessCheckService {
	@Autowired
	private ProcessCheckDao processCheckDao;
	
	@Override
	public ProcessCheckDO get(Long id){
		return processCheckDao.get(id);
	}
	
	@Override
	public List<ProcessCheckDO> list(Map<String, Object> map){
		return processCheckDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processCheckDao.count(map);
	}
	
	@Override
	public int save(ProcessCheckDO processCheck){
		return processCheckDao.save(processCheck);
	}
	
	@Override
	public int update(ProcessCheckDO processCheck){
		return processCheckDao.update(processCheck);
	}
	
	@Override
	public int remove(Long id){
		return processCheckDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processCheckDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> getDetailByProcessId(Map<String, Object> map) {
		return processCheckDao.getDetailByProcessId(map);
	}

	@Override
	public int removeBacthById(Map<String, Object> map) {
		return processCheckDao.removeBacthById(map);
	}

	@Override
	public int removeBacthByforeignId(Map<String, Object> map) {
		return processCheckDao.removeBacthByforeignId(map);
	}

}
