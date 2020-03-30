package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.NoticeTypeDao;
import com.ev.custom.domain.NoticeTypeDO;
import com.ev.custom.service.NoticeTypeService;



@Service
public class NoticeTypeServiceImpl implements NoticeTypeService {
	@Autowired
	private NoticeTypeDao noticeTypeDao;
	
	@Override
	public NoticeTypeDO get(Long id){
		return noticeTypeDao.get(id);
	}
	
	@Override
	public List<NoticeTypeDO> list(Map<String, Object> map){
		return noticeTypeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return noticeTypeDao.count(map);
	}
	
	@Override
	public int save(NoticeTypeDO noticeType){
		return noticeTypeDao.save(noticeType);
	}
	
	@Override
	public int update(NoticeTypeDO noticeType){
		return noticeTypeDao.update(noticeType);
	}

	@Override
	public int updateAll(NoticeTypeDO noticeType){
		return noticeTypeDao.updateAll(noticeType);
	}
	
	@Override
	public int remove(Long id){
		return noticeTypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return noticeTypeDao.batchRemove(ids);
	}
	
}
