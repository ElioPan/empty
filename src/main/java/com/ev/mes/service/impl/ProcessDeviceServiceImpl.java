package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.ProcessDeviceDao;
import com.ev.mes.domain.ProcessDeviceDO;
import com.ev.mes.service.ProcessDeviceService;



@Service
public class ProcessDeviceServiceImpl implements ProcessDeviceService {
	@Autowired
	private ProcessDeviceDao processDeviceDao;
	
	@Override
	public ProcessDeviceDO get(Long id){
		return processDeviceDao.get(id);
	}
	
	@Override
	public List<ProcessDeviceDO> list(Map<String, Object> map){
		return processDeviceDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processDeviceDao.count(map);
	}
	
	@Override
	public int save(ProcessDeviceDO processDevice){
		return processDeviceDao.save(processDevice);
	}
	
	@Override
	public int update(ProcessDeviceDO processDevice){
		return processDeviceDao.update(processDevice);
	}
	
	@Override
	public int remove(Long id){
		return processDeviceDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processDeviceDao.batchRemove(ids);
	}
	
}
