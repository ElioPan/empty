package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ev.scm.dao.ProcessingChargeItemDao;
import com.ev.scm.domain.ProcessingChargeItemDO;
import com.ev.scm.service.ProcessingChargeItemService;



@Service
public class ProcessingChargeItemServiceImpl implements ProcessingChargeItemService {
	@Autowired
	private ProcessingChargeItemDao processingChargeItemDao;
	
	@Override
	public ProcessingChargeItemDO get(Long id){
		return processingChargeItemDao.get(id);
	}
	
	@Override
	public List<ProcessingChargeItemDO> list(Map<String, Object> map){
		return processingChargeItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processingChargeItemDao.count(map);
	}
	
	@Override
	public int save(ProcessingChargeItemDO processingChargeItem){
		return processingChargeItemDao.save(processingChargeItem);
	}
	
	@Override
	public int update(ProcessingChargeItemDO processingChargeItem){
		return processingChargeItemDao.update(processingChargeItem);
	}
	
	@Override
	public int remove(Long id){
		return processingChargeItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processingChargeItemDao.batchRemove(ids);
	}

	@Override
	public BigDecimal getCountBySource(Map<String, Object> map) {
		return processingChargeItemDao.getCountBySource(map);
	}

}
