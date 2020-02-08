package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.TaskEmployeeDao;
import com.ev.custom.domain.TaskEmployeeDO;
import com.ev.custom.service.TaskEmployeeService;



@Service
public class TaskEmployeeServiceImpl implements TaskEmployeeService {
	@Autowired
	private TaskEmployeeDao taskEmployeeDao;
	
	@Override
	public TaskEmployeeDO get(Integer id){
		return taskEmployeeDao.get(id);
	}
	
	@Override
	public List<TaskEmployeeDO> list(Map<String, Object> map){
		return taskEmployeeDao.list(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		map.put("sort", "id");
		map.put("order", "ASC");
		return taskEmployeeDao.listForMap(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return taskEmployeeDao.count(map);
	}
	
	@Override
	public int save(TaskEmployeeDO taskEmployee){
		return taskEmployeeDao.save(taskEmployee);
	}
	
	@Override
	public int update(TaskEmployeeDO taskEmployee){
		return taskEmployeeDao.update(taskEmployee);
	}
	
	@Override
	public int remove(Integer id){
		return taskEmployeeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return taskEmployeeDao.batchRemove(ids);
	}
	
	@Override
	public void removeByAssocIdAndTaskId(Map<String, Object> map) {
		taskEmployeeDao.removeByAssocIdAndTaskId(map);
	}

}
