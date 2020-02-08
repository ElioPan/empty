package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.ReportTaskDao;
import com.ev.custom.domain.ReportTaskDO;
import com.ev.custom.service.ReportTaskService;



@Service
public class ReportTaskServiceImpl implements ReportTaskService {
	@Autowired
	private ReportTaskDao reportTaskDao;
	
	@Override
	public ReportTaskDO get(Integer id){
		return reportTaskDao.get(id);
	}
	
	@Override
	public List<ReportTaskDO> list(Map<String, Object> map){
		return reportTaskDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return reportTaskDao.count(map);
	}
	
	@Override
	public int save(ReportTaskDO reportTask){
		return reportTaskDao.save(reportTask);
	}
	
	@Override
	public int update(ReportTaskDO reportTask){
		return reportTaskDao.update(reportTask);
	}
	
	@Override
	public int remove(Integer id){
		return reportTaskDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return reportTaskDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return reportTaskDao.listForMap(map);
	}
	
}
