package com.ev.scm.service.impl;

import com.ev.scm.dao.PaymentReceivedItemDao;
import com.ev.scm.domain.PaymentReceivedItemDO;
import com.ev.scm.service.PaymentReceivedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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

	@Override
	public List<Map<String, Object>> detailOfitem(Map<String, Object> map) {
		return paymentReceivedItemDao.detailOfitem(map);
	}

	@Override
	public Map<String, Object> totallAmount(Map<String, Object> map) {
		return paymentReceivedItemDao.totallAmount(map);
	}

	@Override
	public Boolean whetherTheReference(String sign,Long id,Long[] payItemId) {

		Map<String,Object>  map= new HashMap<>();
		map.put("sourceId",id);
		map.put("sourceItemId",payItemId);
		map.put("sign",sign);

		int rows = paymentReceivedItemDao.whetherTheReference(map);

		if(rows==0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public BigDecimal getInCountOfPayment(Map<String, Object> map) {
		return paymentReceivedItemDao.getInCountOfPayment(map);
	}
}