package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.NoticeDao;
import com.ev.custom.domain.NoticeDO;
import com.ev.custom.service.NoticeService;



@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private NoticeDao noticeDao;
	
	@Override
	public NoticeDO get(Long id){
		return noticeDao.get(id);
	}
	
	@Override
	public List<NoticeDO> list(Map<String, Object> map){
		return noticeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return noticeDao.count(map);
	}
	
	@Override
	public int save(NoticeDO notice){
		return noticeDao.save(notice);
	}
	
	@Override
	public int update(NoticeDO notice){
		return noticeDao.update(notice);
	}
	
	@Override
	public int remove(Long id){
		return noticeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return noticeDao.batchRemove(ids);
	}
	
}
