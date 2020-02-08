package com.ev.custom.service.impl;

import com.ev.framework.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ev.custom.dao.CommentDao;
import com.ev.custom.domain.CommentDO;
import com.ev.custom.service.CommentService;



@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentDao commentDao;
	
	@Override
	public CommentDO get(Long id){
		return commentDao.get(id);
	}
	
	@Override
	public List<CommentDO> list(Map<String, Object> map){
		return commentDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return commentDao.count(map);
	}
	
	@Override
	public int save(CommentDO comment){
		comment.setCreateBy(ShiroUtils.getUserId());
		comment.setCreateTime(new Date());
		return commentDao.save(comment);
	}
	
	@Override
	public int update(CommentDO comment){
		comment.setUpdateBy(ShiroUtils.getUserId());
		comment.setUpdateTime(new Date());
		return commentDao.update(comment);
	}
	
	@Override
	public int remove(Long id){
		return commentDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return commentDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listOfDetail(Map<String, Object> map) {
		return commentDao.listOfDetail(map);
	}

}
