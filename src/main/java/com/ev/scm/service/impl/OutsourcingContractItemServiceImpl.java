package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.OutsourcingContractItemDao;
import com.ev.scm.domain.OutsourcingContractItemDO;
import com.ev.scm.service.OutsourcingContractItemService;



@Service
public class OutsourcingContractItemServiceImpl implements OutsourcingContractItemService {
	@Autowired
	private OutsourcingContractItemDao outsourcingContractItemDao;
	
	@Override
	public OutsourcingContractItemDO get(Long id){
		return outsourcingContractItemDao.get(id);
	}
	
	@Override
	public List<OutsourcingContractItemDO> list(Map<String, Object> map){
		return outsourcingContractItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return outsourcingContractItemDao.count(map);
	}
	
	@Override
	public int save(OutsourcingContractItemDO outsourcingContractItem){
		return outsourcingContractItemDao.save(outsourcingContractItem);
	}
	
	@Override
	public int update(OutsourcingContractItemDO outsourcingContractItem){
		return outsourcingContractItemDao.update(outsourcingContractItem);
	}
	
	@Override
	public int remove(Long id){
		return outsourcingContractItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return outsourcingContractItemDao.batchRemove(ids);
	}
	
}
