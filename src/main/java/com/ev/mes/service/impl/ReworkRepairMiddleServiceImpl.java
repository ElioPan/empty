package com.ev.mes.service.impl;

import com.ev.mes.dao.ReworkRepairMiddleDao;
import com.ev.mes.domain.ReworkRepairMiddleDO;
import com.ev.mes.service.ReworkRepairMiddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class ReworkRepairMiddleServiceImpl implements ReworkRepairMiddleService {
	@Autowired
	private ReworkRepairMiddleDao reworkRepairMiddleDao;
	
	@Override
	public ReworkRepairMiddleDO get(Long id){
		return reworkRepairMiddleDao.get(id);
	}
	
	@Override
	public List<ReworkRepairMiddleDO> list(Map<String, Object> map){
		return reworkRepairMiddleDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return reworkRepairMiddleDao.count(map);
	}
	
	@Override
	public int save(ReworkRepairMiddleDO reworkRepairMiddle){
		return reworkRepairMiddleDao.save(reworkRepairMiddle);
	}
	
	@Override
	public int update(ReworkRepairMiddleDO reworkRepairMiddle){
		return reworkRepairMiddleDao.update(reworkRepairMiddle);
	}
	
	@Override
	public int remove(Long id){
		return reworkRepairMiddleDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return reworkRepairMiddleDao.batchRemove(ids);
	}

	@Override
	public int removeByPlanId(Long id) {
		return reworkRepairMiddleDao.removeByPlanId(id);
	}

	@Override
	public int canDelReportAboutMiddle(Map<String, Object> map) {
		return reworkRepairMiddleDao.canDelReportAboutMiddle(map);
	}

}
