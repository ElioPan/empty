package com.ev.scm.service.impl;

import com.ev.scm.dao.PurchaseExpenseItemDao;
import com.ev.scm.domain.PurchaseExpenseItemDO;
import com.ev.scm.service.PurchaseExpenseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class PurchaseExpenseItemServiceImpl implements PurchaseExpenseItemService {
	@Autowired
	private PurchaseExpenseItemDao purchaseExpenseItemDao;
	
	@Override
	public PurchaseExpenseItemDO get(Long id){
		return purchaseExpenseItemDao.get(id);
	}
	
	@Override
	public List<PurchaseExpenseItemDO> list(Map<String, Object> map){
		return purchaseExpenseItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchaseExpenseItemDao.count(map);
	}
	
	@Override
	public int save(PurchaseExpenseItemDO purchaseExpenseItem){
		return purchaseExpenseItemDao.save(purchaseExpenseItem);
	}
	
	@Override
	public int update(PurchaseExpenseItemDO purchaseExpenseItem){
		return purchaseExpenseItemDao.update(purchaseExpenseItem);
	}
	
	@Override
	public int remove(Long id){
		return purchaseExpenseItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return purchaseExpenseItemDao.batchRemove(ids);
	}

	@Override
	public int batchRemoveByPurcahseId(Long[] ids) {
		return purchaseExpenseItemDao.batchRemoveByPurcahseId(ids);
	}

	@Override
	public List<Map<String, Object>> getDetailOfBody(Map<String, Object> map) {
		return purchaseExpenseItemDao.getDetailOfBody(map);
	}

}
