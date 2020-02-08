package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.UpkeepPlanProjectDao;
import com.ev.custom.domain.UpkeepPlanProjectDO;
import com.ev.custom.service.UpkeepPlanProjectService;



@Service
public class UpkeepPlanProjectServiceImpl implements UpkeepPlanProjectService {
	@Autowired
	private UpkeepPlanProjectDao upkeepPlanProjectDao;
	
	@Override
	public UpkeepPlanProjectDO get(Long id){
		return upkeepPlanProjectDao.get(id);
	}
	
	@Override
	public List<UpkeepPlanProjectDO> list(Map<String, Object> map){
		return upkeepPlanProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return upkeepPlanProjectDao.count(map);
	}
	
	@Override
	public int save(UpkeepPlanProjectDO upkeepPlanProject){
		return upkeepPlanProjectDao.save(upkeepPlanProject);
	}
	
	@Override
	public int update(UpkeepPlanProjectDO upkeepPlanProject){
		return upkeepPlanProjectDao.update(upkeepPlanProject);
	}
	
	@Override
	public int remove(Long id){
		return upkeepPlanProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return upkeepPlanProjectDao.batchRemove(ids);
	}

	@Override
	public int removeByPlanId(Long planId) {
		return upkeepPlanProjectDao.removeByPlanId(planId);
	}

}
