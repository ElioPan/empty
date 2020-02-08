package com.ev.custom.service.impl;

import com.ev.custom.domain.PatrolProjectDO;
import com.ev.custom.service.PatrolProjectService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PatrolProjectDetailDao;
import com.ev.custom.domain.PatrolProjectDetailDO;
import com.ev.custom.service.PatrolProjectDetailService;



@Service
public class PatrolProjectDetailServiceImpl implements PatrolProjectDetailService {
	@Autowired
	private PatrolProjectDetailDao patrolProjectDetailDao;
	@Autowired
	private PatrolProjectService patrolProjectService;
	
	@Override
	public PatrolProjectDetailDO get(Long id){
		return patrolProjectDetailDao.get(id);
	}
	
	@Override
	public List<PatrolProjectDetailDO> list(Map<String, Object> map){
		return patrolProjectDetailDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return patrolProjectDetailDao.count(map);
	}
	
	@Override
	public int save(PatrolProjectDetailDO patrolProjectDetail){
		return patrolProjectDetailDao.save(patrolProjectDetail);
	}
	
	@Override
	public int update(PatrolProjectDetailDO patrolProjectDetail){
		return patrolProjectDetailDao.update(patrolProjectDetail);
	}
	
	@Override
	public int remove(Long id){
		return patrolProjectDetailDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return patrolProjectDetailDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> addProjectDetail(Long projectId, PatrolProjectDetailDO detail) {
		Map<String,Object> results = Maps.newHashMap();
		PatrolProjectDO project = this.patrolProjectService.get(projectId);
		detail.setProjectId(project.getId());
		this.patrolProjectDetailDao.save(detail);
		return results;
	}

}
