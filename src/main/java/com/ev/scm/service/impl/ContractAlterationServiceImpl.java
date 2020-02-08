package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.ContractAlterationDao;
import com.ev.scm.domain.ContractAlterationDO;
import com.ev.scm.service.ContractAlterationService;



@Service
public class ContractAlterationServiceImpl implements ContractAlterationService {
	@Autowired
	private ContractAlterationDao contractAlterationDao;
	
	@Override
	public ContractAlterationDO get(Long id){
		return contractAlterationDao.get(id);
	}
	
	@Override
	public List<ContractAlterationDO> list(Map<String, Object> map){
		return contractAlterationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return contractAlterationDao.count(map);
	}
	
	@Override
	public int save(ContractAlterationDO contractAlteration){
		return contractAlterationDao.save(contractAlteration);
	}
	
	@Override
	public int update(ContractAlterationDO contractAlteration){
		return contractAlterationDao.update(contractAlteration);
	}
	
	@Override
	public int remove(Long id){
		return contractAlterationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return contractAlterationDao.batchRemove(ids);
	}

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return contractAlterationDao.listForMap(map);
    }

    @Override
    public int countForMap(Map<String, Object> map) {
        return contractAlterationDao.countForMap(map);
    }

}
