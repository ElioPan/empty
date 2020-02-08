package com.ev.scm.service.impl;

import com.ev.scm.dao.PurchasecontractItemDao;
import com.ev.scm.domain.PurchasecontractItemDO;
import com.ev.scm.service.PurchasecontractItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class PurchasecontractItemServiceImpl implements PurchasecontractItemService {
	@Autowired
	private PurchasecontractItemDao purchasecontractItemDao;

	@Override
	public PurchasecontractItemDO get(Long id){
		return purchasecontractItemDao.get(id);
	}

	@Override
	public List<PurchasecontractItemDO> list(Map<String, Object> map){
		return purchasecontractItemDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return purchasecontractItemDao.count(map);
	}

	@Override
	public int save(PurchasecontractItemDO purchasecontractItem){
		return purchasecontractItemDao.save(purchasecontractItem);
	}

	@Override
	public int update(PurchasecontractItemDO purchasecontractItem){
		return purchasecontractItemDao.update(purchasecontractItem);
	}

	@Override
	public int remove(Long id){
		return purchasecontractItemDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids){
		return purchasecontractItemDao.batchRemove(ids);
	}

	@Override
	public int batchRemoveByContractId(Long[] ids) {
		return purchasecontractItemDao.batchRemoveByContractId(ids);
	}

	@Override
	public List<Map<String, Object>> detailOfItem(Map<String, Object> map) {
		return purchasecontractItemDao.detailOfItem(map);
	}

	@Override
	public Map<String, Object> totalOfItem(Map<String, Object> map) {
		return purchasecontractItemDao.totalOfItem(map);
	}

}
