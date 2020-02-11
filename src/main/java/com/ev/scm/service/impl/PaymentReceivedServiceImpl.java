package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.PaymentReceivedDao;
import com.ev.scm.domain.PaymentReceivedDO;
import com.ev.scm.service.PaymentReceivedService;



@Service
public class PaymentReceivedServiceImpl implements PaymentReceivedService {
	@Autowired
	private PaymentReceivedDao paymentReceivedDao;
	
	@Override
	public PaymentReceivedDO get(Long id){
		return paymentReceivedDao.get(id);
	}
	
	@Override
	public List<PaymentReceivedDO> list(Map<String, Object> map){
		return paymentReceivedDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return paymentReceivedDao.count(map);
	}
	
	@Override
	public int save(PaymentReceivedDO paymentReceived){
		return paymentReceivedDao.save(paymentReceived);
	}
	
	@Override
	public int update(PaymentReceivedDO paymentReceived){
		return paymentReceivedDao.update(paymentReceived);
	}
	
	@Override
	public int remove(Long id){
		return paymentReceivedDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return paymentReceivedDao.batchRemove(ids);
	}
	
}
