package com.ev.mes.service.impl;

import com.ev.mes.dao.CheckPlanItemDao;
import com.ev.mes.domain.CheckPlanItemDO;
import com.ev.mes.service.CheckPlanItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class CheckPlanItemServiceImpl implements CheckPlanItemService {
	@Autowired
	private CheckPlanItemDao checkPlanItemDao;
	
	@Override
	public CheckPlanItemDO get(Long id){
		return checkPlanItemDao.get(id);
	}
	
	@Override
	public List<CheckPlanItemDO> list(Map<String, Object> map){
		return checkPlanItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return checkPlanItemDao.count(map);
	}
	
	@Override
	public int save(CheckPlanItemDO checkPlanItem){
		return checkPlanItemDao.save(checkPlanItem);
	}
	
	@Override
	public int update(CheckPlanItemDO checkPlanItem){
		return checkPlanItemDao.update(checkPlanItem);
	}
	
	@Override
	public int remove(Long id){
		return checkPlanItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return checkPlanItemDao.batchRemove(ids);
	}

	@Override
	public int canDeletOfProject(Map<String, Object> map) {
		return checkPlanItemDao.canDeletOfProject(map);
	}


}
