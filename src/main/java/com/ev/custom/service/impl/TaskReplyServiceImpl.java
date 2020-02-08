package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.TaskReplyDao;
import com.ev.custom.domain.TaskReplyDO;
import com.ev.custom.service.TaskReplyService;



@Service
public class TaskReplyServiceImpl implements TaskReplyService {
	@Autowired
	private TaskReplyDao taskReplyDao;
	
	@Override
	public TaskReplyDO get(Integer id){
		return taskReplyDao.get(id);
	}
	
	@Override
	public List<TaskReplyDO> list(Map<String, Object> map){
		return taskReplyDao.list(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return taskReplyDao.listForMap(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return taskReplyDao.count(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return taskReplyDao.countForMap(map);
	}

	@Override
	public int save(TaskReplyDO taskReply){
		return taskReplyDao.save(taskReply);
	}
	
	@Override
	public int update(TaskReplyDO taskReply){

		return taskReplyDao.update(taskReply);
	}
	
	@Override
	public int remove(Integer id){
		return taskReplyDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return taskReplyDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		return taskReplyDao.detail(id);
	}
	
}
