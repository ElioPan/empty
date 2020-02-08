package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.BanktransferslipItemDao;
import com.ev.custom.domain.BanktransferslipItemDO;
import com.ev.custom.service.BanktransferslipItemService;



@Service
public class BanktransferslipItemServiceImpl implements BanktransferslipItemService {
	@Autowired
	private BanktransferslipItemDao banktransferslipItemDao;
	
	@Override
	public BanktransferslipItemDO get(Long tid){
		return null;
	}
	
	@Override
	public List<BanktransferslipItemDO> list(Map<String, Object> map){
		return banktransferslipItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return banktransferslipItemDao.count(map);
	}
	
	@Override
	public int save(BanktransferslipItemDO banktransferslipItem){
		return banktransferslipItemDao.save(banktransferslipItem);
	}
	
	@Override
	public int update(BanktransferslipItemDO banktransferslipItem){
		return banktransferslipItemDao.update(banktransferslipItem);
	}
	
	@Override
	public int remove(Long tid){
		return banktransferslipItemDao.remove(tid);
	}
	
	@Override
	public int batchRemove(Long[] tids){
		return banktransferslipItemDao.batchRemove(tids);
	}
	
}
