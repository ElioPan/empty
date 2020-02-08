package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.UpkeepCheckDao;
import com.ev.custom.domain.UpkeepCheckDO;
import com.ev.custom.service.UpkeepCheckService;



@Service
public class UpkeepCheckServiceImpl implements UpkeepCheckService {
	@Autowired
	private UpkeepCheckDao upkeepCheckDao;
	
	@Override
	public UpkeepCheckDO get(Long id){
		return upkeepCheckDao.get(id);
	}
	
	@Override
	public List<UpkeepCheckDO> list(Map<String, Object> map){
		return upkeepCheckDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return upkeepCheckDao.count(map);
	}
	
	@Override
	public int save(UpkeepCheckDO upkeepCheck){
		return upkeepCheckDao.save(upkeepCheck);
	}
	
	@Override
	public int update(UpkeepCheckDO upkeepCheck){
		return upkeepCheckDao.update(upkeepCheck);
	}
	
	@Override
	public int remove(Long id){
		return upkeepCheckDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return upkeepCheckDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> getDetailByRecordId(Long id) {
		return upkeepCheckDao.getDetailByRecordId(id);
	}

	@Override
	public int updateByRecordId(UpkeepCheckDO upkeepCheck) {
		return upkeepCheckDao.updateByRecordId(upkeepCheck);
	}

}
