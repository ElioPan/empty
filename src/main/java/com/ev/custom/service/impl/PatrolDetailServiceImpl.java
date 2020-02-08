package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PatrolDetailDao;
import com.ev.custom.domain.PatrolDetailDO;
import com.ev.custom.service.PatrolDetailService;
import com.google.common.collect.Maps;



@Service
public class PatrolDetailServiceImpl implements PatrolDetailService {
	@Autowired
	private PatrolDetailDao patrolDetailDao;
	
	@Override
	public PatrolDetailDO get(Long id){
		return patrolDetailDao.get(id);
	}
	
	@Override
	public List<PatrolDetailDO> list(Map<String, Object> map){
		return patrolDetailDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return patrolDetailDao.count(map);
	}
	
	@Override
	public int save(PatrolDetailDO patrolDetail){
		return patrolDetailDao.save(patrolDetail);
	}
	
	@Override
	public int update(PatrolDetailDO patrolDetail){
		return patrolDetailDao.update(patrolDetail);
	}
	
	@Override
	public int remove(Long id){
		return patrolDetailDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return patrolDetailDao.batchRemove(ids);
	}

	@Override
	public PatrolDetailDO getByProjectId(Long recordId,Long deviceId, Long projectId) {
		Map<String,Object> map = Maps.newHashMapWithExpectedSize(3);
		map.put("recordId",recordId);
		map.put("deviceId",deviceId);
		map.put("projectId",projectId);
		return (PatrolDetailDO)patrolDetailDao.list(map).get(0);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return patrolDetailDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return patrolDetailDao.countForMap(map);
	}

	@Override
	public List<Map<String, Object>> getRecordByDevice(Map<String,Object>params) {
		return patrolDetailDao.getRecordByDevice(params);
	}

	@Override
	public List<Map<String, Object>> devicePatrolListForMap(Map<String, Object> params) {
		return patrolDetailDao.devicePatrolListForMap(params);
	}

	@Override
	public int devicePatrolCountForMap(Map<String, Object> params) {
		return patrolDetailDao.devicePatrolCountForMap(params);
	}
	
}
