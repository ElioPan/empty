package com.ev.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.system.dao.ProviderDao;
import com.ev.system.domain.ProviderDO;
import com.ev.system.service.ProviderService;



@Service
public class ProviderServiceImpl implements ProviderService {
	@Autowired
	private ProviderDao providerDao;
	
	@Override
	public ProviderDO get(Long id){
		return providerDao.get(id);
	}
	
	@Override
	public List<ProviderDO> list(Map<String, Object> map){
		return providerDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return providerDao.count(map);
	}
	
	@Override
	public int save(ProviderDO provider){
		return providerDao.save(provider);
	}
	
	@Override
	public int update(ProviderDO provider){
		return providerDao.update(provider);
	}
	
	@Override
	public int remove(Long id){
		return providerDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return providerDao.batchRemove(ids);
	}
	
}
