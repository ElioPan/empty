package com.ev.hr.service.impl;

import com.ev.hr.dao.PostInfoDao;
import com.ev.hr.domain.PostInfoDO;
import com.ev.hr.service.PostInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class PostInfoServiceImpl implements PostInfoService {
	@Autowired
	private PostInfoDao postInfoDao;
	
	@Override
	public PostInfoDO get(Long id){
		return postInfoDao.get(id);
	}
	
	@Override
	public List<PostInfoDO> list(Map<String, Object> map){
		return postInfoDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return postInfoDao.count(map);
	}
	
	@Override
	public int save(PostInfoDO postInfo){
		return postInfoDao.save(postInfo);
	}
	
	@Override
	public int update(PostInfoDO postInfo){
		return postInfoDao.update(postInfo);
	}

	@Override
	public int updateAll(PostInfoDO postInfo){
		return postInfoDao.updateAll(postInfo);
	}
	
	@Override
	public int remove(Long id){
		return postInfoDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return postInfoDao.batchRemove(ids);
	}
	
}
