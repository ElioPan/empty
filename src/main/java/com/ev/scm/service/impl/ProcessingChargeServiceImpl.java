package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.ProcessingChargeDao;
import com.ev.scm.domain.ProcessingChargeDO;
import com.ev.scm.service.ProcessingChargeService;



@Service
public class ProcessingChargeServiceImpl implements ProcessingChargeService {
	@Autowired
	private ProcessingChargeDao processingChargeDao;
	
	@Override
	public ProcessingChargeDO get(Long id){
		return processingChargeDao.get(id);
	}
	
	@Override
	public List<ProcessingChargeDO> list(Map<String, Object> map){
		return processingChargeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processingChargeDao.count(map);
	}
	
	@Override
	public int save(ProcessingChargeDO processingCharge){
		return processingChargeDao.save(processingCharge);
	}
	
	@Override
	public int update(ProcessingChargeDO processingCharge){
		return processingChargeDao.update(processingCharge);
	}
	
	@Override
	public int remove(Long id){
		return processingChargeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processingChargeDao.batchRemove(ids);
	}
	
}
