package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PurchasebillItemDao;
import com.ev.custom.domain.PurchasebillItemDO;
import com.ev.custom.service.PurchasebillItemService;



@Service
public class PurchasebillItemServiceImpl implements PurchasebillItemService {
	@Autowired
	private PurchasebillItemDao purchasebillItemDao;
	
	@Override
	public PurchasebillItemDO get(Long bid){
		return null;
	}
	
	@Override
	public List<PurchasebillItemDO> list(Map<String, Object> map){
		return purchasebillItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchasebillItemDao.count(map);
	}
	
	@Override
	public int save(PurchasebillItemDO purchasebillItem){
		return purchasebillItemDao.save(purchasebillItem);
	}
	
	@Override
	public int update(PurchasebillItemDO purchasebillItem){
		return purchasebillItemDao.update(purchasebillItem);
	}
	
	@Override
	public int remove(Long bid){
		return purchasebillItemDao.remove(bid);
	}
	
	@Override
	public int batchRemove(Long[] bids){
		return purchasebillItemDao.batchRemove(bids);
	}
	
}
