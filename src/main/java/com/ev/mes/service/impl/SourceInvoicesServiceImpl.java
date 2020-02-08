package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.SourceInvoicesDao;
import com.ev.mes.domain.SourceInvoicesDO;
import com.ev.mes.service.SourceInvoicesService;



@Service
public class SourceInvoicesServiceImpl implements SourceInvoicesService {
	@Autowired
	private SourceInvoicesDao sourceInvoicesDao;
	
	@Override
	public SourceInvoicesDO get(Long id){
		return sourceInvoicesDao.get(id);
	}
	
	@Override
	public List<SourceInvoicesDO> list(Map<String, Object> map){
		return sourceInvoicesDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return sourceInvoicesDao.count(map);
	}
	
	@Override
	public int save(SourceInvoicesDO sourceInvoices){
		return sourceInvoicesDao.save(sourceInvoices);
	}
	
	@Override
	public int update(SourceInvoicesDO sourceInvoices){
		return sourceInvoicesDao.update(sourceInvoices);
	}
	
	@Override
	public int remove(Long id){
		return sourceInvoicesDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return sourceInvoicesDao.batchRemove(ids);
	}
	
}
