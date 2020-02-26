package com.ev.scm.service.impl;

import com.ev.scm.dao.PurchasecontractPayDao;
import com.ev.scm.domain.PurchasecontractPayDO;
import com.ev.scm.service.PurchasecontractPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class PurchasecontractPayServiceImpl implements PurchasecontractPayService {
	@Autowired
	private PurchasecontractPayDao purchasecontractPayDao;
	
	@Override
	public PurchasecontractPayDO get(Long id){
		return purchasecontractPayDao.get(id);
	}
	
	@Override
	public List<PurchasecontractPayDO> list(Map<String, Object> map){
		return purchasecontractPayDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchasecontractPayDao.count(map);
	}
	
	@Override
	public int save(PurchasecontractPayDO purchasecontractPay){
		return purchasecontractPayDao.save(purchasecontractPay);
	}
	
	@Override
	public int update(PurchasecontractPayDO purchasecontractPay){
		return purchasecontractPayDao.update(purchasecontractPay);
	}
	
	@Override
	public int remove(Long id){
		return purchasecontractPayDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return purchasecontractPayDao.batchRemove(ids);
	}

	@Override
	public int batchRemoveByContractId(Long[] ids) {
		return purchasecontractPayDao.batchRemoveByContractId(ids);
	}

	@Override
	public List<PurchasecontractPayDO> detailOfPay(Map<String, Object> map) {
		return purchasecontractPayDao.detailOfPay(map);
	}

	@Override
	public Map<String, Object> totalOfPay(Map<String, Object> map) {
		return purchasecontractPayDao.totalOfPay(map);
	}

	@Override
	public List<PurchasecontractPayDO> detailOfPayById(Map<String, Object> map) {
		return purchasecontractPayDao.detailOfPayById(map);
	}

	@Override
	public List<Map<String, Object>> listOfPay(Map<String, Object> map) {
		return purchasecontractPayDao.listOfPay(map);
	}

	@Override
	public Map<String, Object> countListOfPay(Map<String, Object> map) {
		return purchasecontractPayDao.countListOfPay(map);
	}

}
