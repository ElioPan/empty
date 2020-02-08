package com.ev.scm.service.impl;

import com.ev.scm.dao.SalesbillItemDao;
import com.ev.scm.domain.SalesbillItemDO;
import com.ev.scm.service.SalesbillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SalesbillItemServiceImpl implements SalesbillItemService {
	@Autowired
	private SalesbillItemDao salesbillItemDao;
	
	@Override
	public SalesbillItemDO get(Long bid){
		return null;
	}
	
	@Override
	public List<SalesbillItemDO> list(Map<String, Object> map){
		return salesbillItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salesbillItemDao.count(map);
	}
	
	@Override
	public int save(SalesbillItemDO salesbillItem){
		return salesbillItemDao.save(salesbillItem);
	}
	
	@Override
	public int update(SalesbillItemDO salesbillItem){
		return salesbillItemDao.update(salesbillItem);
	}
	
	@Override
	public int remove(Long bid){
		return salesbillItemDao.remove(bid);
	}
	
	@Override
	public int batchRemove(Long[] bids){
		return salesbillItemDao.batchRemove(bids);
	}
	
}
