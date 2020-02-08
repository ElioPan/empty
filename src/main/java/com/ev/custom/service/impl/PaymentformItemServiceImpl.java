package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PaymentformItemDao;
import com.ev.custom.domain.PaymentformItemDO;
import com.ev.custom.service.PaymentformItemService;



@Service
public class PaymentformItemServiceImpl implements PaymentformItemService {
	@Autowired
	private PaymentformItemDao paymentformItemDao;
	
	@Override
	public PaymentformItemDO get(Long pid){
		return null;
	}
	
	@Override
	public List<PaymentformItemDO> list(Map<String, Object> map){
		return paymentformItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return paymentformItemDao.count(map);
	}
	
	@Override
	public int save(PaymentformItemDO paymentformItem){
		return paymentformItemDao.save(paymentformItem);
	}
	
	@Override
	public int update(PaymentformItemDO paymentformItem){
		return paymentformItemDao.update(paymentformItem);
	}
	
	@Override
	public int remove(Long pid){
		return paymentformItemDao.remove(pid);
	}
	
	@Override
	public int batchRemove(Long[] pids){
		return paymentformItemDao.batchRemove(pids);
	}
	
}
