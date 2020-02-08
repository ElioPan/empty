package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PaydetailsDao;
import com.ev.custom.domain.PaydetailsDO;
import com.ev.custom.service.PaydetailsService;



@Service
public class PaydetailsServiceImpl implements PaydetailsService {
	@Autowired
	private PaydetailsDao paydetailsDao;
	
	@Override
	public Map<String,Object> get(Long id){
		return paydetailsDao.get(id);
	}
	
	@Override
	public List<PaydetailsDO> list(Map<String, Object> map){
		return paydetailsDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return paydetailsDao.count(map);
	}
	
	@Override
	public int save(PaydetailsDO paydetails){
		return paydetailsDao.save(paydetails);
	}
	
	@Override
	public int update(PaydetailsDO paydetails){
		return paydetailsDao.update(paydetails);
	}
	
	@Override
	public int remove(Long id){
		return paydetailsDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return paydetailsDao.batchRemove(ids);
	}
	
}
