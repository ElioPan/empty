package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.SalescontractAlterationDao;
import com.ev.scm.domain.SalescontractAlterationDO;
import com.ev.scm.service.SalescontractAlterationService;



@Service
public class SalescontractAlterationServiceImpl implements SalescontractAlterationService {
	@Autowired
	private SalescontractAlterationDao salescontractAlterationDao;
	
	@Override
	public SalescontractAlterationDO get(Long id){
		return salescontractAlterationDao.get(id);
	}
	
	@Override
	public List<SalescontractAlterationDO> list(Map<String, Object> map){
		return salescontractAlterationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salescontractAlterationDao.count(map);
	}
	
	@Override
	public int save(SalescontractAlterationDO salescontractAlteration){
		return salescontractAlterationDao.save(salescontractAlteration);
	}
	
	@Override
	public int update(SalescontractAlterationDO salescontractAlteration){
		return salescontractAlterationDao.update(salescontractAlteration);
	}
	
	@Override
	public int remove(Long id){
		return salescontractAlterationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return salescontractAlterationDao.batchRemove(ids);
	}
	
}
