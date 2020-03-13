package com.ev.scm.service.impl;

import com.ev.scm.dao.PurchaseItemDao;
import com.ev.scm.domain.PurchaseItemDO;
import com.ev.scm.service.PurchaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;



@Service
public class PurchaseItemServiceImpl implements PurchaseItemService {
	@Autowired
	private PurchaseItemDao purchaseItemDao;
	
	@Override
	public PurchaseItemDO get(Long id){
		return purchaseItemDao.get(id);
	}
	
	@Override
	public List<PurchaseItemDO> list(Map<String, Object> map){
		return purchaseItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchaseItemDao.count(map);
	}
	
	@Override
	public int save(PurchaseItemDO purchaseItem){
		return purchaseItemDao.save(purchaseItem);
	}
	
	@Override
	public int update(PurchaseItemDO purchaseItem){
		return purchaseItemDao.update(purchaseItem);
	}
	
	@Override
	public int remove(Long id){
		return purchaseItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return purchaseItemDao.batchRemove(ids);
	}

	@Override
	public int batchRemoveByPurcahseId(Long[] ids) {
		return purchaseItemDao.batchRemoveByPurcahseId(ids);
	}

	@Override
	public List<Map<String, Object>> detailOfItem(Map<String, Object> map) {
		return purchaseItemDao.detailOfItem(map);
	}

	@Override
	public Map<String, Object> aggregate(Map<String, Object> map) {
		return purchaseItemDao.aggregate(map);
	}

	@Override
	public BigDecimal getInCountOfPurchase(Map<String, Object> map) {
		return purchaseItemDao.getInCountOfPurchase(map);
	}

	@Override
	public BigDecimal getInCountOfExclude(Map<String, Object> map) {
		return purchaseItemDao.getInCountOfExclude(map);
	}

}
