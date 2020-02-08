package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.OutsourcingContractPayDao;
import com.ev.scm.domain.OutsourcingContractPayDO;
import com.ev.scm.service.OutsourcingContractPayService;



@Service
public class OutsourcingContractPayServiceImpl implements OutsourcingContractPayService {
	@Autowired
	private OutsourcingContractPayDao outsourcingContractPayDao;
	
	@Override
	public OutsourcingContractPayDO get(Long id){
		return outsourcingContractPayDao.get(id);
	}
	
	@Override
	public List<OutsourcingContractPayDO> list(Map<String, Object> map){
		return outsourcingContractPayDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return outsourcingContractPayDao.count(map);
	}
	
	@Override
	public int save(OutsourcingContractPayDO outsourcingContractPay){
		return outsourcingContractPayDao.save(outsourcingContractPay);
	}
	
	@Override
	public int update(OutsourcingContractPayDO outsourcingContractPay){
		return outsourcingContractPayDao.update(outsourcingContractPay);
	}
	
	@Override
	public int remove(Long id){
		return outsourcingContractPayDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return outsourcingContractPayDao.batchRemove(ids);
	}
	
}
