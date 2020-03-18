package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.BankTransferItemDao;
import com.ev.scm.domain.BankTransferItemDO;
import com.ev.scm.service.BankTransferItemService;



@Service
public class BankTransferItemServiceImpl implements BankTransferItemService {
	@Autowired
	private BankTransferItemDao bankTransferItemDao;
	
	@Override
	public BankTransferItemDO get(Long id){
		return bankTransferItemDao.get(id);
	}
	
	@Override
	public List<BankTransferItemDO> list(Map<String, Object> map){
		return bankTransferItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return bankTransferItemDao.count(map);
	}
	
	@Override
	public int save(BankTransferItemDO bankTransferItem){
		return bankTransferItemDao.save(bankTransferItem);
	}
	
	@Override
	public int update(BankTransferItemDO bankTransferItem){
		return bankTransferItemDao.update(bankTransferItem);
	}
	
	@Override
	public int remove(Long id){
		return bankTransferItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return bankTransferItemDao.batchRemove(ids);
	}

	@Override
	public int removeByTransferId(Long[] ids) {
		return bankTransferItemDao.removeByTransferId(ids);
	}

	@Override
	public List<Map<String, Object>> detailOfItem(Map<String, Object> map) {
		return bankTransferItemDao.detailOfItem(map);
	}

	@Override
	public int totalAmount(Map<String, Object> map) {
		return bankTransferItemDao.totalAmount(map);
	}

	@Override
	public int totalOutOrInAmount(Map<String, Object> map) {
		return bankTransferItemDao.totalOutOrInAmount(map);
	}

	@Override
	public List<Map<String, Object>> getBankDetail(Map<String, Object> map) {
		return bankTransferItemDao.getBankDetail(map);
	}

	@Override
	public List<Map<String, Object>> getBankOutDetail(Map<String, Object> map) {
		return bankTransferItemDao.getBankOutDetail(map);
	}

}
