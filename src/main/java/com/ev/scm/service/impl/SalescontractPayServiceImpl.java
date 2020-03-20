package com.ev.scm.service.impl;

import com.ev.scm.dao.SalescontractPayDao;
import com.ev.scm.domain.SalescontractPayDO;
import com.ev.scm.service.SalescontractPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SalescontractPayServiceImpl implements SalescontractPayService {
	@Autowired
	private SalescontractPayDao salescontractPayDao;
	
	@Override
	public SalescontractPayDO get(Long pid){
		return salescontractPayDao.get(pid);
	}
	
	@Override
	public List<SalescontractPayDO> list(Map<String, Object> map){
		return salescontractPayDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salescontractPayDao.count(map);
	}
	
	@Override
	public int save(SalescontractPayDO salescontractPay){
		return salescontractPayDao.save(salescontractPay);
	}
	
	@Override
	public int update(SalescontractPayDO salescontractPay){
		return salescontractPayDao.update(salescontractPay);
	}
	
	@Override
	public int remove(Long pid){
		return salescontractPayDao.remove(pid);
	}
	
	@Override
	public int batchRemove(Long[] pids){
		return salescontractPayDao.batchRemove(pids);
	}

	@Override
	public Map<String, Object> countByContract(Map<String, Object> map) {
		return salescontractPayDao.countByContract(map);
	}

}
