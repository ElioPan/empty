package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.PaymentReceivedItemDao;
import com.ev.scm.domain.PaymentReceivedItemDO;
import com.ev.scm.service.PaymentReceivedItemService;



@Service
public class PaymentReceivedItemServiceImpl implements PaymentReceivedItemService {
	@Autowired
	private PaymentReceivedItemDao paymentReceivedItemDao;
	
	@Override
	public PaymentReceivedItemDO get(Long id){
		return paymentReceivedItemDao.get(id);
	}
	
	@Override
	public List<PaymentReceivedItemDO> list(Map<String, Object> map){
		return paymentReceivedItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return paymentReceivedItemDao.count(map);
	}
	
	@Override
	public int save(PaymentReceivedItemDO paymentReceivedItem){
		return paymentReceivedItemDao.save(paymentReceivedItem);
	}
	
	@Override
	public int update(PaymentReceivedItemDO paymentReceivedItem){
		return paymentReceivedItemDao.update(paymentReceivedItem);
	}
	
	@Override
	public int remove(Long id){
		return paymentReceivedItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return paymentReceivedItemDao.batchRemove(ids);
	}

	@Override
	public int removeByReceivedId(Long[] ids) {
		return paymentReceivedItemDao.removeByReceivedId(ids);
	}

}
