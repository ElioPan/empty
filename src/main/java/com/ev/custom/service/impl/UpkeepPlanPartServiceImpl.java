package com.ev.custom.service.impl;

import com.ev.custom.dao.UpkeepPlanPartDao;
import com.ev.custom.domain.UpkeepPlanPartDO;
import com.ev.custom.service.UpkeepPlanPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UpkeepPlanPartServiceImpl implements UpkeepPlanPartService {
	@Autowired
	private UpkeepPlanPartDao upkeepPlanPartDao;
	
	@Override
	public UpkeepPlanPartDO get(Long id){
		return upkeepPlanPartDao.get(id);
	}
	
	@Override
	public List<UpkeepPlanPartDO> list(Map<String, Object> map){
		return upkeepPlanPartDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return upkeepPlanPartDao.count(map);
	}
	
	@Override
	public int save(UpkeepPlanPartDO upkeepPlanPart){
		return upkeepPlanPartDao.save(upkeepPlanPart);
	}
	
	@Override
	public int update(UpkeepPlanPartDO upkeepPlanPart){
		return upkeepPlanPartDao.update(upkeepPlanPart);
	}
	
	@Override
	public int remove(Long id){
		return upkeepPlanPartDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return upkeepPlanPartDao.batchRemove(ids);
	}

	@Override
	public int removePartByPlanId(Long planId) {
		return upkeepPlanPartDao.removePartByPlanId(planId);
	}

}
