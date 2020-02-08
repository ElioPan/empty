package com.ev.scm.service.impl;

import com.ev.scm.dao.SalescontractItemDao;
import com.ev.scm.domain.SalescontractItemDO;
import com.ev.scm.service.SalescontractItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SalescontractItemServiceImpl implements SalescontractItemService {
	@Autowired
	private SalescontractItemDao salescontractItemDao;

    @Override
    public SalescontractItemDO get(Long id) {
        return salescontractItemDao.get(id);
    }

    @Override
	public List<SalescontractItemDO> list(Map<String, Object> map){
		return salescontractItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salescontractItemDao.count(map);
	}
	
	@Override
	public int save(SalescontractItemDO salescontractItem){
		return salescontractItemDao.save(salescontractItem);
	}
	
	@Override
	public int update(SalescontractItemDO salescontractItem){
		return salescontractItemDao.update(salescontractItem);
	}
	
	@Override
	public int remove(Long sid){
		return salescontractItemDao.remove(sid);
	}
	
	@Override
	public int batchRemove(Long[] sids){
		return salescontractItemDao.batchRemove(sids);
	}
	
}
