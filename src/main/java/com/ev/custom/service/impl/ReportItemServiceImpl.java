package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.ReportItemDao;
import com.ev.custom.domain.ReportItemDO;
import com.ev.custom.service.ReportItemService;



@Service
public class ReportItemServiceImpl implements ReportItemService {
	@Autowired
	private ReportItemDao reportItemDao;
	
	@Override
	public ReportItemDO get(Long id){
		return reportItemDao.get(id);
	}
	
	@Override
	public List<ReportItemDO> list(Map<String, Object> map){
		return reportItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return reportItemDao.count(map);
	}
	
	@Override
	public int save(ReportItemDO reportItem){
		return reportItemDao.save(reportItem);
	}
	
	@Override
	public int update(ReportItemDO reportItem){
		return reportItemDao.update(reportItem);
	}
	
	@Override
	public int remove(Long id){
		return reportItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return reportItemDao.batchRemove(ids);
	}

	@Override
	public int removeByWeekId(Long weekId) {
		return reportItemDao.removeByWeekId(weekId);
	}

}
