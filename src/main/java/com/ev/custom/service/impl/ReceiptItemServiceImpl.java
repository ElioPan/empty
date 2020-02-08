package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.ReceiptItemDao;
import com.ev.custom.domain.ReceiptItemDO;
import com.ev.custom.service.ReceiptItemService;



@Service
public class ReceiptItemServiceImpl implements ReceiptItemService {
	@Autowired
	private ReceiptItemDao receiptItemDao;
	
	@Override
	public ReceiptItemDO get(Long rid){
		return null;
	}
	
	@Override
	public List<ReceiptItemDO> list(Map<String, Object> map){
		return receiptItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return receiptItemDao.count(map);
	}
	
	@Override
	public int save(ReceiptItemDO receiptItem){
		return receiptItemDao.save(receiptItem);
	}
	
	@Override
	public int update(ReceiptItemDO receiptItem){
		return receiptItemDao.update(receiptItem);
	}
	
	@Override
	public int remove(Long rid){
		return receiptItemDao.remove(rid);
	}
	
	@Override
	public int batchRemove(Long[] rids){
		return receiptItemDao.batchRemove(rids);
	}
	
}
