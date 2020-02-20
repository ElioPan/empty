package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.QrcodeItemDao;
import com.ev.scm.domain.QrcodeItemDO;
import com.ev.scm.service.QrcodeItemService;



@Service
public class QrcodeItemServiceImpl implements QrcodeItemService {
	@Autowired
	private QrcodeItemDao qrcodeItemDao;
	
	@Override
	public QrcodeItemDO get(Long id){
		return qrcodeItemDao.get(id);
	}
	
	@Override
	public List<QrcodeItemDO> list(Map<String, Object> map){
		return qrcodeItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return qrcodeItemDao.count(map);
	}
	
	@Override
	public int save(QrcodeItemDO qrcodeItem){
		return qrcodeItemDao.save(qrcodeItem);
	}
	
	@Override
	public int update(QrcodeItemDO qrcodeItem){
		return qrcodeItemDao.update(qrcodeItem);
	}
	
	@Override
	public int remove(Long id){
		return qrcodeItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return qrcodeItemDao.batchRemove(ids);
	}
	
}
