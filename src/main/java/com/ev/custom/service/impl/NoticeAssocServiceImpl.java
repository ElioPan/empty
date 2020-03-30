package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.NoticeAssocDao;
import com.ev.custom.domain.NoticeAssocDO;
import com.ev.custom.service.NoticeAssocService;



@Service
public class NoticeAssocServiceImpl implements NoticeAssocService {
	@Autowired
	private NoticeAssocDao noticeAssocDao;
	
	@Override
	public NoticeAssocDO get(Long id){
		return noticeAssocDao.get(id);
	}
	
	@Override
	public List<NoticeAssocDO> list(Map<String, Object> map){
		return noticeAssocDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return noticeAssocDao.count(map);
	}
	
	@Override
	public int save(NoticeAssocDO noticeAssoc){
		return noticeAssocDao.save(noticeAssoc);
	}
	
	@Override
	public int update(NoticeAssocDO noticeAssoc){
		return noticeAssocDao.update(noticeAssoc);
	}

	@Override
	public int updateAll(NoticeAssocDO noticeAssoc){
		return noticeAssocDao.updateAll(noticeAssoc);
	}
	
	@Override
	public int remove(Long id){
		return noticeAssocDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return noticeAssocDao.batchRemove(ids);
	}

	@Override
	public int batchInsert(List<NoticeAssocDO> noticeAssocDOList) {
		return noticeAssocDao.batchInsert(noticeAssocDOList);
	}

}
