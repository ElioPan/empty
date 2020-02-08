package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.BankTransferDao;
import com.ev.scm.domain.BankTransferDO;
import com.ev.scm.service.BankTransferService;



@Service
public class BankTransferServiceImpl implements BankTransferService {
	@Autowired
	private BankTransferDao bankTransferDao;
	
	@Override
	public BankTransferDO get(Long id){
		return bankTransferDao.get(id);
	}
	
	@Override
	public List<BankTransferDO> list(Map<String, Object> map){
		return bankTransferDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return bankTransferDao.count(map);
	}
	
	@Override
	public int save(BankTransferDO bankTransfer){
		return bankTransferDao.save(bankTransfer);
	}
	
	@Override
	public int update(BankTransferDO bankTransfer){
		return bankTransferDao.update(bankTransfer);
	}
	
	@Override
	public int remove(Long id){
		return bankTransferDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return bankTransferDao.batchRemove(ids);
	}
	
}
