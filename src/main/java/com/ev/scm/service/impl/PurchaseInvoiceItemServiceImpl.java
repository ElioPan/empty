package com.ev.scm.service.impl;

import com.ev.scm.dao.PurchaseInvoiceItemDao;
import com.ev.scm.domain.PurchaseInvoiceItemDO;
import com.ev.scm.service.PurchaseInvoiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class PurchaseInvoiceItemServiceImpl implements PurchaseInvoiceItemService {
	@Autowired
	private PurchaseInvoiceItemDao purchaseInvoiceItemDao;

	@Override
	public PurchaseInvoiceItemDO get(Long id){
		return purchaseInvoiceItemDao.get(id);
	}

	@Override
	public List<PurchaseInvoiceItemDO> list(Map<String, Object> map){
		return purchaseInvoiceItemDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return purchaseInvoiceItemDao.count(map);
	}

	@Override
	public int save(PurchaseInvoiceItemDO purchaseInvoiceItem){
		return purchaseInvoiceItemDao.save(purchaseInvoiceItem);
	}

	@Override
	public int update(PurchaseInvoiceItemDO purchaseInvoiceItem){
		return purchaseInvoiceItemDao.update(purchaseInvoiceItem);
	}

	@Override
	public int remove(Long id){
		return purchaseInvoiceItemDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids){
		return purchaseInvoiceItemDao.batchRemove(ids);
	}

	@Override
	public int batchRemoveByPurcahseId(Long[] ids) {
		return purchaseInvoiceItemDao.batchRemoveByPurcahseId(ids);
	}

	@Override
	public List<Map<String, Object>> getDetailOfBody(Map<String, Object> map) {
		return purchaseInvoiceItemDao.getDetailOfBody(map);
	}

	@Override
	public Map<String, Object> totalOfItem(Map<String, Object> map) {
		return purchaseInvoiceItemDao.totalOfItem(map);
	}

	@Override
	public List<PurchaseInvoiceItemDO> getSourceCode(Map<String, Object> map) {
		return purchaseInvoiceItemDao.getSourceCode(map);
	}

	@Override
	public Map<String, Object> getTotalTaxAmount(Map<String, Object> map) {
		return purchaseInvoiceItemDao.getTotalTaxAmount(map);
	}

}
