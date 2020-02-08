package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PatrolPlanDetailDao;
import com.ev.custom.domain.PatrolPlanDetailDO;
import com.ev.custom.service.PatrolPlanDetailService;



@Service
public class PatrolPlanDetailServiceImpl implements PatrolPlanDetailService {
	@Autowired
	private PatrolPlanDetailDao patrolPlanDetailDao;
	
	@Override
	public PatrolPlanDetailDO get(Long id){
		return patrolPlanDetailDao.get(id);
	}
	
	@Override
	public List<PatrolPlanDetailDO> list(Map<String, Object> map){
		return patrolPlanDetailDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return patrolPlanDetailDao.count(map);
	}
	
	@Override
	public int save(PatrolPlanDetailDO patrolPlanDetail){
		return patrolPlanDetailDao.save(patrolPlanDetail);
	}
	
	@Override
	public int update(PatrolPlanDetailDO patrolPlanDetail){
		return patrolPlanDetailDao.update(patrolPlanDetail);
	}
	
	@Override
	public int remove(Long id){
		return patrolPlanDetailDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return patrolPlanDetailDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return patrolPlanDetailDao.listForMap(map);
	}
	
}
