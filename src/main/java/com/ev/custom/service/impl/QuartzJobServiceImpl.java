package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.QuartzJobDao;
import com.ev.custom.domain.QuartzJobDO;
import com.ev.custom.service.QuartzJobService;



@Service
public class QuartzJobServiceImpl implements QuartzJobService {
	@Autowired
	private QuartzJobDao quartzJobDao;
	
	@Override
	public QuartzJobDO get(Long id){
		return quartzJobDao.get(id);
	}
	
	@Override
	public List<QuartzJobDO> list(Map<String, Object> map){
		return quartzJobDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return quartzJobDao.count(map);
	}
	
	@Override
	public int save(QuartzJobDO quartzJob){
		return quartzJobDao.save(quartzJob);
	}
	
	@Override
	public int update(QuartzJobDO quartzJob){
		return quartzJobDao.update(quartzJob);
	}
	
	@Override
	public int remove(Long id){
		return quartzJobDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return quartzJobDao.batchRemove(ids);
	}
	
}
